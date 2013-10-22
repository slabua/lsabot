/*
 * Created on 1-nov-2004
 */
package lsabot.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.BufferedReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


/**
 * La classe <code>ShowDocument</code> viene richiamata da <code>LSAbot</code> per visualizzare
 *  il documento risultato dal confronto del vettore della query immessa dall'utente e tutti i
 *  vettori rappresentativi dei documenti.
 * 
 * @author Salvatore La Bua
 * @version 1.0
 * 
 * @see lsabot.gui.LSAbot
 */
public class ShowDocument extends JFrame {
    private static BufferedReader result = null;
    
    private JTextArea resultArea;
    private JScrollPane scrollPane;
    
    /**
     * Costruttore della classe <code>ShowDocument</code>.<br />
     * Viene creata l'interfaccia grafica per visualizzare il documento
     *  trovato.
     */
    public ShowDocument() {
        super("Result");
        
        Container c = getContentPane();
        c.setLayout(new BorderLayout(10, 10));
        
        resultArea = new JTextArea();
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        
        scrollPane = new JScrollPane(resultArea);
        
        c.add(scrollPane, BorderLayout.CENTER);
        
    } // End of ShowDocument contructor
    
    /**
     * Il metodo <code>showResult</code> apre in lettura il file passato come parametro dalla
     *  classe <code>LSAbot</code> e lo visualizza all'utente.
     */
    public void showResult() {
        String line;
	    
	    try {
	        
	        if (result != null) {
	            while ((line = result.readLine()) != null)
	                resultArea.append(line + "\n");
	            
	            resultArea.setCaretPosition(0);
	            
	        }
	        
        } catch (IOException ioe) {
		    System.err.println(ioe);
		    System.exit(-1);
		} // End try-catch block
	    
    } // End of showResult method
    
    /**
     * Il metodo <code>setResultFile</code> imposta la variabile che conterra` il file da leggere.
     * 
     * @param resultFile <code>BufferedReader</code> File che dovra` essere mostrato all'utente
     */
    public void setResultFile(BufferedReader resultFile) {
        result = resultFile;
    } // End of setResultFile method
    
    /**
     * Il metodo <code>run</code> imposta le dimensioni della finestra
     *  da visualizzare e richiama il metodo <code>showResult</code>.
     */
    public void run() {
      ShowDocument window = new ShowDocument();
      window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      
      window.setSize(450, 550);
      window.show();
      window.showResult();
      
    } // End of run method
    
} // End of ShowDocument class