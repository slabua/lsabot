/*
 * Created on 26-ott-2004
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
 * La classe <code>AimlCodRetr</code> provvede a creare un altro files delle
 *  codifiche dei termini unicamente per quelli presenti nei templates AIML, in modo
 *  da poter ridurre notevolmente il tempo di esecuzione nel qual caso l'utente scelga
 *  di effettuare ricerche relativamente al dialogo naturale.
 * 
 * @author Salvatore La Bua
 * @version 1.0
 * 
 * @see elsa.io.TermsRetrieverFile
 * @see elsa.util.TermsNamesId
 */
public class AimlCodRetr {
    private String aimlTermsFileName;
    private String termsFileName;
    private String totCodFileName;
    private String aimlCodFileName;
    
    private static final boolean verbose = true;
	private static final String usageString =	"Uso: QueryVettRetriever aimlTermsFile termsFile totCodFile aimlCodFile\n" +
												"\taimlTermsFile: file contenente i termini relativi ai soli templates AIML\n" +
												"\ttermsFile: file contenente tutti i termini\n" +
												"\ttotCodFile: file che contiene tutte le codifiche vettoriali della matrice U\n" +
												"\taimlCodFile: file che conterra` le codifiche vettoriali dei termini dei" +
												" templates AIML";
	
	/**
	 * Costruttore della classe <code>AimlCodRetr</code>.
	 * 
	 * @param atfn <code>String</code> File contenente i termini relativi ai soli templates AIML
	 * @param tfn <code>String</code> File contenente tutti i termini
	 * @param tcfn <code>String</code> File che contiene tutte le codifiche vettoriali della matrice U
	 * @param acfn <code>String</code> File che conterra` le codifiche vettoriali dei termini dei templates AIML
	 */
    public AimlCodRetr(String atfn, String tfn, String tcfn, String acfn) {
        aimlTermsFileName = atfn;
        termsFileName = tfn;
        totCodFileName = tcfn;
        aimlCodFileName = acfn;
    } // End of AimlCodRetr constructor
    
    /**
     * Il metodo <code>getAimlTermsCod</code>, scorrendo il file dei termini relativi unicamente
     *  ai templates AIML, prende in input ciascun termine e ne verifica la posizione all'interno
     *  del file contenente tutti i possibili termini.<br />
     * Ottenuta la posizione del termine, si preleva la relativa codifica vettoriale presente nel
     *  file di tutte le codifiche dei termini e la si scrive su un altro file che conterra`, alla fine,
     *  tutte le codifiche vettoriali dei soli termini appartenenti ai templates AIML estratte dal file
     *  delle codifiche completo.
     */
    public void getAimlTermsCod() {
        String aimlWord;
        String currentLine;
        int id, currentLineNumber;
        
        try {
            
            BufferedReader aimlTermsFile = new BufferedReader(new FileReader(aimlTermsFileName));
            BufferedWriter aimlCodFile = new BufferedWriter(new FileWriter(aimlCodFileName));
            
            TermsNamesId termsFile = TermsRetrieverFile.getTermsNamesId(termsFileName);
            
            while ((aimlWord = aimlTermsFile.readLine()) != null) {
               id = 1 + termsFile.getId(aimlWord.toLowerCase().trim());
               
               if (verbose)
                   System.out.println("Il termine " + aimlWord + " si trova alla riga " + id +
                       											" del file di tutti i termini");
               
               BufferedReader totCodFile = new BufferedReader(new FileReader(totCodFileName));
               
               currentLineNumber = 0;
               while ((currentLine = totCodFile.readLine()) != null) {
                   currentLineNumber++;
                   
                   if ((id != -1) && (id == currentLineNumber)) {
                       aimlCodFile.write(currentLine + "\n");
                       
                       break;
                   }
                   
               } // End while block
               
               totCodFile.close();
                
            } // End while block
            
            aimlTermsFile.close();
            aimlCodFile.close();
            System.out.println("File saved.");
            
        } catch (IOException ioe) {
		    System.err.println(ioe);
		    System.exit(-1);
		} // End try-catch block
		
    } // End of getAimlTermsCod method
    
    /**
     * Metodo static di <code>getAimlTermsCod</code>.
     * 
	 * @param atfn <code>String</code> File contenente i termini relativi ai soli templates AIML
	 * @param tfn <code>String</code> File contenente tutti i termini
	 * @param tcfn <code>String</code> File che contiene tutte le codifiche vettoriali della matrice U
	 * @param acfn <code>String</code> File che conterra` le codifiche vettoriali dei termini dei templates AIML
     */
    public static void getAimlTermsCod(String atfn, String tfn, String tcfn, String acfn) {
        AimlCodRetr instance = new AimlCodRetr(atfn, tfn, tcfn, acfn);
        instance.getAimlTermsCod();
    } // End of getAimlTermsCod static method
    
	/**
	 * Metodo principale della classe.
	 * 
	 * @param args <code>String[]</code> Parametri del metodo main
	 */
	public static void main(String[] args) {
		int argc = 0;
		int nArgs = args.length;
		String thisArg = null;
		
		String atfn = null;
		String tfn = null;
		String tcfn = null;
		String acfn = null;
		
		while (argc < nArgs) {
			thisArg = args[argc++].trim();
			if (thisArg != null) {
				if (atfn == null) {
					atfn = thisArg;
				} else if (tfn == null) {
					tfn = thisArg;
				} else if (tcfn == null) {
					tcfn = thisArg;
				} else if (acfn == null) {
					acfn = thisArg;
				}
			}
		}

		if (atfn != null && tfn != null && tcfn != null && acfn != null) {
		    getAimlTermsCod(atfn, tfn, tcfn, acfn);
		} else {
		    System.err.println("Non hai specificato tutti i parametri.\n\n" + usageString);
			System.exit(-1);
		}

	} // End of main method
    
} // End of AimlCodRetr class
