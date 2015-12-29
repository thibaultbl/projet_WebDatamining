package main;

import index.Index;

import java.io.File;
import java.io.IOException;

import noeuds.Noeud;
import noeuds.NoeudTerminal;

public class main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		
		String path="Doc/lemmatisation";
		Index index=new Index(path);
		System.out.println("------------------------------------");
		
		Noeud temp=index.getDebutTerme().get(0);
		while(temp.getNoeudsFils().size()>0){
			System.out.println(temp);
			temp=temp.getNoeudsFils().get(0);
		}
		System.out.println("*************************************************");
		System.out.println(((NoeudTerminal)temp).getTerme());
		System.out.println(((NoeudTerminal)temp).getIndexPositions());
		
		File file = new File(path);
		System.out.println(Index.identifiantFichier(file));
		
		index.deleteTerm("monica");
		
System.out.println("------------------------------------");
		
		temp=index.getDebutTerme().get(0);
		temp=temp.getNoeudsFils().get(0);
		temp=temp.getNoeudsFils().get(0);
		temp=temp.getNoeudsFils().get(0);
		temp=temp.getNoeudsFils().get(1);
		while(temp.getNoeudsFils().size()>0){
			System.out.println(temp);
			temp=temp.getNoeudsFils().get(0);
		}
		System.out.println("*************************************************");
		System.out.println(((NoeudTerminal)temp).getTerme());


		/*temp=index.getDebutTerme().get(0);
		System.out.println(temp);
		temp=temp.getNoeudsFils().get(1);
		while(temp.getNoeudsFils().size()>0){
			System.out.println(temp);
			temp=temp.getNoeudsFils().get(0);
		}
		
		System.out.println("------------------------------------");

		temp=index.getDebutTerme().get(0);
		System.out.println(temp);
		temp=temp.getNoeudsFils().get(1);
		System.out.println(temp);
		temp=temp.getNoeudsFils().get(1);
		while(temp.getNoeudsFils().size()>0){
			System.out.println(temp);
			temp=temp.getNoeudsFils().get(0);
		}*/
		


	}

}
