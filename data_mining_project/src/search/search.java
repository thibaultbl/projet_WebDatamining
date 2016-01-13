package search;

import index.Index;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;

import javax.swing.JOptionPane;

import noeuds.NoeudTerminal;

public abstract class search {

	/**
	 * Retourne le tf-idf par documents et par terme recherché
	 * @param index
	 * @param terme
	 * @param path
	 * @return
	 */
	public static HashMap<Integer, ArrayList<Double>> searchTerm(Index index, String terme, String path){
		HashMap<Integer, ArrayList<Double>> idf=new HashMap<Integer, ArrayList<Double>>();
		ArrayList<Double> idf_value;

		//nombre total de documents
		int n=Index.identifiantFichier(new File(path)).size() ;

		//on récupére chacun des termes de la requête
		String text[]= terme.split(" ");
		//on y supprime les doublons
		text=removeDuplicate(text);
		
		
		NoeudTerminal temp;
		ArrayList<NoeudTerminal> list=new  ArrayList<NoeudTerminal>();
		//on récupére tous les noeuds terminaux liés à chacun des termes présent dans la requêtes
		for(int i=0;i<text.length;i++){
			temp=(NoeudTerminal)index.getNoeudTerminal(text[i]);
			if(temp != null){
				list.add(temp);
			}
			else{
				System.out.println("le terme "+text[i]+" n'est pas dans l'index");
			}
		}

		if(list.size()>0){
			Set<Integer> keys=new HashSet<Integer>();
			keys.addAll(list.get(0).getIndexPositions().keySet());
			for(int i=1;i<list.size();i++){
				keys.addAll(list.get(i).getIndexPositions().keySet());
				//keys.retainAll(list.get(i).getIndexPositions().keySet());
			}
			//pour chaque documents contenant tous les termes

			for (Iterator<Integer> it = keys.iterator(); it.hasNext(); ) {
				Integer f = it.next();
				idf_value=new ArrayList<Double>();
				//pour chaque termes de la requête
				for(int j=0;j<list.size();j++){
					if(list.get(j).getIndexPositions().get(f)==null){
						idf_value.add((double)0);
					}
					else{
						idf_value.add(((list.get(j).getIndexPositions().get(f).size())/Math.log10(Index.getSizeFile().get(f)))*Math.log10(n/list.get(j).getIndexPositions().size()));
					}
					//idf_value=idf_value+(list.get(j).getIndexPositions().get(f).size())*Math.log10(n/list.get(j).getIndexPositions().size());
				}
				idf.put(f, idf_value);
			}
		}
		return idf;
	}


	/**
	 * Calcule et renvoi le tf-idf moyen 
	 * @param searchTermResult
	 * @return
	 */
	public static ArrayList<Double> idfMoy(HashMap<Integer, ArrayList<Double>> searchTermResult)  {
		ArrayList<Double> result = new ArrayList<Double>();
		int k=1;
		Iterator it = searchTermResult.entrySet().iterator();
		try
		{
			HashMap.Entry<Integer, ArrayList<Double>> pair = (HashMap.Entry<Integer, ArrayList<Double>>)it.next();
			for(int i=0;i<pair.getValue().size() ;i++){
				result.add(pair.getValue().get(i));
			}
			while (it.hasNext()) {
				k++;
				pair = (HashMap.Entry)it.next();
				for(int i=0;i<pair.getValue().size() ;i++){
					result.set(i, (result.get(i)+pair.getValue().get(i)));
				}
			}
			for(int i=0; i<result.size();i++ ){
				result.set(i, (result.get(i)/k));
			}

		} catch (NoSuchElementException e)
		{
			JOptionPane.showMessageDialog(null, "Le terme recherché n'est pas dans l'index", "Le terme recherché n'est pas dans l'index",
					JOptionPane.ERROR_MESSAGE);
		}

		return result;
	}

	/**
	 * Function to return the idf value of the request
	 * @param index
	 * @param terme
	 * @param path
	 * @return
	 */
	public static ArrayList<Double> idfRequest(Index index, String terme, String path, HashMap<Integer, ArrayList<Double>> searchTermResult){
		ArrayList<Double> idf_value=new ArrayList<Double>();

		//nombre total de documents
		int n=Index.identifiantFichier(new File(path)).size() ;
		
		//on split la requête avec un tableau contenant chaque mot
		String text[]= terme.split(" ");
		//on récupére la taille de la requête
		int requestSize=text.length;
		
		//nombre d'occurence d'un mot dans la requete
		Map<String, Integer> nbOccurenceTerm=nbOccurenceRequest(text);
		//on supprime les doublons dans l'array
		text=removeDuplicate(text);
		System.out.println(nbOccurenceTerm);
		
		NoeudTerminal temp;
		ArrayList<NoeudTerminal> list=new  ArrayList<NoeudTerminal>();
		//on récupére tous les noeuds terminaux liés à chacun des termes présent dans la requêtes
		for(int i=0;i<text.length;i++){
			temp=(NoeudTerminal)index.getNoeudTerminal(text[i]);
			if(temp != null){
				list.add(temp);
			}
			else{
				System.out.println("le terme "+text[i]+" n'est pas dans l'index");
			}
		}
		

		for(int i=0;i<list.size();i++){
			idf_value.add(((nbOccurenceTerm.get(list.get(i).getTerme()))/Math.log10(requestSize))*Math.log10(n/list.get(i).getIndexPositions().size()));
		}

	
	//idf_value=idf_value+(list.get(j).getIndexPositions().get(f).size())*Math.log10(n/list.get(j).getIndexPositions().size());

	return idf_value;
}
	
	/**
	 * Function to remove duplicate in request
	 * @param entree
	 * @return
	 */
	public static String[] removeDuplicate(String[] entree){
		ArrayList<String> temp=new ArrayList<String>(Arrays.asList((entree)));
		// add elements to al, including duplicates
		LinkedHashSet <String> hs = new LinkedHashSet<String>();
		hs.addAll(temp);
		temp.clear();
		temp.addAll(hs);
		String[] result = new String[temp.size()];
		result = temp.toArray(result);
		return result;
	}
	
	
	public static Map<String, Integer> nbOccurenceRequest(String[] text){
		Map<String, Integer> occurences = new HashMap<String, Integer>();

		for(String value : text)
		{
		    Integer oldValue = occurences.get(value);
		    if(oldValue == null)
		        occurences.put(value, 1);
		    else
		        occurences.put(value, oldValue + 1);
		}
		
		return occurences;
	}
	
	
	
}
