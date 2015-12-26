package termToExclude;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class TermToExclude {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		frequentTerm(20);
	}
	
	/**
	 * Détermination des termes à exclure de l'index
	 */
	
	//classement des termes selon leur fréquences d'appartition dans des documents uniques 
	
	public static void frequentTerm(double threeshold){
		
		HashMap<String, Integer> nbDocContenantTerme=tf_idf.Main_idf.nombreDocumentsContenantTerme(new File("Doc/lemmatisation"));
		List<String> keys=new ArrayList<String>();
		for(Entry<String, Integer> entry : nbDocContenantTerme.entrySet()) {
			if(entry.getValue()>threeshold){
				keys.add(entry.getKey());
			}
		}
		System.out.println(nbDocContenantTerme.size());

		
		for(int i=0; i<keys.size();i++){
			System.out.println(keys.get(i));
			nbDocContenantTerme.remove(keys.get(i));
		}
		
		System.out.println(nbDocContenantTerme);
		System.out.println(nbDocContenantTerme.size());

	}
	
	

}
