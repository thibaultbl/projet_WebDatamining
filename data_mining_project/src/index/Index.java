package index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import noeuds.Noeud;
import noeuds.NoeudNonTerminal;
import noeuds.NoeudTerminal;

import com.google.common.collect.HashBiMap;

public class Index {
	//liste des noeus initiaux (correspondant à la première lettre de chaque terme présent dans l'index)
	private ArrayList<Noeud> debutTerme;
	//liste contenant la taille de chaque fichier
	private static HashMap<Integer, Integer> sizeFile=new HashMap<Integer, Integer>();
	//liste des noms de fichiers (utile pour l'update de l'index)
	private static Set<File> listFiles;

	public Index(String path) throws IOException {
		termTooFrequent frequent=new termTooFrequent(path, 1200, 100);
		debutTerme=this.InitialiserIndex(path, frequent);
		listFiles = this.listeFichiers(path);

		/*for(int i=0; i<frequent.getFrequentTerm().size();i++){
			this.deleteTerm(frequent.getFrequentTerm().get(i));
		}*/

		//on supprime les termes trop fréquents de l'index
		/**
		 * Possibilité d'ajouter des termes directement ici : déterminants, ...
		 */
	}

	@Override
	public String toString() {
		return "debut terme :"+debutTerme;
	}

	/**
	 * Fonction qui récupère les noms de fichiers d'un répertoire
	 * @param folder
	 * @return
	 */

	@SuppressWarnings("null")
	public static Set<File> listeFichiers(String path) {

		File folder = new File(path);
		Set<File> files = null;

		for (final File entry : folder.listFiles()) {
			if (entry.isDirectory()==false) {
				files.add(entry.getAbsoluteFile());
			}
		}
		return files;
	}


	/**
	 * Function qui associe un id à un nom de fichier
	 * @param folder
	 * @return
	 */
	public static HashBiMap<String, Integer> identifiantFichier(File folder) {
		// Va lister tous les fichiers dans le répertoire et leur attribue un ID
		HashBiMap<String, Integer> id = HashBiMap.create(folder.list().length);
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
	 * Pour initialiser l'arbre d'index
	 * @throws IOException
	 */
	public static ArrayList<Noeud> InitialiserIndex(String path, termTooFrequent frequent) throws IOException {
		ArrayList<Noeud> result=new ArrayList<Noeud>();

		char[] decomposition;
		int trouve;
		int j;
		int positionLigne;
		ArrayList<Integer> tempPosition;
		HashMap<Integer, ArrayList<Integer>> hashmapTemp;
		Noeud temp;

		File file = new File(path);
		HashBiMap<String, Integer> id = identifiantFichier(file);
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

					if(!frequent.getFrequentTerm().contains(mot)){
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
			sizeFile.put(id.get(f.getName()), positionLigne);
		}
		return result;
	}

	/**
	 * Pour ajouter de nouveaux documents à l'index initial
	 * @throws IOException
	 */
	public static void nvDocIndex(String path, Set<File> listF, termTooFrequent frequent) throws IOException {

		ArrayList<Noeud> result=new ArrayList<Noeud>();

		char[] decomposition;
		int trouve;
		int j;
		int positionLigne;
		ArrayList<Integer> tempPosition;
		Noeud temp;

		/*
		 * Pour ajouter de nouveaux documents à l'index :
		 * 1 - Récupérer l'index initial
		 * 2 - Définir le chemin pour accéder aux nouveaux documents
		 * 2bis - Si les documents ne sont pas déjà inclus dans l'index, on les ajoute. Sinon, on passe au suivant
		 * 3 - MaJer l'index
		 * 
		 */

		//Listage de tous les fichiers dans le répertoire
		File folder = new File(path);
		File[] filesInDir = folder.listFiles();
		HashBiMap<String, Integer> id = identifiantFichier(folder);

		for (final File f : filesInDir) {
			if (f.isDirectory()==false) {
				if (listF.contains(f.getAbsoluteFile())) {
					//Do nothing
				} else {
					positionLigne=0;

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

							if(!frequent.getFrequentTerm().contains(mot)){
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
					listFiles.add(f.getAbsoluteFile());
					sizeFile.put(id.get(f.getName()), positionLigne);
				}
			}
		}
	}


	/**
	 * Pour supprimer un terme dans l'index
	 * @param mot
	 */
	public void deleteTerm(String mot){
		//1 - on se place dans le noeud terminal correspondant au terme
		Noeud temp=this.getNoeudTerminal(mot);
		char[] arrayChar=mot.toCharArray();

		//2 - on supprime tous les noeuds jusqu'a ce qu'un noeud ait plusieurs Noeuds fils
		if(temp!=null){
			int g=0;
			if(temp.getNoeudPere().getNoeudPere()!=null){
				Noeud temp2=temp.getNoeudPere();
				Noeud temp3=temp;
				while((temp2.getNoeudsFils().size()<2)&&(g<arrayChar.length)){
					temp2.getNoeudsFils().remove(temp3);
					temp3=temp2;
					temp2=temp2.getNoeudPere();
					g++;
				}
				temp2.getNoeudsFils().remove(temp3);


				for(int i=0;i<temp2.getNoeudsFils().size();i++){
					if(temp2.getNoeudsFils().get(i) instanceof NoeudTerminal){
						temp2.getNoeudsFils().remove(i);
					}
				}


			}
			else{
				debutTerme.remove(debutTerme.indexOf(temp.getNoeudPere()));
			}
		}

	}



	/**
	 * Function to normalize a string (delete punctuation and change in lower case)
	 * @param string
	 * @return
	 */
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

	public void setDebutTerme(ArrayList<Noeud> debutTerme) {
		this.debutTerme = debutTerme;
	}

	/**
	 * Function to return the terminal node for a particular string
	 * @param terme
	 * @return
	 */
	public NoeudTerminal getNoeudTerminal(String terme){
		Noeud temp=null;

		//on cherche le noeud terminal correspondant au terme
		//1 - on se place dans le noeud terminal correspondant au terme
		char[] arrayChar=terme.toCharArray();
		int trouve=-1;
		//Si il s'agit d'une chaine vide on ne fait rien
		if(arrayChar.length<=0){
			return null;
		}
		for(int j=0;j<this.getDebutTerme().size();j++){
			if(arrayChar[0]==this.getDebutTerme().get(j).getLettre()){
				trouve=j;
			}
		}

		if(trouve !=-1){

			temp=this.getDebutTerme().get(trouve);
			for(int i=1;i<arrayChar.length;i++){
				trouve=-1;
				for(int k=0;k<temp.getNoeudsFils().size();k++){
					if(temp.getNoeudsFils().get(k).getLettre()==arrayChar[i]){
						trouve=k;
					}
				}
				if(trouve==-1){
					return null;
				}
				temp=temp.getNoeudsFils().get(trouve);
			}
			for(int k=0;k<temp.getNoeudsFils().size();k++){
				if(temp.getNoeudsFils().get(k) instanceof NoeudTerminal){
					trouve=k;
				}
			}
			try
			{
				temp=temp.getNoeudsFils().get(trouve);
			}
			catch(IndexOutOfBoundsException e){
				JOptionPane.showMessageDialog(null, "Le terme recherché n'est pas dans l'index", "Le terme recherché n'est pas dans l'index",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		else{
			System.out.println("le terme "+terme+" n'est pas dans l'index");
		}
		try{
			return (NoeudTerminal)temp;
		}
		catch(ClassCastException e){
			JOptionPane.showMessageDialog(null, "Le terme recherché n'est pas dans l'index", "Le terme recherché n'est pas dans l'index",
					JOptionPane.ERROR_MESSAGE);
		}
		return null;

	}


	public static HashMap<Integer, Integer> getSizeFile() {
		return sizeFile;
	}
}


