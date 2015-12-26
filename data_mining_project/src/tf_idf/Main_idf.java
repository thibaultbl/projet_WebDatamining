package tf_idf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

public class Main_idf {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*int[] tableauFrequences;
		ArrayList<String> colNames=new ArrayList<String>();
		ArrayList<String> rowNames;
		Set mots;

		HashMap<String, Integer> result=new HashMap<String, Integer>();
		HashMap<String, Double> resultIDF=new HashMap<String, Double>();
		HashMap<String, Integer> nbIterationsEnsemble=new HashMap<String, Integer>();
		ArrayList<HashMap<String, Integer>> resultFinal=new ArrayList<HashMap<String, Integer>>();
		ArrayList<HashMap<String, Double>> resultFinalIDF=new ArrayList<HashMap<String, Double>>();

		HashMap<String, Integer> nbDocContenantTerme=nombreDocumentsContenantTerme(new File("Doc/lemmatisation"));


		nbIterationsEnsemble=resultFinal( new File("Doc/lemmatisation")); 
		//on récupére l'ensemble des mots présents dans le corpus
		mots=nbIterationsEnsemble.keySet();
		rowNames=new ArrayList<String>(Arrays.asList(Arrays.copyOf(mots.toArray(), mots.toArray().length, String[].class)));

		File folder=new File("Doc/lemmatisation");
		System.out.println("nb file :"+folder.list().length);
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				// listFilesForFolder(fileEntry);
			} else {
				PrintWriter writer=null;
				try {
					writer = new PrintWriter("Doc/resultIDF/result"+fileEntry.getName(), "UTF-8");
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
		}*/

		try {
			resultatFinal();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static HashMap<Integer, String> identifiantFichier(File folder) {
		// Va lister tous les fichiers dans le répertoire et leur attribue un ID
		HashMap<Integer, String> id = new HashMap<Integer, String>();
		int i = 1;
		for (final File entry : folder.listFiles()) {
			if (entry.isDirectory()) {

			} else {
				id.put(i, entry.getName());
			}
		}
		return id;
	}

	public static void resultatFinal() throws IOException {
		//table contenant le nombre d'occurence par mot dans l'ensemble du corpus
		Hashtable<String, Integer> table = new Hashtable<String, Integer>();
		//Table contenant l'ensemble des documents contenant au moins une occurence du mot
		HashMap<String, ArrayList<String>> talb = new HashMap<String, ArrayList<String>>();
		ArrayList<String> listId = null;

		String path = "Doc/lemmatisation";
		File file = new File(path);
		HashMap<Integer, String> id = identifiantFichier(file);
		File[] filesInDir = file.listFiles();

		for (final File f : filesInDir) {

			if (f.isDirectory()==false) {

				BufferedReader entree = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
				String ligne;
				StringTokenizer st;
				String mot;

				int nbOcc;

				while ((ligne = entree.readLine()) != null)
				{
					st = new StringTokenizer(ligne, "\t");
					while(st.hasMoreTokens())
					{
						st.nextToken();
						st.nextToken();
						mot = st.nextToken();
						mot=normalize(mot);
						if (table.containsKey(mot))
						{
							nbOcc = table.get(mot).intValue();
							nbOcc++;
						}
						else  {
							nbOcc = 1;
						}
						table.put(mot, new Integer(nbOcc));
						//talb.put(mot, f.getName());

						if(talb.containsKey(mot)){
							if(talb.get(mot).contains(f.getName())==false ){
								talb.get(mot).add(f.getName());
							}

						}
						else{
							listId=new ArrayList<String>();
							listId.add(f.getName());
							talb.put(mot, listId);
						}
					}
				}

				entree.close();
			}
			
		}
		
		for(Entry<String, ArrayList<String>> entry : talb.entrySet()) {
		    String key = entry.getKey();
		    ArrayList<String> value = entry.getValue();

			System.out.println("Mot : " + key + " | Nombre d'occurences (corpus) : " + table.get(key) + " | Source : " + talb.get(key));

		    // do what you have to do here
		    // In your case, an other loop.
		}

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
