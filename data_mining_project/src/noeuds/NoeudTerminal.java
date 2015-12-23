package noeuds;

import java.util.ArrayList;
import java.util.HashMap;

public class NoeudTerminal extends Noeud{
	private HashMap<String, ArrayList<Integer>> indexPositions;
	private String terme;
	
	public NoeudTerminal(String lettre, NoeudNonTerminal noeudPere) {
		super(noeudPere);
		this.terme = this.recupererTerme();
	}
	public HashMap<String, ArrayList<Integer>> getIndexPositions() {
		return indexPositions;
	}
	public void setIndexPositions(HashMap<String, ArrayList<Integer>> indexPositions) {
		this.indexPositions = indexPositions;
	}
	
	// récupère le terme du noeud terminal en remontant dans l'arbre
	public String recupererTerme(){
		NoeudNonTerminal noeud = this.getNoeudPere();
		String terme = "";
		terme = noeud.getLettre() + terme;
		while(noeud.getNoeudPere()!=null){
			noeud = noeud.getNoeudPere();
			terme = noeud.getLettre() + terme;
		}
		return terme;
	}
	public String getTerme() {
		return terme;
	}
	
	// calcule l'index des positions
	public HashMap<String, ArrayList<Integer>> calculIndexPositions(){
		HashMap<String, ArrayList<Integer>> indexPositions = new HashMap<String, ArrayList<Integer>>();
		//TODO
		return indexPositions;
	}
}
