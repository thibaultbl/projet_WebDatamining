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
		index.setDebutTerme(Index.InitialiserIndex(path));
		
		
		System.out.println(index);
		System.out.println("------------------------------------");
		
		Noeud temp=index.getDebutTerme().get(2);
		System.out.println(temp);
		temp=temp.getNoeudsFils().get(0);
		System.out.println(temp);
		temp=temp.getNoeudsFils().get(0);
		System.out.println(temp);
		System.out.println("nb fils : "+temp.getNoeudsFils().size());
		temp=temp.getNoeudsFils().get(1);
		while(temp.getNoeudsFils().size()>0){
			System.out.println(temp);
			temp=temp.getNoeudsFils().get(0);
		}
		System.out.println("*************************************************");
		System.out.println(temp.getNoeudsFils());
		System.out.println(((NoeudTerminal)temp).getTerme());
		
		index.deleteTerm("avoir");
		

		System.out.println(index);
		System.out.println("------------------------------------");
		
		temp=index.getDebutTerme().get(2);
		System.out.println(temp);
		temp=temp.getNoeudsFils().get(0);
		System.out.println(temp);
		temp=temp.getNoeudsFils().get(0);
		System.out.println(temp);
		System.out.println("nb fils : "+temp.getNoeudsFils().size());
		temp=temp.getNoeudsFils().get(0);
		while(temp.getNoeudsFils().size()>0){
			System.out.println(temp);
			temp=temp.getNoeudsFils().get(0);
		}
		System.out.println("*************************************************");
		System.out.println(temp.getNoeudsFils());
		System.out.println(((NoeudTerminal)temp).getTerme());
		
index.deleteTerm("avocat");
		

		System.out.println(index);
		System.out.println("------------------------------------");
		
		temp=index.getDebutTerme().get(2);
		System.out.println(temp);
		temp=temp.getNoeudsFils().get(0);
		System.out.println(temp);
		temp=temp.getNoeudsFils().get(0);
		System.out.println(temp);
		System.out.println("nb fils : "+temp.getNoeudsFils().size());
		temp=temp.getNoeudsFils().get(0);
		while(temp.getNoeudsFils().size()>0){
			System.out.println(temp);
			temp=temp.getNoeudsFils().get(0);
		}
		System.out.println("*************************************************");
		System.out.println(temp.getNoeudsFils());
		System.out.println(((NoeudTerminal)temp).getTerme());
		
		
	}

}
