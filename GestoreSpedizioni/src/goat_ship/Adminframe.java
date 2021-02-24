package goat_ship;

import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * 
 * @author Daniele Bianchini Matr. Unimore #153087
 * Classe figlia di Tableframe, istanziata quando a loggarsi è l'amministratore.
 *
 */
public class Adminframe extends Tableframe{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Costruttore della classe, richiama la classe padre con Cliente Admin
	 * @param f
	 */
	public Adminframe(Frame f) {
		super(Cliente.Admin,f);
	}
	
	
	/**
	 * Override del metodo della classe padre, carica tutte le spedizioni di tuttii clienti
	 */
	@Override 
	public Vector<Spedizione>  caricasped(){
		Vector<Spedizione> temp1= new Vector<Spedizione>();
		try {
			Cliente temp;
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Frame.fclient));
			do {
				temp=(Cliente)ois.readObject();
				for(Spedizione s: temp.getSpedizioni()) {
					if(s!=null)
						temp1.add(s);
				}
			}while(temp!=null);
				
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return temp1;
		}
		return temp1;
	}
	
	/**
	 * Override del metodo della classe padre, carica le spedizioni assicurate di tutti i clienti
	 */
	@Override
	public Vector<SpedAssicurata> caricaspedass(){
		Vector<SpedAssicurata> temp1= new Vector<SpedAssicurata>();
		try {
			Cliente temp;
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Frame.fclient));
			do {
				temp=(Cliente)ois.readObject();
				for(SpedAssicurata s:temp.getSpedass()) {
					if(s!=null)
						temp1.add(s);
				}
			}while(temp!=null);
				
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return temp1;
		}
		return temp1;
	}
	
	/**
	 * Override del metodo della classe padre che implementa l'interfaccia MouseListener. Se una spedizione normale o assicurata si trova in uno stato finale l'amministratore la puo rimuovere definitivamente con un menù a tendina che compare cliccando su tale spedizione
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		int rowheight=15;
		int nsped=(e.getY()/rowheight)-1 ;
		JPopupMenu jp = new JPopupMenu("Chiedi rimborso");
		JMenuItem rimb=new JMenuItem();
		if(e.getY()<(spedizioni.size()+1)*rowheight) {
			if(spedizioni.get(nsped).getStato().equals(Spedizione.STATO_RICEVUTA) || spedizioni.get(nsped).getStato().equals(Spedizione.STATO_FALLITA)) {
				rimb=new JMenuItem("rimuovi spedizione "+spedizioni.get(nsped).getCodice());
				jp.add(rimb);
				jp.show(e.getComponent(),e.getX(), e.getY());
			}
		}
		else {
			if(spedass.get(nsped-spedizioni.size()).getStato().equals(SpedAssicurata.STATO_RIMBORSO_EROGATO) || spedass.get(nsped-spedizioni.size()).getStato().equals(Spedizione.STATO_RICEVUTA)) {
				rimb=new JMenuItem("rimuovi spedizione "+spedass.get(nsped-spedizioni.size()).getCodice());
				jp.add(rimb);
				jp.show(e.getComponent(),e.getX(), e.getY());
			}
		}
		rimb.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource().getClass()== new JMenuItem().getClass())
					if(e.getY()<(spedizioni.size()+1)*rowheight)
					spedizioni.remove(nsped);
					else
						spedass.remove(nsped-spedizioni.size());
				tabella.fireTableDataChanged();
				
			}
			
		});
	    
	}
	
}
