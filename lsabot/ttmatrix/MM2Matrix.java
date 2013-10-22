/*
 * Created on 21-set-2004
 */
package lsabot.ttmatrix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * La classe <code>MM2Matrix</code> trasforma una matrice dal formato MatrixMarket
 *  in formato ascii 'normale'.<br />
 * Poiche` Matlab scrive le matrici secondo la convenzione '<i>per colonna</i>', e` necessario,
 *  in questa classe, utilizzare le trasposte delle matrici originarie salvate in formato
 *  MatrixMarket per ottenere quelle in testo normale. 
 * 
 * @author Salvatore La Bua
 * @version 1.0
 */
public class MM2Matrix {
    private String mmFileName;
    private String matrixFileName;
    
    private static final String usageString =	"Uso: MM2Matrix mmFile matrixFile\n" + 
    											"\tmmFile Matrice MarketMatrix da convertire\n" +
    											"\tmatrixFile Matrice 'normale' di output\n";
    
    /**
     * Costruttore della classe <code>MM2Matrix</code>. 
     * 
     * @param mmfn <code>String</code> Matrice MarketMatrix da convertire
     * @param mfn <code>String</code> Matrice 'normale' di output
     */
    public MM2Matrix(String mmfn, String mfn) {
        mmFileName = mmfn;
        matrixFileName = mfn;
    } // End of MM2Matrix constructor
    
    /**
     * Il metodo <code>convert</code> effettua le operazioni necessarie alla
     *  scrittura della matrice su file.
     */
    public void convert() {
        String line;
        String lineVec[];
        int rows = 0, columns = 0;
        int lineNumber = 0;
        int rCounter, cCounter;
        
		try {
		    
			BufferedReader inputFile = new BufferedReader(new FileReader(mmFileName));
			BufferedWriter outputFile = new BufferedWriter(new FileWriter(matrixFileName));
			
			while ((line = inputFile.readLine()) != null) {
				
				if (lineNumber == 1) {
				    
				    lineVec = line.trim().split(" ");
				    rows = Integer.parseInt(lineVec[0]);
				    columns = Integer.parseInt(lineVec[1]);
				    
				} else if (lineNumber >= 2) {
				    
				    for (cCounter=0; cCounter < columns; cCounter++) {
				        for (rCounter=0; rCounter < rows; rCounter++) {
				            
				            // if (rCounter < (rows - 2)) {
				                outputFile.write(line + " ");
				            /*
				        	} else {
				                outputFile.write(line);
				            }
				            */
				            
				            line = inputFile.readLine();
				            lineNumber++;
				        }
				        outputFile.write("\n");
				    } // End for block
				    
				} // End if-else block
				
				lineNumber++;
				
			} // End while block
			
			inputFile.close();
			outputFile.close();
			System.out.println("File " + matrixFileName + " saved.");
			
		} catch (IOException ioe) {
			System.err.println(ioe);
			System.exit(-1);
		} // End try-catch block
		
    } // End of convert method
    
    /**
     * Metodo static di <code>convert</code>.
     * 
     * @param mmfn <code>String</code> Matrice MarketMatrix da convertire
     * @param mfn <code>String</code> Matrice 'normale' di output
     */
	public static void convert(String mmfn, String mfn) {
	    MM2Matrix instance = new MM2Matrix(mmfn, mfn);
		instance.convert();
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
		
		String mmfn = null;
		String mfn = null;
		
		while (argc < nArgs) {
			thisArg = args[argc++].trim();
			if (thisArg != null) {
				if (mmfn == null) {
					mmfn = thisArg;
				} else if (mfn == null) {
					mfn = thisArg;
				}
			}
		}
		
		if (mmfn != null && mfn != null) {
			convert(mmfn, mfn);
		} else {
			System.err.println("Non hai specificato tutti i parametri.\n\n" + usageString);
			System.exit(-1);
		}
		
	} // End of main method

} // End of MM2Matrix class
