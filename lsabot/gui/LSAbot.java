/*
 * Created on 26-set-2004
 */
package lsabot.gui;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import lsabot.vect.CodVettRetriever;
import lsabot.vect.FindResult;
import lsabot.vect.GetDistance;
import lsabot.vect.QueryVettRetriever;

/**
 * La classe <code>LSAbot</code> rappresenta l'interfaccia principale
 *  tra l'utente ed il bot stesso.<br />
 * <code>LSAbot</code> estende <code>JFrame</code>.
 * 
 * @author Salvatore La Bua
 * @version 1.0
 * 
 * @see javax.swing.JFrame
 * @see lsabot.gui.ShowDocument
 * @see lsabot.vect.CodVettRetriever#findVettCod
 * @see lsabot.vect.FindResult
 * @see lsabot.vect.GetDistance#findDistances
 * @see lsabot.vect.QueryVettRetriever#getQUS
 */
public class LSAbot extends JFrame {
    private static boolean firstRun = true;
    private static int chosenItem = 1; // inizializza il funzionamento alla scelta
    private static String[] choices = {"Ask to LSA-Bot", "Improve knowledge", "Search EuroParl Documents"};
    private static String[] distances = {"Distance 1", "Ortog/Parall", "Dot Product"};
    
    private Icon icon;
    private JLabel iconLabel, lsaLabel, queryLabel, resultLabel;
    private JTextField queryField;
    private JTextArea resultArea;
    private JScrollPane scrollPane;
    private JButton searchButton, cancelButton;
    private JSlider scaleValue;
	private JComboBox chooserBox;
    
    private JPanel topPanel;
    private JPanel resultPanel;
    private JPanel chooserPanel;
    private JPanel queryPanel;
    private JPanel qBoxPanel;
    
    private JPanel buttonPanel;
    
    /**
     * Il costruttore della classe <code>LSAbot</code> chiama il costruttore della
     *  classe <code>JFrame</code> per impostare il titolo della finestra.<br />
     * Vengono inoltre create e posizionate le componenti GUI che faranno parte
     *  dell'interfaccia grafica dell'applicazione.
     */
    public LSAbot() {
        super("LSA-bot");
        
        ActionEventHandler handler = new ActionEventHandler();
        
        Container c = getContentPane();
        c.setLayout(new BorderLayout(5, 10));
        
        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(2, 10));
        
        resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout(10, 10));
        
        chooserPanel = new JPanel();
        chooserPanel.setLayout(new BorderLayout(10, 10));
        
        queryPanel = new JPanel();
        queryPanel.setLayout(new BorderLayout(10, 10));
        
        qBoxPanel = new JPanel();
        qBoxPanel.setLayout(new BorderLayout(10, 10));
        
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        
        //icon = new ImageIcon("euflag.jpg");
        icon = new ImageIcon("lsabot.png");
        iconLabel = new JLabel("", icon, SwingConstants.CENTER);
        //iconLabel.setBorder(new BevelBorder(1)); // MatteBorder(1,1,1,1,Color.DARK_GRAY));
        
        queryLabel = new JLabel("Query:");
        queryField = new JTextField(); // 30
        queryField.addActionListener(handler);
        
        resultLabel = new JLabel(" "); // Result:
        resultArea = new JTextArea(); // 10, 30
        resultArea.setText("\n Welcome to LSA-Bot." +
                			"\n\n   Please insert your question in the query field below." +
                			"\n   Select interaction's modes from the Options' menu."/* +
                			"\n   Use the slider to change the sensibility."*/);
        resultArea.setEditable(false);
        resultArea.setEnabled(false);
        scrollPane = new JScrollPane(resultArea);
        
        chooserBox = new JComboBox(choices);
        chooserBox.addActionListener(handler);
        
        searchButton = new JButton("Ask!");
        searchButton.addActionListener(handler);
        cancelButton = new JButton("Exit");
        cancelButton.addActionListener(handler);
        
        scaleValue = new JSlider(JSlider.VERTICAL, 1, 1001, 100);
        scaleValue.setMajorTickSpacing(100); // 25
        scaleValue.setMinorTickSpacing(25); // 5
        scaleValue.setPaintTicks(true);
		// scaleValue.setPaintLabels(true);
        scaleValue.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
        
        buttonPanel.add(searchButton);
        buttonPanel.add(cancelButton);
        
        queryPanel.add(queryLabel, BorderLayout.WEST);
        queryPanel.add(queryField, BorderLayout.CENTER);
        
        resultPanel.add(scrollPane, BorderLayout.CENTER);
        
        chooserPanel.add(chooserBox, BorderLayout.CENTER);
        
        // topPanel.add(chooserPanel, BorderLayout.NORTH);
        // topPanel.add(resultLabel, BorderLayout.NORTH);
        // topPanel.add(scaleValue, BorderLayout.EAST);
        topPanel.add(iconLabel, BorderLayout.WEST);
        topPanel.add(resultPanel, BorderLayout.CENTER);
        
        qBoxPanel.add(topPanel, BorderLayout.CENTER);
        qBoxPanel.add(queryPanel, BorderLayout.SOUTH);
        
        c.add(resultLabel, BorderLayout.NORTH);
        // c.add(chooserPanel, BorderLayout.NORTH);
        c.add(qBoxPanel, BorderLayout.CENTER);
        // c.add(scaleValue, BorderLayout.EAST);
        c.add(buttonPanel, BorderLayout.SOUTH);
        
        
        
        // makes the menu bar
		JMenuBar bar = new JMenuBar();
		setJMenuBar(bar);
		
		// makes the file menu
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic('x');
		exitItem.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			}
		);
		fileMenu.add(exitItem);
		
		bar.add(fileMenu);
		
		
		// makes the options menu
		JMenu optionsMenu = new JMenu("Options");
		optionsMenu.setMnemonic('O');
		
		ButtonGroup functionsGroup = new ButtonGroup();
		ButtonGroup distanceGroup = new ButtonGroup();
		
		JMenu chatModeMenu = new JMenu("Interaction Mode");
		chatModeMenu.setMnemonic('I');
		optionsMenu.add(chatModeMenu);
		
		JMenu distanceModeMenu = new JMenu("Distance Type");
		distanceModeMenu.setMnemonic('D');
		optionsMenu.add(distanceModeMenu);
		
		JMenuItem chatMenuItem = new JRadioButtonMenuItem(choices[0]);
		functionsGroup.add(chatMenuItem);
		chatMenuItem.setMnemonic('L');
		chatMenuItem.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				    setItem(1);
				    System.err.println("L'id della funzione selezionata e` " + chosenItem);
				}
			}
		);
		chatMenuItem.setSelected(true);
		chatModeMenu.add(chatMenuItem);
		
		JMenuItem klMenuItem = new JRadioButtonMenuItem(choices[1]);
		functionsGroup.add(klMenuItem);
		klMenuItem.setMnemonic('k');
		klMenuItem.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				    setItem(2);
				    System.err.println("L'id della funzione selezionata e` " + chosenItem);
				}
			}
		);
		chatModeMenu.add(klMenuItem);
		
		JMenuItem epMenuItem = new JRadioButtonMenuItem(choices[2]);
		functionsGroup.add(epMenuItem);
		epMenuItem.setMnemonic('E');
		epMenuItem.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				    setItem(3);
				    System.err.println("L'id della funzione selezionata e` " + chosenItem);
				}
			}
		);
		chatModeMenu.add(epMenuItem);
		
		JMenuItem distance1 = new JRadioButtonMenuItem(distances[0]);
		distanceGroup.add(distance1);
		distance1.setMnemonic('1');
		distance1.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					GetDistance.setDistanceMode(1);
				}
			}
		);
		// distance1.setSelected(true);
		// distanceModeMenu.add(distance1);
		
		JMenuItem distance2 = new JRadioButtonMenuItem(distances[1]);
		distanceGroup.add(distance2);
		distance2.setMnemonic('2');
		distance2.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				    GetDistance.setDistanceMode(2);
				}
			}
		);
		distance2.setSelected(true);
		distanceModeMenu.add(distance2);
		
		JMenuItem distance3 = new JRadioButtonMenuItem(distances[2]);
		distanceGroup.add(distance3);
		distance3.setMnemonic('3');
		distance3.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				    GetDistance.setDistanceMode(3);
				}
			}
		);
		distanceModeMenu.add(distance3);
		
		bar.add(optionsMenu);
		
		
		// makes the about menu
		JMenu helpMenu = new JMenu("About");
		helpMenu.setMnemonic('A');
		JMenuItem aboutItem = new JMenuItem("Latent Semantic ChatBot Info");
		aboutItem.setMnemonic('L');
		aboutItem.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				    JOptionPane.showMessageDialog(LSAbot.this,
				            						"Latent Semantic ChatBot."+
				            						"\n\nAuthor:\n" +
				            						"\n     Salvatore  La Bua" +
				            						"\n  <slabua@gmail.com>",
				            						"Credits",
				            						JOptionPane.INFORMATION_MESSAGE);
				}
			}
		);
		
		helpMenu.add(aboutItem);
		bar.add(helpMenu);
		
		
		
    } // End of LSAbot contructor
    
    /**
     * Il metodo <code>search</code> richiama l'intera procedura di acquisizione query,
     *  codifica della query e misura della distanza tra la query e i micro-documenti.<br />
     * Viene fornito in output il micro-documento piu` vicino alla query.
     */
    public void search() {
        String query = queryField.getText();
        
        if (firstRun) {
            resultArea.setText("\n");
            resultArea.setEnabled(true);
            firstRun = false;
        }
        
        try {
            
            BufferedWriter outputFile = new BufferedWriter(new FileWriter("data/query.txt"));
            
            outputFile.write(query + "\n");
            outputFile.close();
            
            CodVettRetriever.setScale(scaleValue.getValue()); // + 1);
            
            switch (chosenItem) {
            	
            	case 1: { // "Ask to LSA-Bot"
            	    
            	    CodVettRetriever.findVettCod("data/query.txt", "data/AIMLterms.txt", "data/AIML_U.txt", "data/query_cod.txt");
            	    GetDistance.findDistances("data/query_cod.txt", "data/AIMLcodif.txt", "data/dist.txt");
            	    
            	    resultArea.append("=>  " + FindResult.find("data/dist.txt", "data/AIMLfull.txt") + "\n\n");
            	    
            	    break;
            	}
            	case 2: { // "Improve knowledge"
            	    
            	    resultArea.setText("");
            	    
            	    CodVettRetriever.setAppendMode(true);
            	    CodVettRetriever.findVettCod("data/query.txt", "data/terms_tot.txt", "data/Uterm-doc.txt", "data/AIMLcodif.txt");
            	    
            	    BufferedWriter sentences = new BufferedWriter(new FileWriter("data/AIMLfull.txt", true));
            	    sentences.write(query + "\n");
            	    sentences.close();
            	    
            	    CodVettRetriever.setAppendMode(false);
            	    
            	    JOptionPane.showMessageDialog(LSAbot.this,
            	            						"Your statement was added succesfully" +
            	            						"\ninto the knowledge base",
            	            						"Knowledge status...",
            	            						JOptionPane.INFORMATION_MESSAGE);
            	    
            	    break;
            	}
            	case 3: { // "Search EuroParl Documents"
            	    
            	    resultArea.setText("Searching... please wait...");
            	    
            	    QueryVettRetriever.getQUS("data/query.txt", "data/terms_tot.txt", "data/traspUInvS.txt", "data/query_cod_doc.txt");
            	    
            	    resultArea.setText("Calculating distances...");
            	    GetDistance.findDistances("data/query_cod_doc.txt", "data/Vt.txt", "data/dist.txt");
            	    
            	    int full = 1;
            	    String resultFileNumber = FindResult.find("data/dist.txt", "", full);
            	    
            	    if (Integer.parseInt(resultFileNumber) > -1) {
            	        ShowDocument document = new ShowDocument();
                        BufferedReader resultFile = new BufferedReader(new FileReader("data/numbered/" + resultFileNumber + ".num"));
            	        document.setResultFile(resultFile);
            	        document.run();
            	        resultArea.setText("Document found.");
            	    } else {
            	        resultArea.setText("Document not found.");
            	    }
                    
            	    break;
            	}
            	default: {
            	    
            	}
            	
            } // End switch block
            
            queryField.setText("");
    	    
        } catch (IOException ioe) {
		    System.err.println(ioe);
		    System.exit(-1);
        } // End try-catch block
        
    } // End of search method
    
    /**
     * Il metodo statico <code>setItem</code> imposta il valore della
	 *  variabile <code>chooserItem</code>.
     * 
     * @param item <code>int</code> Indice dell'elemento selezionato (il primo elemento ha indice pari a 0)
     */
    public static void setItem(int item) {
        chosenItem = item;
    } // End of setItem method
    
    /**
     * Il metodo <code>main</code> imposta le dimensioni della finestra
     *  da visualizzare.
     *
     * @param args <code>String[]</code> Parametri del metodo main
     */
    public static void main(String[] args) {
      LSAbot window = new LSAbot();

      window.setSize(500, 315); // 340
      // window.setResizable(false);
      window.show();
      
      window.addWindowListener(
              new WindowAdapter() {
                  public void windowClosing(WindowEvent e) {
                      System.exit(0);
                  }
              }
      );

    } // End of main method

    /**
     * La classe privata <code>ActionEventHandler</code> gestisce gli eventi per l'applicazione.
     * 
     * @author Salvatore La Bua
     * @version 1.0
     */
    private class ActionEventHandler implements ActionListener {
      public void actionPerformed(ActionEvent e) {
        if ((e.getSource() == searchButton) || (e.getSource() == queryField)) {
            search();
        } else if (e.getSource() == cancelButton) {
            JOptionPane.showMessageDialog(LSAbot.this,
                    						"Thanks for enjoying with LSA-Bot.",
                    						"Exit",
                    						JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        } else if (e.getSource() == chooserBox) {
            resultArea.setText("\n Now you can fill the query field with your question.");
            for (int i=0; i < choices.length; i++) {
                if (chooserBox.getSelectedItem().equals(choices[i])) {
                    setItem(i + 1);
                    System.err.println("L'id della funzione selezionata e` " + chosenItem);
                    // JOptionPane.showMessageDialog(null, "L'id della funzione selezionata e` " + chooserItem);
                }
            } // End for block
            
        } // End if-else block
        
      	} // End of actionPerformed method
    } // End of ActionEventHandler private class
    
} // End of LSAbot class