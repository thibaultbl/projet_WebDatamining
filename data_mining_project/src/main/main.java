package main;

import noeuds.Noeud;
import noeuds.NoeudNonTerminal;
import noeuds.NoeudTerminal;

public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		char[] s1="nom".toCharArray();
		Noeud noeud1=new NoeudNonTerminal(s1[0], null);
		Noeud noeud2=new NoeudNonTerminal(s1[1], noeud1);
		Noeud noeud3=new NoeudNonTerminal(s1[2], noeud2);
		NoeudTerminal noeud4=new NoeudTerminal(noeud3);
		
		System.out.println(noeud4.getTerme());

	}

}
