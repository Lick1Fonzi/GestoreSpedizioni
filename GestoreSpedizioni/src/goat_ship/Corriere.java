package goat_ship;

import java.util.TimerTask;

/**
 * 
 * @author Daniele Bianchini Matr. Unimore #153087 
 * Classe che implementa un thread periodico. Tale classe è utilizzata per modificare casualmente lo stato delle spedizioni
 *
 */
public class Corriere extends TimerTask{

	private Tableframe f;

	/**
	 * Costruttore
	 * @param f frame che lo invoca
	 */
	public Corriere(Tableframe f) {
		this.f=f;
	}

	/**
	 * Override del metodo della classe TimerTask, piu generalmente della classe Thread che specifica il comportamento del Thread. Esso richiama il metodo tracking() dell'oggetto tableframe passato come parametro. Le spedizioni su cui tracking opera sono synchronized per la definzione thread-safe di Vector
	 */
	@Override
	public void run() {
		f.tracking();

	}

}
