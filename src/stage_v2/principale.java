package stage_v2;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;



import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;

public class principale extends JFrame {

	private JPanel contentPane;
	private JPanel panelEntet,panelBody;
	private JLabel lblIconSostem,lblIconMelliti;

	private JPanel pHome,pListeRecherche,pListeMachine,pAjout,p5,p6;

	private JLabel labelExit ;

	private JTextField textRecherche;
	private JButton btnRecherche;

	private int x,y;

	private JTable liste;
	JScrollPane list=new JScrollPane();

	JPanel panelModif;
	JTextField Acode,code,dsgn,unite,stock,cout,consommation,ref;
	JComboBox<String> four;
	JLabel id=new JLabel();
	JButton btnModif,btnAjout;
	JCheckBox addPiece;
	private JComboBox<String> machine,listeMachine,listeRecherche;
	JLabel lblRehercheSelon,lblMAchine;


	String listeRecherches[]= {"toutes","designation","code_piece","an_Code","fournisseur","stock"};
	JLabel lblExitModif,lblAcd,lblCd,lblD,lblU,lblS,lblCn,lblCt,lblR,lblF,lblMachine;
	item item;

	DefaultTableModel model;
	Object[] columns={"ID","Acode","Code","dsgn","unite","stock","N°-1","cout","ref","fournisseur"};
	//Home homePanel;

	Connection con;


	public principale() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension sc=Toolkit.getDefaultToolkit().getScreenSize();

		this.setSize((int)sc.getWidth(),(int) sc.getHeight());
		this.setUndecorated(true);
		this.setLocationRelativeTo(null);
		contentPane = new JPanel();
		panelEntet=new JPanel();
		panelBody=new JPanel();
		lblIconMelliti=new JLabel("Melliti");
		lblIconSostem=new JLabel("SOSTEM");
		//System.out.println(getConsommation(con,14862));
		pHome=new JPanel();
		pAjout=new JPanel();
		pListeMachine=new JPanel();
		pListeRecherche=new JPanel();
		textRecherche=new JTextField();
		btnRecherche=new JButton("recherche");

		lblMAchine=new JLabel("selon la machine");
		lblRehercheSelon=new JLabel("recherche selon");
		listeMachine=new JComboBox<String>(getListeMachine());
		listeMachine.setSelectedIndex(0);

		pHome.addMouseListener(new panelButtonMouseAdapter(pHome));
		pAjout.addMouseListener(new panelButtonMouseAdapter(pAjout));
		//**************************************
		listeRecherche=new JComboBox<String>(listeRecherches);

		listeMachine.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent event) {
				JComboBox cd=(JComboBox)event.getSource();

				Object item=event.getItem();

				if(event.getStateChange()==ItemEvent.SELECTED) {

					getDataMAchine(item.toString());
				}	
			}
		});

		btnRecherche.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				String requet="";
				switch (listeRecherche.getSelectedItem().toString()) {
				case "toutes": getDataRecherche("SELECT * FROM `piece`,stock WHERE piece.id_piece=stock.id_piece");break;
				case "designation": 
					requet="SELECT * FROM `piece`,stock WHERE piece.id_piece=stock.id_piece"
							+" and piece.designation like '%"+textRecherche.getText()+"%'";
					getDataRecherche(requet);
					break;
				case "code_piece": 
					requet="SELECT * FROM `piece`,stock WHERE piece.id_piece=stock.id_piece"
							+" and piece.code_piece like '%"+textRecherche.getText()+"%'";
					getDataRecherche(requet);
					break;
				case "an_Code": 
					requet="SELECT * FROM `piece`,stock WHERE piece.id_piece=stock.id_piece and piece.an_Code like '%"+textRecherche.getText().toString()+"%'";
					getDataRecherche(requet);
					break;
				case "fournisseur": 
					requet="SELECT * FROM `piece`,stock WHERE piece.id_piece=stock.id_piece and piece.id_fournisseur in(SELECT id_fournisseur from fournisseur WHERE fournisseur.code_fournisseur like '%"+textRecherche.getText().toString()+"%')";

					getDataRecherche(requet);

					break;
				case "stock": 
					requet="SELECT * FROM `piece`,stock WHERE piece.id_piece=stock.id_piece and piece.id_piece in(SELECT stock.id_piece from stock WHERE stock.qt_stock="+textRecherche.getText()+")";
					getDataRecherche(requet);

					break;

				default:
					getDataRecherche("SELECT * FROM `piece`,stock WHERE piece.id_piece=stock.id_piece");
					break;
				}
			}
		});

		lblIconSostem.setFont(new Font("DejaVu Math TeX Gyre", Font.BOLD | Font.ITALIC, 18));
		lblIconSostem.setForeground(new Color(204, 51, 0));
		lblIconMelliti.setFont(new Font("DejaVu Math TeX Gyre", Font.BOLD | Font.ITALIC, 18));
		lblIconMelliti.setForeground(new Color(204, 51, 0));
		contentPane.setLayout(null);
		panelEntet.setLayout(null);
		panelBody.setLayout(null);
		panelEntet.setBorder(new MatteBorder(0, 0, 3, 0, (Color) new Color(0, 0, 0)));
		panelEntet.setBackground(Color.WHITE);

		panelEntet.setBounds(1,1,getWidth()-74,getHeight()/15*2);
		panelBody.setBounds(panelEntet.getX(),panelEntet.getY()+panelEntet.getHeight()+2,panelEntet.getWidth(),this.getHeight()-panelEntet.getHeight()-35);
		list.setBounds(0,0,panelBody.getWidth(),panelBody.getHeight());
		
		panelBody.add(list);

		list.setToolTipText("");
		list.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		model=new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		liste = new JTable();
		liste.setModel(model);
		model.setColumnIdentifiers(columns);

		int widthListe=(liste.getWidth()-150)/7;

		liste.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		liste.getColumnModel().getColumn(1).setPreferredWidth(130);
		liste.getColumnModel().getColumn(2).setPreferredWidth(130);
		liste.getColumnModel().getColumn(3).setPreferredWidth(300);

		liste.setRowHeight(30);
		liste.setBorder(null);
		list.setBackground(Color.white);
		list.setViewportView(liste);
		getDataFromDB();

		panelModif=new JPanel();
		panelModif.setBorder(new MatteBorder(2, 0, 0, 0, (Color) new Color(0)));
		lblExitModif=new JLabel("X");
		lblAcd=new JLabel("An_Code :");
		lblCd=new JLabel("Code :");
		lblD=new JLabel("Dsgntion :");
		lblU=new JLabel("Unite :");
		lblS=new JLabel("stock :");
		lblCn=new JLabel("conssomation :");
		lblCt=new JLabel("cout :");
		lblR=new JLabel("ref :");
		lblF=new JLabel("fournissuer :");
		lblMachine=new JLabel("machine");

		Acode=new JTextField();
		code=new JTextField();
		dsgn=new JTextField();
		unite=new JTextField();
		stock=new JTextField();
		consommation=new JTextField();
		consommation.setEditable(false);
		cout=new JTextField();
		ref=new JTextField();
		four=new JComboBox<String>(getListeFournisseur());
		machine=new JComboBox<String>(getListeMachine());
		
		btnModif=new JButton("modifier");
		btnAjout=new JButton("ajouter");
		addPiece=new JCheckBox("ajouter neveau piece");
		liste.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				panelModif.setVisible(true);
				list.setBounds(list.getX(),list.getY(),list.getWidth(),panelBody.getHeight()/7*5);
				panelModif.setBounds(list.getX(),list.getY()+list.getHeight()+1,list.getWidth(),panelBody.getHeight()/7*2);
				panelBody.add(panelModif);
				panelModif.setLayout(null);
				setElementPanelModif();
				panelModif.setBackground(Color.lightGray);
			}
		});
		x=panelEntet.getWidth()/10;
		y=this.getHeight()/10;
		this.setBounds(0,0,panelEntet.getWidth()+10,this.getHeight());

		lblExit();

		lblIconSostem.setBounds(2,2,x,panelEntet.getHeight()/3);
		lblIconMelliti.setBounds(lblIconSostem.getX(),lblIconSostem.getY()+lblIconSostem.getHeight()+1,lblIconSostem.getWidth(),panelEntet.getHeight()/4);
		panelMenu(lblIconSostem.getY()+lblIconSostem.getHeight()/2);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		panelEntet.add(lblIconSostem);
		panelEntet.add(lblIconMelliti);
		contentPane.add(panelEntet);
		contentPane.add(panelBody);
		setContentPane(contentPane);
	}
	public void lblExit() {
		labelExit = new JLabel("X");
		labelExit.setForeground(Color.black);
		labelExit.setFont(new Font("Dialog", Font.BOLD, 24));
		labelExit.setBounds(this.getWidth()-35,5,25,25);
		labelExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				if(JOptionPane.showConfirmDialog(null, " Close this application?","confirmation",JOptionPane.YES_NO_OPTION)==0) {
					System.exit(0);
				}	
			}
			@Override
			public void mouseEntered(MouseEvent e) {labelExit.setForeground(Color.red);}

			@Override
			public void mouseExited(MouseEvent e) {labelExit.setForeground(Color.black);}
		}); 
		panelEntet.add(labelExit);
	}

	public void panelMenu(int yp) {

		//pHome.setBorder(new LineBorder(new Color(0, 0, 0)));
		//pAjout.setBorder(new LineBorder(new Color(0, 0, 0)));
		//pListeMachine.setBorder(new LineBorder(new Color(0, 0, 0)));
		//pListeRecherche.setBorder(new LineBorder(new Color(0, 0, 0)));

		p5=new JPanel();
		p6=new JPanel();


		int width=panelEntet.getWidth()/10;
		int height=panelEntet.getHeight()-lblIconSostem.getHeight()/2-10;
		pHome.setBounds(lblIconMelliti.getX()+lblIconMelliti.getWidth()+width/2,yp*2,width/4*3,height/4*3);
		pAjout.setBounds(pHome.getX()+width/2+35,yp*2,width/4*3,height/4*3);
		//p5.setBounds(pAjout.getX()+width+5,yp,width,height);



		//t[0].addMouseListener(new panelButtonMouseAdapter(t[0]));
		panelEntet.add(pHome);
		panelEntet.add(pAjout);
		//panelEntet.add(p5);






		btnRecherche.setBounds(panelEntet.getWidth()-pHome.getWidth()-20,pHome.getY()+pHome.getHeight()/2-4,pHome.getWidth()+10,28);
		textRecherche.setBounds(btnRecherche.getX()-btnRecherche.getWidth()-5,btnRecherche.getY()+2,btnRecherche.getWidth(),23);
		pListeRecherche.setBounds(textRecherche.getX()-width-5,yp,width,height);
		pListeMachine.setBounds(pListeRecherche.getX()-width-5,yp,width,height);
		panelEntet.add(btnRecherche);
		panelEntet.add(textRecherche);
		panelEntet.add(pListeRecherche);
		panelEntet.add(pListeMachine);

		pHome.setBackground(Color.white);
		pAjout.setBackground(Color.white);
		pHome.setLayout(null);
		pAjout.setLayout(null);
		JLabel labelHome = new JLabel("");
		labelHome.setBounds(10,10,pHome.getWidth()-20,pHome.getHeight()-20);
		ImageIcon logoImage=new ImageIcon(principale.class.getResource("/images/home_e.png"));
		Image image=logoImage.getImage();
		Image tmp_Image=image.getScaledInstance(labelHome.getWidth(), labelHome.getHeight(), Image.SCALE_SMOOTH);
		logoImage=new ImageIcon(tmp_Image);
		labelHome.setIcon(logoImage);
		pHome.add(labelHome);

		JLabel labelAjout = new JLabel("");
		labelAjout.setBounds(10,10,pHome.getWidth()-20,pHome.getHeight()-20);
		ImageIcon logoImageA=new ImageIcon(principale.class.getResource("/images/ajout1.png"));
		Image imageA=logoImageA.getImage();
		Image tmp_ImageA=imageA.getScaledInstance(labelAjout.getWidth(), labelAjout.getHeight(), Image.SCALE_SMOOTH);
		logoImageA=new ImageIcon(tmp_ImageA);
		labelAjout.setIcon(logoImageA);
		pAjout.add(labelAjout);

		pListeMachine.setLayout(null);
		pListeMachine.setBackground(Color.white);
		lblMAchine.setBounds(0,0,pListeMachine.getWidth(),pListeMachine.getHeight()/2-3);
		listeMachine.setBounds(2,pListeMachine.getHeight()/2,pListeMachine.getWidth()-4,pListeMachine.getHeight()/3);
		pListeMachine.add(lblMAchine);
		pListeMachine.add(listeMachine);

		pListeRecherche.setLayout(null);
		pListeRecherche.setBackground(Color.white);
		lblRehercheSelon.setBounds(0,0,pListeRecherche.getWidth(),pListeRecherche.getHeight()/2-3);
		listeRecherche.setBounds(2,pListeRecherche.getHeight()/2,pListeRecherche.getWidth()-4,pListeRecherche.getHeight()/3);
		pListeRecherche.add(lblRehercheSelon);
		pListeRecherche.add(listeRecherche);
	}


	public void setElementPanelModif()
	{
		item=null;

		int ligne=liste.getSelectedRow();
		id.setText(liste.getValueAt(ligne, 0).toString());
		Acode.setText(liste.getValueAt(ligne, 1).toString());
		code.setText(liste.getValueAt(ligne, 2).toString());
		dsgn.setText(liste.getValueAt(ligne, 3).toString());
		unite.setText(liste.getValueAt(ligne, 4).toString());
		stock.setText(liste.getValueAt(ligne, 5).toString());
		consommation.setText(String.valueOf(getConsommation(con,id.getText())));
		cout.setText(liste.getValueAt(ligne, 7).toString());
		ref.setText(liste.getValueAt(ligne, 8).toString());



		item=new item(id.getText(),Acode.getText(),code.getText(),dsgn.getText(),unite.getText(),stock.getText(),consommation.getText(),cout.getText(),ref.getText(),liste.getValueAt(ligne, 9).toString(),getMachine(id.getText()));
		addPiece.setSelected(false);
		btnModif.setEnabled(true);
		btnAjout.setEnabled(false);

		machine.getModel().setSelectedItem(item.getmachine());
		four.getModel().setSelectedItem(item.getfournisseur());


		btnModif.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int index =liste.getSelectedRow();
				try {
					String rq="UPDATE `piece` SET";
					rq=rq+"`code_piece`='"+code.getText()+"'";
					if(!code.getText().equals(item.getCode())){
						item.setCode(code.getText());
						liste.setValueAt(code.getText(), index, 2);
					}
					if(!dsgn.getText().equals(item.getdesgnation())){
						rq=rq+" ,`designation`='"+dsgn.getText()+"'";
						liste.setValueAt(dsgn.getText(), index, 3);
						item.setdesgnation(dsgn.getText());
					}		

					if(!Acode.getText().equals(item.getAnCode() )) {
						rq=rq+",`an_Code`='"+Acode.getText()+"'";
						liste.setValueAt(Acode.getText(), index, 1);
						item.setAnCode(Acode.getText());
					}
					if(!unite.getText().equals(item.getunite()) ) {
						rq=rq+",`unite`='"+unite.getText()+"'";
						liste.setValueAt(unite.getText(), index, 4);
						item.setunite(unite.getText());
					}
					if(!cout.getText().equals(item.getcout())) {
						rq=rq+",`cout`='"+cout.getText()+"'";
						liste.setValueAt(cout.getText(), index, 7);
						item.setcout(cout.getText());
					}

					if(!four.getItemAt(four.getSelectedIndex()).toString().equals(item.getfournisseur())){
						rq=rq+",`id_fournisseur`='"+getIdFournisseur(four.getSelectedItem().toString())+"'";
						liste.setValueAt(four.getSelectedItem().toString(), index, 9);
						item.setfournisseur(four.getSelectedItem().toString());
					}
					if(!machine.getItemAt(machine.getSelectedIndex()).equals(item.getmachine())) {
						rq=rq+",`id_machine`='"+getIdMachine(machine.getSelectedItem().toString())+"'";
						item.setmachine(machine.getSelectedItem().toString());
					}
					if(!ref.getText().equals(item.getref())) {
						rq=rq+",`refirence`='"+ref.getText()+"'";
						liste.setValueAt(ref.getText(), index, 8);
						item.setref(code.getText());
					}
					rq=rq+"where id_piece='"+item.getId()+"'";
					PreparedStatement ps;
					//System.out.println("rqt "+rq);
					ps = con.prepareStatement(rq);

					ps.executeUpdate();
					ps.close();

				} catch (SQLException e) {
					System.out.println("errer update  piece "+e.getMessage());
				}

				try {
					LocalDate dt=LocalDate.now();
					DateTimeFormatter df=DateTimeFormatter.ofPattern("yyyy-MM-dd");
					String dte=df.format(dt);
					ArrayList<String> val=new ArrayList<String>(Arrays.asList(dte.split("-")));
					int annePas=Integer.parseInt(val.get(0))-1;


					if(!item.getstock().equals(stock.getText()) ) {
							
						String qrStock="UPDATE `stock` SET `qt_stock`='";
						qrStock=qrStock+ stock.getText()+"'";
						
						if(Integer.parseInt(stock.getText())<Integer.parseInt(item.getstock())) {
							
							int consom=Integer.parseInt(item.getconsommation());
							
							qrStock=qrStock+", `qt_conssomable`='"+consom+"'";
							//consommation.setText("0");
						}
						
						qrStock=qrStock+" WHERE id_piece='"+item.getId()+"' and annee like '"+val.get(0)+"%';";

						PreparedStatement ps;
						System.out.println("ùùùùùùùùùùùùùù "+qrStock+"\n");
						ps = con.prepareStatement(qrStock);
						ps.executeUpdate();
						ps.close();
						item.setstock(stock.getText());
						//item.setconsommation(consommation.getText());
						liste.setValueAt(stock.getText(), index, 5);
					}

				} catch (Exception e) {
					System.out.println("errer update  stock "+e.getMessage());
				}
			}
		});

		lblExitModif.setBounds(panelModif.getWidth()-35,5,25,25);
		lblExitModif.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				panelModif.setVisible(false);
				list.setBounds(list.getX(),list.getY(),list.getWidth(),panelBody.getHeight());
			}
			@Override
			public void mouseEntered(MouseEvent e) {lblExitModif.setForeground(Color.red);}

			@Override
			public void mouseExited(MouseEvent e) {lblExitModif.setForeground(Color.black);}
		}); 
		panelModif.add(lblExitModif);

		int wid=panelModif.getWidth()/6;
		int he=panelModif.getHeight()/6;

		id.setBounds(5,labelExit.getY()+13,wid/2,he);
		lblAcd.setBounds(id.getX()+id.getWidth()+5,id.getY(),wid/3,he);
		Acode.setBounds(lblAcd.getX()+lblAcd.getWidth(),id.getY(),wid/2,he);
		lblCd.setBounds(Acode.getX()+Acode.getWidth()+3,id.getY(),lblAcd.getWidth(),lblAcd.getHeight());
		code.setBounds(lblCd.getX()+lblCd.getWidth(),Acode.getY(),Acode.getWidth(),Acode.getHeight());
		lblD.setBounds(code.getX()+code.getWidth()+13,id.getY(),wid/2,lblAcd.getHeight());
		dsgn.setBounds(lblD.getX()+lblD.getWidth(),id.getY(),wid,he);
		lblU.setBounds(dsgn.getX()+dsgn.getWidth()+13,id.getY(),lblAcd.getWidth(),lblAcd.getHeight());
		unite.setBounds(lblU.getX()+lblU.getWidth(),id.getY(),code.getWidth(),code.getHeight());
		lblS.setBounds(unite.getX()+unite.getWidth()+15,id.getY(),lblAcd.getWidth(),lblAcd.getHeight());
		stock.setBounds(lblS.getX()+lblS.getWidth(),id.getY(),Acode.getWidth(),Acode.getHeight());
		lblCn.setBounds(lblAcd.getX(),id.getY()+id.getHeight()*2,lblAcd.getWidth()+5,lblAcd.getHeight());
		consommation.setBounds(Acode.getX(),lblCn.getY(),Acode.getWidth(),Acode.getHeight());
		lblR.setBounds(lblCd.getX(),lblCn.getY(),lblCd.getWidth(),lblCd.getHeight());
		ref.setBounds(code.getX(),lblR.getY(),code.getWidth(),code.getHeight());
		lblF.setBounds(lblD.getX(),ref.getY(),lblR.getWidth()+5,lblR.getHeight());
		four.setBounds(dsgn.getX(),lblF.getY(),ref.getWidth(),ref.getHeight());
		lblCt.setBounds(lblU.getX(),lblF.getY(),lblR.getWidth(),lblR.getHeight());
		cout.setBounds(unite.getX(),four.getY(),four.getWidth(),four.getHeight());
		btnModif.setBounds(lblExitModif.getX()-cout.getWidth(),lblCt.getY()+lblCt.getHeight()*3/2,cout.getWidth(),cout.getHeight());
		btnAjout.setBounds(btnModif.getX()-btnModif.getWidth()-10,btnModif.getY(),btnModif.getWidth(),btnModif.getHeight());
		addPiece.setBounds(lblCn.getX(),btnModif.getY(),lblCn.getWidth()*3,lblCn.getHeight());
		addPiece.setBackground(Color.lightGray);
		lblMachine.setBounds(lblS.getX(),lblCt.getY(),lblCt.getWidth(),lblCt.getHeight());
		machine.setBounds(stock.getX(),cout.getY(),cout.getWidth()+23,cout.getHeight());

		addPiece.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				if(addPiece.isSelected()) {

					btnModif.setEnabled(false);
					btnAjout.setEnabled(true);

					//*****
					id.setText("id:xxxx");
					Acode.setText("");
					code.setText("");
					dsgn.setText("");
					unite.setText("");
					stock.setText("");
					consommation.setText("");
					cout.setText("");
					ref.setText("");
					four.setSelectedIndex(0);
					machine.setSelectedIndex(0);

				}
				else {
					
					btnModif.setEnabled(true);
					btnAjout.setEnabled(false);
					id.setText(liste.getValueAt(ligne, 0).toString());
					Acode.setText(liste.getValueAt(ligne, 1).toString());
					code.setText(liste.getValueAt(ligne, 2).toString());
					dsgn.setText(liste.getValueAt(ligne, 3).toString());
					unite.setText(liste.getValueAt(ligne, 4).toString());
					stock.setText(liste.getValueAt(ligne, 5).toString());
					consommation.setText(item.getconsommation());
					cout.setText(liste.getValueAt(ligne, 7).toString());
					ref.setText(liste.getValueAt(ligne, 8).toString());
					//four.setText(liste.getValueAt(ligne, 9).toString());

					machine.getModel().setSelectedItem(item.getmachine());
					four.getModel().setSelectedItem(item.getfournisseur());
				}
			}
		});






		panelModif.add(id);
		panelModif.add(lblAcd);
		panelModif.add(Acode);
		panelModif.add(lblCd);
		panelModif.add(code);
		panelModif.add(lblD);
		panelModif.add(dsgn);
		panelModif.add(lblU);
		panelModif.add(unite);
		panelModif.add(lblS);
		panelModif.add(stock);
		panelModif.add(lblCn);
		panelModif.add(consommation);
		panelModif.add(lblR);
		panelModif.add(ref);
		panelModif.add(lblF);
		panelModif.add(four);
		panelModif.add(lblCt);
		panelModif.add(cout);
		panelModif.add(lblMachine);
		panelModif.add(machine);

		panelModif.add(btnModif);
		panelModif.add(btnAjout);
		panelModif.add(addPiece);
		btnModif.setEnabled(true);
		btnAjout.setEnabled(false);


	}

	public void getDataFromDB()
	{

		try {



			String qr="SELECT p.id_piece,p.an_Code,p.code_piece,p.designation,p.unite,s1.qt_stock,s2.qt_conssomable,p.cout ,p. refirence,p.id_fournisseur FROM `stock` s1 ,stock s2,piece p WHERE s1.annee LIKE '2020%' and s2.annee LIKE '2019%' AND s1.id_piece=s2.id_piece and p.id_piece=s1.id_piece  ";

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con=DriverManager.getConnection("jdbc:mysql://localhost/sostem", "root", null);

			Statement st= (Statement) con.createStatement();
			ResultSet rs=st.executeQuery(qr);
			Object[] rows=new Object[10];
			while(rs.next())
			{
				rows[0]=rs.getInt(1);//id
				rows[1]=rs.getString(2);//Acode
				rows[2]=rs.getString(3);//code
				rows[3]=rs.getString(4);//desgn
				rows[4]=rs.getString(5);//unite
				rows[5]=rs.getInt(6);//stock
				rows[6]=rs.getInt(7);//consommation
				rows[7]=rs.getInt(8);//cout
				rows[8]=rs.getInt(9);//ref

				if(rs.getInt(10)>0) {
					rows[9]=getfournisseur(con,rs.getInt(10));//four
				}
				else {rows[9]="";}

				model.addRow(rows);
				rows[0]="";
				rows[1]="";
				rows[2]="";
				rows[3]="";
				rows[4]="";
				rows[5]="";
				rows[6]="";
				rows[7]="";
				rows[8]="";
				rows[9]="";

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getfournisseur(Connection cn,int id_f) {
		String qr="select* from fournisseur where id_fournisseur="+id_f;

		Statement st;
		try {
			st = (Statement) con.createStatement();
			ResultSet rs=st.executeQuery(qr);
			rs.next();
			return rs.getString(2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("four "+e.getMessage());
			return null;
		}

	}

	public void getDataMAchine(String machine){
		//******************
		try {

			listeRecherche.setSelectedIndex(0);
			textRecherche.setText("");

			System.out.println("machine ["+machine);
			String qr="SELECT * FROM `piece`,stock WHERE piece.id_piece=stock.id_piece";
			if(!machine.contentEquals("")) {
				qr=qr+"					and piece.id_machine in(SELECT machine.id_machine from machine WHERE machine.nom_machine LIKE'%"+machine+"%')" ;
			}
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con=DriverManager.getConnection("jdbc:mysql://localhost/sostem", "root", null);

			Statement st= (Statement) con.createStatement();
			ResultSet rs=st.executeQuery(qr);

			model=new DefaultTableModel() {
				@Override
				public boolean isCellEditable(int arg0, int arg1) {
					// TODO Auto-generated method stub
					return false;
				}
			};
			model.setColumnIdentifiers(columns);
			liste.setModel(model);
			Object[] rows=new Object[10];
			while(rs.next())
			{
				rows[0]=rs.getInt(1);//id
				rows[1]=rs.getString(4);//Acode
				rows[2]=rs.getString(3);//code
				rows[3]=rs.getString(2);//desgn
				rows[4]=rs.getString(5);//unite
				rows[5]=rs.getInt(12);//stock
				rows[6]=rs.getInt(13);//consommation
				rows[7]=rs.getInt(6);//cout
				rows[8]=rs.getInt(9);//ref
				String d=rs.getString(14);
				ArrayList<String> val=new ArrayList<String>(Arrays.asList(rs.getString(14).split("-")));
				//**********************************************************************
				Integer.parseInt(val.get(0));
				if(rs.getInt(7)>0) {
					rows[9]=getfournisseur(con,rs.getInt(7));//four
				}
				else {rows[9]=11;}

				model.addRow(rows);
				rows[0]="";
				rows[1]="";
				rows[2]="";
				rows[3]="";
				rows[4]="";
				rows[5]="";
				rows[6]="";
				rows[7]="";
				rows[8]="";
				rows[9]="";

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("errer machine  recherche :["+e.getMessage()+" ]");
		}

	}

	public void getDataRecherche(String rqt) {
		try {


			String mchin=listeMachine.getSelectedItem().toString();
			if(!mchin.equals("")) {
				rqt=rqt+"and piece.id_machine in(SELECT machine.id_machine from machine WHERE machine.nom_machine LIKE'%"+mchin+"%')" ;
			}

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con=DriverManager.getConnection("jdbc:mysql://localhost/sostem", "root", null);

			Statement st= (Statement) con.createStatement();
			ResultSet rs=st.executeQuery(rqt);
			model=null;
			model=new DefaultTableModel() {
				@Override
				public boolean isCellEditable(int arg0, int arg1) {
					// TODO Auto-generated method stub
					return false;
				}
			};
			model.setColumnIdentifiers(columns);
			liste.setModel(model);
			Object[] rows=new Object[10];
			while(rs.next())
			{
				rows[0]=rs.getInt(1);//id
				rows[1]=rs.getString(4);//Acode
				rows[2]=rs.getString(3);//code
				rows[3]=rs.getString(2);//desgn
				rows[4]=rs.getString(5);//unite
				rows[5]=rs.getInt(12);//stock
				rows[6]=rs.getInt(13);//consommation
				rows[7]=rs.getInt(6);//cout
				rows[8]=rs.getInt(9);//ref
				String d=rs.getString(14);
				ArrayList<String> val=new ArrayList<String>(Arrays.asList(rs.getString(14).split("-")));
				//**********************************************************************
				Integer.parseInt(val.get(0));
				if(rs.getInt(7)>0) {
					rows[9]=getfournisseur(con,rs.getInt(7));//four
				}
				else {rows[9]=11;}

				model.addRow(rows);
				rows[0]="";
				rows[1]="";
				rows[2]="";
				rows[3]="";
				rows[4]="";
				rows[5]="";
				rows[6]="";
				rows[7]="";
				rows[8]="";
				rows[9]="";

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String[] getListeMachine() {
		Object[] listeMachines ;
		ArrayList<String> listeM=new ArrayList<String>();
		listeM.add("");
		try {
			String qr="SELECT * FROM `machine`";

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con=DriverManager.getConnection("jdbc:mysql://localhost/sostem", "root", null);

			Statement st= (Statement) con.createStatement();
			ResultSet rs=st.executeQuery(qr);
			int i=1;

			while(rs.next())
			{

				listeM.add(rs.getString(2));

			}
			listeMachines=new String[listeM.size()];
			listeMachines=listeM.toArray(listeMachines);

			return (String[]) listeMachines;
		} catch (Exception e) {
			System.out.println("errer machine  liste :["+e.getMessage()+" ]");
			String l[]= {""};
			return l;

		}
	}

	public String[] getListeFournisseur() {
		Object[] listeMachines ;
		ArrayList<String> listeM=new ArrayList<String>();
		listeM.add("");
		try {
			String qr="SELECT * FROM `fournisseur`";

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con=DriverManager.getConnection("jdbc:mysql://localhost/sostem", "root", null);

			Statement st= (Statement) con.createStatement();
			ResultSet rs=st.executeQuery(qr);

			while(rs.next())
			{

				listeM.add(rs.getString(2));

			}
			listeMachines=new String[listeM.size()];
			listeMachines=listeM.toArray(listeMachines);

			return (String[]) listeMachines;
		} catch (Exception e) {
			System.out.println("errer fournisseur  liste :["+e.getMessage()+" ]");
			String l[]= {""};
			return l;

		}
	}
	public String getMachine(String string) {

		try {
			String qr="SELECT * FROM `machine` where 	id_machine=(select id_machine from piece where id_piece='"+string+"')";

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con=DriverManager.getConnection("jdbc:mysql://localhost/sostem", "root", null);

			Statement st= (Statement) con.createStatement();
			ResultSet rs=st.executeQuery(qr);

			rs.next();
			return rs.getString(2);
		} catch (Exception e) {
			System.out.println("errer getMachine  liste :["+e.getMessage()+" ]");
			return "";

		}
	}

	public int getConsommation(Connection cn,String id_piece) {

		try {
			LocalDate dt=LocalDate.now();
			DateTimeFormatter df=DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String dte=df.format(dt);
			ArrayList<String> val=new ArrayList<String>(Arrays.asList(dte.split("-")));
			int annePas=Integer.parseInt(val.get(0));

			String qr="SELECT qt_conssomable FROM `stock` WHERE id_piece='"+id_piece+"' and annee like'"+annePas+"%'";

			//Class.forName("com.mysql.jdbc.Driver").newInstance();
			//con=DriverManager.getConnection("jdbc:mysql://localhost/sostem", "root", null);

			Statement st= (Statement) cn.createStatement();
			ResultSet rs=st.executeQuery(qr);

			rs.next();
			return rs.getInt(1);
		} catch (Exception e) {
			System.out.println("errer consommation  get :["+e.getMessage()+" ]");
			return 0;

		}
	}

	public int getIdFournisseur(String n_fournisseur) {

		try {
			String qr="SELECT * FROM `fournisseur` where 	code_fournisseur='"+n_fournisseur+"'";

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con=DriverManager.getConnection("jdbc:mysql://localhost/sostem", "root", null);

			Statement st= (Statement) con.createStatement();
			ResultSet rs=st.executeQuery(qr);

			rs.next();
			return rs.getInt(1);
		} catch (Exception e) {
			System.out.println("errer fournisseur  id :["+e.getMessage()+" ]");
			return 0;

		}
	}

	public int getIdMachine(String n_machine) {
		try {
			String qr="SELECT * FROM `machine` where 	nom_machine='"+n_machine+"'";

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con=DriverManager.getConnection("jdbc:mysql://localhost/sostem", "root", null);

			Statement st= (Statement) con.createStatement();
			ResultSet rs=st.executeQuery(qr);
			rs.next();
			return rs.getInt(1);
		} catch (Exception e) {
			System.out.println("errer machine  Id :["+e.getMessage()+" ]");
			return 0;	}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					principale frame = new principale();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
}
