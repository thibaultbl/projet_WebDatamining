package main;

import index.Index;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import search.search;

public class main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		
		String path="Doc/lemmatisation";
		Index index=new Index(path);
		System.out.println(index.identifiantFichier(new File(path)));
		
		String requete=JOptionPane.showInputDialog ("Rentrez votre requête ici");
		//"avocat syndicat"

		HashMap<Integer, Double> testSearch=search.searchTerm(index, requete, path);
		System.out.println(testSearch);
		final JFrame frame = new JFrame();

		// Release the window and quit the application when it has been closed
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JOptionPane.showMessageDialog(frame, testSearch);


	}
	
	

}
