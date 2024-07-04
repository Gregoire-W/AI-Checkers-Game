/*
 * Cette classe contient le code main du projet. 
 */
package premier_paquet;

public class Main {

	public static void main(String[] args) {
		Partie dames = new Partie(new Jeu());
		dames.lancerPartie();
		dames.finDeJeu();
	}

}