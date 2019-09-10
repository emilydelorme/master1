package fr.istic.m1.aco.tp1;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Emily Delorme
 *
 */
public class Cours2 {
	private Set<Etudiant2> etudiants;
	
	public Cours2() {
		this.etudiants = new HashSet<>();
	}

	public Set<Etudiant2> getEtudiants() {
		return this.etudiants;
	}

	public void setEtudiants(Set<Etudiant2> etudiants) {
		this.etudiants = etudiants;
	}
	
	public void addEtudiant(Etudiant2 etudiant) {
		this.etudiants.add(etudiant);
	}
	
	public void removeEtudiant(Etudiant2 etudiant) {
		this.etudiants.remove(etudiant);
	}
}