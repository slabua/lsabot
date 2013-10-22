/*
 * Created on 22-set-2004
 */
package lsabot.vect;


/**
 * La classe <code>Vett</code> contiene alcuni metodi utili per gestire
 *  le operazioni tra vettori.
 * 
 * @author Salvatore La Bua
 * @version 1.0
 */
public class Vett {
    private int DIM;
    private double[] inVett;
    private double[] vett1, vett2;
    
    /**
     * Costruttore con un solo parametro.
     * 
     * @param vettore <code>double[]</code> Vettore su effettuare operazioni che richiedono un solo vettore
     */
    public Vett(double[] vettore) {
        inVett = vettore;
        DIM = inVett.length;
    } // End of Vett constructor
    
    /**
     * Costruttore con due parametri.
     * 
     * @param vettore1 <code>double[]</code> Primo vettore
     * @param vettore2 <code>double[]</code> Secondo vettore
     */
    public Vett(double[] vettore1, double[] vettore2) {
        vett1 = vettore1;
        vett2 = vettore1;
        DIM = vettore1.length;
    } // End of Vett constructor
    
    /**
     * Il metodo <code>norma2</code> calcola la norma2 di un vettore.
     * 
     * @return norm2 <code>double</code> E` la norma2 del vettore
     */
    public double norma2() {
        int i;
        double pow_sum = 0;
        double norm2 = 0;
        
        for (i=0; i < DIM; i++)
            pow_sum += (inVett[i] * inVett[i]);
        
        norm2 = Math.sqrt(pow_sum);
        
        return norm2;
    } // End of norma2 method
    
    /**
     * Il metodo <code>dotProduct</code> calcola il prodotto scalare tra due vettori.
     * 
     * @return dotP <code>double</code> E` il prodotto scalare tra i due vettori
     */
    public double dotProduct() {
        int i;
        double dotP = 0;
        double[] ps = new double[DIM];
        
        for (i=0; i < DIM; i++)
            ps[i] = (vett1[i] * vett2[i]);
        
        for (i=0; i < DIM; i++)
            dotP += ps[i];
        
        return dotP;
    } // End of dotProduct method
    
    /**
     * Il metodo <code>cosT</code> calcola il coseno tra due vettori.
     * 
     * @return cos <code>double</code> E` il coseno tra i due vettori
     */
    public double cosT() {
        double cos = (dotProduct(vett1, vett2) / (norma2(vett1) * norma2(vett2)));
        
        return cos;
    } // End of cosT method
    
    /**
     * Il metodo <code>sinT</code> calcola il seno tra due vettori partendo dal coseno
     *  e dalla norma di uno dei due.
     * 
     * @return sin <code>double</code> E` il seno tra i due vettori
     */
    public double sinT() {
        double cos = cosT(vett1, vett2);
        // double sin = (norma2(vett1) - cos);
        double sin = Math.sqrt(1 - (cos * cos));
        
        return sin;
    } // End of sinT method
    
    /**
     * Il metodo <code>distance</code> calcola la distanza tra due vettori intesa come rapporto
     *  tra la parte ortogonale e quella parallela, minore e` il valore della distanza,
     *  maggiore e` la similarita` dei due vettori.
     * 
     * @return dist <code>double</code> E` la distanza tra i due vettori
     */
    public double distance() {
        double dist = (sinT(vett1, vett2) / cosT(vett1, vett2));
        
        return dist;
    } // End of distance method
    
    /**
     * Il metodo <code>distanceNorm</code> calcola la distanza tra due vettori gia` normalizzati intesa come
     *  rapporto tra la parte ortogonale e quella parallela, minore e` il valore della distanza,
     *  maggiore e` la similarita` dei due vettori.<br />
     * Per calcolare tale distanza, e` necessario disporre soltanto del prodotto scalare tra i due vettori,
     *  in quanto, essendo i vettori a norma unitaria, il coseno corrisponde al prodotto scalare, mentre il
     *  seno e` facilmente ricavabile dall'espressione (1 - coseno) ovvero (1 - prodotto scalare).
     * 
     * @return dist <code>double</code> E` la distanza tra i due vettori gia` normalizzati
     */
    public double distanceNorm() {
        double dist = ((1 - dotProduct(vett1, vett2)) / dotProduct(vett1, vett2));
        
        return dist;
    } // End of distanceNorm method

    /**
     * Metodo static di <code>norma2</code>.
     * 
     * @param inputvett <code>double[]</code> Vettore su cui effettuare la norma2
     * 
     * @return norm2 <code>double</code> E` la norma2 del vettore
     */
	public static double norma2(double[] inputvett) {
	    double norm2 = 0;
	    Vett instance = new Vett(inputvett);
	    norm2 = instance.norma2();
	    
	    return norm2;
	} // End of norma2 static method
	
	/**
	 * Metodo static di <code>dotProduct</code>.
	 * 
	 * @param vettore1 <code>double[]</code> Primo vettore
	 * @param vettore2 <code>double[]</code> Secondo vettore
	 * 
	 * @return dotP <code>double</code> E` il prodotto scalare tra i due vettori
	 */
	public static double dotProduct(double[] vettore1, double[] vettore2) {
	    double dotP = 0;
	    Vett instance = new Vett(vettore1, vettore2);
	    dotP = instance.dotProduct();
	    
	    return dotP;
	} // End of dotProduct static method
	
	/**
	 * Metodo static di <code>cosT</code>.
	 * 
	 * @param vettore1 <code>double[]</code> Primo vettore
	 * @param vettore2 <code>double[]</code> Secondo vettore
	 * 
	 * @return cos <code>double</code> E` il coseno tra i due vettori
	 */
	public static double cosT(double[] vettore1, double[] vettore2) {
	    double cos = 0;
	    Vett instance = new Vett(vettore1, vettore2);
	    cos = instance.cosT();
	    
	    return cos;
	} // End of cosT static method
	
	/**
	 * Metodo static di <code>sinT</code>.
	 * 
	 * @param vettore1 <code>double[]</code> Primo vettore
	 * @param vettore2 <code>double[]</code> Secondo vettore
	 * 
	 * @return sin <code>double</code> E` il seno tra i due vettori
	 */
	public static double sinT(double[] vettore1, double[] vettore2) {
	    double sin = 0;
	    Vett instance = new Vett(vettore1, vettore2);
	    sin = instance.sinT();
	    
	    return sin;
	} // End of sinT static method

	/**
	 * Metodo static di <code>distance</code>.
	 * 
	 * @param vettore1 <code>double[]</code> Primo vettore
	 * @param vettore2 <code>double[]</code> Secondo vettore
	 * 
	 * @return dist <code>double</code> E` la distanza tra i due vettori
	 */
	public static double distance(double[] vettore1, double[] vettore2) {
	    double dist = 0;
	    Vett instance = new Vett(vettore1, vettore2);
	    dist = instance.distance();
	    
	    return dist;
	} // End of distance static method
	
	/**
	 * Metodo static di <code>distanceNorm</code>.
	 * 
	 * @param vettore1 <code>double[]</code> Primo vettore
	 * @param vettore2 <code>double[]</code> Secondo vettore
	 * 
	 * @return dist <code>double</code> E` la distanza tra i due vettori gia` normalizzati
	 */
	public static double distanceNorm(double[] vettore1, double[] vettore2) {
	    double dist = 0;
	    Vett instance = new Vett(vettore1, vettore2);
	    dist = instance.distanceNorm();
	    
	    return dist;
	} // End of distanceNorm static method
	
} // End of Vett class