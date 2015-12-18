package tf_idf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Main_idf {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] tableauFrequences;
		ArrayList<String> colNames=new ArrayList<String>();
		ArrayList<String> rowNames;
		Set mots;
		
		HashMap<String, Integer> result=new HashMap<String, Integer>();
		HashMap<String, Double> resultIDF=new HashMap<String, Double>();
		HashMap<String, Integer> nbIterationsEnsemble=new HashMap<String, Integer>();
		ArrayList<HashMap<String, Integer>> resultFinal=new ArrayList<HashMap<String, Integer>>();
		ArrayList<HashMap<String, Double>> resultFinalIDF=new ArrayList<HashMap<String, Double>>();

		HashMap<String, Integer> nbDocContenantTerme=nombreDocumentsContenantTerme(new File("/home/thibault/Documents/cours/WebDataMining/webDatamining sources/data_mining_project/Doc/lemmatisation"));

		
		nbIterationsEnsemble=resultFinal( new File("/home/thibault/Documents/cours/WebDataMining/webDatamining sources/data_mining_project/Doc/lemmatisation")); 
		//on récupére l'ensemble des mots présents dans le corpus
		mots=nbIterationsEnsemble.keySet();
		rowNames=new ArrayList<String>(Arrays.asList(Arrays.copyOf(mots.toArray(), mots.toArray().length, String[].class)));
		
		File folder=new File("/home/thibault/Documents/cours/WebDataMining/webDatamining sources/data_mining_project/Doc/lemmatisation");
		System.out.println("nb file :"+folder.list().length);
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	           // listFilesForFolder(fileEntry);
	        } else {
	        	PrintWriter writer=null;
	    		try {
	    			writer = new PrintWriter("/home/thibault/Documents/cours/WebDataMining/webDatamining sources/data_mining_project/Doc/resultIDF/result"+fileEntry.getName(), "UTF-8");
	    		} catch (FileNotFoundException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		} catch (UnsupportedEncodingException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    		writer.println(fileEntry.getName());
	    		
	        	colNames.add(fileEntry.getName());
	        	result=countWordLematise(fileEntry, mots);
	        	resultFinal.add(result);
	        	resultIDF=convertFrequenceToIDF(result, nbDocContenantTerme);
	        	resultFinalIDF.add(resultIDF);
	        	
	        	
	        	
	    		String res;
	    		for (HashMap.Entry<String, Double> entry : resultIDF.entrySet()) {
	    			res= entry.getKey() +" "+ entry.getValue();
	    		    writer.println(res);
	    		}
	    		writer.close();

	        }
		}
		
	}
	
	
	
	public static HashMap<String, Integer> resultFinal(File folder){
		HashMap<String, Integer> result=new HashMap<String, Integer>();
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	           // listFilesForFolder(fileEntry);
	        } else {
	        	String text[];
	    		String word;
	    		//lecture du fichier texte	
	    		InputStream ips=null;
	    		try {
	    			ips = new FileInputStream(fileEntry);
	    		} catch (FileNotFoundException e1) {
	    			// TODO Auto-generated catch block
	    			e1.printStackTrace();
	    		} 
	    		InputStreamReader ipsr=null;
	    		try {
	    			ipsr = new InputStreamReader(ips, "ISO-8859-1");
	    		} catch (UnsupportedEncodingException e1) {
	    			// TODO Auto-generated catch block
	    			e1.printStackTrace();
	    		}
	    		BufferedReader br = new BufferedReader(ipsr);
	    		String line;
	    		try {
	    			while ((line = br.readLine())!=null)
	    			 {
	    			           text= line.split("\t");
	    			           word=text[2];
	    			           word=normalize(word);
	    					    
	    					    if (result.containsKey(word) ) {
	    					    	result.replace(word, result.get(word), (Integer)result.get(word)+1);
	    					    } else {
	    					    	result.put(word, 1);
	    					    }
	    			}
	    		} catch (IOException e1) {
	    			// TODO Auto-generated catch block
	    			e1.printStackTrace();
	    		}
	    		try {
	    			br.close();
	    		} catch (IOException e1) {
	    			// TODO Auto-generated catch block
	    			e1.printStackTrace();
	    		} 
	        }
		}
		PrintWriter writer=null;
		try {
			writer = new PrintWriter("/home/thibault/Documents/cours/WebDataMining/webDatamining sources/data_mining_project/Doc/result/resultFinal", "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, Integer> mapE = new HashMap<String, Integer>();
		String res;
		for (HashMap.Entry<String, Integer> entry : result.entrySet()) {
			res="Key = " + entry.getKey() + ", Value = " + entry.getValue();
		    writer.println(res);
		}
		System.out.println("nombre de mot différent : "+result.size());
		writer.close();
		return result;
	}
	
	public static String normalize(String string){
		string=string.toLowerCase();
		string = Normalizer.normalize(string, Normalizer.Form.NFD);
		string=string.replace(",",  "");
		string=string.replace(".",  "");
		string=string.replace(";",  "");
		return string;
	}
	
	
	public static HashMap<String, Integer> countWordLematise(File file, Set reference){
		HashMap<String, Integer> map=new HashMap();
		String text[];
		String word;
		//lecture du fichier texte	
		InputStream ips=null;
		try {
			ips = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		InputStreamReader ipsr=null;
		try {
			ipsr = new InputStreamReader(ips, "ISO-8859-1");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader br = new BufferedReader(ipsr);
		String line;
		try {
			//initialisation des mots
			String[] mots=Arrays.copyOf(reference.toArray(), reference.toArray().length, String[].class);
			for(int i=0;i<mots.length;i++ ){
				map.put(mots[i], 0);
			}
			
			while ((line = br.readLine())!=null)
			 {
			           text= line.split("\t");
			           word=text[2];
			           word=normalize(word);
					    
					    if (map.containsKey(word) ) {
					        map.replace(word, map.get(word), (Integer)map.get(word)+1);
					    } else {
					        map.put(word, 1);
					    }
				        
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		
		return map;
	}
	
	
	public static HashMap<String, Double> convertFrequenceToIDF(HashMap<String, Integer> matFrequence, HashMap<String, Integer> nbIterationsTotal){
		int NB_DOC=123;
		HashMap<String, Double> matIDF=new HashMap<String, Double>();
		Set mots=matFrequence.keySet();
		ArrayList<String> cles=new ArrayList<String>(Arrays.asList(Arrays.copyOf(mots.toArray(), mots.toArray().length, String[].class)));
		for(int i=0; i<cles.size();i++){
		matIDF.put(cles.get(i), matFrequence.get(cles.get(i))*Math.log10(NB_DOC/nbIterationsTotal.get(cles.get(i))));
		}
		return matIDF;
	}
	
	
	public static HashMap<String, Integer> nombreDocumentsContenantTerme(File folder){
		HashMap<String, Integer> result=new HashMap<String, Integer>();
		HashMap<String, Boolean> temp=new HashMap<String, Boolean>();
		for (final File fileEntry : folder.listFiles()) {
			temp=new HashMap<String, Boolean>();
	        if (fileEntry.isDirectory()) {
	           // listFilesForFolder(fileEntry);
	        } else {
	        	String text[];
	    		String word;
	    		//lecture du fichier texte	
	    		InputStream ips=null;
	    		try {
	    			ips = new FileInputStream(fileEntry);
	    		} catch (FileNotFoundException e1) {
	    			e1.printStackTrace();
	    		} 
	    		InputStreamReader ipsr=null;
	    		try {
	    			ipsr = new InputStreamReader(ips, "ISO-8859-1");
	    		} catch (UnsupportedEncodingException e1) {
	    			e1.printStackTrace();
	    		}
	    		BufferedReader br = new BufferedReader(ipsr);
	    		String line;
	    		try {
	    			while (((line = br.readLine())!=null) )
	    			 {
	    			           text= line.split("\t");
	    			           word=text[2];
	    			           word=normalize(word);
	    			           if(temp.containsKey(word)==false){
	    			        	   if (result.containsKey(word) ) {
		    					    	result.replace(word, result.get(word), (Integer)result.get(word)+1);
		    					    } else {
		    					    	result.put(word, 1);
		    					    }
	    			           }
	    					    temp.put(word, true);
	    			}
	    		} catch (IOException e1) {
	    			// TODO Auto-generated catch block
	    			e1.printStackTrace();
	    		}
	    		try {
	    			br.close();
	    		} catch (IOException e1) {
	    			// TODO Auto-generated catch block
	    			e1.printStackTrace();
	    		} 
	        }
		}
		return result;
	}
	
	

}
