/*
 * Created on 23-set-2004
 */
package lsabot.vect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * La classe <code>GetDistance</code> produce come output un file contenente su ciascuna riga le distanze
 *  tra la codifica vettoriale della query immessa dall'utente e le codifiche vettoriali dei microdocumenti.<br />
 * La distanza viene misurata considerando, per ciascuna codifica vettoriale dei microdocumenti, la sua parte
 *  parallela ed ortogonale con il vettore rappresentativo della query.<br />
 * La misura della distanza e` quindi il rapporto tra la parte ortogonale e quella parallela dei due vettori,
 *  indicante quanto siano vicini: minore e` il valore di tale distanza, maggiore sara` la similarita` tra i
 *  vettori.
 * 
 * @author Salvatore La Bua
 * @version 1.0
 */
public class GetDistance {
    private String queryCodFileName;
    private String docCodFileName;
    private String distancesFileName;
    private static int DIM = 100;
    private static int distanceMode = 2; // imposta il funzionamento di default alla metrica di misura
    
    private static final boolean verbose = false;
	private static final String usageString =	"Uso: GetDistance queryCodFile docCodFile distancesFile [svdDIM]\n" +
												"\tqueryCodFile: file contenente la codifica vettoriale della query\n" +
												"\tdocCodFile: file che contiene le codifiche vettoriali dei microdocumenti\n" +
												"\tdistancesFile: file che conterra` il vettore delle distanze\n" +
												"\t[svdDIM]: dimensione del troncamento SVD (default = 100)";
	
	/**
	 * Costruttore della classe <code>GetDistance</code> con quattro parametri.
	 * 
	 * @param queryCodFile <code>String</code> File contenente la codifica vettoriale della query
	 * @param docCodFile <code>String</code> File che contiene le codifiche vettoriali dei microdocumenti
	 * @param distancesFile <code>String</code> File che conterra` il vettore delle distanze
	 * @param svdDIM <code>int</code> Dimensione del troncamento SVD
	 */
	public GetDistance(String queryCodFile, String docCodFile, String distancesFile, int svdDIM) {
	    queryCodFileName = queryCodFile;
	    docCodFileName = docCodFile;
	    distancesFileName = distancesFile;
	    DIM = svdDIM;
	} // End of GetDistance constructor
	
	/**
	 * Costruttore di <code>GetDistance</code> con tre parametri.
	 * 
	 * @param queryCodFile <code>String</code> File contenente la codifica vettoriale della query
	 * @param docCodFile <code>String</code> File che contiene le codifiche vettoriali dei microdocumenti
	 * @param distancesFile <code>String</code> File che conterra` il vettore delle distanze
	 */
	public GetDistance(String queryCodFile, String docCodFile, String distancesFile) {
	    this(queryCodFile, docCodFile, distancesFile, 100);
	} // End of GetDistance constructor

	/**
	 * Questo metodo scrive su un file i valori di somiglianza tra la query e i microdocumenti.
	 */
	public void findDistances() {
	    int i;
	    String currDocString;
	    String[] queryLine;
	    double[] queryVett = new double[DIM];
	    double[] docVett = new double[DIM];
	    
	    try {
		    
			BufferedReader queryCodFile = new BufferedReader(new FileReader(queryCodFileName));
			BufferedReader docCodFile = new BufferedReader(new FileReader(docCodFileName));
			BufferedWriter distancesFile = new BufferedWriter(new FileWriter(distancesFileName));
			
			queryLine = queryCodFile.readLine().trim().split(" ");
			
			for (i=0; i < DIM; i++)
	            queryVett[i] = Double.parseDouble(queryLine[i]);
	        
			if (verbose) {
			    for (i=0; i < DIM; i++)
			        System.out.print(queryVett[i] + " ");
			
				System.out.println("\n");
			}
			
			while ((currDocString = docCodFile.readLine()) != null) {
	            String[] currDoc = currDocString.trim().split(" ");
	            
	            for (i=0; i < DIM; i++)
	                docVett[i] = Double.parseDouble(currDoc[i]);
	            
	            double dist = calculate(queryVett, docVett);
	            
	            // System.err.println("\n\n\tDistanza: " + dist + "\n");
	        	
	            distancesFile.write(dist + "\n");
	            
	            for (i=0; i < DIM; i++)
	                docVett[i] = 0;
	            
	        } // End while block
			
			queryCodFile.close();
			docCodFile.close();
	        distancesFile.close();
	        
	    } catch (IOException ioe) {
			System.err.println(ioe);
			System.exit(-1);
		} // End try-catch block
	    
	} // End of findDistances method
	
	/**
	 * Il metodo <code>calculate</code> effettua le misure di distanza tra i due vettori passati
	 *  come parametro.
	 * 
	 * @param qVett <code>String[]</code> Vettore della query
	 * @param dVett <code>String[]</code> Vettore del documento
	 * 
	 * @return dist <code>double</code> E` la distanza tra i vettori misurata come parte ortogonale su parte parallela
	 */
	public double calculate(double[] qVett, double[] dVett) {
	    int i;
	    double[] queryVett = qVett;
	    double[] docVett = dVett;
	    
	    double dist = 0, ps;
	    double[] ps1 = new double[DIM];
		
	    // DISTANCE // 
        for (i=0; i < DIM; i++)
            ps1[i] = (queryVett[i] * docVett[i]);
        
        ps = 0;
        for (i=0; i < DIM; i++)
            ps += ps1[i];
        
        switch (distanceMode) {
        
        	case 1: {
        	    dist = Math.abs((1 - ps) / ps);
        	    break;
        	}
        	case 2: {
        	    dist = Math.abs(Math.sqrt(1 - (ps * ps)) / ps); // seno/coseno
        	    break;
        	}
        	case 3: {
        	    dist = ps; // coseno, prodotto scalare
        	    break;
        	}
        	default: {
        	    
        	}
        	
        } // End switch block
        
        if (verbose)
        	System.out.println("Prodotto scalare: " + ps);
        // DISTANCE //
        
        return dist;
	}
	
	/**
	 * Metodo static della classe <code>findDistances</code> con quattro parametri.
	 * 
	 * @param queryCodFile <code>String</code> File contenente la codifica vettoriale della query
	 * @param docCodFile <code>String</code> File che contiene le codifiche vettoriali dei microdocumenti
	 * @param distancesFile <code>String</code> File che conterra` il vettore delle distanze
	 * @param svdDIM <code>int</code> Dimensione del troncamento SVD
	 */
	public static void findDistances(String queryCodFile, String docCodFile, String distancesFile, int svdDIM) {
	    GetDistance instance = new GetDistance(queryCodFile, docCodFile, distancesFile, svdDIM);
	    instance.findDistances();
	} // End of findDistances static method
	
	/**
	 * Metodo static della classe <code>findDistances</code> con tre parametri.
	 * 
	 * @param queryCodFile <code>String</code> File contenente la codifica vettoriale della query
	 * @param docCodFile <code>String</code> File che contiene le codifiche vettoriali dei microdocumenti
	 * @param distancesFile <code>String</code> File che conterra` il vettore delle distanze
	 */
	public static void findDistances(String queryCodFile, String docCodFile, String distancesFile) {
	    GetDistance instance = new GetDistance(queryCodFile, docCodFile, distancesFile);
	    instance.findDistances();
	} // End of findDistances static method
	
	/**
	 * Il metodo statico <code>setDistanceMode</code> imposta il valore della
	 *  variabile <code>distanceMode</code>.
	 * 
	 * @param distanceValue <code>int</code> Il valore del parametro indica quale misura di distanze
	 * 						deva essere applicata
	 */
	public static void setDistanceMode(int distanceValue) {
	    distanceMode = distanceValue;
	    if (distanceMode == 1) {
	        System.err.println("Distance 1");
	    } else if (distanceMode == 2) {
	        System.err.println("Distance 2");
	    } else if (distanceMode == 3) {
	        System.err.println("Distance 3");
	    } // End if-else block
	} // End of setDistanceMode static method
	
	/**
	 * Il metodo statico <code>getDistanceMode</code> ritorna il valore della
	 *  variabile <code>distanceMode</code>.
	 */
	public static int getDistanceMode() {
	    return distanceMode;
	} // End of getDistanceMode static method
	
	/**
	 * Metodo principale della classe.
	 * 
	 * @param args <code>String[]</code> Parametri del metodo main
	 */
	public static void main(String[] args) {
		int argc = 0;
		int nArgs = args.length;
		String thisArg = null;
		
		String queryCodFile = null;
		String docCodFile = null;
		String distancesFile = null;
		int svdDIM = 100;
		
		while (argc < nArgs) {
			thisArg = args[argc++].trim();
			if (thisArg != null) {
				if (queryCodFile == null) {
				    queryCodFile = thisArg;
				} else if (docCodFile == null) {
				    docCodFile = thisArg;
				} else if (distancesFile == null) {
				    distancesFile = thisArg;
				} else if (svdDIM == 100) {
				    svdDIM = Integer.parseInt(thisArg);
				}
			}
		}

		if (queryCodFile != null && docCodFile != null && distancesFile != null) {
		    findDistances(queryCodFile, docCodFile, distancesFile, svdDIM);
		} else {
		    System.err.println("Non hai specificato tutti i parametri.\n\n" + usageString);
			System.exit(-1);
		}

	} // End of main method
	
} // End of GetDistance class
