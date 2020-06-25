package gg.bayes.challenge.rest.controller;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gg.bayes.challenge.repository.DotaRepository;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/match")
public class MatchController {

	private final MatchService matchService;

	@Autowired
	public MatchController(MatchService matchService, DotaRepository dotaRepository) {
		this.matchService = matchService;
	}

	@PostMapping(consumes = "text/plain")
	public ResponseEntity<Long> ingestMatch(@RequestBody @NotNull @NotBlank String payload) {
		log.info("ingestMatch()->Loading data ...");
		final Long matchId = matchService.ingestMatch(payload);
		log.info("ingestMatch()->data loaded;");
		return ResponseEntity.ok(matchId);
	}

	@GetMapping("{matchId}")
	public ResponseEntity<List<HeroKills>> getMatch(@PathVariable("matchId") Long matchId) {
		return ResponseEntity.ok(matchService.findHeroKills(matchId));
	}

	@GetMapping("{matchId}/{heroName}/items")
	public ResponseEntity<List<HeroItems>> getItems(@PathVariable("matchId") Long matchId,
			@PathVariable("heroName") String heroName) {
		return ResponseEntity.ok(matchService.findHeroItems(matchId, heroName));
	}

	@GetMapping("{matchId}/{heroName}/spells")
	public ResponseEntity<List<HeroSpells>> getSpells(@PathVariable("matchId") Long matchId,
			@PathVariable("heroName") String heroName) {
		return ResponseEntity.ok(matchService.findHeroSpells(matchId, heroName));
	}

	@GetMapping("{matchId}/{heroName}/damage")
	public ResponseEntity<List<HeroDamage>> getDamage(@PathVariable("matchId") Long matchId,
			@PathVariable("heroName") String heroName) {
		return ResponseEntity.ok(matchService.findHeroDamages(matchId, heroName));
	}
}
