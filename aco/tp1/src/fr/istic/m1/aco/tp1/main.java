package fr.istic.m1.aco.tp1;

import java.util.HashSet;

/**
 * @author Emily Delorme
 *
 */
public class main {

	/**
	 * @param args: arguments
	 */
	public static void main(String[] args) {
		Etudiant1 e1_1 = new Etudiant1();
		Etudiant1 e1_2 = new Etudiant1();
		Etudiant2 e2_1 = new Etudiant2();
		Etudiant2 e2_2 = new Etudiant2();
		Etudiant3 e3_1 = new Etudiant3();
		Etudiant3 e3_2 = new Etudiant3();

		Cours1 c1_1 = new Cours1();
		Cours1 c1_2 = new Cours1();
		Cours2 c2_1 = new Cours2();
		Cours2 c2_2 = new Cours2();
		Cours3 c3_1 = new Cours3();
		Cours3 c3_2 = new Cours3();
		
		c1_1.setEtudiant(e1_1);
		e1_1.setCours(c1_1);
		c1_1.equals(null);

		c2_1.addEtudiant(e2_1); c2_1.addEtudiant(e2_2);
		e2_1.setCours(c2_1);
		e2_2.setCours(c2_1);
		c2_1.removeEtudiant(e2_2);

		c3_1.addEtudiant(e3_1); c3_1.addEtudiant(e3_2);
		c3_2.addEtudiant(e3_1); c3_2.addEtudiant(e3_2);
		e3_1.addCours(c3_1); e3_1.addCours(c3_2);
		e3_2.addCours(c3_1); e3_2.addCours(c3_2);
		e3_1.removeCours(c3_1);
	}

}
