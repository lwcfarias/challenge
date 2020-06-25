package gg.bayes.challenge.service;

import java.util.List;

import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;

public interface MatchService {
	
    Long ingestMatch(String payload);
    
    List<HeroKills> findHeroKills(Long matchId);
	
    List<HeroItems> findHeroItems(Long matchId, String heroName);
    
    List<HeroSpells> findHeroSpells(Long matchId, String heroName);
    
    List<HeroDamage> findHeroDamages(Long matchId, String heroName);
    
}
