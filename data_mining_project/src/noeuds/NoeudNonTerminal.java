package noeuds;

import java.util.ArrayList;

public class NoeudNonTerminal extends Noeud{
	private char lettre;
	
	public NoeudNonTerminal(char lettre, Noeud noeudPere) {
		super(noeudPere);
		this.lettre = lettre;
	}


	public char getLettre() {
		return lettre;
	}
	
}
