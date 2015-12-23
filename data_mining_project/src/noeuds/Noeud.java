package noeuds;


public class Noeud {
	
	private NoeudNonTerminal noeudPere;
	
	public Noeud(NoeudNonTerminal noeudPere) {
		super();
		this.noeudPere = noeudPere;
	}

	public NoeudNonTerminal getNoeudPere() {
		return noeudPere;
	}
}
