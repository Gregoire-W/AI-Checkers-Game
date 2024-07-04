/*
 * Cette classe à pour objectif de pouvoir simuler une partie de dame entre deux joueur (IA ou non)
 */
package premier_paquet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Partie {
	
	private Jeu jeuDeDames;
	
	private Scanner input ;
	
	public Partie(Jeu jeuDeDames) {
		this.jeuDeDames = jeuDeDames;
		input = new Scanner(System.in);
	}
	
	
	/*
	 * Cette méthode teste si un des deux joueurs a gagné la partie
	 * entrée : String couleur, la couleur du joueur dont on veut vérifier la victoire
	 * sortie : boolean, true si le joueur a gagné, false sinon
	 */
	public boolean aGagne(String couleur) {
		String couleurAdverse = couleur.equals("blanc")? "noir" : "blanc";
		if(this.jeuDeDames.coupsPossibles(couleurAdverse).size() == 0) {
			return true;
		}
		return false;
	}
	
	
	/*
	 * Cette méthode lance la partie de dames en demandant à l'utilisateur si il veut ajouter des IA et si oui leur niveau
	 * entrée : aucune
	 * sortie : aucune
	 */
	public void lancerPartie() {
		int tour = 0;
		ArrayList<Integer> joueurIA = new ArrayList<Integer>();
		
		System.out.println("Combien de partie seront jouees a la suite ?");
		int nbPartie = input.nextInt();
		
		System.out.println("Combien d'IA joueront cette partie ? (0, 1 ou 2)");
		int nbIA = input.nextInt();
		if(nbIA == 1) {
			System.out.println("L'IA sera le joueur blanc ou le joueur noir ? (ce choix sera conserve pour chaque partie veuillez repondre blanc ou noir)");
			String couleurIA = input.next();
			if(couleurIA.equals("blanc")) {
				joueurIA.add(0);
			}else if(couleurIA.equals("noir")) {
				joueurIA.add(1);
			}
		}else if(nbIA == 2){
				joueurIA.add(0);
				joueurIA.add(1);
		}
		ArrayList<String> niveauIA = new ArrayList<String>();
		for(int i = 0; i < joueurIA.size(); i++) {
			System.out.println("Quel niveau voulez vous pour la " + (int)(i+1) + "e IA (aleatoire / facile / moyen / difficile)");
			niveauIA.add(new Scanner(System.in).next());
		}
		
		//On compte le nombre de victoire de chaque joueur et le nombre d'égalité
		int vicBlanc = 0;
		int vicNoir = 0;
		int egalite = 0;
		//On joue autant de partie que l'a voulu l'utilisateur
		for(int k = 0; k < nbPartie; k++) {
			System.out.println("Partie numero " + k);
			//Tant qu'aucun des joueurs n'a gagné ou que 200 tours n'ont pas été joués (pour éviter les parties infinies des IAs)
			while(!aGagne("blanc") && !aGagne("noir") && tour < 200) {
				tour++;
				//on lance le tour de jeu
				this.tourDeJeu(tour,joueurIA, niveauIA);
			}
			//A la fin d'une partie on vérifie qui a gagné pour garder les comptes
			if(aGagne("blanc")) {
				vicBlanc++;
			}else if(aGagne("noir")) {
				vicNoir++;
			}else {
				System.out.println("Egalite");
				egalite++;
			}
			//On réinitialise le jeu pour relancer une partie
			this.jeuDeDames = new Jeu();
			System.out.println("nombre de tour de la partie : " + tour);
			tour = 1;
		}
		//On affiche les statistiques de la totalité des parties
		System.out.println("Le joueur blanc a gagne " + vicBlanc + " partie(s)");
		System.out.println("Le joueur noir a gagne " + vicNoir + " partie(s)");
		System.out.println("Il y a eu " + egalite + " egalites");
	}
	
	/*
	 * Cette méthode joue un tour de jeu entier, c'est a dire un coup par joueur
	 * entrée : aucune
	 * sortie : booolean, true si le jeu continue, false si un joueur a gagne
	 */
	public boolean tourDeJeu(int tour, ArrayList<Integer> joueurIA, ArrayList<String> niveauIA) {
		ArrayList<String> couleurs = new ArrayList<String>();
		couleurs.add("blanc");
		couleurs.add("noir");
		//Si il y a au moins un joueur humain dans la partie on affiche le tour
		if(joueurIA.size() != 2) {
			System.out.println("Tour numero " + tour);
		}
		for(int i = 0; i<2; i++) {
			//Pour les joueur humain on affiche le plateau de jeu
			if(!joueurIA.contains(i)) {
				jeuDeDames.afficherPlateau();
			}
			//Si le joueur est une IA elle effectue le coup d'une IA
			if(joueurIA.contains(i)) {
				this.coupIA(couleurs.get(i), niveauIA.get(i));
			//Sinon c'est un humain qui jouera
			}else {
				this.coupJoueur(couleurs.get(i));
			}
			//On vérifie si un des deus joueurs a gagne
			if(this.jeuDeDames.aGagne(couleurs.get(i))) {
				//Si il y a au moins un joueur humain
				if(joueurIA.size() != 2) {
					//On affiche le gagnant
					this.jeuDeDames.finJeu(couleurs.get(i));
				}
				//Puis on arrête le jeu
				return false; 
			}
		}
		return true;
		
	}
	
	/*
	 * Cette méthode joue un coup pour une IA en fonction de son niveau passé en paramètre
	 * entrée : String couleur, la couleur de l'IA qui joue le coup
	 *          String niveau, le niveau de l'IA qui joue le coup
	 * sortie : aucune
	 */
	public void coupIA(String couleur, String niveau) {
		IA ia = new IA();
		//Si le niveau est aléatoire on fait jouer l'IA aléatoire
		if(niveau.equals("aleatoire")) {
			//On choisit le coup que l'IA va jouer
			HashMap<Pion,Coup> coupEtPionIA = ia.aleatoire(jeuDeDames,couleur);
			for(Pion pion : coupEtPionIA.keySet()) {
				//Si c'est une prise on prend
				if(pion.peutManger(jeuDeDames.getPlateau()).size() != 0){
					pion.manger(jeuDeDames.getPlateau(), coupEtPionIA.get(pion));
				//Sinon on avance
				}else {
					pion.avancer(jeuDeDames.getPlateau(), coupEtPionIA.get(pion).get(0));
				}
				//On teste si a l'issus du coup le pion peut devenir une dame
				this.jeuDeDames.testDame(pion);
			}
		//Sinon si le niveau est autre qu'aléatoire
		}else {
			//On choisi le coup qu'on va joueur en fonction du niveau d'IA rentré
			HashMap<Pion,Coup> coupEtPionIA = ia.maxDecision(jeuDeDames, -1000000000, 1000000000, couleur, niveau);
			for(Pion pion : coupEtPionIA.keySet()) {
				//Si c'est une prise on prend
				if(pion.peutManger(jeuDeDames.getPlateau()).size() != 0){
					pion.manger(jeuDeDames.getPlateau(), coupEtPionIA.get(pion));
				//Sinon on avance
				}else {
					pion.avancer(jeuDeDames.getPlateau(), coupEtPionIA.get(pion).get(0));
				}
				//On teste si a l'issus du coup le pion peut devenir une dame
				this.jeuDeDames.testDame(pion);
			}
		}
	}
	
	/*
	 * Cette méthode joue un coup pour un joueur humain 
	 * entrée : String couleur, la couleur du joueur qui joue le coup
	 * sortie : aucune
	 */
	public void coupJoueur(String couleur) {
		int indexCase = -1;
		int indexPion = -1;
		int numeroPion = 1;
		Pion pion = null;
		//On calcule tous les coups possibles pour le joueur
		HashMap<Pion,ArrayList<Coup>> coupsPossibles =  jeuDeDames.coupsPossibles(couleur);
		do {
			//On affiche au joueur les pions qu'il peut déplacer
			System.out.println("Pions pouvant bouger : ");
			for(Pion pion2 : coupsPossibles.keySet()) {
				System.out.println(numeroPion + ") " + pion2);
				numeroPion++;
			}
			ArrayList<Pion> listePion = new ArrayList<Pion>();
			listePion.addAll(coupsPossibles.keySet());
			while(indexPion > listePion.size() + 1 || indexPion < 0) {
				try {
					indexPion = input.nextInt() - 1;
					pion = listePion.get(indexPion);
				}catch(Exception e) {
					System.out.println("Veuillez rentrer une valeur correcte");
					indexCase = -1;
					input.nextLine();
				}
			}
			//Si sa réponse n'est pas valide on lui indique
			if(coupsPossibles.get(pion).size() == 0) {
				System.out.println("Ce pion ne peut pas bouger veuillez en choisir un autre");
			}else if(!coupsPossibles.containsKey(pion)){
				System.out.println("Ce pion n'existe pas");
			//Sinon on lui demande quel coup il compte jouer pour le pion choisi
			}else {
				ArrayList<Integer> numeroCoup = new ArrayList<Integer>();
				System.out.println("Quel coup voulez-vous jouer ?");
				for(int i =0; i < coupsPossibles.get(pion).size(); i++) {
					numeroCoup.add(i);
					System.out.println((int)(i+1) + ")" + coupsPossibles.get(pion).get(i) + "\n");
				}
				while(!numeroCoup.contains(indexCase)) {
					try {
						indexCase = input.nextInt() - 1;
						if(!numeroCoup.contains(indexCase)) {
							System.out.println("Veuillez rentrer une valeur correcte");
						}
					}catch(Exception e) {
						System.out.println("Veuillez rentrer une valeur correcte");
						indexCase = -1;
						input.nextLine();
					}
				}
			}
		//Tant que le coup choisi n'est pas valide on continue 
		}while(!coupsPossibles.keySet().contains(pion) || !coupsPossibles.get(pion).contains(coupsPossibles.get(pion).get(indexCase)));
		//Si le coup choisi est un coup de prise
		if(pion.peutManger(jeuDeDames.getPlateau()).size() != 0){
			//Le pion mange
			pion.manger(jeuDeDames.getPlateau(), coupsPossibles.get(pion).get(indexCase));
		//Sinon le pion se déplace
		}else {
			pion.avancer(jeuDeDames.getPlateau(), coupsPossibles.get(pion).get(indexCase).get(0));
		}
		//Enfin on test si le pion devient une dame à la suite de son coup
		this.jeuDeDames.testDame(pion);
	}
	
	/*
	 * Cette méthode ferme le scanner ouvert dans cette classe
	 * entrée : aucune
	 * sortie : aucune
	 */
	public void finDeJeu() {
		input.close();
	}
	
}

