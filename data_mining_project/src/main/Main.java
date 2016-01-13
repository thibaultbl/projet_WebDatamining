package main;

import index.Index;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.google.common.collect.HashBiMap;

import search.search;

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		String path="Doc/lemmatisation";
		Index index=new Index(path);
		HashBiMap<String, Integer> id=index.identifiantFichier(new File(path));
		//System.out.println(id);
		
		String requete=JOptionPane.showInputDialog ("Rentrez votre requête ici");
		//"avocat syndicat"

		//on récupére les valeurs des documents suite à la requête
		HashMap<Integer, ArrayList<Double>> testSearch=search.searchTerm(index, requete, path);
		//on calcule la valeur moyenne des termes recherchés dans l'ednsemble du corpus
		//ArrayList<Double> idfMoy=search.idfMoy(testSearch);
		ArrayList<Double> idfMoy=search.idfRequest(index, requete, path, testSearch);
		
		//printMap(testSearch);
		final JFrame frame = new JFrame();

		// Release the window and quit the application when it has been closed
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		System.out.println(testSearch);
		JOptionPane.showMessageDialog(frame, printMap(testSearch, id, idfMoy));
	}
	
	public static String printMap(Map mp, HashBiMap<String, Integer> id, ArrayList<Double> idfMoy) {
	    Iterator it = mp.entrySet().iterator();
	    String str="";
	    String idfMoyList="les idf moyennes sont :"+idfMoy;
	    System.out.println(idfMoyList);
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	       str=str+"Texte contenant les termes : "+id.inverse().get(pair.getKey())+" (id="+ pair.getKey()+ ") avec un score de match de " + pair.getValue()+System.getProperty("line.separator");
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    return str;
	}
	
	public void comparaisonVecteur(HashMap<Integer, ArrayList<Double>> docValue, ArrayList<Double> termeMeanValue){
		
		
	}
	
	

}
