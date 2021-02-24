package goat_ship;

/**
 * @author Daniele Bianchini Matr. Unimore #153087
 * Classe che implementa una spedizione assicurata
 */
public class SpedAssicurata extends Spedizione{
	
	private static final long serialVersionUID = 1L;
	
	protected final static String STATO_RIMBORSO_RICHIESTO = "RIMBORSO RICHIESTO";
	protected final static String STATO_RIMBORSO_EROGATO = "RIMBORSO EROGATO";
	private String assicurata;
	private String valore;
	
	
	/**
	 * Costruttore della classe Spedizione Assicurata, richiama il costruttore della classe padre e calcola l'eventuale valore di rimborso
	 * @param username
	 * @param destinazione
	 * @param peso
	 */
	public SpedAssicurata(String username,String destinazione, String peso) {
		super(username,destinazione,peso);
		valore=Integer.toString((Integer.parseInt(peso)*4));
		assicurata="A";
		this.setCodice();
	}
	
	/**
	 * Override del metodo setter della variabile codice
	 */
	@Override
	public void setCodice() {
		super.setCodice();
		this.codice=this.codice+assicurata;
		}
	/**
	 * Override del metodo toString per l'utilizzo nella classe Spedtable
	 */
	@Override
	public String toString() {
		if(this.getStato().equals(STATO_RIMBORSO_EROGATO))
			return "RIMBORSO EROGATO: €"+ this.valore;
		return this.getStato();
	}
	
}
