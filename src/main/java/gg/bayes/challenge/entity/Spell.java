package gg.bayes.challenge.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Spell {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String heroName;
    private String spell;
}
