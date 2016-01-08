package search;

import index.Index;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import noeuds.NoeudTerminal;

public abstract class search {

	public static HashMap<Integer, ArrayList<Double>> searchTerm(Index index, String terme, String path){
		HashMap<Integer, ArrayList<Double>> idf=new HashMap<Integer, ArrayList<Double>>();
		ArrayList<Double> idf_value;
		//nombre total de documents
		int n=Index.identifiantFichier(new File(path)).size() ;
		String text[]= terme.split(" ");
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
			Set<Integer> keys=list.get(0).getIndexPositions().keySet();
			for(int i=1;i<list.size();i++){
				keys.retainAll(list.get(i).getIndexPositions().keySet());
			}
			//pour chaque documents contenant tous les termes

			for (Iterator<Integer> it = keys.iterator(); it.hasNext(); ) {
				Integer f = it.next();
				idf_value=new ArrayList<Double>();
				//pour chaque termes de la requête
				for(int j=0;j<list.size();j++){
					idf_value.add(((list.get(j).getIndexPositions().get(f).size())/Math.log10(Index.getSizeFile().get(f)))*Math.log10(n/list.get(j).getIndexPositions().size()));
					//idf_value=idf_value+(list.get(j).getIndexPositions().get(f).size())*Math.log10(n/list.get(j).getIndexPositions().size());
				}
				idf.put(f, idf_value);
			}
		}
		return idf;
	}
	
	public static HashMap<Integer, Double> searchTermNear(Index index, String terme, String path, int position){
		HashMap<Integer, Double> idf=new HashMap<Integer, Double>();
		double idf_value;
		//nombre total de documents
		int n=Index.identifiantFichier(new File(path)).size() ;
		String text[]= terme.split(" ");
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
			Set<Integer> keys=list.get(0).getIndexPositions().keySet();
			for(int i=1;i<list.size();i++){
				keys.retainAll(list.get(i).getIndexPositions().keySet());
			}
			//pour chaque documents contenant tous les termes
			
			// 1 - on ne garde que les documents ayant les termes rapprochés de X position
			

			// 2 - On calcule les indices idf
			for (Iterator<Integer> it = keys.iterator(); it.hasNext(); ) {
				Integer f = it.next();
				idf_value=0;
				//pour chaque termes de la requête
				for(int j=0;j<list.size();j++){
					idf_value=idf_value+(list.get(j).getIndexPositions().get(f).size())*Math.log10(n/list.get(j).getIndexPositions().size());
				}
				idf.put(f, idf_value);
			}
		}
		return idf;
	}
	
	public static ArrayList<Double> idfMoy(HashMap<Integer, ArrayList<Double>> searchTermResult){
		ArrayList<Double> result = new ArrayList<Double>();
		int k=1;
		 Iterator it = searchTermResult.entrySet().iterator();
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
		return result;
	}
}
