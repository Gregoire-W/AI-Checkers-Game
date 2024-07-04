/*
 * Cette classe à pour objectif de représenter une dame et de lui permettre certaines actions :
 * savoir les cases sur lesquelles elle peut avancer, savoir les cases sur lesquelles elle peut manger, avancer et manger. 
 */
package premier_paquet;

import java.util.ArrayList;


//C'est une classe fille de la classe pion car elle réutilisera certaines des méthodes de celle-ci. 
public class Dame extends Pion{
	
	public Dame(Case caseActuelle,String symbole,Jeu jeu) {
		super(caseActuelle,symbole,jeu);
	}
	
	
	/*
	 * Cette méthode permet de connaître la liste des Coup que peut faire la dame pour avancer
	 * entrée : String[][] plateau, le plateau de jeu actuel
	 * sortie : ArrayList<Coup>, la liste des Coups que peut effectuer la dame pour avancer  
	 */
	public ArrayList<Coup> peutAvancer(String[][] plateau) {
		ArrayList<Coup> casesPossibles = new ArrayList<Coup>();
		int lignePion = caseActuelle.getLigne(); 
		int colonnePion = caseActuelle.getColonne();
		int i = 1;
		int j = 1;
		boolean trouve1 = false; //variables qui arrêtent la recherche sur une diagonale lorsqu'un pion bloque la diagonale de la dame
		boolean trouve2 = false;
		boolean trouve3 = false;
		boolean trouve4 = false;
		while(lignePion + i < 10 || lignePion - i > -1 || colonnePion + j < 10 || colonnePion - j > -1) {
			if(!trouve1 && lignePion - i > -1 && colonnePion + j < 10 && plateau[lignePion - i][colonnePion + j].equals("   ")) {
				casesPossibles.add(new Coup(new Case(lignePion - i,colonnePion + j)));
			}else {
				trouve1 = true;
			}
			if(!trouve2 && lignePion - i > -1 && colonnePion - j > -1 && plateau[lignePion - i][colonnePion - j].equals("   ")) {
				casesPossibles.add(new Coup(new Case(lignePion - i,colonnePion - j)));
			}else {
				trouve2 = true;
			}
			if(!trouve3 && lignePion + i < 10 && colonnePion + j < 10 && plateau[lignePion + i][colonnePion + j].equals("   ")) {
				casesPossibles.add(new Coup(new Case(lignePion + i,colonnePion + j)));
			}else {
				trouve3 = true;
			}
			if(!trouve4 && lignePion + i < 10 && colonnePion - j > -1 && plateau[lignePion + i][colonnePion - j].equals("   ")) {
				casesPossibles.add(new Coup(new Case(lignePion + i,colonnePion - j)));
			}else {
				trouve4 = true;
			}
			j++;
			i++;
		}
		return casesPossibles;
	}
	
	/*
	 * Cette méthode permet de connaître la liste des Coup que peut faire la dame pour manger un pion adverse
	 * entrée : String[][] plateau, le plateau de jeu actuel
	 * sortie : ArrayList<Coup>, la liste des coups que peut effectuer la dame pour manger  
	 */
	public ArrayList<Coup> peutManger(String[][] plateau){
		ArrayList<Coup> casesPossibles = new ArrayList<Coup>();
		
		String symbolePionAdverse = (symbole.equals(" X "))? " o " : " x ";
		String symboleDameAdverse = (symbole.equals(" X "))? " O " : " X ";
		
		String symbolePionAllie = (symbole.equals(" X "))? " x " : " o ";
		
		int lignePion = caseActuelle.getLigne(); 
		int colonnePion = caseActuelle.getColonne();
		int i = 1;
		int j = 1;
		int k;
		boolean trouve1 = false; //variables qui arrêtent la recherche sur une diagonale lorsqu'un pion blanc ou un pion mangeable est trouvé 
		boolean trouve2 = false;
		boolean trouve3 = false;
		boolean trouve4 = false;
		//Chaque while continue tant que toutes les diagonales n'ont pas été éxplorées entièrement ou qu'un pion a été trouvé
		
		//DIAGONALE HAUT DROITE
		while(lignePion - i - 1 > -1 && colonnePion + j + 1 < 10) {
			//Si un pion ou une dame de la même couleur que la dame bloque la diagonale on arrête la recherche
			if(plateau[lignePion - i][colonnePion + j].equals(symbole) || plateau[lignePion - i][colonnePion + j].equals(symbolePionAllie)) {
				trouve1 = true;
			}
			//Sinon si deux pions adverses au moins ou un pion adverse pus un pion allié sont à la suite en diagonale, ils la bloquent, on arrête la recherche
			else if(!trouve1 && lignePion - i - 1 > -1 && colonnePion + j + 1 < 10 && (plateau[lignePion - i][colonnePion + j].equals(symbolePionAdverse) || plateau[lignePion - i][colonnePion + j].equals(symboleDameAdverse)) && !plateau[lignePion - i - 1][colonnePion + j + 1].equals("   ")){
				trouve1 = true;
			}
			//Sinon si un pion ou une dame adverse est sur la diagonale avec une case vide derrière
			else if(!trouve1 && lignePion - i - 1 > -1 && colonnePion + j + 1 < 10 && (plateau[lignePion - i][colonnePion + j].equals(symbolePionAdverse) || plateau[lignePion - i][colonnePion + j].equals(symboleDameAdverse)) && plateau[lignePion - i - 1][colonnePion + j + 1].equals("   ")) {
				casesPossibles.add(new Coup(new Case(lignePion - i - 1,colonnePion + j + 1))); //On ajoute ce coup dans les coups possibles
				trouve1 = true;
				k = 1;
				//Pour toutes les autres cases de la diagonales derrière le pion adverse trouvé et tant qu'il n'y a pas d'autres pions et qu'on est pas sorti du plateau
				while(lignePion - i - 1 - k > -1 && colonnePion + j + 1 + k < 10 && plateau[lignePion - i - 1 - k][colonnePion + j + 1 + k].equals("   ")) {
					//Si elle est vide on l'ajoute aussi aux cases possibles
					if(plateau[lignePion - i - 1 - k][colonnePion + j + 1 + k].equals("   ")) {
						casesPossibles.add(new Coup(new Case(lignePion - i - 1 - k,colonnePion + j + 1 + k)));
					}
					k++;
				}
			}
			j++;
			i++;
		}
		
		//DIAGONALE HAUT GAUCHE
		i = 1;
		j = 1;
		while(lignePion - i - 1 > -1 && colonnePion - j - 1 > -1) {
			//Si un pion de la même couleur que la dame bloque la diagonale on arrête la recherche
			if(plateau[lignePion - i][colonnePion - j].equals(symbole) || plateau[lignePion - i][colonnePion - j].equals(symbolePionAllie)) {
				trouve2 = true;
			}
			//Sinon si deux pions adverses au moins ou un pion adverse pus un pion allié sont à la suite en diagonale, ils la bloquent, on arrête la recherche
			else if(!trouve2 && lignePion - i - 1 > -1 && colonnePion - j - 1 < 10 && (plateau[lignePion - i][colonnePion - j].equals(symbolePionAdverse) || plateau[lignePion - i][colonnePion - j].equals(symboleDameAdverse)) && !plateau[lignePion - i - 1][colonnePion - j - 1].equals("   ")){
				trouve2 = true;
			}
			else if(!trouve2 && lignePion - i - 1 > -1 && colonnePion - j - 1 > -1 && (plateau[lignePion - i][colonnePion - j].equals(symbolePionAdverse) || plateau[lignePion - i][colonnePion - j].equals(symboleDameAdverse)) && plateau[lignePion - i - 1][colonnePion - j - 1].equals("   ")) {
				casesPossibles.add(new Coup(new Case(lignePion - i - 1 ,colonnePion - j - 1)));
				trouve2 = true;
				k = 1;
				//Pour toutes les autres cases de la diagonales derrière le pion adverse trouvé et tant qu'il n'y a pas d'autres pions
				while(lignePion - i - 1 - k > -1 && colonnePion - j - 1 - k > - 1 && plateau[lignePion - i - 1 - k][colonnePion - j - 1 - k].equals("   ")) {
					//Si elle est vide on l'ajoute aussi aux cases possibles
					if(plateau[lignePion - i - 1 - k][colonnePion - j - 1 - k].equals("   ")) {
						casesPossibles.add(new Coup(new Case(lignePion - i - 1 - k,colonnePion - j - 1 - k)));
					}
					k++;
				}
			}
			j++;
			i++;
		}
		
		//DIAGONALE BAS DROITE
		i = 1;
		j = 1;
		while(lignePion + i + 1 < 10 && colonnePion + j + 1 < 10) {
			//Si un pion de la même couleur que la dame bloque la diagonale on arrête la recherche
			if(plateau[lignePion + i][colonnePion + j].equals(symbole) || plateau[lignePion + i][colonnePion + j].equals(symbolePionAllie)) {
				trouve3 = true;
			}
			//Sinon si deux pions adverses au moins ou un pion adverse pus un pion allié sont à la suite en diagonale, ils la bloquent, on arrête la recherche
			else if(!trouve3 && lignePion + i + 1 > 1 && colonnePion + j + 1 < 10 && (plateau[lignePion + i][colonnePion + j].equals(symbolePionAdverse) || plateau[lignePion + i][colonnePion + j].equals(symboleDameAdverse)) && !plateau[lignePion + i + 1][colonnePion + j + 1].equals("   ")){
				trouve3 = true;
			}
			else if(!trouve3 && lignePion + i + 1 < 10 && colonnePion + j + 1 < 10 && (plateau[lignePion + i][colonnePion + j].equals(symbolePionAdverse) || plateau[lignePion + i][colonnePion + j].equals(symboleDameAdverse)) && plateau[lignePion + i + 1][colonnePion + j + 1].equals("   ")) {
				casesPossibles.add(new Coup(new Case(lignePion + i + 1 ,colonnePion + j + 1 )));
				trouve3 = true;
				k = 1;
				//Pour toutes les autres cases de la diagonales derrière le pion adverse trouvé et tant qu'il n'y a pas d'autres pions
				while(lignePion + i + 1 + k < 10 && colonnePion + j + 1 + k < 10 && plateau[lignePion + i + 1 + k][colonnePion + j + 1 + k].equals("   ")) {
					//Si elle est vide on l'ajoute aussi aux cases possibles
					if(plateau[lignePion + i + 1 + k][colonnePion + j + 1 + k].equals("   ")) {
						casesPossibles.add(new Coup(new Case(lignePion + i + 1 + k,colonnePion + j + 1 + k)));
					}
					k++;
				}
			}
			j++;
			i++;
		}
		
		//DIAGONALE BAS GAUCHE
		i = 1;
		j = 1;
		while(lignePion + i + 1 < 10 && colonnePion - j - 1 > -1) {
			//Si un pion de la même couleur que la dame bloque la diagonale on arrête la recherche
			if(plateau[lignePion + i][colonnePion - j].equals(symbole) || plateau[lignePion + i][colonnePion - j].equals(symbolePionAllie)) {
				trouve4 = true;
			}
			//Sinon si deux pions adverses au moins ou un pion adverse pus un pion allié sont à la suite en diagonale, ils la bloquent, on arrête la recherche
			else if(!trouve4 && lignePion + i + 1 > 1 && colonnePion - j - 1 < 10 && (plateau[lignePion + i][colonnePion - j].equals(symbolePionAdverse) || plateau[lignePion + i][colonnePion - j].equals(symboleDameAdverse)) && !plateau[lignePion + i + 1][colonnePion - j - 1].equals("   ")){
				trouve4 = true;
			}
			else if(!trouve4 && lignePion + i + 1 < 10 && colonnePion - j - 1 > -1 && (plateau[lignePion + i][colonnePion - j].equals(symbolePionAdverse) || plateau[lignePion + i][colonnePion - j].equals(symboleDameAdverse)) && plateau[lignePion + i + 1][colonnePion - j - 1].equals("   ")) {
				casesPossibles.add(new Coup(new Case(lignePion + i + 1,colonnePion - j - 1)));
				trouve4 = true;
				k = 1;
				//Pour toutes les autres cases de la diagonales derrière le pion adverse trouvé et tant qu'il n'y a pas d'autres pions
				while(lignePion + i + 1 + k < 10 && colonnePion - j - 1 - k > -1 && plateau[lignePion + i + 1 + k][colonnePion - j - 1 - k].equals("   ")) {
					//Si elle est vide on l'ajoute aussi aux cases possibles
					if(plateau[lignePion + i + 1 + k][colonnePion - j - 1 - k].equals("   ")) {
						casesPossibles.add(new Coup(new Case(lignePion + i + 1 + k,colonnePion - j - 1 - k)));
					}
					k++;
				}
			}
			j++;
			i++;
		}	
	
			
		return casesPossibles;
	}
	
	
	/*
	 * Cette méthode permet à la dame de manger un ou plusieurs pion adverse selon le coup passé en paramètre
	 * entrée : String[][] plateau, le plateau de jeu actuel
	 *          Coup coup, le coup que la dame doit effectuer
	 * sortie : ArrayList<Pion>, la liste des pions manger pendant ce coup
	 */
	public ArrayList<Pion> manger(String[][] plateau, Coup coup) {
		ArrayList<Pion> pionMange = new ArrayList<Pion>(); //Liste des pions mangés
		int lignePion;
		int colonnePion;
		String symboleAdverse = symbole.equals(" O ")? " x " : " o ";
		String symboleDameAdverse = symbole.equals(" O ")? " X " : " O ";
		
		//On récupère la liste des pions adverse
		ArrayList<Pion> listePionAdverse = symbole.equals(" O ")? this.jeu.getPionsNoirs() : this.jeu.getPionsBlancs();
		//Pour chaque Case du Coup en paramètre
		for(int i = 0; i < coup.taille(); i++) {
			lignePion = caseActuelle.getLigne(); 
			colonnePion = caseActuelle.getColonne();
			//On avance la dame sur sa nouvelle Case
			this.avancer(plateau, coup.get(i));
			//Variables pour connaitre les cases à supprimer entre la dame et sa nouvelle case
			int sensLigne = (coup.get(i).getLigne() > lignePion)? 1 : -1;
			int sensColonne = (coup.get(i).getColonne() > colonnePion)? 1 : -1;
			//Pour toutes les cases entre l'ancienne Case de la dame et la Case sur laquelle elle vient de manger
			for(int j = lignePion , k = colonnePion ; j != coup.get(i).getLigne() && k != coup.get(i).getColonne(); j+= sensLigne, k += sensColonne) {
				//Si on trouve un pion adverse, c'est un pion qu'on vient de manger
				if(plateau[j][k].equals(symboleAdverse)) {
					pionMange.add(new Pion(new Case(j,k),symboleAdverse,this.jeu));
				//Sinon si on trouve une dame adverse, c'est une dame qu'on vient de manger
				}else if(plateau[j][k].equals(symboleDameAdverse)) {
					pionMange.add(new Dame(new Case(j,k),symboleDameAdverse,this.jeu));
				}
				plateau[j][k] = "   "; //On enlève le pion mange du plateau
				Pion pionAdverse = new Pion(new Case(j,k),symboleAdverse,jeu);
				if(listePionAdverse.contains(pionAdverse)){ //Puis ici, on l'enleve de la liste des pions adverses si c'est un pion
					listePionAdverse.remove(pionAdverse);
				}else if(listePionAdverse.contains(new Dame(new Case(j,k),symboleDameAdverse,jeu))){ //Et ici si c'est une dame
					listePionAdverse.remove(new Dame(new Case(j,k),symboleDameAdverse,jeu));
				}
			}
		}
		return pionMange;
	}
}
