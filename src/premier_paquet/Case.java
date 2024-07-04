/*
 * Cette classe à pour objectif de représenter une case du plateau elle servivra pour faciliter le reste du jeu
 * comme les prises de pion etc 
 */

package premier_paquet;

/*
 * Une case est simplement défini par sa ligne et sa colonne sur le plateau
 */
public class Case {
	private int ligne;
	private int colonne;
	
	public Case(int ligne, int colonne) {
		this.ligne = ligne;
		this.colonne = colonne;
	}
	
	public int getLigne() {
		return ligne;
	}
	
	public int getColonne() {
		return colonne;
	}
	
	/*
	 * Cette méthode permet de rendre l'affichage d'une case compréhensible
	 * entrée : aucune
	 * sortie : String, l'affichage de la case
	 */
	public String toString() {
		return "ligne = " + ligne + " colonne = " + colonne;
	}
	
	/*
	 * On redéfini les méthodes equals et hashcode afin de pouvoir utiliser certaines fonctions de comparaison Java comme par exemple
	 * .contains() d'une liste ou encore .containsKey() d'un HashMap.
	 */
	@Override
	public boolean equals(Object obj){
		  
		if(obj == null) return false;
		  
		if(obj instanceof Case && this == obj) return true;
		 
		Case caseTestee = (Case)obj;
		
		if(ligne != caseTestee.ligne || colonne != caseTestee.colonne) return false;
		  
		return true;
	}
	
	@Override
	public int hashCode() {
		return ligne*100 + colonne;
	}
	
}
