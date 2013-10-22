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
 * La classe <code>CodVettRetriever</code> analizza il file dei microdocumenti e, per
 *  ciascuna parola trovata, ne identifica la posizione nel file dei termini in modo
 *  da poter recuperare la sua codifica vettoriale dalla matrice U.
 * 
 * @author Salvatore La Bua
 * @version 1.0
 * 
 * @see elsa.io.TermsRetrieverFile
 * @see elsa.util.TermsNamesId
 */
public class CodVettRetriever {
	private String microdocFileName;
	private String termsFileName;
	private String codvettFileName;
	private String codVettDocFileName;
	private static int DIM = 100;
	private static boolean APPEND = false;
	
	// SCALE e` un fattore di proporzionalita` che puo` essere utilizzato per
	// ottenere diversi risultati nel reperimento delle codifiche vettoriali
	private static double SCALE = 100;
	
	private String[] vett;
	private String[] documents;
	
	private int id = 0;
	
	private static double[] codif_sum = new double[DIM];
	private double norm2 = 0;
	
	private static final boolean verbose = true;
	private static final String usageString =	"Uso: CodVettRetriever microdocFile termsFile codvettFile output [svdDIM]\n" +
												"\tmicrodocFile: file contenente tutti i microdocumenti\n" +
												"\ttermsFile: file contenente tutti i termini\n" +
												"\tcodvettFile: file che contiene le codifiche vettoriali delle parole\n" +
												"\toutput: file che conterra` le codifiche vettoriali dei microdocumenti\n" +
												"\tsvdDIM: dimensione del troncamento SVD (Default = 100)";
	
	/**
	 * Costruttore della classe <code>CodVettRetriever</code> con cinque parametri.
	 * 
	 * @param mdfn <code>String</code> File dei microdocumenti
	 * @param tfn <code>String</code> File dei termini
	 * @param cvfn <code>String</code> File delle codifiche vettoriali delle parole
	 * @param cvdfn <code>String</code> File che conterra` le codifiche vettoriali dei microdocumenti
	 * @param svdDIM <code>int</code> Dimensione del troncamento SVD (Default = 100)
	 */
	public CodVettRetriever(String mdfn, String tfn, String cvfn, String cvdfn, int svdDIM) {
	    microdocFileName = mdfn;
	    termsFileName = tfn;
	    codvettFileName = cvfn;
	    codVettDocFileName = cvdfn;
	    DIM = svdDIM;
	} // End of CodVettRetriever constructor
	
	/**
	 * Costruttore della classe <code>CodVettRetriever</code> con quattro parametri.
	 * 
	 * @param mdfn <code>String</code> File dei microdocumenti
	 * @param tfn <code>String</code> File dei termini
	 * @param cvfn <code>String</code> File delle codifiche vettoriali delle parole
	 * @param cvdfn <code>String</code> File che conterra` le codifiche vettoriali dei microdocumenti
	 */
	public CodVettRetriever(String mdfn, String tfn, String cvfn, String cvdfn) {
	    this(mdfn, tfn, cvfn, cvdfn, 100);
	} // End of CodVettRetriever constructor
	
	/**
	 * Il metodo <code>findVettCod</code> e` quello che effettua l'associazione
	 *  dei vettori presenti nella matrice U.
	 */
	public void findVettCod() {
	    int i, docNum = 0;
		String linedoc, linevett;
		
		try {
		    
		    System.err.println("Scale factor: " + SCALE);
		    
		    BufferedReader reader = new BufferedReader(new FileReader(microdocFileName));
		    BufferedWriter outputFile;
		    
		    if (APPEND) {
		        // System.out.println("Append mode is ON");
		        outputFile = new BufferedWriter(new FileWriter(codVettDocFileName, true));
		    } else {
		        outputFile = new BufferedWriter(new FileWriter(codVettDocFileName));
		    }
		    
		    TermsNamesId term = TermsRetrieverFile.getTermsNamesId(termsFileName);
		    
		    while ((linedoc = reader.readLine()) != null) {
		        docNum++;
		        
		        // crea un vettore di String delle parole della prima frase
		        documents = linedoc.trim().split("[^a-zA-Z]");
		        
		        // apre il file delle codifiche vettoriali delle parole
		        BufferedReader vettFile = new BufferedReader(new FileReader(codvettFileName));
		        
		        if (verbose)
		            System.out.println("Documento numero " + docNum);
		        
		        i = 0;
		        while (i < documents.length) {
		            id = term.getId(documents[i].toLowerCase().trim());
		            
		            if (id != -1) {
		                
		                vettRetr(documents[i], vettFile, outputFile);
		                
		                vettFile.close();
		                vettFile = new BufferedReader(new FileReader(codvettFileName));
		            }
		            
		            i++;
		            
		        } // End while block
		        
		        for (int el=0; el < DIM; el++) {
	                if (el != (DIM - 1)) {
	                    outputFile.write(codif_sum[el] + " ");
	                } else {
	                    outputFile.write(codif_sum[el] + "\n");
	                    
	                    for (int el2=0; el2 < DIM; el2++)
	                        codif_sum[el2] = 0;
	                    
	                } // End if-else block
			
	             } // End for block
		        
		        vettFile.close();
		        
		    } // End while block
		    
		    reader.close();
		    outputFile.close();
		    System.out.println("File saved.");
		    
		} catch (IOException ioe) {
		    System.err.println(ioe);
		    System.exit(-1);
		} // End try-catch block
		
	} // End of findVettCod method
	
	/**
	 * Questo metodo effettua le operazioni sulle codifiche vettoriali delle parole.
	 * 
	 * @param word <code>String</code> Parola corrente
	 * @param vettF <code>BufferedReader</code> File delle codifiche vettoriali delle parole
	 * @param outF <code>BufferedWriter</code> File che conterra` le codifiche vettoriali dei microdocumenti
	 */
	public void vettRetr(String word, BufferedReader vettF, BufferedWriter outF) { 
	    int id;
	    int counter = 0;
	    String currentWord = word;
	    String linevett;
	    
		try {
		    
		    BufferedReader vettFile = vettF;
		    BufferedWriter outputFile = outF;
		    
		    TermsNamesId term = TermsRetrieverFile.getTermsNamesId(termsFileName);
		    id = term.getId(currentWord.toLowerCase().trim());
		    
		    counter = 0;
		    
		    while ((id != -1) && (linevett = vettFile.readLine()) != null) {
		    	
		        if (counter == id) {
		            
		            if (verbose)
		                System.out.println(id + "\t" + currentWord);
		            
		            sumCod(linevett, outputFile);
		            
		            break;
		            
		        } else {
		           counter++;
		        } // End if-else block
		        
		    } // End while block
		    
		} catch (IOException ioe) {
		    System.err.println(ioe);
		    System.exit(-1);
		} // End try-catch block
		
	} // End of vettRetr method
	
	/**
	 * Il metodo <code>sumCod</code> calcola la codifica vettoriale per ciascun microdocumento.
	 * 
	 * @param line <code>String</code> Linea attualmente letta dal file delle codifiche vettoriali delle parole
	 * @param outF <code>BufferedWriter</code> File che conterra` le codifiche vettoriali dei microdocumenti
	 */
	public void sumCod(String line, BufferedWriter outF) {
	    int i, doc;
	    String[] linevett = line.trim().split(" ");
	    
        BufferedWriter outputFile = outF;
        
        for (i=0; i < DIM; i++) {
            codif_sum[i] += (SCALE * Double.parseDouble(linevett[i]));
            
            // System.err.print(codif_sum[i] + " ");
        }
        // System.err.print("\n");
        
        norm2 = Vett.norma2(codif_sum);
        
        for (i=0; i < DIM; i++) {
            if (norm2 != 0) {
            
                // codif_sum adesso e` la codifica vettoriale
                // normalizzata del microdocumento
                codif_sum[i] /= norm2;
                
            } else {
                
        	    // gestisce le divisioni per zero: quando la norma e` zero,
        	    // il modulo del vettore normalizzato sara` anche zero
        	    codif_sum[i] = 0;
		    
        	} // End if-else block
		
        } // End for block
        
	} // End of sumCod method
	
	/**
	 * Il metodo statico <code>setScale</code> imposta il valore della
	 *  variabile <code>SCALE</code>.
	 * 
	 * @param scaleValue <code>double</code> Fattore moltiplicativo di scala
	 */
	public static void setScale(double scaleValue) {
	    SCALE = scaleValue;
	} // End of setScale static method
	
	/**
	 * Il metodo statico <code>setAppendMode</code> imposta il valore della
	 *  variabile <code>APPEND</code>.
	 * 
	 * @param appendValue <code>boolean</code> Flag che indica se il file delle codifiche deve essere
	 * 						aperto in scrittura semplice o in modalita` append
	 */
	public static void setAppendMode(boolean appendValue) {
	    APPEND = appendValue;
	    if (APPEND) {
	        System.err.println("Append mode is ON");
	    } else {
	        System.err.println("Append mode is OFF");
	    } // End if-else block
	} // End of setAppendMode static method
	
	/**
	 * Metodo static di <code>findVettCod</code> con cinque parametri.
	 * 
	 * @param mdfn <code>String</code> File dei microdocumenti
	 * @param tfn <code>String</code> File dei termini
	 * @param cvfn <code>String</code> File delle codifiche vettoriali delle parole
	 * @param cvdfn <code>String</code> File che conterra` le codifiche vettoriali dei microdocumenti
	 * @param svdDIM <code>int</code> Dimensione del troncamento SVD (Default = 100)
	 */
	public static void findVettCod(String mdfn, String tfn, String cvfn, String cvdfn, int svdDIM) {
	    CodVettRetriever instance = new CodVettRetriever(mdfn, tfn, cvfn, cvdfn, svdDIM);
	    instance.findVettCod();
	} // End of findVettCod static method
	
	/**
	 * Metodo static di <code>findVettCod</code> con quattro parametri.
	 * 
	 * @param mdfn <code>String</code> File dei microdocumenti
	 * @param tfn <code>String</code> File dei termini
	 * @param cvfn <code>String</code> File delle codifiche vettoriali delle parole
	 * @param cvdfn <code>String</code> File che conterra` le codifiche vettoriali dei microdocumenti
	 */
	public static void findVettCod(String mdfn, String tfn, String cvfn, String cvdfn) {
	    CodVettRetriever instance = new CodVettRetriever(mdfn, tfn, cvfn, cvdfn);
	    instance.findVettCod();
	} // End of findVettCod static method
	
	/**
	 * Metodo principale della classe.
	 * 
	 * @param args <code>String[]</code> Parametri del metodo main
	 */
	public static void main(String[] args) {
		int argc = 0;
		int nArgs = args.length;
		String thisArg = null;
		
		String mdfn = null;
		String tfn = null;
		String cvfn = null;
		String cvdfn = null;
		int svdDIM = 100;
		
		while (argc < nArgs) {
			thisArg = args[argc++].trim();
			if (thisArg != null) {
				if (mdfn == null) {
					mdfn = thisArg;
				} else if (tfn == null) {
					tfn = thisArg;
				} else if (cvfn == null) {
					cvfn = thisArg;
				} else if (cvdfn == null) {
					cvdfn = thisArg;
				} else if (svdDIM == 100) {
					svdDIM = Integer.parseInt(thisArg);
				}
			}
		}

		if (mdfn != null && tfn != null && cvfn != null && cvdfn != null) {
		    findVettCod(mdfn, tfn, cvfn, cvdfn, svdDIM);
		} else {
		    System.err.println("Non hai specificato tutti i parametri.\n\n" + usageString);
			System.exit(-1);
		}

	} // End of main method
	
} // End of CodVettRetriever class