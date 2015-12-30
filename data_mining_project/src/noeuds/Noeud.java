package noeuds;

import java.util.ArrayList;


public abstract class Noeud {
	
	private Noeud noeudPere;
	protected ArrayList<Noeud> noeudsFils;
	
	public Noeud(Noeud noeudPere) {
		super();
		this.noeudPere = noeudPere;
		this.noeudsFils=new ArrayList<Noeud>();
	}

	public Noeud getNoeudPere() {
		return noeudPere;
	}
	
	public abstract char getLettre();
	
	public ArrayList<Noeud> getNoeudsFils() {
		return noeudsFils;
	}
	
	

}
