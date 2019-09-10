package fr.istic.m1.aco.tp1;

/**
 * @author Emily Delorme
 *
 */
public class Etudiant2 {
	private Cours2 cours;
	
	public Etudiant2() { }

	/**
	 * Return the [cours] of the [etudiant]
	 * @return cours
	 */
	public Cours2 getCours() {
		return this.cours;
	}

	/**
	 * Get the [cours] of the [etudiant]
	 * @param cours: [cours] to set
	 */
	public void setCours(Cours2 cours) {
		this.cours = cours;
	}
}