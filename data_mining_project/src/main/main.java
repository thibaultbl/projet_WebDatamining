package main;

import index.Index;

import java.io.IOException;

import noeuds.Noeud;

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
		System.out.println("------------------------------------");

		temp=index.getDebutTerme().get(0);
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
		}



	}

}
