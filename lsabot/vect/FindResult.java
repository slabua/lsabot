/*
 * Created on 24-set-2004
 */
package lsabot.vect;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 * La classe <code>FindResult</code> trova e visualizza il microdocumento
 *  che ha minore distanza rispetto alla query. 
 * 
 * @author Salvatore La Bua
 * @version 1.0
 */
public class FindResult {
    private String distFileName;
    private String microdocFileName;
    private int fullDocuments;
    
    private static String repeatQuestString = "Would you please repeat the question?";
    
    private static final boolean verbose = true;
	private static final String usageString =	"Uso: FindResult distFile microdocFile\n" +
    											"\tdistFileName: file contenente le distanze tra la query" +
    											" e i vari microdocumenti\n" +
    											"\tmicrodocFile: file contenente tutti i microdocumenti" +
    											"\t[full]: flag che indica se si deve effettuare il recupero di" +
    											" interi documenti";
    
    /**
     * Costruttore della classe <code>FindResult</code> con tre parametri.
     * 
     * @param distfn <code>String</code> File contenente le distanze tra la query e i vari microdocumenti
     * @param mdfn <code>String</code> File contenente tutti i microdocumenti
     * @param full <code>int</code> Flag che indica se si deve effettuare il recupero di interi documenti
     */
	public FindResult(String distfn, String mdfn, int full) {
	    distFileName = distfn;
	    microdocFileName = mdfn;
	    fullDocuments = full;
	} // End of FindResult constructor
    
	/**
     * Costruttore della classe <code>FindResult</code> con due parametri.
     * 
     * @param distfn <code>String</code> File contenente le distanze tra la query e i vari microdocumenti
     * @param mdfn <code>String</code> File contenente tutti i microdocumenti
     */
	public FindResult(String distfn, String mdfn) {
	    this(distfn, mdfn, 0);
	} // End of FindResult constructor
	
	/**
	 * Il metodo <code>find</code> e` il metodo principale della classe, effettua la ricerca del minimo valore
	 *  di distanza all'interno del file delle distanze, ne prende l'indice (la posizione all'interno del file)
	 *  e recupera il corrispondente microdocumento.
	 */
	public String find() {
	    int distNum = 0, docNum = 0;
	    int maxDocs;
	    int index = -1;
	    double distValue;
	    double currentValue;
	    String distLine, docLine;
	    String result = null;
	    
	    if (fullDocuments == 1) {
            maxDocs = 1574;
	    } else {
	        maxDocs = Integer.MAX_VALUE;
	    } // End if-else block
        
	    if (GetDistance.getDistanceMode() == 3) {
            distValue = 0;
	    } else {
	        distValue = Double.MAX_VALUE;
	    } // End if-else block
        
	    try {
	        
	        BufferedReader dist = new BufferedReader(new FileReader(distFileName));
	        
	        while (((distLine = dist.readLine()) != null) && (distNum <= maxDocs)) {
	            currentValue = Double.parseDouble(distLine);
	            
	            if (GetDistance.getDistanceMode() == 3) {
	                if (currentValue > distValue) {
		                distValue = currentValue;
		                index = distNum;
		            }
	            } else {
	                if (currentValue < distValue) {
	                    distValue = currentValue;
	                    index = distNum;
	                }
	            } // End if-else block
	            
	            distNum++;
	            
	        } // End while block
	        
	        dist.close();
	        
	        if (verbose)
	            System.out.println(distValue + "\t" + index);
	        
	        if (fullDocuments == 1) {
	            if (index != -1) //{
	                return Integer.toString(index);
	            //} else {
	            //    return repeatQuestString;
	            //}
	        }
	        
	        BufferedReader docs = new BufferedReader(new FileReader(microdocFileName));
	        
	        while ((docLine = docs.readLine()) != null) {
	            
	            if (index == -1) {
	                result = repeatQuestString;
	                
	                break;
	            }
	            
	            if (docNum == index) {
	                if (verbose)
	                    System.err.println(docLine);
	                result = docLine;
	                
	                break;
	            } else {
	                docNum++;
	            } // End if-else block
	            
	        } // End while block
	        
	        docs.close();
	        
	    } catch (IOException ioe) {
		    System.err.println(ioe);
		    System.exit(-1);
		} // End try-catch block
        
	    // ritorna la frase a minor distanza dalla query
	    return result;
	    
	}  // End of find method
	
	/**
	 * Metodo static di <code>find</code>.
	 * 
     * @param distfn <code>String</code> File contenente le distanze tra la query e i vari micro-documenti
     * @param mdfn <code>String</code> File contenente tutti i micro-documenti
     * @param full <code>int</code> Flag che indica se si deve effettuare il recupero di interi documenti
     * 
     * @return result <code>String</code> Indice di risposta
 	 */
	public static String find(String distfn, String mdfn, int full) {
	    String result;
	    FindResult instance = new FindResult(distfn, mdfn, full);
	    result = instance.find();
	    
	    return result;
	} // End of find static method
	
	/**
	 * Metodo static di <code>find</code> con due parametri.
	 * 
     * @param distfn <code>String</code> File contenente le distanze tra la query e i vari micro-documenti
     * @param mdfn <code>String</code> File contenente tutti i micro-documenti
     * 
     * @return result <code>String</code> Micro-documento di risposta
   */
	public static String find(String distfn, String mdfn) {
	    String result;
	    FindResult instance = new FindResult(distfn, mdfn);
	    result = instance.find();
	    
	    return result;
	} // End of find static method
	
	/**
	 * Metodo principale della classe.
	 * 
	 * @param args <code>String[]</code> Parametri del metodo main
	 */
	public static void main(String[] args) {
		int argc = 0;
		int nArgs = args.length;
		String thisArg = null;
		
		String distfn = null;
		String mdfn = null;
		int full = 0;
		
		while (argc < nArgs) {
			thisArg = args[argc++].trim();
			if (thisArg != null) {
				if (distfn == null) {
					distfn = thisArg;
				} else if (mdfn == null) {
					mdfn = thisArg;
				} else if (full == 0) {
					full = Integer.parseInt(thisArg);
				}
			}
		}

		if (distfn != null && mdfn != null) {
		    find(distfn, mdfn, full);
		    System.exit(0);
		} else {
		    System.err.println("Non hai specificato tutti i parametri.\n\n" + usageString);
			System.exit(-1);
		}

	} // End of main method

} // End of FindResult class
