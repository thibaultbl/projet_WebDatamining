package main;

import index.Index;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import search.search;

public class main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		
		String path="Doc/lemmatisation";
		Index index=new Index(path);
		System.out.println(index.identifiantFichier(new File(path)));
		HashMap<Integer, Double> testSearch=search.searchTerm(index, "avocat syndicat", path);
		System.out.println(testSearch);
	}

}
