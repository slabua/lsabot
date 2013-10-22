package elsa.util;

import java.util.*;

/**
 * @author I. Alessandro Motisi
 * @author Giorgio Vassallo
 */

/**
 * La classe TermsIdFreq e' usata per associare gli id dei termini alle frequenze di occorrenza.<br>
 */

public class TermsIdFreq extends Hashtable {

	/**
	 * Costruisce un nuovo TermsIdFreq.<br>
	 * 
	 */
	public TermsIdFreq() {
		super();
	}

	/**
	 * Associa l'id passato alla frequenza passata.<br>
	 * 
	 * @param id Id del termine
	 * @param freq Frequenza del termine con l'id passato
	 */
	public void insert(Integer id, Integer freq) {
		put(id, freq);
	}
	
	/**
	 * Associa l'id passato alla frequenza passata.<br>
	 * 
	 * @param id Id del termine
	 * @param freq Frequenza del termine con l'id passato
	 */
	public void insert(int id, int freq) {
		insert(new Integer(id), new Integer(freq));
	}
	
	/**
	 * Ritorna la frequenza relativa all'id passato.<br>
	 * 
	 * @param id Id del termine
	 * @return La frequenza del termine con l'id passato (se il termine non e' presente ritorna 0)
	 */
	public int getFreq(Integer id) {
		Integer freq = (Integer) get(id);
		if(freq==null){
			return 0;
		}
		return  freq.intValue();
	}

	/**
	 * Ritorna la frequenza relativa all'id passato.<br>
	 * 
	 * @param id Id del termine
	 * @return La frequenza del termine con l'id passato (se il termine non e' presente ritorna 0)
	 */
	public int getFreq(int id) {
		return  getFreq(new Integer(id));
	}
	
	
	public Enumeration ids() {
		return keys();
	}

}
