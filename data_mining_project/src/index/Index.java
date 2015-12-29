package index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import noeuds.Noeud;
import noeuds.NoeudNonTerminal;
import noeuds.NoeudTerminal;

public class Index {
	private ArrayList<Noeud> debutTerme;

	public Index(String path) throws IOException {
		debutTerme=this.InitialiserIndex(path);
	}

	@Override
	public String toString() {
		return "debut terme :"+debutTerme;
	}


	public static HashMap<String, Integer> identifiantFichier(File folder) {
		// Va lister tous les fichiers dans le répertoire et leur attribue un ID
		HashMap<String, Integer> id = new HashMap<String, Integer>();
		int i = 1;
		for (final File entry : folder.listFiles()) {
			if (entry.isDirectory()==false) {
				id.put(entry.getName(), i );
				i++;
			}
		}
		return id;
	}

	/**
	 * <pour détecter les termes apparaissant trop fréquemment dans le corpus ou dans un nombre de textes trop important
	 * @throws IOException
	 */
	public static ArrayList<Noeud> InitialiserIndex(String path) throws IOException {
		ArrayList<Noeud> result=new ArrayList<Noeud>();
		char[] decomposition;
		int trouve;
		int j;
		int positionLigne;
		ArrayList<Integer> tempPosition;
		HashMap<Integer, ArrayList<Integer>> hashmapTemp;
		Noeud temp;

		File file = new File(path);
		HashMap<String, Integer> id = identifiantFichier(file);
		File[] filesInDir = file.listFiles();

		for (final File f : filesInDir) 
		{
			positionLigne=0;

			if (f.isDirectory()==false) 
			{
				BufferedReader entree = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
				String ligne;
				StringTokenizer st;
				String mot;
				int nbOcc;
				//pour chaque ligne dans lématisation
				while ((ligne = entree.readLine()) != null)
				{
					positionLigne++;
					//On récupére le mot lématiser
					String text[]= ligne.split("\t");
					mot=text[2];
					//on le normalise à nouveau (étape inutile si la lématisation à été faite correctement)
					mot=normalize(mot);		

					//on décompose le mot
					decomposition=mot.toCharArray();

					//on regarde si le noeud correspondant à la première lettre du mot existe déja
					if(decomposition.length>0){

						trouve=-1;
						for(int i=0;i<result.size();i++){
							if(result.get(i).getLettre()==decomposition[0]){
								trouve=i;
							}
						}
						//Si il n'existe pas on le crée
						if(trouve ==-1){
							result.add(new NoeudNonTerminal(decomposition[0], null));
							trouve=result.size()-1;
						}
						j=1;
						temp=result.get(trouve);
						//jusqu'à la fin du mot
						while(j<decomposition.length){

							trouve=-1;
							if(temp.getNoeudsFils()!=null){
								for(int i=0;i<temp.getNoeudsFils().size();i++){
									if(temp.getNoeudsFils().get(i).getLettre()==decomposition[j]){
										trouve=i;
									}
								}


							}
							//Si il n'existe pas on le crée
							if(trouve ==-1){
								temp.getNoeudsFils().add(new NoeudNonTerminal(decomposition[j], temp));
								trouve=temp.getNoeudsFils().size()-1;
							}
							temp=temp.getNoeudsFils().get(trouve);
							j++;
						}

						//On crée le noeud terminal
						trouve=-1;
						for(int i=0; i<temp.getNoeudsFils().size();i++){
							if(temp.getNoeudsFils().get(i) instanceof NoeudTerminal){
								trouve=i;
							}
						}
						
						if(trouve==-1){
							temp.getNoeudsFils().add(new NoeudTerminal(temp));
							trouve=temp.getNoeudsFils().size()-1;
							temp=temp.getNoeudsFils().get(trouve);
							
						}
						else{
							temp=temp.getNoeudsFils().get(trouve);
						}
						//On modifie les éléemnts ud noeud terminal
						((NoeudTerminal)temp).setFrequenceCorpus((((NoeudTerminal)temp).getFrequenceCorpus())+1);
						if(((NoeudTerminal)temp).getIndexPositions().containsKey(id.get(f.getName()))){
							((NoeudTerminal)temp).getIndexPositions().get(id.get(f.getName())).add(Integer.valueOf(positionLigne));
						}
						else{
							tempPosition=new ArrayList<Integer>();
							tempPosition.add(positionLigne);
							((NoeudTerminal)temp).getIndexPositions().put(id.get(f.getName()),tempPosition);
						}
							

					}
				}


			}

		}

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

	public ArrayList<Noeud> getDebutTerme() {
		return debutTerme;
	}


}
