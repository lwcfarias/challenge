package gg.bayes.challenge.fatory;

import java.util.ArrayList;
import java.util.List;

import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;

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
