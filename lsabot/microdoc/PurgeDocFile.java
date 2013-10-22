/*
 * Created on 12-set-2004
 */
package lsabot.microdoc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * La classe <code>PurgeDocFile</code> realizza il file dei micro-documenti
 *  escludendo le frasi piu` corte di un certo numero di parole, questo per
 *  evitare di utilizzare frasi troppo corte per la realizzazione dello spazio
 *  semantico che altrimenti potrebbe contenere codifiche errate dei termini.<br />
 * Viene salvato inoltre un file contenente gli indici posizionali dei
 *  micro-documenti prelevati dal file originario, in modo da poter recuperare
 *  successivamente dal file di tutti i micro-documenti completi di stop-words
 *  solo quelli effettivamente utilizzati per la realizzazione dello spazio.
 * 
 * @author Salvatore La Bua
 * @version 1.0
 */
public class PurgeDocFile {
	private String completeDocFileName;
	private String finalDocFileName;
	private String indexFileName;
	private int wordsNumber;
	
	private static final String usageString =	"Uso: PurgeDocFile inputFile outputFile indexFile minWords\n" +
												"\tinputFile: file contenente tutti i microdocumenti\n" +
												"\toutputFile: file che conterra` i microdocumenti filtrati\n" +
												"\tindexFile: file che conterra` gli indici posizionali dei" +
												" microdocumenti selezionati dal file originario\n" +
												"\tminWords: numero minimo di parole del microdocumento";
	
	/**
	 * Il costruttore assegna semplicemente i valori dei parametri alle
	 *  rispettive variabili.
	 * 
	 * @param completefn <code>String</code> File che contiene tutti i microdocumenti (senza stopwords)
	 * @param finalfn <code>String</code> File che conterra` solo i microdocumenti con un minimo numero di parole
	 * @param indexfn <code>String</code> File che conterra` gli indici posizionali dei micro-documenti selezionati
	 * @param wnum <code>int</code> Numero minimo di parole per i microdocumenti
	 */
	public PurgeDocFile(String completefn, String finalfn, String indexfn, String wnum) {
		completeDocFileName = completefn;
		finalDocFileName = finalfn;
		indexFileName = indexfn;
		wordsNumber = Integer.parseInt(wnum);
	} // End of PurgeDocFile constructor
	
	/**
	 * Questo metodo analizza il file indicato come parametro di ingresso
	 *  e realizza il nuovo file con le sole frasi della lunghezza minima
	 *  impostata.
	 */
	public void writeFile() {
	    String line;
		String[] microDoc;
		int index = 0, maxLength = 0;
		
		try {
		    
			BufferedReader inputFile = new BufferedReader(new FileReader(completeDocFileName));
			BufferedWriter outputFile = new BufferedWriter(new FileWriter(finalDocFileName));
			BufferedWriter indexFile = new BufferedWriter(new FileWriter(indexFileName));
			
			while ((line = inputFile.readLine()) != null) {
				
				// crea un vettore con gli elementi della riga letta
				microDoc = line.trim().split(" ");
				
				// calcola il numero di parole del microdocumento piu` lungo
				// e` soltanto un informazione riguardo le frasi analizzate
				if (microDoc.length > maxLength)
					maxLength = microDoc.length;
				
				if (microDoc.length >= wordsNumber) {
					outputFile.write(line + "\n");
					indexFile.write(index + "\n");
				}
				
				index++;
				
			} // End while block
			
			inputFile.close();
			outputFile.close();
			indexFile.close();
			
			System.err.println("Il numero di parole del microdocumento piu` lungo presente " +
								"nel file completo e`: " + maxLength);
			System.out.println("File " + finalDocFileName + " saved.");
			System.out.println("File " + indexFileName + " saved.");
			
		} catch (IOException ioe) {
			System.err.println(ioe);
			System.exit(-1);
		} // End try-catch block
		
	} // End of writeFile method
	
	/**
	 * Metodo static di <code>writeFile</code>.
	 * 
	 * @param completefn <code>String</code> File che contiene tutti i microdocumenti (senza stopwords)
	 * @param finalfn <code>String</code> File che conterra` solo i microdocumenti con un minimo numero di parole
	 * @param indexfn <code>String</code> File che conterra` gli indici posizionali dei micro-documenti selezionati
	 * @param wnum <code>int</code> Numero minimo di parole per i microdocumenti
	 */
	public static void writeFile(String completefn, String finalfn, String indexfn, String wnum) {
		PurgeDocFile instance = new PurgeDocFile(completefn, finalfn, indexfn, wnum);
		instance.writeFile();
	} // End of writeFile static method
	
	/**
	 * Metodo principale della classe.
	 * 
	 * @param args <code>String[]</code> Parametri del metodo main
	 */
	public static void main(String[] args) {
		int argc = 0;
		int nArgs = args.length;
		String thisArg = null;
		
		String completefn = null;
		String finalfn = null;
		String indexfn = null;
		String wnum = null;
		
		while (argc < nArgs) {
			thisArg = args[argc++].trim();
			if (thisArg != null) {
				if (completefn == null) {
					completefn = thisArg;
				} else if (finalfn == null) {
					finalfn = thisArg;
				} else if (indexfn == null) {
					indexfn = thisArg;
				} else if (wnum == null) {
					wnum = thisArg;
				}
			}
		}
		
		if (completefn != null && finalfn != null && indexfn != null && wnum != null) {
			writeFile(completefn, finalfn, indexfn, wnum);
		} else {
			System.err.println("Non hai specificato tutti i parametri.\n\n" + usageString);
			System.exit(-1);
		}
		
	} // End of main method

} // End of PurgeDocFile class
