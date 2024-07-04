/*
 * Cette classe à pour objectif de créer des IAs de 3 niveaux différents pouvant jouer au jeu de dames ainsi qu'un joueur aléatoire
 * La méthode implémenter pour les IAs et l'algorithme MinMax avec élagage alpha-bêta
 */
package premier_paquet;
import java.util.Random;

import java.util.ArrayList;
import java.util.HashMap;

public class IA {
	
	
	/*
	 * Cette méthode permet de retourner un pion et un de ces coups de façon aléatoire parmi tous les coups possibles disponibles.  
	 * entrée : Jeu jeu, le jeu dans lequel l'IA jouera 
	 *          String couleur, (blanc ou noir) qui précise la couleur de l'IA pour laquelle on cherche la liste de coup
	 * sortie : HashMap<Pion,Coup>, le dictionnaire contenant le pion et le coup
	 */
	public HashMap<Pion,Coup> aleatoire(Jeu jeu, String couleur){
		//On calcule tous les coups possibles 
		HashMap<Pion, ArrayList<Coup>> coupPossible = jeu.coupsPossibles(couleur);
		HashMap<Pion,Coup> coupEtPion = new HashMap<Pion,Coup>();
		
		Object[] pions = coupPossible.keySet().toArray();
		Random random = new Random();
		//Puis de façon aléatoire on choisi un des pions disponibles
		int indexPion = random.nextInt(pions.length);
		Pion randomPion = (Pion) pions[indexPion];
		//Et de façon aléatoire aussi un des coups de ce pion
		int indexCoup = random.nextInt(coupPossible.get(randomPion).size());
		
		coupEtPion.put(randomPion, coupPossible.get(randomPion).get(indexCoup));
		return coupEtPion;
	}
	
	/*
	 * Cette méthode permet de retourner un pion et un de ses coups choisi au hasard parmi ceux qui ont la meilleure utilitée
	 * entrée : Jeu jeu, le jeu dans lequel l'IA jouera 
	 *          int alpha, qui fait référence au alpha de l'algorithme utilisé (MinMax avec élagage alpha-bêta)
	 *          int beta, qui fait référence au beta de l'algorithme utilisé (MinMax avec élagage alpha-bêta)
	 *          String couleur, (blanc ou noir) qui précise la couleur de l'IA pour laquelle on cherche la liste de coup
	 *          String niveau, (facile, moyen ou difficile) qui définira la difficulté de l'IA. 
	 * sortie : HashMap<Pion,Coup>, le dictionnaire contenant le pion et le coup
	 */
	public HashMap<Pion,Coup> maxDecision(Jeu jeu, int alpha, int beta, String couleur, String niveau) {
		//On défini tout d'abord la profondeur voulue selon la difficulté passée en paramètre
		int profondeur = 2;
		if(niveau.equals("facile")) {
			profondeur = 2;
		}else if(niveau.equals("moyen")) {
			profondeur = 4;
		}else if(niveau.equals("difficile")) {
			profondeur = 4;
		}
		String couleurAdverse = couleur.equals("blanc")? "noir" : "blanc";
		int max = -1000000000;
		HashMap<Pion,ArrayList<Coup>> coupEtPion = new HashMap<Pion,ArrayList<Coup>>();
		ArrayList<Pion> pionMange = new ArrayList<Pion>();
		//boolean qui permet de savoir si le pion a avancé ou mangé
		boolean aAvancer = false;
		
		HashMap<Pion, ArrayList<Coup>> coupPossible = jeu.coupsPossibles(couleur);
		//Pour chaque pion qu'on peut bouger
		for(Pion pion : coupPossible.keySet()) {
			//Pour chacun des coups d'un pion
			for(int i = 0; i<coupPossible.get(pion).size(); i++) {
				//On sauvegarde la case et le coup afin de pouvoir les annuler après les avoir testés 
				Case sauvegardeCase = new Case(pion.getCaseActuelle().getLigne(),pion.getCaseActuelle().getColonne());
				Coup sauvegardeCoup = coupPossible.get(pion).get(i);
				//Si le pion ne peut pas manger avec ce coup on le fait avancer
				if(pion.peutManger(jeu.getPlateau()).size() == 0) {
					pion.avancer(jeu.getPlateau(), coupPossible.get(pion).get(i).get(0));
					aAvancer = true;
				//Sinon il peut manger, on le fait manger
				}else {
					pionMange = pion.manger(jeu.getPlateau(), coupPossible.get(pion).get(i));
				}
				//On calcule la valeur du plateau une fois que le coup à été joué en commençant par minValue puisque c'est au joueur adverse de jouer
				int valeurCoup = minValue(jeu, alpha, beta, profondeur - 1, couleurAdverse, niveau);
				//Si le coup nous a fait avancer on l'annule ici
				if(aAvancer) {
					pion.annulerAvancer(jeu.getPlateau(), sauvegardeCase);
				//Sinon si le coup nous a fait manger on l'annule ici
				}else{
					pion.annulerManger(jeu.getPlateau(), sauvegardeCoup, sauvegardeCase, pionMange);
				}
				//Si la valeur de plateau obtenue est égale au maximum
				if(valeurCoup == max) {
					if(!coupEtPion.containsKey(pion)) {
						coupEtPion.put(pion, new ArrayList<Coup>());
					}
					//On ajoute ce coup dans la liste des coups avec la meilleure utilité
					coupEtPion.get(pion).add(coupPossible.get(pion).get(i));
				//Sinon si la valeur de plateau obtenue est supérieure au maximum
				}else if(valeurCoup > max) {
					//Le maximum devient cette valeur
					max = valeurCoup;
					//On supprime tous les anciens coups trouvés de  la liste des coups avec la meilleure utilité
					coupEtPion.clear();
					//Et on ajoute celui-ci
					coupEtPion.put(pion, new ArrayList<Coup>());
					coupEtPion.get(pion).add(coupPossible.get(pion).get(i));
				}
				aAvancer = false;
				//Si alpha est inférieur au maximum alors il prend sa valeur
				alpha = (alpha > max)? alpha : max;
			}
		}
		//On renvoie un pion et un coup qui lui est associé de la liste des coups avec la meilleure utilité
		HashMap<Pion,Coup> coupAleatoire = new HashMap<Pion,Coup>();
		Object[] pions = coupEtPion.keySet().toArray();
		Random random = new Random();
		
		int indexPion = random.nextInt(pions.length);
		Pion randomPion = (Pion) pions[indexPion];
		int indexCoup = random.nextInt(coupEtPion.get(randomPion).size());
		
		coupAleatoire.put(randomPion, coupEtPion.get(randomPion).get(indexCoup));
		return coupAleatoire;
	}
	
	
	/*
	 * Cette méthode permet de un entier qui correspond à la plus petite valeur de plateau que pourra obtenir son adversaire parmi tous les coups qu'il peut jouer
	 * entrée : Jeu jeu, le jeu dans lequel l'IA jouera 
	 *          int alpha, qui fait référence au alpha de l'algorithme utilisé (MinMax avec élagage alpha-bêta)
	 *          int beta, qui fait référence au beta de l'algorithme utilisé (MinMax avec élagage alpha-bêta)
	 *          int profondeur, qui correspond à la profondeur qu'il reste à parcourir dans l'arbre de MinMax
	 *          String couleur, (blanc ou noir) qui précise la couleur de l'IA pour laquelle on cherche la liste de coup
	 *          String niveau, (facile, moyen ou difficile) qui définira la difficulté de l'IA. 
	 * sortie : int, la plus petite valeur de plateau que pourra obtenir son adversaire parmi tous les coups qu'il peut jouer
	 */
	public int minValue(Jeu jeu, int alpha, int beta, int profondeur, String couleur,String niveau) {
		String couleurAdverse = couleur.equals("blanc")? "noir" : "blanc";
		int min = 1000000;
		HashMap<Pion, ArrayList<Coup>> coupPossible = jeu.coupsPossibles(couleur);
		ArrayList<Pion> pionMange = new ArrayList<Pion>();
		//boolean qui permet de savoir si le pion a avancé ou mangé
		boolean aAvancer = false;
		
		//Pour chaque pion
		for(Pion pion : coupPossible.keySet()) {
			//Pour chacun des coups d'un pion
			for(int i = 0; i<coupPossible.get(pion).size(); i++) {
				//On sauvegarde la case et le coup afin de pouvoir les annuler après les avoir testés 
				Case sauvegardeCase = new Case(pion.getCaseActuelle().getLigne(),pion.getCaseActuelle().getColonne());
				Coup sauvegardeCoup = coupPossible.get(pion).get(i);
				//Si le pion ne peut pas manger avec ce coup on le fait avancer
				if(pion.peutManger(jeu.getPlateau()).size() == 0) {
					pion.avancer(jeu.getPlateau(), coupPossible.get(pion).get(i).get(0));
					aAvancer = true;
				//Sinon il peut manger, on le fait manger
				}else {
					pionMange = pion.manger(jeu.getPlateau(), coupPossible.get(pion).get(i));
				}
				//On calcule la valeur du plateau une fois que le coup à été joué avec maxVal puisque c'est de nouveau au joueur qui teste de jouer
				int valeurCoup = maxValue(jeu, alpha, beta, profondeur - 1, couleurAdverse, niveau);
				//Si le minimum est plus grand que la valeur du plateau obtenue le minimum devient cette valeur
				min = min < valeurCoup? min : valeurCoup;
				//Si le coup nous a fait avancer on l'annule ici
				if(aAvancer) {
					pion.annulerAvancer(jeu.getPlateau(), sauvegardeCase);
				//Sinon si le coup nous a fait manger on l'annule ici
				}else {
					pion.annulerManger(jeu.getPlateau(), sauvegardeCoup, sauvegardeCase, pionMange);
				}
				aAvancer = false;
				//Si jamais le minimum est inférieur ou égale à alpha ça ne sert plus à rien d'explorer cette branche de l'arbre on la "coupe"
				if(min <= alpha) {
					return min;
				}
				//Si beta est supérieur au minimum il prend sa valeur
				beta = (beta < min)? beta : min;
			}
		}
		return min;
	}
	
	/*
	 * Cette méthode permet de retourner un entier qui correspond à la plus grande valeur de plateau que pourra obtenir ce joueur parmi tous les coups qu'il peut jouer
	 * entrée : Jeu jeu, le jeu dans lequel l'IA jouera 
	 *          int alpha, qui fait référence au alpha de l'algorithme utilisé (MinMax avec élagage alpha-bêta)
	 *          int beta, qui fait référence au beta de l'algorithme utilisé (MinMax avec élagage alpha-bêta)
	 *          int profondeur, qui correspond à la profondeur qu'il reste à parcourir dans l'arbre de MinMax
	 *          String couleur, (blanc ou noir) qui précise la couleur de l'IA pour laquelle on cherche la liste de coup
	 *          String niveau, (facile, moyen ou difficile) qui définira la difficulté de l'IA. 
	 * sortie : int, la plus grande valeur de plateau que pourra obtenir ce joueur parmi tous les coups qu'il peut jouer
	 */
	public int maxValue(Jeu jeu, int alpha, int beta, int profondeur, String couleur, String niveau) {
		//boolean qui permet de savoir si le pion a avancé ou mangé
		boolean aAvancer = false;
		ArrayList<Pion> pionMange = new ArrayList<Pion>();
		
		if(profondeur <= 0) {
			if(niveau.equals("facile")) {
				return fonctionEvaluationFacile(jeu, couleur);
			}else if(niveau.equals("moyen")) {
				return fonctionEvaluationMoyenne(jeu, couleur);
			}else if(niveau.equals("difficile")) {
				return fonctionEvaluationDifficile(jeu, couleur);
			}
		}
		
		String couleurAdverse = couleur.equals("blanc")? "noir" : "blanc";
		int max = -1000000;
		HashMap<Pion, ArrayList<Coup>> coupPossible = jeu.coupsPossibles(couleur);
		//Pour chaque pion
		for(Pion pion : coupPossible.keySet()) {
			//Pour chacun des coups d'un pion
			for(int i = 0; i<coupPossible.get(pion).size(); i++) {
				//On sauvegarde la case et le coup afin de pouvoir les annuler après les avoir testés 
				Case sauvegardeCase = new Case(pion.getCaseActuelle().getLigne(),pion.getCaseActuelle().getColonne());
				Coup sauvegardeCoup = coupPossible.get(pion).get(i);
				//Si le pion ne peut pas manger avec ce coup on le fait avancer
				if(pion.peutManger(jeu.getPlateau()).size() == 0) {
					pion.avancer(jeu.getPlateau(), coupPossible.get(pion).get(i).get(0));
					aAvancer = true;
				//Sinon il peut manger, on le fait manger
				}else {
					pionMange = pion.manger(jeu.getPlateau(), coupPossible.get(pion).get(i));
				}
				//On calcule la valeur du plateau une fois que le coup à été joué avec minValue puisque c'est au joueur adverse de jouer
				int valeurCoup = minValue(jeu, alpha, beta, profondeur - 1, couleurAdverse, niveau);
				//Si le maximum est plus petit que la valeur du plateau obtenue le maximum devient cette valeur
				max = max > valeurCoup? max : valeurCoup;
				//Si le coup nous a fait avancer on l'annule ici
				if(aAvancer) {
					pion.annulerAvancer(jeu.getPlateau(), sauvegardeCase);
				//Sinon si le coup nous a fait manger on l'annule ici
				}else {
					pion.annulerManger(jeu.getPlateau(), sauvegardeCoup, sauvegardeCase, pionMange);
				}
				aAvancer = false;
				//Si jamais le maximum est supérieur ou égale à beta ça ne sert plus à rien d'explorer cette branche de l'arbre on la "coupe"
				if(max >= beta) {
					return max;
				}
				//Si alpha est inférieur au maximum il prend sa valeur
				alpha = (alpha > max)? alpha : max;
			}
		}
		return max;
	}
	
	/*
	 * Cette méthode permet de retourner la valeur d'un plateau pour une couleur de pion donnée. Elle compte simplement les pions alliés 
	 * et adverses
	 * entrée : Jeu jeu, le jeu dans lequel l'IA jouera 
	 *          String couleur, (blanc ou noir) qui précise la couleur de pion pour lequels on veut la valeur de plateau
	 * sortie : int, la valeur actuelle du plateau pour le joueur dont on a entré la couleur en paramètre
	 */
	public int fonctionEvaluationFacile(Jeu jeu, String couleur) {
		int res=0;
		String symbole= couleur.equals("blanc")? " o " : " x ";
		String symboleDame= couleur.equals("blanc")? " O " : " X ";
		String symboleAdverse= couleur.equals("blanc")? " x " : " o ";
		String symboleDameAdverse= couleur.equals("blanc")? " X " : " O ";
			
		//Pour chaque case du plateau
		for(int i=0;i<jeu.getPlateau().length;i++) {
			for(int j=0;j<jeu.getPlateau().length;j++) {
				//Si on trouve un pion allié la position gagne en utilité 
				if(jeu.getPlateau()[i][j].equals(symbole)){
					res+=10;
				}
				//Sinon si on trouve une dame alliée la position gagne encore plus en utilité 
				else if(jeu.getPlateau()[i][j].equals(symboleDame)){
					res+=30;
				}
				//Sinon si on trouve un pion adverse la position perd en utilité 
				else if(jeu.getPlateau()[i][j].equals(symboleAdverse)){
					res-=10;
				}
				//Sinon si on trouve une dame adverse la position perd encore plus en utilité 
				else if(jeu.getPlateau()[i][j].equals(symboleDameAdverse)){
					res-=30;
				}
			}
		}
		
		return res;
	}
	
	/*
	 * Cette méthode permet de retourner la valeur d'un plateau pour une couleur de pion donnée. Elle fonctionne comme la fonction 
	 * d'évaluation facile, simplement elle sera appelée avec une plus grande profondeur donc sera plus performante. 
	 * entrée : Jeu jeu, le jeu dans lequel l'IA jouera 
	 *          String couleur, (blanc ou noir) qui précise la couleur de pion pour lequels on veut la valeur de plateau
	 * sortie : int, la valeur actuelle du plateau pour le joueur dont on a entré la couleur en paramètre
	 */
	public int fonctionEvaluationMoyenne(Jeu jeu, String couleur) {
		int res=0;
		res+=fonctionEvaluationFacile(jeu,couleur);
		return res;
	}
	
	/*
	 * Cette méthode permet de retourner la valeur d'un plateau pour une couleur de pion donnée. Elle fait appelle à la fonction d'évaluation
	 * facile mais compte en plus les pions qui protègent la premiere ligne de la couleur passée en paramètre
	 * entrée : Jeu jeu, le jeu dans lequel l'IA jouera 
	 *          String couleur, (blanc ou noir) qui précise la couleur de pion pour lequels on veut la valeur de plateau
	 * sortie : int, la valeur actuelle du plateau pour le joueur dont on a entré la couleur en paramètre
	 */
	public int fonctionEvaluationDifficile(Jeu jeu, String couleur) {
		int res=0;
		//On défini le numéro de la première ligne selon la couleur entrée en paramètre
		int ligne= couleur.equals("blanc")? 9 : 0;
		String symbole= couleur.equals("blanc")? " o " : " x ";
		String symboleDame= couleur.equals("blanc")? " O " : " X ";
		//On utilise la fonction d'évaluation facile
		res+=fonctionEvaluationFacile(jeu,couleur);
		
		//On ajoute à cela un peu de poids pour les pions qui défendent la première ligne
		for(int j=0;j<10;j++) {
			if(jeu.getPlateau()[ligne][j].equals(symbole)|| jeu.getPlateau()[ligne][j].equals(symboleDame)) {
				res+=2;
			}
		}
		
		return res;
	}
	

}
