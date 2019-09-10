package fr.istic.m1.aco.tp1;

/**
 * @author Emily Delorme
 *
 */
public class Etudiant1 {
	private Cours1 cours;
	
	public Etudiant1() { }

	/**
	 * Return the [cours] of the [etudiant]
	 * @return cours
	 */
	public Cours1 getCours() {
		return this.cours;
	}

	/**
	 * Get the [cours] of the [etudiant]
	 * @param cours: [cours] to set
	 */
	public void setCours(Cours1 cours) {
		this.cours = cours;
	}
}