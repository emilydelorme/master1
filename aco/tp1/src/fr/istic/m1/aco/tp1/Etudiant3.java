package fr.istic.m1.aco.tp1;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Emily Delorme
 *
 */
public class Etudiant3 {
	private Set<Cours3> cours;
	
	public Etudiant3() {
		this.cours = new HashSet<>();
	}

	/**
	 * Return the set of the [cours] of the [etudiant]
	 * @return cours
	 */
	public Set<Cours3> getCours() {
		return this.cours;
	}

	/**
	 * Set the set of the [cours] of the [etudiant]
	 * @param cours: set of [cours] to set
	 */
	public void setCours(Set<Cours3> cours) {
		this.cours = cours;
	}
	
	/**
	 * Add one [cours] for the [etudiant]
	 * @param cours: [cours] to add
	 */
	public void addCours(Cours3 cours) {
		this.cours.add(cours);
	}
	
	/**
	 * Remove one [cours] for the [etudiant]
	 * @param cours: [cours] to remove
	 */
	public void removeCours(Cours3 cours) {
		this.cours.remove(cours);
	}
}