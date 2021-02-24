package goat_ship;

import java.io.Serializable;
import java.util.Vector;

/**
 * @author Daniele Bianchini Matr. Unimore #153087
 * Classe rappresentante il generico Utente Cliente, l'utente Admin è definito come un final static Cliente
 */
public class Cliente implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	private Vector<Spedizione> spedizioni;
	private Vector<SpedAssicurata> spedass;

	protected final static Cliente Admin = new Cliente("admin","admin");
	
	/**
	 * Costruttore della classe Cliente, inizializza username e password e organizza le spedizioni in due vettori synchronized
	 * @param username
	 * @param password
	 */
	public Cliente(String username, String password){
		this.setUsername(username);
		this.setPassword(password);
		spedizioni=new Vector<Spedizione>();
		spedass=new Vector<SpedAssicurata>();
	}
	
	/**
	 * Metodo per l'aggiunta di una spedizione assicurata alla propria lista 
	 * @param destinazione
	 * @param peso
	 */
	public void addspedassicurata(String destinazione, String peso) {
		spedass.add(new SpedAssicurata(this.getUsername(),destinazione,peso));
	}
	
	/**
	 * Metodo per l'aggiunta di una spedizine alla propria lista 
	 * @param destinazione
	 * @param peso
	 */
	public void addsped(String destinazione, String peso) {
		spedizioni.add(new Spedizione(this.getUsername(),destinazione,peso));
	}
	
	/**
	 * Metodo getter dell'attributo username 
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Metodo setter dell'attributo username
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Metodo getter dell'attributo password 
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Metodo setter dell'attributo password
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Metodo  getter della collection spedizioni
	 * @return the spedizioni
	 */
	public Vector<Spedizione> getSpedizioni() {
		return spedizioni;
	}

	/**
	 * Metodo setter della collection di spedizioni
	 * @param spedizioni 
	 */
	public void setSpedizioni(Vector<Spedizione> spedizioni) {
		this.spedizioni = spedizioni;
	}

	/**
	 * Metodo getter della collection di spedizioni assicurate
	 * @return spedass
	 */
	public Vector<SpedAssicurata> getSpedass() {
		return spedass;
	}

	/**
	 * Metodo setter della collection di spedizioni assicurate
	 * @param spedass
	 */
	public void setSpedass(Vector<SpedAssicurata> spedass) {
		this.spedass = spedass;
	}

	
}
