package noeuds;


public abstract class Noeud {
	
	private Noeud noeudPere;
	
	public Noeud(Noeud noeudPere) {
		super();
		this.noeudPere = noeudPere;
	}

	public Noeud getNoeudPere() {
		return noeudPere;
	}
	
	public abstract char getLettre();
}
