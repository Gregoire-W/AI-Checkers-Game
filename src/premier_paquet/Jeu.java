/*
 * Cette classe à pour objectif de simuler un jeu de dames et contient donc son plateau, la liste des pions ainsi que des méthodes qui implémente 
 * les règles du jeu
 */
package premier_paquet;

import java.util.ArrayList;
import java.util.HashMap;

public class Jeu {
	
	private String[][] plateau;
	private ArrayList<Pion> pionsNoirs;
	private ArrayList<Pion> pionsBlancs;
	
	public Jeu() {
		initialiserJeu();
	}
	
	public Jeu(String[][] plateau,ArrayList<Pion> pionsBlancs,ArrayList<Pion> pionsNoirs) {
		this.plateau = plateau;
		this.pionsBlancs = pionsBlancs;
		this.pionsNoirs = pionsNoirs;
	}
	
	/*
	 * Cette méthode permet d'initialiser un plateau de jeu en plaçant les différents pions à leurs positions de départ
	 * entrée : aucune
	 * sortie : aucune
	 */
	public void initialiserPlateau() {
		this.plateau = new String[10][10];
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				switch(i) {
				case 0 :
					plateau[i][j] = (j%2 == 0)? "   " : " x ";
					break;
				case 2 :
					plateau[i][j] = (j%2 == 0)? "   " : " x ";
					break;
				case 1 :
					plateau[i][j] = (j%2 != 0)? "   " : " x ";
					break;
				case 3 :
					plateau[i][j] = (j%2 != 0)? "   " : " x ";
					break;
				case 6 :
					plateau[i][j] = (j%2 == 0)? "   " : " o ";
					break;
				case 8 :
					plateau[i][j] = (j%2 == 0)? "   " : " o ";
					break;
				case 7 :
					plateau[i][j] = (j%2 != 0)? "   " : " o ";
					break;
				case 9 :
					plateau[i][j] = (j%2 != 0)? "   " : " o ";
					break;
				default :
					plateau[i][j] = "   ";
				}
			}
		}
	}
	
	/*
	 * Cette méthode permet d'afficher le plateau relié à ce jeu
	 * entrée : aucune
	 * sortie : aucune
	 */
	public void afficherPlateau() {
		StringBuffer affichage = new StringBuffer("  0   1   2   3   4   5   6   7   8   9");
		for(int i = 0; i < 10; i++) {
			affichage.append("\n-----------------------------------------\n|");
			for(int j = 0; j < 10; j++) {
				affichage.append(plateau[i][j] + "|");
			}
			affichage.append(" " + i);
		}
		affichage.append("\n-----------------------------------------");
		System.out.println(affichage);
	}
	
	/*
	 * Cette méthode permet d'initialiser les listes de pions blancs et noirs comme ils sont placés sur le plateau au début de la partie
	 * entrée : aucune
	 * sortie : aucune
	 */
	public void initialiserPions() {
		pionsNoirs = new ArrayList<Pion>();
		pionsBlancs = new ArrayList<Pion>();
		for(int i = 0; i <10; i++) {
			for(int j = 0; j<5; j++) {
				if(i%2 == 0) {
					if(i<4) {
						pionsNoirs.add(new Pion(new Case(i,2*j+1)," x ",this));
					}else if(i>5){
						pionsBlancs.add(new Pion(new Case(i,2*j+1)," o ",this));
					}
				}else {
					if(i<4) {
						pionsNoirs.add(new Pion(new Case(i,2*j)," x ",this));
					}else if(i>5){
						pionsBlancs.add(new Pion(new Case(i,2*j)," o ",this));
					}
				}
			}
		}
	}
	
	/*
	 * Cette méthode appelle l'initialisation du plateau de jeu et des listes de pion pour initialiser le jeu
	 * entrée : aucune
	 * sortie : aucune
	 */
	public void initialiserJeu() {
		initialiserPlateau();
		initialiserPions();
	}
	
	/*
	 * Cette méthode calcule tous les coups possibles pour le joueur de la couleur passé en paramètre
	 * cette méthode prend en compte toutes les règles du jeu de dame pour son calcul
	 * entrée : String couleur, la couleur du joueur pour lequel on veut connaître les coups possibles
	 * sortie : HashMap<Pion,ArrayList<Coup>>, le dictionnaire contenant comme clefs les pions et comme valeurs les coups reliés à chaque pion
	 */
	public HashMap<Pion,ArrayList<Coup>> coupsPossibles(String couleur){
		HashMap<Pion,ArrayList<Coup>> coupsPossibles = new HashMap<Pion,ArrayList<Coup>>();
		ArrayList<Pion> listePion = (couleur.equals("blanc"))? pionsBlancs : pionsNoirs;
		boolean trouveManger = false;
		
		for(int i = 0; i<listePion.size(); i++) { //Pour chaque pion du joueur courants
			if(listePion.get(i).peutManger(plateau).size() > 0) { //Si le pion testé peut manger un ou plusieurs autres pions 
				if(trouveManger == false) { //Si c'est le premier trouvé coup où on peut manger un pion
					coupsPossibles.clear(); //On vide le dictionnaire des potentiels coups de déplacements
					trouveManger = true; //On ne rechere plus que les coups où on peut manger un pion adverse
				}
				coupsPossibles.put(listePion.get(i), listePion.get(i).peutManger(plateau)); //On ajoute ces coups à ce pion
			}else if(!trouveManger) { //Si on a pas encore trouvé de coups où on mange un pion advese on peut chercher les déplacements
				if(listePion.get(i).peutAvancer(plateau).size() != 0) { //Si le pion testé peut avancer d'une ou plusieurs cases
					coupsPossibles.put(listePion.get(i), listePion.get(i).peutAvancer(plateau)); //On ajoute ces coups à ce pion
				}
			}
		}
		if(trouveManger) { //Si coupsPossibles contient des coups de prise (où on mange des pions adverses)
			coupsPossibles = this.priseMajoritaire(coupsPossibles); //On ne garde que ceux où la prise est majoritaires
		}
		return coupsPossibles;
	}
	
	
	/*
	 * Cette méthode remplie la liste de coup qu'elle prend en entree avec tous les coups qui ont une prise majoritaire
	 * entrée : Pion pion, le pion qui teste ses prises multiples
	 *          ArrayList<Coup> listeCoup, la liste des coups qui ont une prise multiple maximale
	 *          Coup sauvegardeCoup, le coup pour lequel on teste la prise multiple
	 * sortie : HashMap<Pion,ArrayList<Coup>>, le dictionnaire contenant comme clefs les pions et comme valeurs les coups reliés à chaque pion
	 */
	public void coupMultiple(Pion pion, ArrayList<Coup> listeCoup, Coup sauvegardeCoup){
		ArrayList<Coup> listeCoupMax = new ArrayList<Coup>(); //Si il y a plusieurs coup avec la meme prise majoritaire (maximale) il faut tous les stocker
		int max = 0;
		
		if(pion.peutManger(plateau).size() == 0) {
			listeCoup.add(sauvegardeCoup);
		}
		if(pion.peutManger(plateau).size() == 1) { //Si le pion n'a pas le choix 
			Case sauvegardeCase = new Case(pion.getCaseActuelle().getLigne(),pion.getCaseActuelle().getColonne());
			Coup sauvegardePremierCoup = pion.peutManger(plateau).get(0);
			sauvegardeCoup.ajouter(pion.peutManger(plateau).get(0).get(0));
			ArrayList<Pion> pionMange = pion.manger(plateau, pion.peutManger(plateau).get(0));//Il mange le seul pion qu'il peut manger
			coupMultiple(pion, listeCoup, sauvegardeCoup); //Puis on regarde à sa position suivante si il peut en manger d'autre
			pion.annulerManger(plateau, sauvegardePremierCoup, sauvegardeCase, pionMange);
		}else if (pion.peutManger(plateau).size() > 1){ //Si il a le choix entre plusieurs coup
			for(int i = 0; i < pion.peutManger(plateau).size(); i++) { //Pour chacun de ces coup
				int priseMultiple = priseMultiple(pion, pion.peutManger(plateau).get(i)); //On la sauvegarde pour ne pas rappeler la fonction à chaque fois
				if(priseMultiple > max) { //Si il a la plus grande prise majoritaire
					max = priseMultiple; //Il devient le max
					listeCoupMax.clear(); //On enleve tous les autres coups sauvegardés
					listeCoupMax.add(pion.peutManger(plateau).get(i)); //Et on ajoute celui-ci
				}else if(this.priseMultiple(pion, pion.peutManger(plateau).get(i)) == max) { //Si il une prise majoritaire égale au max actuel
					listeCoupMax.add(pion.peutManger(plateau).get(i));
				}
			}
			for(int j = 1; j < listeCoupMax.size(); j++) { //Pour les "coups maxs" suivant, si il y en a 
				Coup nouvelleSauvegardeCoup = new Coup();//On créer un nouveau coup identique à l'actuel
				nouvelleSauvegardeCoup.getCoups().addAll(sauvegardeCoup.getCoups());
				Case sauvegardeCase3 = new Case(pion.getCaseActuelle().getLigne(),pion.getCaseActuelle().getColonne());
				nouvelleSauvegardeCoup.ajouter(listeCoupMax.get(j).get(0));
				ArrayList<Pion> pionMange3 = pion.manger(plateau, listeCoupMax.get(j)); //On fait manger le pion pour voir si il peut en manger d'autre ensuite
				coupMultiple(pion, listeCoup, nouvelleSauvegardeCoup); //on relance cette fonction avec le nouveau coup en argument
				pion.annulerManger(plateau, listeCoupMax.get(j), sauvegardeCase3, pionMange3);
			}
			Case sauvegardeCase2 = new Case(pion.getCaseActuelle().getLigne(),pion.getCaseActuelle().getColonne());
			ArrayList<Pion> pionMange2 = pion.manger(plateau, listeCoupMax.get(0)); //On fait manger le pion pour voir si il peut en manger d'autre ensuite
			sauvegardeCoup.ajouter(listeCoupMax.get(0).get(0));
			coupMultiple(pion, listeCoup, sauvegardeCoup); //on relance cette fonction avec le meme coup en argument pour le premier "coup max" testé
			pion.annulerManger(plateau, listeCoupMax.get(0), sauvegardeCase2, pionMange2);
		}
		
	}
	
	/*
	 * Cette méthode renvoie le nombre de pion adverse qu'un pion allié peut manger
	 * entrée : Pion pion, le pion qui teste ses prises multiples
	 *          Coup premierCoup, le premier coup de la prise multiple testée
	 * sortie : HashMap<Pion,ArrayList<Coup>>, le dictionnaire contenant comme clefs les pions et comme valeurs les coups reliés à chaque pion
	 */
	public int priseMultiple(Pion pion, Coup premierCoup) {
		int maximum = 2;
			
		//Si le pion ne peut pas manger de pions adverses on renvoie 0
		if(pion.peutManger(this.plateau).size() == 0) {
			return 0;
		}
		
		//Si le pion peut manger la case rentrée dans la fonction on le fait manger
		Case sauvegardeCase = new Case(pion.getCaseActuelle().getLigne(),pion.getCaseActuelle().getColonne());
		Coup sauvegardePremierCoup = premierCoup;
		ArrayList<Pion> pionMange = pion.manger(this.getPlateau(), premierCoup);
		//Si ensuite il ne peut plus rien manger on retourne 1 (il n'a mangé qu'un seul pion) 
		if(pion.peutManger(this.plateau).size() == 0) {
			pion.annulerManger(plateau, sauvegardePremierCoup, sauvegardeCase, pionMange);
			return 1;	
		}
		
		//Sinon si il peut encore manger on stock la case dans laquelle il pourra manger le plus de pion adverse (par défaut la première)
		//Puis on la cherche
		for(int i = 0; i<pion.peutManger(this.plateau).size(); i++) {
			//Si une case permet de manger plus de pion que la maximum actuel 
			if (priseMultiple(pion,pion.peutManger(this.plateau).get(i)) + 1 > maximum){
				//Elle devient le maximum
				maximum = priseMultiple(pion,pion.peutManger(this.plateau).get(i)) + 1;
				//Et on la garde dans la varibale caseMax
			}
		}
		pion.annulerManger(plateau, sauvegardePremierCoup, sauvegardeCase, pionMange);
		return maximum;
	}
	
	/*
	 * Cette méthode renvoie un dictoinnaire de tous les pions et de leur coups ayant une prise majoritaire
	 * entrée : HashMap<Pion,ArrayList<Coup>> prisesPossibles, toutes les prises possibles sans tenur compte de la règle de la prise majoritaire
	 * sortie : HashMap<Pion,ArrayList<Coup>>, le dictionnaire contenant comme clefs les pions et comme valeurs les coups reliés à chaque pion
	 */
	public HashMap<Pion,ArrayList<Coup>> priseMajoritaire(HashMap<Pion,ArrayList<Coup>> prisesPossibles){
		int maximum = 1;
		HashMap<Pion,ArrayList<Coup>> prisesMajoritaires = new HashMap<Pion,ArrayList<Coup>>();
		for(Pion pion : prisesPossibles.keySet()) {
			ArrayList<Coup> coupsMajoritaires = new ArrayList<Coup>();
			Coup coupMajoritaire = new Coup();
			this.coupMultiple(pion, coupsMajoritaires, coupMajoritaire);
			//Si la prise majoritaire des coups de ce pion est égal au maximum 
			if(coupsMajoritaires.get(0).taille() == maximum) {
				//on l'ajoute
				prisesMajoritaires.put(pion, coupsMajoritaires);
			//Sinon si la prise majoritaire des coups de ce pion est supérieur au maximum
			}else if(coupsMajoritaires.get(0).taille() > maximum) {
				//il devient le maximum
				maximum = coupsMajoritaires.get(0).taille();
				//On vide le dictionnaire des anciens coups majoritaires qui sont donc plus petit
				prisesMajoritaires.clear();
				//On ajoute celui-ci
				prisesMajoritaires.put(pion, coupsMajoritaires);	
			}
		}
		return prisesMajoritaires;
	}
		

	/*
	 * Cette méthode permet de récupérer le plateau de jeu
	 * entrée : aucune
	 * sortie : String[][], le plateau de jeu
	 */
	public String[][] getPlateau(){
		return plateau;
	}

	/*
	 * Cette méthode permet de la liste des pions noirs
	 * entrée : aucune
	 * sortie : ArrayList<Pion>, la liste des pions noirs
	 */
	public ArrayList<Pion> getPionsNoirs() {
		return pionsNoirs;
	}

	/*
	 * Cette méthode permet de la liste des pions blancs
	 * entrée : aucune
	 * sortie : ArrayList<Pion>, la liste des pions blancs
	 */
	public ArrayList<Pion> getPionsBlancs() {
		return pionsBlancs;
	}
	
	
	/*
	 * Cette méthode permet de dupliquer le jeu à sa posqition exacte (plateau et listes de pions comprises)
	 * entrée : aucune
	 * sortie : Jeu, le duplicat de ce jeu
	 */
	public Jeu dupliquerJeu() {
		ArrayList<Pion> nouvPionsBlancs = new ArrayList<Pion>();
		ArrayList<Pion> nouvPionsNoirs = new ArrayList<Pion>();
		nouvPionsBlancs.addAll(this.pionsBlancs);
		nouvPionsNoirs.addAll(this.pionsNoirs);
		String[][] plateauTemporaire = new String[10][10];
		for(int i = 0; i < this.plateau.length; i++) {
			for(int j = 0; j < this.plateau[i].length; j++) {
				plateauTemporaire[i][j] = this.plateau[i][j];
			}
		}
		return new Jeu(plateauTemporaire,nouvPionsBlancs, nouvPionsNoirs);
	}
	
	/*
	 * Cette méthode teste si un pion peut devenir une dame et le transforme si c'est le cas
	 * entrée : Pion pion, le pion à tester
	 * sortie : aucune
	 */
	public void testDame(Pion pion) {
		//On teste seulement si le pion n'est pas encore une dame
		if(!(pion instanceof Dame)) {
			int ligneMax = pion.getSymbole().equals(" o ")? 0 : 9; //On définit la ligne pour devenir une dame selon la couleur du pion
			if(pion.getCaseActuelle().getLigne() == ligneMax) {
				ArrayList<Pion> listePion = (pion.getSymbole().equals(" o "))? pionsBlancs : pionsNoirs;
				String symboleDame = pion.getSymbole().equals(" o ")? " O " : " X ";
				//On enleve le pion de la liste de pion
				listePion.remove(pion);
				//On le rajoute en tant que dame
				listePion.add(new Dame(pion.getCaseActuelle(),symboleDame,this));
				//On le change aussi dans le plateau
				plateau[pion.getCaseActuelle().getLigne()][pion.getCaseActuelle().getColonne()] = symboleDame;
			}
		}
	}
	
	/*
	 * Cette méthode teste si un des deux joueurs a gagné la partie
	 * entrée : String couleur, la couleur du joueur dont on veut vérifier la victoire
	 * sortie : boolean, true si le joueur a gagné, false sinon
	 */
	 public boolean aGagne(String couleur) {
		 String couleurAdverse = couleur.equals("blanc")? "noir" : "blanc";
		 if(this.coupsPossibles(couleurAdverse).size() == 0) {
			 return true;
		 }else {
			 return false;
		 }
	 }
	 
	 /*
		 * Cette méthode anonce la fin du jeu selon la couleur du gagnant
		 * entrée : String couleur, la couleur du joueur gagnant
		 * sortie : aucune
		 */
	 public void finJeu(String couleur) {
		 System.out.println("Fin de la partie : le joueur " + couleur + " a gagne");
	 }
}

