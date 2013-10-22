package elsa.util;

import java.util.*;

/**
 * @author I. Alessandro Motisi
 * @author Giorgio Vassallo
 */

/**
 * La classe TermsNamesId e' usata per associare i nomi dei termini ai loro rispettivi id (e viceversa).<br>
 */

public class TermsNamesId {
	private Hashtable names2id;
	private Hashtable id2names;

	/**
	 * Costruisce un nuovo TermsNamesId.<br>
	 * 
	 */
	public TermsNamesId() {
		names2id = new Hashtable();
		id2names = new Hashtable();
	}
	
	/**
	 * Associa il nome all'id passato (e viceversa).<br>
	 * 
	 * @param name Il nome del termine al quale si vuole associare un id  
	 * @param id l'id al quale si vuole associare il nome passato
	 */
	public void insert(String name, Integer id) {
		names2id.put(name, id);
		id2names.put(id, name);
	}

	/**
	 * Associa il nome all'id passato (e viceversa).<br>
	 * 
	 * @param name Il nome del termine al quale si vuole associare un id  
	 * @param id L'id al quale si vuole associare il nome passato
	 */
	public void insert(String name, int id) {
		insert(name, new Integer(id));
	}
	
	/**
	 * Ritorna il nome del termine avente l'id passato.<br>
	 * 
	 * @param id L'id del termine del quale si desidera ottenere il nome 
	 * @return Il nome del termine avente l'id passato
	 */
	public String getName(Integer id) {
		return (String) id2names.get(id);
	}

	/**
	 * Ritorna il nome del termine avente l'id passato.<br>
	 * 
	 * @param id L'id del termine del quale si desidera ottenere il nome 
	 * @return Il nome del termine avente l'id passato
	 */
	public String getName(int id) {
		return getName(new Integer(id));
	}
	
	/**
	 * Ritorna l'id del termine avente il nome passato.<br>
	 * 
	 * @param name Il nome del termine del quale si desidera ottenere l'id 
	 * @return L'id del termine avente il nome passato, oppure -1 nel caso il nome passato non sia presente
	 */
	public int getId(String name) {
		Integer id = (Integer) names2id.get(name); 
		if(id!=null) {
			return id.intValue();
		}
		return -1;
	}

	
	/**
	 * Ritorna una <code>Enumeration</code> degli id dei termini.<br>
	 * 
	 * @return Una <code>Enumeration</code> degli id dei termini
	 */
	public Enumeration getIds() {
		return id2names.keys();
	}

	/**
	 * Ritorna una <code>Enumeration</code> dei nomi dei termini.<br>
	 * 
	 * @return Una <code>Enumeration</code> dei nomi dei termini
	 */
	public Enumeration getNames() {
			return names2id.keys();
	}
	
	/**
	 * Ritorna il numero di termini archiviati.<br>
	 * 
	 * @return Il numero di termini archiviati
	 */
	public int size() {
		return names2id.size();
	}
}
