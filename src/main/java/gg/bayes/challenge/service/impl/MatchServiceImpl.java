package gg.bayes.challenge.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import gg.bayes.challenge.entity.Damage;
import gg.bayes.challenge.entity.Match;
import gg.bayes.challenge.entity.Purchase;
import gg.bayes.challenge.entity.Spell;
import gg.bayes.challenge.repository.DotaRepository;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MatchServiceImpl implements MatchService {

	@Autowired
	private DotaRepository dotaRepository;

	@Autowired
	public MatchServiceImpl() {
	}

	@Override
	public Long ingestMatch(String payload) {
		return loadData(getBufferedData(payload));
	}

	private Long loadData(BufferedReader payloadData) {
		Long matchId = null;
		try {
			String payload = payloadData.readLine();
			while (payload != null) {
				if (matchId == null) {
					matchId = findMatchId(payload);
				} else {	
					uploadEvent(matchId, payload);
				}
				payload = payloadData.readLine();
			}
		} catch (IOException e) {
			log.error("Error reading payload buffer.", e);
		}
		return matchId;
	}

	private Long findMatchId(String payload) {
		Long matchId = null;
		if(payload.contains("Content-Disposition:")) {
			String fileName = payload.split("filename=")[1];
			matchId = Long.parseLong(fileName.replaceAll("[^\\d]", ""));
		}
		return matchId;
	}

	private void uploadEvent(Long matchId, String payload) {
		log.debug("Data: [matchId=" + matchId + "][payload: [" + payload + "]");
		String[] splitedData = payload.split("\\ ");
		if (splitedData.length > 2) {
			switch (splitedData[2]) {
			case "casts":
				saveSpell(matchId, splitedData);
				break;
			case "buys":
				saveItems(matchId, splitedData);
				break;
			case "is":
				if (splitedData[3].equals("killed")) {
					saveKills(matchId, splitedData);
				}
				break;
			case "hits":
				saveDamage(matchId, splitedData);
				break;
			default:
				log.debug("Message not supported.");
			}
		}
	}

	private void saveKills(Long matchId, String[] splitedData) {
		log.debug("Kills...");
		Match match = getMatch(matchId, splitedData[5]);
		int kills = match.getKills() == null ? 0 : match.getKills();
		match.setKills(++kills);
		dotaRepository.save(match);
		log.debug("Loaded.");		
	}
	
	private void saveDamage(Long matchId, String[] splitedData) {
		log.debug("saveDamage...");
		Match match = getMatch(matchId, splitedData[1]);
		List<Damage> damages = match.getDamage();
		if (CollectionUtils.isEmpty(damages)) {
			match.setDamage(new ArrayList<>());
		}
		Damage singleDamage = new Damage();
		singleDamage.setDamage(Integer.parseInt(splitedData[7]));
		singleDamage.setHeroName(splitedData[1]);
		singleDamage.setTarget(splitedData[3]);
		match.getDamage().add(singleDamage);
		dotaRepository.save(match);
		log.debug("loaded.");
	}

	private void saveItems(Long matchId, String[] splitedData) {
		log.debug("saveItems...");
		Match match = getMatch(matchId, splitedData[1]);
		if (CollectionUtils.isEmpty(match.getPurchase())) {
			match.setPurchase(new ArrayList<>());
		}
		Purchase item = new Purchase();
		item.setTimestamp(Long.parseLong(splitedData[0].replaceAll("[(\\[\\].:*)]", "")));

		item.setItem(splitedData[4]);
		match.getPurchase().add(item);
		dotaRepository.save(match);
		log.debug("loaded.");
	}

	private void saveSpell(Long matchId, String[] splitedData) {
		log.debug("saveSpell...");
		Match match = getMatch(matchId, splitedData[1]);
		List<Spell> listSpell = match.getSpell();
		if (CollectionUtils.isEmpty(listSpell)) {
			match.setSpell(new ArrayList<>());
		}
		Spell spell = new Spell();
		spell.setHeroName(splitedData[1]);
		spell.setSpell(splitedData[4]);
		match.getSpell().add(spell);
		dotaRepository.save(match);
		log.debug("loaded.");
	}

	private BufferedReader getBufferedData(String payload) {
		InputStream streamPayload = new ByteArrayInputStream(payload.getBytes());
		return new BufferedReader(new InputStreamReader(streamPayload));
	}

	private Match getMatch(Long matchId, String name) {
		Match match = dotaRepository.findMatch(matchId, name);
		if (match == null) {
			match = new Match(matchId, name);
		}
		return match;
	}

	@Override
	public List<HeroKills> findHeroKills(Long matchId) {
		List<HeroKills> heroKills = new ArrayList<>();
		dotaRepository.findByMatchId(matchId).stream().forEach(m -> {
			HeroKills kills = new HeroKills();
			kills.setHero(m.getName());
			kills.setKills(m.getKills());
			heroKills.add(kills);
		});
		return heroKills;
	}

	@Override
	public List<HeroItems> findHeroItems(Long matchId, String heroName) {
		List<HeroItems> heroItems = new ArrayList<>();
		Match match = dotaRepository.findMatch(matchId, heroName);
		if (match != null) {
			match.getPurchase().stream().forEach(m -> {
				HeroItems item = new HeroItems();
				item.setItem(m.getItem());
				item.setTimestamp(m.getTimestamp());
				heroItems.add(item);
			});
		}
		return heroItems;
	}
	@Override
	public List<HeroSpells> findHeroSpells(Long matchId, String heroName) {
		return dotaRepository.findMatchHeroSpells(heroName, matchId);
	}

	@Override
	public List<HeroDamage> findHeroDamages(Long matchId, String heroName) {
		return dotaRepository.findMatchHeroDamages(heroName, matchId);
	}

}
