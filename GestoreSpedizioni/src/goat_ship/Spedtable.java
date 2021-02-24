package goat_ship;


import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 * Classe modello della tabella per le spedizioni
 * @author Daniele Bianchini Matr. Unimore #153087 
 *
 */
public class Spedtable extends AbstractTableModel{

	private static final long serialVersionUID = 1L;

	private Vector<Spedizione> spedizioni;
	private Vector<SpedAssicurata> spedass;
	private String str[];

	/**
	 * riceve i vettori di spedizione e inizializza il modello e la prima riga della tabella con le etichette dei vari campi
	 * @param s
	 * @param sa
	 */
	public Spedtable(Vector<Spedizione> s,Vector<SpedAssicurata> sa) {
		spedizioni=s;
		spedass=sa;
		str=new String[6];
		str[0]="Username";
		str[1]="Destinazione";
		str[2]="Data";
		str[3]="Codice";
		str[4]="Peso";
		str[5]="STATO";
	}


	/**
	 * restituisce il numero di righe
	 */
	@Override
	public int getRowCount() {
		return spedizioni.size()+spedass.size()+1;
	}

	/**
	 * restituisce il numero di colonne, ossia i campi notevoli della spedizione 
	 */
	@Override
	public int getColumnCount() {
		return 6;
	}

	/**
	 * Nella tabella compaiono: nella prima riga i nomi delle colonne, di seguito tutte le spedizioni normali, infine le spedizioni assicurate
	 */
	@Override
	public String getValueAt(int rowIndex, int columnIndex) {

		if(rowIndex==0) 
			return str[columnIndex];

		else if(rowIndex>spedizioni.size()) {
			SpedAssicurata temp1= spedass.get(rowIndex-spedizioni.size()-1);
			switch(columnIndex) {
			case(0): return temp1.getUsername();
			case(1): return temp1.getDestinazione();
			case(2): return temp1.getData();
			case(3): return temp1.getCodice();
			case(4): return temp1.getPeso_kg()+" kg";
			case(5): return temp1.toString();
			default: return "";
			}
		}


		else{Spedizione temp = spedizioni.get(rowIndex-1);
		switch(columnIndex) {
		case(0): return temp.getUsername();
		case(1): return temp.getDestinazione();
		case(2): return temp.getData();
		case(3): return temp.getCodice();
		case(4): return temp.getPeso_kg()+" kg";
		case(5): return temp.toString();
		default: return "";
		}
		}

	}


}
