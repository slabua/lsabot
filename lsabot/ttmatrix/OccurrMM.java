/*
 * Created on 5-ott-2004
 */
package lsabot.ttmatrix;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import elsa.io.TermsRetrieverFile;
import elsa.util.TermsNamesId;


/**
 * La classe <code>OccurrMM</code>, leggendo un file di testo contenente due colonne
 *  separate da un carattere di tabulazione, di cui la prima delle due
 *  contiene il valore di occorrenza della parola presente nella
 *  seconda colonna alla medesima riga, permette di scrivere su un file una matrice
 *  in formato MatrixMarket.<br />
 * La classe puo` essere eseguita su tutti i files di una directory tramite l'ausilio
 *  dello script bash allegato (matrix.sh).
 * 
 * @author Salvatore La Bua
 * @version 1.0
 * 
 * @see elsa.io.TermsRetrieverFile
 * @see elsa.util.TermsNamesId
 */
public class OccurrMM {
    private String occurrFileName;
    private String termsFileName;
    private String matrixFileName;
    private String docNum;
    
    private static final boolean verbose = true;
	private static final String usageString =	"Uso: OccurrMM occurrFile termsFile matrixFile docNum\n" +
												"\toccurrFile: file contenente tutti i termini e le loro occorrenze" +
												" nel documento corrente\n" +
												"\ttermsFile: file che conterra` tutti i termini\n" +
												"\tmatrixFile: file in cui verra` salvata la matrice MatrixMarket\n" +
												"\tdocNum: numero del documento corrente";

    /**
     * Costruttore della classe <code>OccurrMM</code>.
     * 
     * @param ofn <code>String</code> File contenente tutti i termini e le loro occorrenze
     * @param tfn <code>String</code> File che conterra` tutti i termini
     * @param mfn <code>String</code> File in cui verra` salvata la matrice MatrixMarket
     * @param num <code>String</code> Numero del documento corrente
     */
    public OccurrMM(String ofn, String tfn, String mfn, String num) {
        occurrFileName = ofn;
        termsFileName = tfn;
        matrixFileName = mfn;
        docNum = num;
    } // End of OccurrMM construtor
    
    /**
     * Il metodo <code>occurr</code> apre in lettura il file delle occorrenze e
     *  quello dei termini, dal primo file legge i termini e tramite il secondo
     *  file ne identifica l'<i>id</i>, ovvero l'indice di riga.<br />
     * Si ottengono quindi i valori per riempire la matrice: l'id del termine
     *  rappresentera` l'indice di riga <i>i</i> della matrice, il numero del
     *  documento attuale invece sara` l'indice di colonna <i>j</i> della matrice
     *  ed infine, il valore di occorrenza presente nel relativo file, sara` il
     *  valore dell'elemento i,j della matrice.
     */
    public void occurr() {
        String line, occ, term;
        String[] couple;
        
        try {
            
            BufferedReader occurrFile = new BufferedReader(new FileReader(occurrFileName));
            BufferedWriter matrixFile = new BufferedWriter(new FileWriter(matrixFileName, true));
            
            if (verbose)
                System.out.println("Analisi dati del file " + docNum + " \"" + occurrFileName + "\" in corso...");
            
            int DIM = 0;
            BufferedReader termsDimFile = new BufferedReader(new FileReader(termsFileName));
            while (termsDimFile.readLine() != null)
                DIM++;
            
            termsDimFile.close();
            
            int[] values = new int[DIM];
            
            TermsNamesId termsFile = TermsRetrieverFile.getTermsNamesId(termsFileName);
            
            int id;
            while ((line = occurrFile.readLine()) != null) {
                couple = line.trim().split("\t");
                
                occ = couple[0];
                term = couple[1];
                
                id = 1 + termsFile.getId(term.toLowerCase().trim());
                
                // System.out.println(id + " " + docNum + " " + occ);
                matrixFile.write(id + " " + docNum + " " + occ + "\n");
                
                // values[id] = Integer.parseInt(occ);
                
            } // End while block
            
            occurrFile.close();
            
            /*
            for (int i=0; i < DIM; i++) {
                matrixFile.write(values[i] + "\n");
                // System.out.println(values[i]);
            }
            */
            
            matrixFile.close();
            
            if (verbose)
                System.out.println("Scrittura effettuata sul file: " + matrixFileName + "\n");
            
        } catch (IOException ioe) {
			System.err.println(ioe);
			System.exit(-1);
		} // End try-catch block
        
    } // End of occurr method
    
    /**
     * Metodo static di <code>occurr</code>.
     * 
     * @param ofn <code>String</code> File contenente tutti i termini e le loro occorrenze
     * @param tfn <code>String</code> File che conterra` tutti i termini
     * @param mfn <code>String</code> File in cui verra` salvata la matrice MatrixMarket
     * @param num <code>String</code> Numero del documento corrente
     */
	public static void occurr(String ofn, String tfn, String mfn, String num) {
		OccurrMM instance = new OccurrMM(ofn, tfn, mfn, num);
		instance.occurr();		
	} // End of count static method
	
	/**
	 * Metodo principale della classe.
	 * 
	 * @param args <code>String[]</code> Parametri del metodo main
	 */
	public static void main(String[] args) {
		int argc = 0;
		int nArgs = args.length;
		String thisArg = null;
		
		String ofn = null;
		String tfn = null;
		String mfn = null;
		String num = null;
		
		while (argc < nArgs) {
			thisArg = args[argc++].trim();
			if (thisArg != null) {
				if (ofn == null) {
					ofn = thisArg;
				} else if (tfn == null) {
					tfn = thisArg;
				} else if (mfn == null) {
					mfn = thisArg;
				} else if (num == null) {
				    num = thisArg;
				}
			}
		}

		if (ofn != null && tfn != null && mfn != null && num != null) {
			occurr(ofn, tfn, mfn, num);
		} else {
		    System.err.println("Non hai specificato tutti i parametri.\n\n" + usageString);
			System.exit(-1);
		}

	} // End of main method
	
} // End of OccurrMM class
