package noeuds;

import java.util.ArrayList;

public class NoeudNonTerminal extends Noeud{
	private ArrayList<Noeud> noeudsFils;
	private char lettre;
	
	public NoeudNonTerminal(char lettre, Noeud noeudPere) {
		super(noeudPere);
		this.lettre = lettre;
	}

	public ArrayList<Noeud> getNoeudsFils() {
		return noeudsFils;
	}

	public char getLettre() {
		return lettre;
	}
	
	public void addNoeudFils(Noeud noeud){
		noeudsFils.add(noeud);
	}
}
