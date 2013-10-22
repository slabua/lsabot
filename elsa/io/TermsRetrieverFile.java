package elsa.io;


import java.io.*;

import elsa.util.TermsIdFreq;
import elsa.util.TermsNamesId;

/**
 * @author I. Alessandro Motisi
 * @author Giorgio Vassallo
 */

/**
 * La classe TermsRetrieverFile e' usata per ottenere le informazioni riguardanti le occorrenza dei termini.<br>
 * I valori sono letti dal file specificato nel costruttore. Il quale deve rispettare il seguente formato:<br>
 *   
 * frequenza_del_temine_1 \t termine_1 \n<br>
 * frequenza_del_temine_2 \t termine_2 \n<br>
 * ...<br>
 * frequenza_del_temine_N \t termine_N \n<br>
 *<br>
 * Gli id sono assegnati in ordine di lettura, il primo termine avra' quindi id pari a zero, mentre l'N-esimo termine avra' id pari ad N-1.<br> 
 * 
 */

public class TermsRetrieverFile implements TermsRetriever {
	private String fileName;
	
	/**
	 * Costruisce un nuovo <code>TermsRetrieverFile</code>.<br>
	 * 
	 * @param fName Nome del file dal quale sono letti i dati riguardanti i termini
	 */
	public TermsRetrieverFile(String fName) {
		fileName = fName;
	}

	/* (non-Javadoc)
	 * @see elsa.io.getTermsNamesId#getTermsNamesId()
	 */
	public TermsNamesId getTermsNamesId() {
		TermsNamesId terms = new TermsNamesId();
		String line;
		String elements[];
		int counter = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));

			while((line = reader.readLine()) != null) {
				elements = line.trim().split("\\t");
				terms.insert(elements[elements.length-1], counter);
				counter++;
			}
		} catch( IOException ioe) {
			System.err.println(ioe);
			System.exit(-1);
		}
		return terms;
	}

	/* (non-Javadoc)
	 * @see elsa.io.TermsRetriever#getTermsIdFreq()
	 */
	public TermsIdFreq getTermsIdFreq() {
		TermsIdFreq terms = new TermsIdFreq();
		String line;
		String elements[];
		int counter = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));

			while((line = reader.readLine()) != null) {
				elements = line.trim().split("\\t");
				terms.insert(counter, Integer.parseInt(elements[0]));
				counter++;
			}
		} catch( IOException ioe) {
			System.err.println(ioe);
			System.exit(-1);
		}
		return terms;

	}
	
	/**
	 * Ritorna un oggetto della classe TermsNamesId, il quale consente di associare i nomi agli id dei termini (e viceversa).<br>
	 * 
	 * @param fName Nome del file dal quale sono letti i dati riguardanti i termini
	 * @return Un oggetto della classe TermsNamesId, il quale consente di associare i nomi agli id dei termini (e viceversa)
	 */
	public static TermsNamesId getTermsNamesId(String fName) {
		TermsRetrieverFile trf = new TermsRetrieverFile(fName);
		return trf.getTermsNamesId();
	}
	
	/**
	 * Ritorna un oggetto della classe TermsIdFreq, il quale consente di associare gli id alle frequenza di occorrenza.<br>
	 * 
	 * @param fName Nome del file dal quale sono letti i dati riguardanti i termini
	 * @return Un oggetto della classe TermsIdFreq, il quale consente di associare gli id alle frequenza di occorrenza
	 */
	public static TermsIdFreq getTermsIdFreq(String fName) {
		TermsRetrieverFile trf = new TermsRetrieverFile(fName);
		return trf.getTermsIdFreq();
	}


}
