package metier;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Set;

import org.annolab.tt4j.TokenHandler;
import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TreeTaggerWrapper;

public class main2 {

	/**
	 * @param args
	 */
	private static String OUTPUT="/home/thibault/Documents/cours/WebDataMining/webDatamining sources/data_mining_project/Doc/result";
	private static String DIRNAME="/home/thibault/Documents/cours/WebDataMining/webDatamining sources/data_mining_project/Doc/vrac";
	private static String FILENAME=DIRNAME+"texte.95-1.txt";

	

	private static void tag(Set words) throws UnsupportedEncodingException, TreeTaggerException
	{
		String res="";
		TreeTaggerWrapper<String> tt=new TreeTaggerWrapper<String>();
		
		try{
			tt.setModel("/home/thibault/Documents/cours/WebDataMining/webDatamining sources/TreeTagger/lib/french-utf8.par");
			tt.setHandler( new TokenHandler<String>() {
				String res;
				public void token(String token, String pos, String lemma)
				{
					res=lemma;
					System.out.println(lemma);
				}
			});
			tt.process(words);
		}
		catch(IOException ex){}
	}
	

	public static void main(String[] args) throws TreeTaggerException {
		HashMap<String, Integer> result;
		
		
		File folder=new File("/home/thibault/Documents/cours/WebDataMining/webDatamining sources/data_mining_project/Doc/vrac");
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	           // listFilesForFolder(fileEntry);
	        } else {
	        	HashMap<String, Integer> map=Main.countWordFile(fileEntry);
	        	
	        	try {
	        		System.out.println(map.size());
					tag(map.keySet());
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
		}
	}
	

}
