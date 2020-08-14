package com.farias.dota_challenge.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class Match {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NonNull
	private long matchId;
	@NonNull
	private String name;	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Purchase> purchase;
	@OneToMany(cascade = CascadeType.ALL)
	private List<Spell> spell;
	@OneToMany(cascade = CascadeType.ALL)
	private List<Damage> damage;
	private Integer Kills;
}
