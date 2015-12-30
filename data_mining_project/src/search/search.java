package search;

import index.Index;
import noeuds.Noeud;
import noeuds.NoeudTerminal;

public abstract class search {

	public static void searchTerm(Index index, String terme){
		Noeud temp;

		//on cherche le noeud terminal correspondant au terme
		//1 - on se place dans le noeud terminal correspondant au terme
		char[] arrayChar=terme.toCharArray();
		int trouve=-1;
		//Si il s'agit d'une chaine vide on ne fait rien
		if(arrayChar.length<=0){
			return;
		}
		for(int j=0;j<index.getDebutTerme().size();j++){
			if(arrayChar[0]==index.getDebutTerme().get(j).getLettre()){
				trouve=j;
			}
		}

		if(trouve !=-1){
			temp=index.getDebutTerme().get(trouve);
			for(int i=1;i<arrayChar.length;i++){
				trouve=-1;
				for(int k=0;k<temp.getNoeudsFils().size();k++){
					if(temp.getNoeudsFils().get(k).getLettre()==arrayChar[i]){
						trouve=k;
					}
				}
				if(trouve==-1){
					return;
				}
				temp=temp.getNoeudsFils().get(trouve);
			}
			for(int k=0;k<temp.getNoeudsFils().size();k++){
				if(temp.getNoeudsFils().get(k) instanceof NoeudTerminal){
					trouve=k;
				}
			}
			temp=temp.getNoeudsFils().get(trouve);
		}

	}
}
