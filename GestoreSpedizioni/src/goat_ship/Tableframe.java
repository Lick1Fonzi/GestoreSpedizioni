package goat_ship;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;

/**
 * 
 * @author Daniele Bianchini Matr. Unimore #153087
 * Classe della finestra in cui l'utente puo gestire le proprie spedizioni una volta effettuato il login 
 *
 */
public class Tableframe extends JFrame implements ActionListener,MouseListener,WindowListener{

	private static final long serialVersionUID = 1L;

	private JPanel tablepanel;
	private JPanel addpanel;
	protected Spedtable tabella;
	protected JTable tab;
	private JButton newsped;
	private JButton logout;
	private JPanel log;
	private Cliente c;
	private Frame f;

	protected Vector<Spedizione> spedizioni;
	protected Vector<SpedAssicurata> spedass;

	private Corriere postino;

	/**
	 * Costruttore della classe, implementa un frame con la tabella delle spedizioni sul modello della classe Spedtable, e utilizza un thread periodico Corriere per aggiornare lo stato delle spedizioni 
	 * @param c
	 * @param f
	 */
	public Tableframe(Cliente c, Frame f) {
		super("Goat_shipping");

		this.c=c;
		this.f=f;


		tablepanel=new JPanel();
		log=new JPanel();

		spedizioni=caricasped();
		spedass=caricaspedass();

		if(!(c.getUsername().equals(Cliente.Admin.getUsername()))) {
			addpanel=new JPanel();
			newsped= new JButton("Nuova Spedizione");
			newsped.addActionListener(this);
			addpanel.add(newsped);
			this.add(addpanel,BorderLayout.CENTER);
		}

		tabella=new Spedtable(spedizioni,spedass);
		tab= new JTable(tabella);tab.setRowHeight(15);
		tab.addMouseListener(this);
		TableColumn column = null;
		for (int i = 0; i < 6; i++) {
			column = tab.getColumnModel().getColumn(i);
			if(i==0 || i==4)
				column.setPreferredWidth(80);
			else if(i==5)
				column.setPreferredWidth(200);
			else
				column.setPreferredWidth(120);
		}

		logout=new JButton("Logout");
		tablepanel.add(tab);
		log.add(logout);
		logout.addActionListener(this);
		this.add(log,BorderLayout.SOUTH);
		this.add(tablepanel,BorderLayout.NORTH);

		postino=new Corriere(this);
		Timer timer=new Timer();
		int tempoconsegna=3000;
		timer.scheduleAtFixedRate(postino, 0, tempoconsegna);

		this.pack();
		this.addWindowListener(this);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}

	/**
	 * Ridimensiona la finestra, utilizzato per blocchi di codice ActionListener anonimi
	 */
	public void repack() {
		this.pack();
	}

	/**
	 * Tramite un numero random sceglie una spedizione o spedizione assicurata a caso ed invoca il metodo cambiastato(), invocata dal Corriere
	 */
	public void tracking() {
		Random rnd = new Random(System.currentTimeMillis());
		if(rnd.nextInt()%2==0) {
			if(spedizioni.size()>0) {
				Spedizione s=spedizioni.get(rnd.nextInt(spedizioni.size()));
				cambiastato(s);
			}

		}
		else {
			if(spedass.size()>0) {
				SpedAssicurata sa = spedass.get(rnd.nextInt(spedass.size()));
				cambiastato(sa);
			}
		}

	}

	/**
	 * Fa avanzare lo stato alla spedizione passata come parametro a meno che essa non si trovi gia in uno stato finale. Una generica spedizione ha probabilita di fallire p(x)=(100-peso*3)% , se il numero random generato (con valore massimo 100) è maggiore la spedizione ha successo. Una spedizione ha peso massimo 29 kg.
	 * @param s
	 */
	public void cambiastato(Spedizione s) {
		Random rnd = new Random(System.currentTimeMillis());
		switch(s.getStato()) {
		case(Spedizione.STATO_INIZIO): s.setStato(Spedizione.STATO_TRANSITO); break;
		case(Spedizione.STATO_TRANSITO): {
			if(rnd.nextInt(100)>100- Integer.parseInt(s.getPeso_kg())*3)
				s.setStato(Spedizione.STATO_RICEVUTA);
			else
				s.setStato(Spedizione.STATO_FALLITA); };

		case(SpedAssicurata.STATO_RIMBORSO_RICHIESTO): {
			if(s instanceof SpedAssicurata && s.getStato().equals(SpedAssicurata.STATO_RIMBORSO_RICHIESTO))
				s.setStato(SpedAssicurata.STATO_RIMBORSO_EROGATO);};
		default: ;		

		}
		tabella.fireTableDataChanged();
	}

	/**
	 * Implementazione dell'interfaccia ActionListener. Se viene cliccato il pulsante di logout vengono salvate le spedizioni e rimanda alla finestra di login. Se cliccato il pulsante aggiungi spedizione, appare un frame dove si chiede di inserire peso, destinazine e tipo di spedizione, in seguito invoca il metodo aggiungi spedizione della classe cliente. Per poter aggiungere la spedizione deve essere selezionato un peso valido e una destinazione non nulla, altrimenti mostra un messaggio di errore
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(logout)) {
			salvafile();
			f.visible();
			this.dispose();
		}
		if(e.getSource().equals(newsped)) {
			JFrame spedframe = new JFrame("Nuova spedizione");
			JComboBox<String> p = new JComboBox<String>();
			p.setEditable(false);
			p.addItem("Normale");
			p.addItem("Assicurata");
			JTextField f1 = new JTextField("inserire destinazione",20);
			String[] lista = new String[30];
			lista[0]="selezionare peso in kg";
			for(int i=1;i<lista.length;i++)
				lista[i]=String.valueOf(i);
			JList<String> f2 = new JList<String>(lista);
			f2.setVisibleRowCount(4);
			JScrollPane jp=new JScrollPane(f2);
			JButton b = new JButton("Aggiungi");

			b.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if(f1.getText().length()>0 && !(f2.getSelectedValue().equals(lista[0]))) {
						if(p.getSelectedItem().equals("Normale"))
							spedizioni.add(new Spedizione(c.getUsername(),f1.getText(),f2.getSelectedValue()));
						else if(p.getSelectedItem().equals("Assicurata"))
							spedass.add(new SpedAssicurata(c.getUsername(),f1.getText(),f2.getSelectedValue()));

						tabella.fireTableDataChanged();
						tab.repaint();
						repack();
						spedframe.dispose();
					}
					else {
						JOptionPane.showMessageDialog(new JFrame(),"Dati non validi");
					}
				}
			});

			spedframe.add(f1,BorderLayout.NORTH);
			spedframe.add(jp,BorderLayout.CENTER);
			spedframe.add(p,BorderLayout.EAST);
			spedframe.add(b,BorderLayout.SOUTH);

			spedframe.pack();
			spedframe.setVisible(true);
			spedframe.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			spedframe.setLocationRelativeTo(null);
		}
	}

	/**
	 * Salva su Cliente.txt le spedizioni per il cliente autenticato , se l'utente loggato è Admin aggiorna le spedizioni per tutti i clienti
	 */
	public void salvafile() {
		ObjectInputStream ois = null;
		ObjectOutputStream oos;
		ArrayList<Cliente> clienti= new ArrayList<Cliente>();
		Vector<Spedizione> spedtemp = new Vector<Spedizione>();
		Vector<SpedAssicurata> spedtemp1 = new Vector<SpedAssicurata>();
		Cliente temp;
		try {
			ois = new ObjectInputStream(new FileInputStream(Frame.fclient));
			do {
				temp=(Cliente)ois.readObject();
				if(temp!=null)
					clienti.add(temp);
			}while(temp!=null);
			ois.close();



		}catch(ClassNotFoundException | IOException e) {
			for(Cliente c: clienti) {
				if(spedizioni!=null) {
					for(Spedizione s:spedizioni) {
						if(s.getUsername().equals(c.getUsername()))
							spedtemp.add(s);
					}
					if(this.c.getUsername().equals(c.getUsername()) || this.c.getUsername().equals(Cliente.Admin.getUsername()))
						c.setSpedizioni(spedtemp);
				}
				if(spedass!=null) {
					for(SpedAssicurata s1:spedass) {
						if(s1.getUsername().equals(c.getUsername()))
							spedtemp1.add(s1);
					}
					if(this.c.getUsername().equals(c.getUsername()) || this.c.getUsername().equals(Cliente.Admin.getUsername()))
						c.setSpedass(spedtemp1);
				}

				spedtemp=new Vector<Spedizione>();
				spedtemp1=new Vector<SpedAssicurata>();
			}
			try {
				oos=new ObjectOutputStream(new FileOutputStream(Frame.fclient));
				for(Cliente c: clienti)
					oos.writeObject(c);
				oos.flush();
				oos.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}

	}

	/**
	 * carica il vettore di spedizioni normali dell'utente loggato
	 * @return spedizioni
	 */
	public Vector<Spedizione> caricasped() {
		return c.getSpedizioni();
	}

	/**
	 * carica il vettore delle spedizioni assicurate dell'utente loggato
	 * @return spedizioni assicurate
	 */
	public Vector<SpedAssicurata> caricaspedass(){
		return c.getSpedass();
	}


	/**
	 * Implementazione dell'interfaccia MouseListener. Se una spedizione assicurata in tabella è fallita, il cliente con click del mouse sulla spedizione può chiedere il rimborso tramite un menu a tendina. Dopo aver cliccato sul menù a tendina modifica lo stato di tale spedizione in rimborso richiesto
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		int rowheight=15;
		JPopupMenu jp = new JPopupMenu("Chiedi rimborso");
		JMenuItem rimb=new JMenuItem();
		int nspedass = (e.getY()/rowheight) -(spedizioni.size()+1);


		if(e.getY()>rowheight+spedizioni.size()*rowheight && spedass.get(nspedass).getStato().equals(Spedizione.STATO_FALLITA)) {
			rimb=new JMenuItem("richiedi rimborso per spedizione "+spedass.get(nspedass).getCodice());
			jp.add(rimb);
			jp.show(e.getComponent(),e.getX(), e.getY());
		}
		rimb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource().getClass()== new JMenuItem().getClass())
					spedass.get(nspedass).setStato(SpedAssicurata.STATO_RIMBORSO_RICHIESTO);;
					tabella.fireTableDataChanged();
			}
		});

	}
	
	/**
	 * Implementazione dell'interfaccia WindowListener. Intercetta la chiusura di un Tableframe, chiede conferma di uscita dal programma, se confermata salva le spedizioni ed esce, altrimenti l'utente rimane loggato
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?","Exit",JOptionPane.YES_NO_OPTION);
		if(input==0) {
			salvafile();
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);;
			this.dispose();
		}
		else
			this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}


	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}


	//EOF
}
