package main;

import noeuds.Noeud;
import noeuds.NoeudNonTerminal;

public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		char[] s1="nom".toCharArray();
		Noeud noeud1=new NoeudNonTerminal(s1[0], null);
		Noeud noeud2=new NoeudNonTerminal(s1[1], noeud1);
		Noeud noeud3=new NoeudNonTerminal(s1[2], noeud2);

	}

}
