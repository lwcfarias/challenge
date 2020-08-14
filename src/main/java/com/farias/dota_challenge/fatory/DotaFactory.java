package com.farias.dota_challenge.fatory;

import java.util.ArrayList;
import java.util.List;

import com.farias.dota_challenge.rest.model.HeroDamage;
import com.farias.dota_challenge.rest.model.HeroItems;
import com.farias.dota_challenge.rest.model.HeroKills;
import com.farias.dota_challenge.rest.model.HeroSpells;

public class DotaFactory {
	public static List<HeroItems> createEmptyHeroItems() {
		return new ArrayList<>();
	}
	
	public static List<HeroDamage> createEmptyHeroDamage() {
		return new ArrayList<>();
	}
	
	public static List<HeroSpells> createEmptyHeroSpells() {
		return new ArrayList<>();
	}

	public static List<HeroKills> createEmptyHeroKills() {
		return new ArrayList<>();
	}
}
