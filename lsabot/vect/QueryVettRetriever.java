/*
 * Created on 17-set-2004
 */
package lsabot.vect;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import elsa.io.TermsRetrieverFile;
import elsa.util.TermsNamesId;


/**
 * La classe <code>QueryVettRetriever</code> analizza il file della query e per
 *  ciascuna parola trovata, ne identifica la posizione nel file dei termini in modo
 *  da ottenere un vettore contenente per ciascuna parola, il proprio numero di
 *  occorrenza nella query stessa.<br />
 * Costruito il vettore della query immessa dall'utente, e` necessario effettuare
 *  prodotti tra tale vettore e la matrice U*inv(Sigma) per poter proiettare il vettore
 *  di query nello stesso spazio dei vettori dei documenti, contenuti nella matrice V.
 * La classe necessita del file contenente il trasposto del prodotto tra la matrice U
 *  e l'inverso della matrice Sigma.
 * 
 * @author Salvatore La Bua
 * @version 1.0
 * 
 * @see elsa.io.TermsRetrieverFile
 * @see elsa.util.TermsNamesId
 */
public class QueryVettRetriever {
	private String queryFileName;
	private String termsFileName;
	private String USMatrixFileName;
	private String queryCodFileName;
	
	private static int DIM = 100;
	
	private int id = 0;
	private int termsNum = 0;
	
	private int[] queryVett;
	
	private static final boolean verbose = false;
	private static final String usageString =	"Uso: QueryVettRetriever queryFile termsFile USMatrixFile queryCodFile [svdDIM]\n" +
												"\tqueryFile: file contenente la query da codificare\n" +
												"\ttermsFile: file contenente tutti i termini\n" +
												"\tUSMatriFile: file che contiene il trasposto del prodotto tra la matrice U e l'inverso" +
												" della matrice Sigma\n" +
												"\tqueryCodFile: file che conterra` le codifiche vettoriali dei microdocumenti\n" +
												"\tsvdDIM: dimensione del troncamento SVD (Default = 100)";
	
	/**
	 * Costruttore della classe <code>QueryVettRetriever</code> con sei parametri.
	 * 
	 * @param qfn <code>String</code> File contenente la query
	 * @param tfn <code>String</code> File dei termini
	 * @param usmfn <code>String</code> File della matrice (U*Inv(Sigma))'
	 * @param qcfn <code>String</code> File che conterra` la codifica vettoriale della query
	 * @param svdDIM <code>int</code> Dimensione del troncamento SVD (Default = 100)
	 */
	public QueryVettRetriever(String qfn, String tfn, String usmfn, String qcfn, int svdDIM) {
	    queryFileName = qfn;
	    termsFileName = tfn;
	    USMatrixFileName = usmfn;
	    queryCodFileName = qcfn;
	    DIM = svdDIM;
	} // End of QueryVettRetriever constructor
	
	/**
	 * Costruttore della classe <code>QueryVettRetriever</code> con cinque parametri.
	 * 
	 * @param qfn <code>String</code> File contenente la query
	 * @param tfn <code>String</code> File dei termini
	 * @param usmfn <code>String</code> File della matrice (U*Inv(Sigma))'
	 * @param qcfn <code>String</code> File che conterra` la codifica vettoriale della query
	 */
	public QueryVettRetriever(String qfn, String tfn, String usmfn, String qcfn) {
	    this(qfn, tfn, usmfn, qcfn, 100);
	} // End of QueryVettRetriever constructor
	
	/**
	 * Il metodo <code>findQueryCod</code> produce un vettore contenente, per ciascuna
	 *  parola presente nel file di tutti i termini, il suo numero di occorrenza nella
	 *  query.
	 */
	public void findQueryOcc() {
	    int i;
		String linedoc, linevett;
		String[] document;
		
		try {
		    
		    BufferedReader reader = new BufferedReader(new FileReader(queryFileName));
		    BufferedReader termsFile = new BufferedReader(new FileReader(termsFileName));
		    
		    while (termsFile.readLine() != null)
		        termsNum++;
		    
		    termsFile.close();
		    
		    // inizializzo il vettore che conterra` le occorrenze
		    // di ciascuna parola della query
		    queryVett = new int[termsNum];
		    
		    TermsNamesId term = TermsRetrieverFile.getTermsNamesId(termsFileName);
		    
		    if ((linedoc = reader.readLine()) != null) {
		        // crea un vettore di String delle parole della query
		        document = linedoc.trim().split("[^a-zA-Z]");
		        
		        for (i=0; i < document.length; i++) {
		            id = term.getId(document[i].toLowerCase().trim());
		            
		            if (id != -1)
		                queryVett[id]++;
		            
		        } // End for block
		        
		        if (verbose)
		            for (i=0; i < termsNum; i++)
		                if (queryVett[i] != 0)
		                    System.err.println(i + " -> " + queryVett[i]);
		        
		    } // End if block
		    
		    reader.close();
		    
		} catch (IOException ioe) {
		    System.err.println(ioe);
		    System.exit(-1);
		} // End try-catch block
		
	} // End of findQueryOcc method
	
	/**
	 * Il metodo <code>getQUS</code> effettua il prodotto tra il vettore della query e
	 *  la matrice (U*Inv(Sigma))'.
	 * 
	 * @return qu E` il vettore a dimensione DIM della query che dovra` essere
	 *             confrontato con i vettori dei documenti
	 */
	public double[] getQUS() {
	    double[] qu = new double[DIM];
	    
	    findQueryOcc();
	    
	    try {
	        
	        BufferedReader USMatrixFile = new BufferedReader(new FileReader(USMatrixFileName));
	        BufferedWriter queryCodFile  = new BufferedWriter(new FileWriter(queryCodFileName));
	        
		    String USMatrixLine;
		    String[] line;
		    
		    int i, j = 0;
		    while ((USMatrixLine = USMatrixFile.readLine()) != null) {
	            line = USMatrixLine.trim().split(" ");
	            
	            for (i=0; i < termsNum; i++)
	                if (queryVett[i] != 0)
	                    qu[j] += (queryVett[i] * Double.parseDouble(line[i]));
	            
	            if (j != (DIM - 1)) {
	                queryCodFile.write(qu[j] + " ");
                } else {
                    queryCodFile.write(qu[j] + "");
                } // End if-else block
	            
	            if (verbose)
	                System.out.println("Prodotto per l'elemento " + j + " " + qu[j]);
	            
	            j++;
	            
	        } // End while block
	        
		    queryCodFile.close();
	        System.out.println("File saved.");
		    
	    } catch (IOException ioe) {
		    System.err.println(ioe);
		    System.exit(-1);
		} // End try-catch block
		
	    return qu;
	    
	} // End of getQUS method
	
	/**
	 * Metodo static di <code>findQueryOcc</code> con sei parametri.
	 * 
	 * @param qfn <code>String</code> File contenente la query
	 * @param tfn <code>String</code> File dei termini
	 * @param usmfn <code>String</code> File della matrice (U*Inv(Sigma))'
	 * @param qcfn <code>String</code> File che conterra` la codifica vettoriale della query
	 * @param svdDIM <code>int</code> Dimensione del troncamento SVD (Default = 100)
	 */
	public static void getQUS(String qfn, String tfn, String usmfn, String qcfn, int svdDIM) {
	    QueryVettRetriever instance = new QueryVettRetriever(qfn, tfn, usmfn, qcfn, svdDIM);
	    instance.getQUS();
	} // End of findQueryOcc static method
	
	/**
	 * Metodo static di <code>findQueryOcc</code> con cinque parametri.
	 * 
	 * @param qfn <code>String</code> File contenente la query
	 * @param tfn <code>String</code> File dei termini
	 * @param usmfn <code>String</code> File della matrice (U*Inv(Sigma))'
	 * @param qcfn <code>String</code> File che conterra` la codifica vettoriale della query
	 */
	public static void getQUS(String qfn, String tfn, String usmfn, String qcfn) {
	    QueryVettRetriever instance = new QueryVettRetriever(qfn, tfn, usmfn, qcfn);
	    instance.getQUS();
	} // End of findQueryOcc static method
	
	/**
	 * Metodo principale della classe.
	 * 
	 * @param args <code>String[]</code> Parametri del metodo main
	 */
	public static void main(String[] args) {
		int argc = 0;
		int nArgs = args.length;
		String thisArg = null;
		
		String qfn = null;
		String tfn = null;
		String usmfn = null;
		String qcfn = null;
		int svdDIM = 100;
		
		while (argc < nArgs) {
			thisArg = args[argc++].trim();
			if (thisArg != null) {
				if (qfn == null) {
					qfn = thisArg;
				} else if (tfn == null) {
					tfn = thisArg;
				} else if (usmfn == null) {
					usmfn = thisArg;
				} else if (qcfn == null) {
					qcfn = thisArg;
				} else if (svdDIM == 100) {
					svdDIM = Integer.parseInt(thisArg);
				}
			}
		}

		if (qfn != null && tfn != null && usmfn != null && qcfn != null) {
		    getQUS(qfn, tfn, usmfn, qcfn, svdDIM);
		} else {
		    System.err.println("Non hai specificato tutti i parametri.\n\n" + usageString);
			System.exit(-1);
		}

	} // End of main method
	
} // End of QueryVettRetriever class