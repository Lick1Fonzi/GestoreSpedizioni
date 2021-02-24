package goat_ship;
import java.util.Date;
import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * @author Daniele Bianchini Matr. Unimore #153087
 * Classe che rappresenta la singola spedizione standard
 */
public class Spedizione implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	protected final static String STATO_INIZIO = "IN PREPARAZIONE";
	protected final static String STATO_TRANSITO = "IN TRANSITO";
	protected final static String STATO_RICEVUTA = "RICEVUTA";
	protected final static String STATO_FALLITA = "FALLITA";
	protected static int ID = 0;
	
	private String username;
	protected String codice;
	private String destinazione; 
	private String peso_kg;
	private String data;
	private String stato;
	
	
	/**
	 * costruttore della classe, inizializza i campi dell'oggetto Spedizione
	 * @param username
	 * @param destinazione
	 * @param peso
	 */
	public Spedizione(String username,String destinazione, String peso) {
		this.destinazione = destinazione;
		this.peso_kg = peso;
		setData();
		setStato(STATO_INIZIO);
		setUsername(username);
	}
	
	
	/**
	 * metodo getter della variabile username
	 */
	public String getUsername() {
		return username;
	}


	/**
	 * metodo setter della variabile username 
	 */
	public void setUsername(String username) {
		this.username = username;
		this.setCodice();
	}


	/**
	 * metodo getter della variabile codice
	 */
	public String getCodice() {
		return codice;
	}


	/**
	 * metodo setter della variabile codice, utilizza lo static int progressivo ID
	 */
	public void setCodice() {
		this.codice = username.toUpperCase()+"0"+Integer.toString(++ID);
		}



	/**
	 * metodo getter della variabile destinazione
	 */
	public String getDestinazione() {
		return destinazione;
	}


	/**
	 * metodo setter della variabile destinazione
	 */
	public void setDestinazione(String destinazione) {
		this.destinazione = destinazione;
	}


	/**
	 * metodo getter della variabile peso
	 */
	public String getPeso_kg() {
		return peso_kg;
	}


	/**
	 * @metodo setter della variabile peso
	 */
	public void setPeso_kg(String peso_kg) {
		this.peso_kg = peso_kg;
	}


	/**
	 * metodo getter della variabile data
	 */
	public String getData() {
		return data;
	}


	/**
	 * metodo setter della variabile data che utilizza java.util.Date e java.text.SimpleDateFormat dello standard JDK
	 */
	public void setData() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		this.data=(String)dateFormat.format(date);
	}


	/**
	 * metodo getter della variabile stato
	 */
	public String getStato() {
		return stato;
	}


	/**
	 * metodo setter della variabile stato
	 */
	public void setStato(String stato) {
		this.stato = stato;
	}


	/**
	 * Override del metodo toString, restituisce lo stato della spedizione
	 */
	@Override
	public String toString() {
		return this.getStato();
	}
	
}
