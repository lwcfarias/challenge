package com.farias.dota_challenge.service;

import java.util.List;

import com.farias.dota_challenge.rest.model.HeroDamage;
import com.farias.dota_challenge.rest.model.HeroItems;
import com.farias.dota_challenge.rest.model.HeroKills;
import com.farias.dota_challenge.rest.model.HeroSpells;

public interface MatchService {
	
    Long ingestMatch(String payload);
    
    List<HeroKills> findHeroKills(Long matchId);
	
    List<HeroItems> findHeroItems(Long matchId, String heroName);
    
    List<HeroSpells> findHeroSpells(Long matchId, String heroName);
    
    List<HeroDamage> findHeroDamages(Long matchId, String heroName);
    
}
