package fr.istic.m1.aco.tp1;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Emily Delorme
 *
 */
public class Cours3 {
	private Set<Etudiant3> etudiants;
	
	public Cours3() {
		this.etudiants = new HashSet<>();
	}

	public Set<Etudiant3> getEtudiants() {
		return this.etudiants;
	}

	public void setEtudiants(Set<Etudiant3> etudiants) {
		this.etudiants = etudiants;
	}
	
	public void addEtudiant(Etudiant3 etudiant) {
		this.etudiants.add(etudiant);
	}
	
	public void removeEtudiant(Etudiant3 etudiant) {
		this.etudiants.remove(etudiant);
	}
}