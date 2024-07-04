/*
 * Cette classe à pour objectif de représenter un coup, c'est a dire une ou plusieurs cases sur lesquelles se déplacera un pion au cours
 * d'un seul tour.
 */

package premier_paquet;

import java.util.ArrayList;

public class Coup {
	
	private ArrayList<Case> coups;
	
	
	public Coup() {
		coups = new ArrayList<Case>();
	}
	/*
	 * La plupart des coups ne contiennent qu'une case ce constructeur simplifiera donc le code
	 */
	public Coup(Case nouvCase) {
		this();
		coups.add(nouvCase);
	}
	
	/*
	 * Cette méthode permet d'ajouter une case à un coup
	 * entrée : Case nouvCase, la case à ajouter 
	 * sortie : aucune 
	 */
	public void ajouter(Case nouvCase) {
		coups.add(nouvCase);
	}
	
	
	/*
	 * Cette méthode permet de tester si une case appartient deja à ce coup
	 * entrée : Case caseTestee, la case à tester 
	 * sortie : boolean, true si la case est dans le coup false sinon 
	 */
	public boolean contient(Case caseTestee) {
		return coups.contains(caseTestee);
	}
	
	/*
	 * Cette méthode permet de retourner la taille d'un Coup (son nombre de Case)
	 * entrée : aucune
	 * sortie : int, le nombre de case contenues dans ce coup
	 */
	public int taille() {
		return coups.size();
	}
	
	
	/*
	 * Cette méthode permet de retourner la case contenu à une position donnée du coup
	 * entrée : int i, la position de la case qu'on veut récupérer 
	 * sortie : Case, la case situé à la position donnée dans ce coup
	 */
	public Case get(int i) {
		return coups.get(i);
	}

	
	/*
	 * Cette méthode permet de retourner toutes les Cases contenues dans ce Coup
	 * entrée : aucune
	 * sortie : ArrayList<Case>, la liste des cases du coup
	 */
	public ArrayList<Case> getCoups() {
		return coups;
	}
	
	/*
	 * Cette méthode permet de rendre l'affichage d'un coup compréhensible
	 * entrée : aucune
	 * sortie : String, l'affichage du coup
	 */
	public String toString() {
		StringBuffer affichage = new StringBuffer();
		for(int i = 0; i < coups.size(); i++) {
			affichage.append(coups.get(i) + " ");
		}
		return affichage.toString();
	}
	
	/*
	 * On redéfini les méthodes equals et hashcode afin de pouvoir utiliser certaines fonctions de comparaison Java comme par exemple
	 * .contains() d'une liste ou encore .containsKey() d'un HashMap.
	 */
	
	@Override
	public boolean equals(Object obj){
		
		if(obj == null) return false;
		  
		if(obj instanceof Coup && this == obj) return true;
		  
		Coup coup = (Coup)obj;
		
		if(!coups.equals(coup.coups)) return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		int hashCode = 0;
		for(int i = 0; i < coups.size(); i++) {
			hashCode += (i+1)*coups.get(i).hashCode();
		}
		return hashCode;
	}
}
