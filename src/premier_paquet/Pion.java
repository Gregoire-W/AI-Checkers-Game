/*
 * Cette classe à pour objectif de représenter un pion et de lui permettre certaines actions :
 * savoir les cases sur lesquelles il peut avancer, sur lesquelles il peut manger, pouvoir avancer et pouvoir manger. 
 */
package premier_paquet;

import java.util.ArrayList;

public class Pion {
	
	protected Case caseActuelle; //case du pion sur le plateau
	protected String symbole;
	protected Jeu jeu;
	
	public Pion(Case caseActuelle,String symbole,Jeu jeu) {
		this.symbole = symbole;
		this.caseActuelle = caseActuelle;
		this.jeu = jeu;
	}


	/*
	 * Cette méthode permet de récupérer la case actuelle du pion
	 * entrée : aucune
	 * sortie : Case, la case actuelle du pion
	 */
	public Case getCaseActuelle() {
		return caseActuelle;
	}

	/*
	 * Cette méthode permet changer la case actuelle du pion
	 * entrée : Case caseActuelle, la nouvelle case qu'on veut donner au pion
	 * sortie : aucune
	 */
	public void setCaseActuelle(Case caseActuelle) {
		this.caseActuelle = caseActuelle;
	}

	/*
	 * Cette méthode permet de récupérer le symbole du pion
	 * entrée : aucune
	 * sortie : String, la symbole du pion
	 */
	public String getSymbole() {
		return symbole;
	}

	/*
	 * Cette méthode permet changer le symbole du pion
	 * entrée : String symbole, le nouveau symbole qu'on veut donner au pion
	 * sortie : aucune
	 */
	public void setSymbole(String symbole) {
		this.symbole = symbole;
	}
	
	
	/*
	 * Cette méthode permet de connaître la liste des Coup que peut faire le pion pour manger un pion adverse
	 * entrée : String[][] plateau, le plateau de jeu actuel
	 * sortie : ArrayList<Coup>, la liste des coups que peut effectuer le pion pour manger  
	 */
	public ArrayList<Coup> peutManger(String[][] plateau) {
		ArrayList<Coup> casesPossibles = new ArrayList<Coup>();
		int lignePion = caseActuelle.getLigne();
		int colonnePion = caseActuelle.getColonne();
		String symbolePionAdverse = (symbole.equals(" x "))? " o " : " x ";
		String symboleDameAdverse = (symbole.equals(" x "))? " O " : " X ";
		//On regarde tout autour du pion si il peut manger la case (4 cases en tout -1/-1 | -1/1 | 1/-1 | 1/1)
		for(int i = -1; i < 2; i+=2) {
			for(int j = -1; j < 2; j+=2) {
				//Si les cases testées sont bien dans les dimensions du plateau
				if(colonnePion  + 2*j > -1 && colonnePion + 2*j < 10 && lignePion + 2*i > -1 && lignePion + 2*i < 10){
					//Et si il y a un pion adverse en diagonale puis une case vide ensuite 
					if((plateau[lignePion + i][colonnePion + j].equals(symbolePionAdverse) || plateau[lignePion + i][colonnePion + j].equals(symboleDameAdverse)) && plateau[lignePion + 2*i][colonnePion + 2*j].equals("   ")) {
						casesPossibles.add(new Coup(new Case(lignePion + 2*i,colonnePion + 2*j))); //Le pion peut manger sur cette diagonale
					}
				}
			}
		}
		return casesPossibles;
	}
	
	/*
	 * Cette méthode permet au pion de manger un ou plusieurs pion adverse selon le coup passé en paramètre
	 * entrée : String[][] plateau, le plateau de jeu actuel
	 *          Coup coup, le coup que le pion doit effectuer pour manger
	 * sortie : ArrayList<Pion>, la liste des pions manger pendant ce coup
	 */
	public ArrayList<Pion> manger(String[][] plateau, Coup coup) {
		ArrayList<Pion> pionMange = new ArrayList<Pion>();//Liste des pions mangés
		int lignePion; 
		int colonnePion;
		String symbolePionAdverse = symbole.equals(" o ")? " x " : " o ";
		String symboleDameAdverse = symbole.equals(" o ")? " X " : " O ";
		ArrayList<Pion> listePionAdverse = symbole.equals(" o ")? this.jeu.getPionsNoirs() : this.jeu.getPionsBlancs();
		for(int i = 0; i < coup.taille(); i++) {
			lignePion = caseActuelle.getLigne(); 
			colonnePion = caseActuelle.getColonne();
			this.avancer(plateau, coup.get(i));
			//On enlève le pion mangé du plateau
			plateau[(lignePion + coup.get(i).getLigne()) / 2][(colonnePion + coup.get(i).getColonne()) / 2] = "   ";
			//et on l'enlève de la liste de pion adverse
			Pion pionAdverse = new Pion(new Case((lignePion + coup.get(i).getLigne()) / 2,(colonnePion + coup.get(i).getColonne()) / 2),symbolePionAdverse,jeu);
			if(listePionAdverse.contains(pionAdverse)) {//Si on a mangé un pion on le supprime ici 
				//System.out.println("Pion supprimé : " + pionAdverse);
				listePionAdverse.remove(pionAdverse);
				pionMange.add(pionAdverse);
			}else {//Sinon c'était une dame et on la supprime ici
				listePionAdverse.remove(new Dame(new Case((lignePion + coup.get(i).getLigne()) / 2,(colonnePion + coup.get(i).getColonne()) / 2),symboleDameAdverse,jeu));
				pionMange.add(new Dame(new Case((lignePion + coup.get(i).getLigne()) / 2,(colonnePion + coup.get(i).getColonne()) / 2),symboleDameAdverse,jeu));
			}
			
		}
		return pionMange;
	}
	
	/*
	 * Cette méthode permet de connaître la liste des Coup que peut faire le pion pour avancer
	 * entrée : String[][] plateau, le plateau de jeu actuel
	 * sortie : ArrayList<Coup>, la liste des coups que peut effectuer le pion pour avancer  
	 */
	public ArrayList<Coup> peutAvancer(String[][] plateau) {
		ArrayList<Coup> casesPossibles = new ArrayList<Coup>();
		int lignePion = caseActuelle.getLigne();
		int colonnePion = caseActuelle.getColonne();
		int ligneSuivante = (symbole.equals(" x "))? 1 : -1; //l'indice de la ligne suivante en fonction de la couleur du pion
		//Si le pion peut avancer d'une case à gauche
		if((colonnePion != 9 && (lignePion != 0|| symbole.equals(" x ")) && (lignePion != 9 || symbole.equals(" o "))) && plateau[lignePion + ligneSuivante][colonnePion + 1] == "   ") {
			casesPossibles.add(new Coup(new Case(lignePion + ligneSuivante,colonnePion + 1))); //On ajoute cette case dans les cases possibles
		}
		//Si le pion peut avancer d'une case à droite
		if((colonnePion != 0 && (lignePion != 0|| symbole.equals(" x ")) && (lignePion != 9 || symbole.equals(" o "))) && plateau[lignePion + ligneSuivante][colonnePion - 1] == "   "){
			casesPossibles.add(new Coup(new Case(lignePion + ligneSuivante,colonnePion - 1))); //On ajoute cette case dans les cases possibles
		}
		return casesPossibles;
	}
	
	/*
	 * Cette méthode permet au pion d'avancer d'une case
	 * entrée : String[][] plateau, le plateau de jeu actuel
	 *          Case nouvelleCase, la nouvelle case sur lequelle le pion doit avancer
	 * sortie : aucune
	 */
	public void avancer(String[][] plateau,Case nouvelleCase) {
		int lignePion = caseActuelle.getLigne(); 
		int colonnePion = caseActuelle.getColonne();
		ArrayList<Pion> listeCePion = symbole.equals(" o ") || symbole.equals(" O ")? this.jeu.getPionsBlancs() : this.jeu.getPionsNoirs();
		listeCePion.remove(this); //On actualise la position du pion dans la liste des pions
		plateau[lignePion][colonnePion] = "   "; //on enleve le pion de sa case actuelle
		plateau[nouvelleCase.getLigne()][nouvelleCase.getColonne()] = symbole; //on met le pion sur sa nouvelle case
		caseActuelle = nouvelleCase; 
		listeCePion.add(this); //On ajoute ce nouveau pion dans la liste de pion
	}
	
	/*
	 * Cette méthode permet d'annuler une action "avancer" de ce pion
	 * entrée : String[][] plateau, le plateau de jeu actuel
	 *          Case ancienneCase, la case sur laquelle il se trouvait avancer d'avancer
	 * sortie : aucune
	 */
	public void annulerAvancer(String[][] plateau,Case ancienneCase) {
		int lignePion = caseActuelle.getLigne(); 
		int colonnePion = caseActuelle.getColonne();
		ArrayList<Pion> listeCePion = symbole.equals(" o ") || symbole.equals(" O ")? this.jeu.getPionsBlancs() : this.jeu.getPionsNoirs();
		listeCePion.remove(this); //On actualise la position du pion dans la liste des pions
		plateau[lignePion][colonnePion] = "   "; //on enleve le pion de sa case actuelle
		plateau[ancienneCase.getLigne()][ancienneCase.getColonne()] = symbole; //on met le pion sur sa nouvelle case
		caseActuelle = ancienneCase; 
		listeCePion.add(this); //On ajoute ce nouveau pion dans la liste de pion
	}
	
	/*
	 * Cette méthode permet d'annuler une action "manger" de ce pion
	 * entrée : String[][] plateau, le plateau de jeu actuel
	 *          Coup coup, le coup qu'a fait le pion pour manger 
	 *          Case caseDeBase, la case sur laquelle il se trouvait avancer de manger
	 *          ArrayList<Pion> pionMange, la liste de tous les pions qu'il a mangé
	 * sortie : aucune
	 */
	public void annulerManger(String[][] plateau,Coup coup, Case caseDeBase, ArrayList<Pion> pionMange) {
		ArrayList<Pion> listePionAdverse = symbole.equals(" o ") || symbole.equals(" O ")? this.jeu.getPionsNoirs() : this.jeu.getPionsBlancs();
		for(int i = coup.taille() - 2; i >= 0 ; i--) { //On commence par retourner sur la case de l'avant dernier coup car le dernier coup est notre case actuelle
			this.annulerAvancer(plateau, coup.get(i));
			//On rajoute le pion mangé sur la plateau
			plateau[pionMange.get(i+1).getCaseActuelle().getLigne()][pionMange.get(i+1).getCaseActuelle().getColonne()] = pionMange.get(i+1).getSymbole();
			//System.out.println("on retire dans manger le pion : " + pionAdverse);
			//On remet le pion mangé danse sa liste
			listePionAdverse.add(pionMange.get(i+1));
			
		}
		//On s'occupe d'annuler le premier coup fait
		this.annulerAvancer(plateau, caseDeBase);
		plateau[pionMange.get(0).getCaseActuelle().getLigne()][pionMange.get(0).getCaseActuelle().getColonne()] = pionMange.get(0).getSymbole();
		listePionAdverse.add(pionMange.get(0));
	}
	
	/*
	 * Cette méthode permet de rendre l'affichage d'un pion compréhensible
	 * entrée : aucune
	 * sortie : String, l'affichage du pion
	 */
	public String toString() {
		return caseActuelle.toString() + " symbole : " + symbole;
	}
	
	/*
	 * On redéfini les méthodes equals et hashcode afin de pouvoir utiliser certaines fonctions de comparaison Java comme par exemple
	 * .contains() d'une liste ou encore .containsKey() d'un HashMap.
	 */
	@Override
	public boolean equals(Object obj){
		
		if(obj == null) return false;
		  
		if(obj instanceof Pion && this == obj) return true;
		  
		Pion pion = (Pion)obj;
		
		if(!caseActuelle.equals(pion.caseActuelle)) return false;
		if(symbole != null && !symbole.equals(pion.symbole)) return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		return caseActuelle.hashCode();
	}
	
}

