package elsa.io;

import elsa.util.TermsIdFreq;
import elsa.util.TermsNamesId;

/**
 * @author I. Alessandro Motisi
 * @author Giorgio Vassallo
 */

/**
 * L'interfaccia TermsRetriever fornisce un livello di astrazione per la ricezione delle informazioni sui termini.<br>
 */

public interface TermsRetriever {

	/**
	 * Ritorna un oggetto della classe TermsNamesId, il quale consente di associare i nomi agli id dei termini (e viceversa).<br> 
	 * @return Un oggetto della classe TermsNamesId, il quale consente di associare i nomi agli id dei termini (e viceversa)
	 */
	public TermsNamesId getTermsNamesId();

	/**
	 * Ritorna un oggetto della classe TermsIdFreq, il quale consente di associare gli id alle frequenza di occorrenza.<br> 
	 * @return Un oggetto della classe TermsIdFreq, il quale consente di associare gli id alle frequenza di occorrenza
	 */
	public TermsIdFreq getTermsIdFreq();
	
}
