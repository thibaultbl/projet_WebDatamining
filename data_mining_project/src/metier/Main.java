package metier;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.annolab.tt4j.TokenHandler;
import org.annolab.tt4j.TreeTaggerWrapper;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HashMap<String, Integer> result=null;
		
		File folder=new File("/home/thibault/Documents/cours/WebDataMining/webDatamining sources/data_mining_project/Doc/lemmatisation");
		System.out.println("nb file :"+folder.list().length);
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	           // listFilesForFolder(fileEntry);
	        } else {
	        	//result=countWordFile(fileEntry);
	        	System.out.println(fileEntry.getName());
	        	result=countWordLematise(fileEntry);
	        }
	        
		}
		//resultFinal( new File("/home/thibault/Documents/cours/WebDataMining/webDatamining sources/data_mining_project/Doc/lemmatisation"));
	}
	
	
	
	public static void resultFinal(File folder){
		HashMap<String, Integer> result=new HashMap<String, Integer>();
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	           // listFilesForFolder(fileEntry);
	        } else {
	        	Scanner sc=null;
	    		try {
	    			sc = new Scanner(new FileInputStream(fileEntry));
	    		} catch (FileNotFoundException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    		int count=0;
	    		String word;
	    		while(sc.hasNext()){
	    		    word=sc.next();
	    		    word=normalize(word);
	    		    
	    		    if (result.containsKey(word) ) {
	    		    	result.replace(word, result.get(word), (Integer)result.get(word)+1);
	    		    } else {
	    		    	result.put(word, 1);
	    		    }
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
	}
	
	public static String normalize(String string){
		string=string.toLowerCase();
		string = Normalizer.normalize(string, Normalizer.Form.NFD);
		string=string.replace(",",  "");
		string=string.replace(".",  "");
		string=string.replace(";",  "");
		return string;
	}
	
	
	public static HashMap<String, Integer> countWordLematise(File file){
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
		
		System.out.println(map);
	
		return map;
	}

}
