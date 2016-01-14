package statsDes;

import index.Index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class StatsDes {
	
	public static void nbFichiers(){
		String path="Doc/lemmatisation";
		File directory = new File(path);
		System.out.println("Il y a "+directory.listFiles().length+" fichiers.");
	}
	
	public static void nbMots() throws IOException{
		String path="Doc/lemmatisation";
		File directory = new File(path);
		File[] filesInDir = directory.listFiles();
		
		int nbLignes=0;
		
		for (final File f : filesInDir){
			if (f.isDirectory()==false){
				BufferedReader entree = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
				while ((entree.readLine()) != null){
					nbLignes++;
				}
				entree.close();
			}
		}
		System.out.println("Il y a "+nbLignes+" mots dans le corpus.");
	}
	
	public static void motsFrequents(Index index){
		//TODO
	}
	
	public static void supprimesFrequents(){
		//TODO
	}
	
	public static void supprimesPeuFrequents(){
		//TODO
	}
}
