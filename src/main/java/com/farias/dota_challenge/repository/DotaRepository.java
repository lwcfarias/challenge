package com.farias.dota_challenge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.farias.dota_challenge.entity.Match;
import com.farias.dota_challenge.rest.model.HeroDamage;
import com.farias.dota_challenge.rest.model.HeroSpells;

public interface DotaRepository extends JpaRepository<Match, Long>{
		
	List<Match> findByMatchId(long matchId);

	@Query(value = "SELECT m FROM Match m where m.matchId=?1 and m.name=?2")
	Match findMatch(Long matchId, String name);
	
	@Query(value = "SELECT new com.farias.dota_challenge.rest.model.HeroSpells(s.spell, CAST(count(*) as integer)) FROM Spell s, Match m "
			+ "where s.heroName = ?1  and m.matchId = ?2 and s.heroName=m.name group by s.spell")
	List<HeroSpells> findMatchHeroSpells(String name, Long matchId);
	
	@Query(value="select new com.farias.dota_challenge.rest.model.HeroDamage(d.target, "
			+ "CAST(count(d.heroName) as integer) , CAST(sum(d.damage) as integer)) from Damage d, Match m "
			+ "where d.heroName= ?1 and m.matchId= ?2 and m.name=d.heroName group by d.target")
	List<HeroDamage> findMatchHeroDamages(String name, Long matchId);

}
