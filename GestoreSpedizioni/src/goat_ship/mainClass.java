package goat_ship;

import javax.swing.JFrame;

/**
 * 
 * @author Daniele Bianchini Matr. Unimore #153087
 * Classe del Main
 *
 */
public class mainClass {

	/**
	 * Punto di ingresso, funzione main
	 * @param args
	 */
	public static void main(String[] args) {
		Frame frame = new Frame("Login");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.pack();
	}
	
}
