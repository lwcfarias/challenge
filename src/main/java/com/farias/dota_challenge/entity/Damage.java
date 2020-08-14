package com.farias.dota_challenge.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Damage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String heroName;
    private String target;
    private Integer damage;
}
