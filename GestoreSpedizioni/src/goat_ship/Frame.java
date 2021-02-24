package goat_ship;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import javax.swing.*;

/**
 * @author Daniele Bianchini Matr. Unimore #153087
 * Classe del frame che gestisce login per l'amministratore/cliente e la registrazione di un nuovo cliente
 */
public class Frame extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;

	private static File f = new File("");
	protected final static File fclient = new File(f.getAbsolutePath()+File.separator+"Clienti.txt");


	private JPanel userpanel;
	private JTextField userfield;

	private JPanel pswpanel;
	private JTextField pswfield;

	private JPanel loginpanel;
	private JCheckBox admin;
	private JCheckBox cliente;

	private JButton loginbut;
	private JButton signinbut;

	private Tableframe tframe;

	/**
	 * costruttore della classe, richiama il costruttore della classe padre, invoca il metodo filemanager() e inizializza il frame iniziale di login
	 * @param s
	 */
	public Frame(String s) {
		super(s);

		filemanager();

		userpanel=new JPanel();
		userfield=new JTextField("",20);
		userpanel.add(new JLabel("Username"));
		userpanel.add(userfield);

		pswpanel=new JPanel();
		pswfield=new JTextField("",20);
		pswpanel.add(new JLabel("Password"));
		pswpanel.add(pswfield);

		loginpanel=new JPanel();
		admin=new JCheckBox("Amministratore");
		cliente=new JCheckBox("Cliente");
		ButtonGroup bgroup = new ButtonGroup();
		bgroup.add(admin);
		bgroup.add(cliente);
		cliente.setSelected(true);
		loginbut=new JButton("Login");
		signinbut=new JButton("Sign in");
		loginbut.addActionListener(this);
		signinbut.addActionListener(this);

		loginpanel.add(admin);
		loginpanel.add(cliente);
		loginpanel.add(loginbut);
		loginpanel.add(signinbut);

		this.add(userpanel,BorderLayout.NORTH);
		this.add(pswpanel,BorderLayout.CENTER);
		this.add(loginpanel,BorderLayout.SOUTH);


	}

	/**
	 * Implementazione del metodo fornito dall'interfaccia ActionListener che gestisce gli eventi dei bottoni di login e signin invcando le relative funzioni
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(loginbut))
			login();
		else if(e.getSource().equals(signinbut))
			signin();
	}



	/**
	 * Controlla se il file Cliente.txt che contiene gli utenti registrati è esistente, altrimenti lo crea
	 */
	public void filemanager() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fclient));
			ois.close();
		}catch(IOException e) {
			ObjectOutputStream oos;
			try {
				oos = new ObjectOutputStream(new FileOutputStream(fclient));
				oos.writeObject(Cliente.Admin);
				oos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}



	/**
	 * rende visibile il frame e pulisce i campi di login
	 */
	public void visible() {
		this.setVisible(true);
		userfield.setText("");
		pswfield.setText("");
	}



	/**
	 * 
	 * @param s crea un messaggio di avviso specificato dal parametro
	 */
	public void aggiungiavviso(String s) {
		JFrame f = new JFrame();
		JOptionPane.showMessageDialog(f,s);
	}


	/**
	 * metodo invocato alla pressione del JButton di login, controlla le credenziali di accesso. Se corrispondono ad un utente gia registrato rende invisibile questo frame e crea un istanza della classe Tableframe oppure Adminframe a seconda del login scelto passandogli come parametro il Cliente che si è autenticato. Se credenziali errate restituisce un avviso di errore richiamndo il metodo aggiungiavviso()
	 */
	public void login() {

		if(admin.isSelected()) {
			if(userfield.getText().equals(Cliente.Admin.getUsername()) && pswfield.getText().equals(Cliente.Admin.getPassword())) {
				tframe= new Adminframe(this);
				this.setVisible(false);}
			else
				aggiungiavviso("Username/password Amministratore errati");
		}
		else {
			try {
				Cliente temp;
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fclient));

				do {
					temp=(Cliente)ois.readObject();
					if(temp.getUsername().equals(userfield.getText()) && temp.getPassword().equals(pswfield.getText()) && !(temp.getUsername().equals(Cliente.Admin.getUsername()))) {
						tframe=new Tableframe(temp,this);
						this.setVisible(false);
						break;
					}
				}while(temp!=null);

				ois.close();

			}catch(IOException | ClassNotFoundException e) {
				e.printStackTrace();
				aggiungiavviso("Username/password Cliente errati");
			}
		}
	}



	/**
	 * metodo invocato alla pressione del JButton di sign in. Apre un JFrame che permette ad un nuovo cliente di registrarsi. Se l'username scelto è gia esistente restituisce un messaggio di errore invocando aggiungiavviso(), altrimenti restituisce un avviso di riuscita registrazione
	 */
	public void signin() {
		JFrame signframe = new JFrame("Registrazione nuovo cliente");
		this.setVisible(false);

		JTextField registra_user = new JTextField("Inserisci username",20);
		JTextField registra_psw = new JTextField("Inserisci psw",20);
		JButton registrati = new JButton("Registrati");
		JButton goback = new JButton("Torna indietro");
		String avviso1 = new String("username/password non validi");
		JLabel avviso2=new JLabel("Un altro utente utilizza questo username");

		JPanel signpanel1 = new JPanel();
		JPanel signpanel2 = new JPanel();
		signpanel1.add(registra_user,BorderLayout.NORTH);
		signpanel1.add(registra_psw,BorderLayout.CENTER);
		signpanel2.add(registrati,BorderLayout.CENTER);
		signpanel2.add(goback,BorderLayout.CENTER);
		signframe.add(signpanel1,BorderLayout.NORTH);
		signframe.add(signpanel2,BorderLayout.CENTER);

		signframe.setVisible(true);
		signframe.setSize(500,300);
		signframe.setDefaultCloseOperation(EXIT_ON_CLOSE);
		signframe.setLocationRelativeTo(null);

		registrati.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if(!(registra_user.getText().equals(null)) && registra_user.getText().length()>0 && (!registra_psw.getText().equals(null) && registra_psw.getText().length()>0)) {
					LinkedList<Cliente> listaclienti=new LinkedList<Cliente>();
					try {
						ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fclient));
						Cliente temp;
						do {
							temp=(Cliente)ois.readObject();
							listaclienti.add(temp);
						}while(temp!=null);
						ois.close();
					}catch(IOException | ClassNotFoundException e1) {
						e1.printStackTrace();
					}

					Boolean found=false;
					for(Cliente c: listaclienti) {
						if((c.getUsername().equals(registra_user.getText()))) {
							found=true;
						}
					}
					if(!found) {
						try {
							listaclienti.add(new Cliente(registra_user.getText(),registra_psw.getText()));
							ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fclient));
							for(Cliente c: listaclienti) {
								oos.writeObject(c);
							}
							oos.flush();
							oos.close();
							signframe.dispose();
							visible();
							String s = new String("Registrazione dell'utente "+registra_user.getText()+" avvenuta con successo");
							aggiungiavviso(s);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					else {
						JPanel ad = new JPanel();
						ad.add(avviso2);
						signframe.add(ad,BorderLayout.SOUTH);
						signframe.validate();
					}
				}
				else {
					aggiungiavviso(avviso1);
				}
			}	
		});

		goback.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource().equals(goback)) {
					signframe.dispose();
					visible();
				}
			}
		});
	}



	//EOF	
}
