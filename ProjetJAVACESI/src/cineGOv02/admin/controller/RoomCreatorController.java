/*
 * RoomCreatorController.java                                11 févr. 2016
 * CESI RILA 2015/2017
 */
package cineGOv02.admin.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import cineGOv02.admin.model.AdminModel;
import cineGOv02.admin.view.GestionCinemaView;
import cineGOv02.admin.view.RoomCreatorView;
import cineGOv02.common.entity.Salle;
import cineGOv02.common.graphics.MainApp;
import cineGOv02.common.graphics.SalleIcon;
import cineGOv02.common.graphics.SiegeIcon;
import cineGOv02.common.graphics.WallIcon;

/**
 * Controlleur de la vue de création de plan de salle
 * @author Remi
 *
 */
public class RoomCreatorController implements MouseListener, ActionListener, KeyListener{
    /** Vue de création d'un plan de salle */
    private RoomCreatorView roomView;
    /** Liste des éléments du plan */
    private JPanel[][] panelIcon;
    /** Buffer d'image */
    private BufferedImage bufferImage;
    /** Longueur de la salle */
    private static int X;
    /** largeur */
    private static int Y;
    /** Taille max d'un icone*/
    private static int size = 44;
    /** Fenêtre principale */
    private MainApp frame;
    private boolean ajout, suppr, normal, hand, allee;    
    private ImageIcon selectedIcon, selectedIconAllee, selectedIconSiege, selectedIconPorte,
    selectedIconHand, selectedIconEcran, iconeSiege, iconeSiegeHand, iconeAllee, iconeEcran, iconePorte = null;
    /** Objet servant à la manipulation des documents XML */
    private final DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
    /** Objet servant à la manipulation des documents XML */
    private DocumentBuilder builder = null;
    /** Liste des salles */
    private ArrayList<Salle> listeSalle;
    /** Salle selectionnées */
    private Salle salle;
    /** Objet servant à la manipulation des documents XML */
    private DOMSource XMLsource;
    /** XML au format texte */
    private String XMLText;
    /** Modèle de données */
    private AdminModel model;
    /** Nombre de sièges placés*/
    private int nbSiege;

    /**
     * Constructeur du controlleur de la vue de création de salle 
     * @param frame
     * @param factory 
     */
    public RoomCreatorController(MainApp frame, RoomCreatorView roomView, AdminModel model){
        this.model = model;
        listeSalle = model.getAllSalles(frame.getCinema());
        salle = frame.getSalle();
        this.frame = frame;
        this.roomView = roomView;
        try {
            bufferImage = ImageIO.read(new File("images/siege_min.jpg"));
            iconeSiege = new ImageIcon(bufferImage);
            bufferImage = ImageIO.read(new File("images/siegeHand_min.jpg"));
            iconeSiegeHand = new ImageIcon(bufferImage);
            bufferImage = ImageIO.read(new File("images/allee_min.jpg"));
            iconeAllee = new ImageIcon(bufferImage);
            bufferImage = ImageIO.read(new File("images/ecran_min.jpg"));
            iconeEcran = new ImageIcon(bufferImage);
            bufferImage = ImageIO.read(new File("images/porte_min.jpg"));
            iconePorte = new ImageIcon(bufferImage);
        } catch (IOException e) {}

        frame.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent event) {
                if(panelIcon.length > 0){
                    if(event.getWheelRotation() < 0 && size < 80 ){
                        size++;
                    }else if(size > roomView.getScrollPane().getWidth() / X){
                        size--;
                    }
                    for(int x = 0 ; x < X ; x++){

                        for(int y = 0 ; y < Y ; y++){  
                            panelIcon[x][y].setMinimumSize(new Dimension(size, size));
                            panelIcon[x][y].setPreferredSize(new Dimension(size, size));
                            panelIcon[x][y].setMaximumSize(new Dimension(size, size));
                            if(panelIcon[x][y].getComponent(0) instanceof SiegeIcon && ((SalleIcon)panelIcon[x][y].getComponent(0)).isSet()){
                                if(((SiegeIcon)panelIcon[x][y].getComponent(0)).isNormal()){
                                    selectedIconSiege = new ImageIcon(iconeSiege.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
                                    ((SiegeIcon)panelIcon[x][y].getComponent(0)).setIcon(selectedIconSiege);
                                }else if(((SiegeIcon)panelIcon[x][y].getComponent(0)).isHand()){
                                    selectedIconHand = new ImageIcon(iconeSiegeHand.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
                                    ((SiegeIcon)panelIcon[x][y].getComponent(0)).setIcon(selectedIconHand);
                                }else if (((SiegeIcon)panelIcon[x][y].getComponent(0)).isAllee()){
                                    selectedIconAllee = new ImageIcon(iconeAllee.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
                                    ((SiegeIcon)panelIcon[x][y].getComponent(0)).setIcon(selectedIconAllee);
                                }
                                ((SiegeIcon)panelIcon[x][y].getComponent(0)).revalidate();
                            }else if(panelIcon[x][y].getComponent(0) instanceof WallIcon){
                                if(((WallIcon) panelIcon[x][y].getComponent(0)).isEcran()){
                                    selectedIconEcran = new ImageIcon(iconeEcran.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
                                    ((WallIcon)panelIcon[x][y].getComponent(0)).setIcon(selectedIconEcran);
                                }else if(((WallIcon) panelIcon[x][y].getComponent(0)).isEcran()){
                                    selectedIconEcran = new ImageIcon(iconeEcran.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
                                    ((WallIcon)panelIcon[x][y].getComponent(0)).setIcon(selectedIconEcran);
                                }
                            }
                            panelIcon[x][y].revalidate();
                        }
                    }
                }
            }
        });
    }

    /**
     * Génération du plan
     */
    private void generationPlan() {
        Thread t = new Thread(){
            public void run(){
                panelIcon = new JPanel[X][Y];
                JPanel plan = roomView.getPlan();
                size = plan.getWidth() / X;
                plan.removeAll();
                plan.setLayout(new GridLayout(X, Y));
                for(int x = 0 ; x < X ;x++){
                    for(int y = 0 ; y < Y ; y++){
                        if(x == 0 || x == X-1 || y == 0 || y == Y-1){
                            generateWallIcon(plan,panelIcon,x,y);
                        }else{
                            generateSiegeIcon(plan,panelIcon,x,y);
                        }
                    }
                } 
                int largeur = Y / 3 + (Y % 3 == 0 ? 0 : 1);
                int debut = (Y / 2) - (largeur / 2);
                for (int i = debut; i < debut+largeur; i++){
                    WallIcon ecran = new WallIcon();
                    selectedIconEcran = new ImageIcon(iconeEcran.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
                    ecran.setIcon(selectedIconEcran);
                    panelIcon[0][i].removeAll();
                    ecran.setEcran(true);
                    panelIcon[0][i].add(ecran);
                    panelIcon[0][i].revalidate();
                }
            }
        };t.start();
    }

    /**
     * Génaration des icones de murs
     * @param panelIcon2
     * @param x2
     * @param y2
     */
    private void generateWallIcon(JPanel plan, JPanel[][] panelIcon, int x, int y) {
        panelIcon[x][y] = new JPanel();
        panelIcon[x][y].setMinimumSize(new Dimension(size, size));
        panelIcon[x][y].setPreferredSize(new Dimension(size, size));
        panelIcon[x][y].setMaximumSize(new Dimension(size, size));
        panelIcon[x][y].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panelIcon[x][y].setLayout(new BorderLayout());
        WallIcon wall = new WallIcon();
        wall.setBounds(0, 0, 0, 0);
        wall.setAnX(x);
        wall.setAnY(y);
        panelIcon[x][y].addMouseListener(this);
        panelIcon[x][y].setBorder(null);
        panelIcon[x][y].setBackground(Color.GRAY);
        //siege.setIcon(icone);
        panelIcon[x][y].add(wall, BorderLayout.CENTER);
        plan.add(panelIcon[x][y]);
        panelIcon[x][y].revalidate();
    }

    /**
     * Génération des icones de sièges
     * @param panelIcon2
     * @param x2
     * @param y2
     */
    private void generateSiegeIcon(JPanel plan, JPanel[][] panelIcon, int x, int y) {
        panelIcon[x][y] = new JPanel();
        panelIcon[x][y].setMinimumSize(new Dimension(size, size));
        panelIcon[x][y].setPreferredSize(new Dimension(size, size));
        panelIcon[x][y].setMaximumSize(new Dimension(size, size));
        panelIcon[x][y].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panelIcon[x][y].setLayout(new BorderLayout());
        SiegeIcon siege = new SiegeIcon();
        siege.setBounds(0, 0, 0, 0);
        siege.setAnX(x);
        siege.setAnY(y);
        panelIcon[x][y].addMouseListener(this);
        //siege.setIcon(icone);
        panelIcon[x][y].add(siege, BorderLayout.CENTER);
        plan.add(panelIcon[x][y]);
        panelIcon[x][y].revalidate();
    }

    @Override
    public void mouseClicked(MouseEvent event) {}

    @Override
    public void mouseEntered(MouseEvent event) {
        if(((SalleIcon)((JPanel)event.getSource()).getComponent(0)) instanceof SiegeIcon){
            normal = false;
            hand = false;
            allee= false;
            if(ajout){
                ((JPanel)event.getSource()).setBorder(null);
                ImageIcon icon = null;
                if(roomView.getRdbSiegeNormal().isSelected()){
                    normal = true;
                    icon = new ImageIcon(iconeSiege.getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT));
                    ((SiegeIcon)((JPanel)event.getSource()).getComponent(0)).setNormal(normal);
                    ((SiegeIcon)((JPanel)event.getSource()).getComponent(0)).setHand(hand);
                    ((SiegeIcon)((JPanel)event.getSource()).getComponent(0)).setAllee(allee);
                    ((SiegeIcon)((JPanel)event.getSource()).getComponent(0)).setSet(true);
                    ((JLabel) ((JPanel)event.getSource()).getComponent(0)).setIcon(icon);
                    ((JPanel)event.getSource()).setBorder(null);
                    ((JPanel)event.getSource()).getComponent(0).revalidate();
                }else if(roomView.getRdbSiegeHand().isSelected()){
                    hand = true;
                    icon = new ImageIcon(iconeSiegeHand.getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT));
                    ((SiegeIcon)((JPanel)event.getSource()).getComponent(0)).setNormal(normal);
                    ((SiegeIcon)((JPanel)event.getSource()).getComponent(0)).setHand(hand);
                    ((SiegeIcon)((JPanel)event.getSource()).getComponent(0)).setAllee(allee);
                    ((SiegeIcon)((JPanel)event.getSource()).getComponent(0)).setSet(true);
                    ((JLabel) ((JPanel)event.getSource()).getComponent(0)).setIcon(icon);
                    ((JPanel)event.getSource()).setBorder(null);
                    ((JPanel)event.getSource()).getComponent(0).revalidate();
                }else if (roomView.getRdbAllee().isSelected()){
                    allee = true;
                    icon = new ImageIcon(iconeAllee.getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT));
                    ((SiegeIcon)((JPanel)event.getSource()).getComponent(0)).setNormal(normal);
                    ((SiegeIcon)((JPanel)event.getSource()).getComponent(0)).setHand(hand);
                    ((SiegeIcon)((JPanel)event.getSource()).getComponent(0)).setAllee(allee);
                    ((SiegeIcon)((JPanel)event.getSource()).getComponent(0)).setSet(true);
                    ((JLabel) ((JPanel)event.getSource()).getComponent(0)).setIcon(icon);
                    ((JPanel)event.getSource()).setBorder(null);
                    ((JPanel)event.getSource()).getComponent(0).revalidate();
                }
            }else if(suppr){
                ((SiegeIcon)((JPanel)event.getSource()).getComponent(0)).setNormal(normal);
                ((SiegeIcon)((JPanel)event.getSource()).getComponent(0)).setHand(hand);
                ((SiegeIcon)((JPanel)event.getSource()).getComponent(0)).setAllee(allee);
                ((SiegeIcon)((JPanel)event.getSource()).getComponent(0)).setSet(false);
                ((JLabel) ((JPanel)event.getSource()).getComponent(0)).setIcon(null);
                ((JPanel)event.getSource()).setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                ((JPanel)event.getSource()).getComponent(0).revalidate();
            }
        }else if(((SalleIcon)((JPanel)event.getSource()).getComponent(0)) instanceof WallIcon){
            if(ajout){
                ((JPanel)event.getSource()).setBorder(null);
                ImageIcon icon = null;
                if(roomView.getRdbPorte().isSelected()){
                    icon = new ImageIcon(iconePorte.getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT));
                    ((WallIcon)((JPanel)event.getSource()).getComponent(0)).setPorte(true);
                    ((JLabel) ((JPanel)event.getSource()).getComponent(0)).setIcon(icon);
                    ((JPanel)event.getSource()).setBorder(null);
                    ((JPanel)event.getSource()).getComponent(0).revalidate();
                }
            }else if(suppr){
                ((WallIcon)((JPanel)event.getSource()).getComponent(0)).setPorte(false);
                ((JLabel) ((JPanel)event.getSource()).getComponent(0)).setIcon(null);
                ((JPanel)event.getSource()).getComponent(0).revalidate();
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent event) {
        if(((SalleIcon)((JPanel)event.getSource()).getComponent(0)) instanceof SiegeIcon){
            normal = false;
            hand = false;
            allee= false;
            if(SwingUtilities.isLeftMouseButton(event)){
                ajout = true;
                ((JPanel)event.getSource()).setBorder(null);
                ImageIcon icone = null;
                if(roomView.getRdbSiegeNormal().isSelected()){
                    normal = true;
                    icone = new ImageIcon(iconeSiege.getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT));
                }else if(roomView.getRdbSiegeHand().isSelected()){
                    hand = true;
                    icone = new ImageIcon(iconeSiegeHand.getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT));
                }else if (roomView.getRdbAllee().isSelected()){
                    allee = true;
                    icone = new ImageIcon(iconeAllee.getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT));
                }
                ((SiegeIcon)((JPanel)event.getSource()).getComponent(0)).setSet(true);
                ((JLabel) ((JPanel)event.getSource()).getComponent(0)).setIcon(icone);
                ((JPanel)event.getSource()).setBorder(null);
                ((SiegeIcon)((JPanel)event.getSource()).getComponent(0)).setNormal(normal);
                ((SiegeIcon)((JPanel)event.getSource()).getComponent(0)).setHand(hand);
                ((SiegeIcon)((JPanel)event.getSource()).getComponent(0)).setAllee(allee);
                ((JPanel)event.getSource()).getComponent(0).revalidate();

            }else if(SwingUtilities.isRightMouseButton(event)){
                suppr = true;
                ((SiegeIcon)((JPanel)event.getSource()).getComponent(0)).setNormal(normal);
                ((SiegeIcon)((JPanel)event.getSource()).getComponent(0)).setHand(hand);
                ((SiegeIcon)((JPanel)event.getSource()).getComponent(0)).setAllee(allee);
                ((SiegeIcon)((JPanel)event.getSource()).getComponent(0)).setSet(false);
                ((JLabel) ((JPanel)event.getSource()).getComponent(0)).setIcon(null);
                ((JPanel)event.getSource()).setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                ((JPanel)event.getSource()).getComponent(0).revalidate();
            }
        }else if(((SalleIcon)((JPanel)event.getSource()).getComponent(0)) instanceof WallIcon){
            if(SwingUtilities.isLeftMouseButton(event)){
                ajout = true;
                ((JPanel)event.getSource()).setBorder(null);
                ImageIcon icone = null;
                if(roomView.getRdbPorte().isSelected()){
                    icone = new ImageIcon(iconePorte.getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT));
                    ((WallIcon)((JPanel)event.getSource()).getComponent(0)).setPorte(true);
                    ((JLabel) ((JPanel)event.getSource()).getComponent(0)).setIcon(icone);
                    ((JPanel)event.getSource()).setBorder(null);
                    ((JPanel)event.getSource()).getComponent(0).revalidate();
                }
            }else if(SwingUtilities.isRightMouseButton(event)){
                suppr = true;
                ((WallIcon)((JPanel)event.getSource()).getComponent(0)).setPorte(false);
                ((JLabel) ((JPanel)event.getSource()).getComponent(0)).setIcon(null);
                ((JPanel)event.getSource()).setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                ((JPanel)event.getSource()).getComponent(0).revalidate();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        ajout = false;
        suppr = false;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == roomView.getBtnGenerer()){
            if(!roomView.getTxtColonnes().getText().equals("")
                    && !roomView.getTxtRangees().getText().equals("")){ 
                int x = Integer.parseInt(roomView.getTxtRangees().getText());
                int y =  Integer.parseInt(roomView.getTxtColonnes().getText());
                if(x > 0 && y > 0){
                    Y = x + 2;
                    X = y + 2;
                    generationPlan();
                }else{
                    JOptionPane.showInternalMessageDialog(roomView, "Les dimensions de la salle ne peuvent être nulle", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                }

            }       
        }else if(event.getSource() == roomView.getBtnExporter()){
            genererXML();
            exportXMLFile();
        }else if(event.getSource() == roomView.getBtnEnregistrer()){
            genererXML();
            enregistrerPlan();
        }else if(event.getSource() == roomView.getBtnAnnuler()){
            GestionCinemaView view = new GestionCinemaView();
            GestionCinemaController controller = new GestionCinemaController(frame, view, model);
            view.setController(controller);
            frame.setContentPane(view);
            frame.pack();
        }else if(event.getSource() == roomView.getBtnImporter()){
            JFileChooser browser = new JFileChooser();
            browser.showOpenDialog(null);
            File file = browser.getSelectedFile();
            String filename = file.getAbsolutePath();
            if(validateXMLSchema("plan.xsd",filename)){
                try {
                    FileInputStream fis = new FileInputStream(file);
                    byte[] data = new byte[(int) file.length()];
                    fis.read(data);
                    fis.close();
                    String XMLString = new String(data, "UTF-8");
                    int[] xy = new int[2];
                    xy = getXYFromXMLFile(file);
                    salle.setPlanDeSalle(XMLString);
                    salle.setPlacesDisponibles(nbSiege);
                    salle.setNbRangees(xy[0]-2);
                    salle.setNbSieges(xy[1]-2);
                    model.updateEntity(salle);
                    JOptionPane.showMessageDialog(frame, "Le plan a bien été importé", "Import", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    e.printStackTrace();
                }              
            }else{
                JOptionPane.showMessageDialog(frame, "Le fichier XML comporte des erreurs", "Erreur import", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    /** 
     * Récupération de la largeur et longueur de la salle 
     * @param XMLFile
     * @return
     */
    public int[] getXYFromXMLFile(File XMLFile){
        int[] xy = new int[2];
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(XMLFile);
            Element root = doc.getDocumentElement();
            xy[0] = Integer.parseInt(root.getAttribute("colonnes"));
            xy[1] = Integer.parseInt(root.getAttribute("lignes"));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return xy;
    }
    /** Validation du plan au format XML avant l'enregistrement en BD */
    public boolean validateXMLSchema(String xsdPath, String xmlPath){
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            File planImport = new File(xmlPath);
            validator.validate(new StreamSource(planImport));
        } catch (IOException | SAXException e) {
            return false;
        }
        return true;
    }

    /**
     * Sauvegarde du plan en BD
     */
    private void enregistrerPlan() {
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(XMLsource, new StreamResult(sw));
            XMLText = sw.toString();
            Salle salle = frame.getSalle();
            salle.setPlanDeSalle(XMLText);
            salle.setPlacesDisponibles(nbSiege);
            salle.setNbRangees(X-2);
            salle.setNbSieges(Y-2);
            model.updateEntity(salle);
            JOptionPane.showMessageDialog(frame, "Plan enregistré !", "Enregistrement", JOptionPane.INFORMATION_MESSAGE);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Génération d'un plan XML
     */
    private void genererXML() {
        try {
            builder = documentFactory.newDocumentBuilder();
            Document document= builder.newDocument();
            final Element racine = document.createElement("plan");
            Comment commentaire = document.createComment("Representation d'un plan de salle au format XML");
            racine.setAttribute("lignes", Y+"");
            racine.setAttribute("colonnes", X+"");
            document.appendChild(racine);
            document.appendChild(commentaire);
            for(int x = 0 ;panelIcon!= null && x < panelIcon.length; x++){
                for(int y = 0; y < panelIcon[x].length; y++){
                    if(panelIcon[x][y].getComponent(0) instanceof WallIcon) {
                        if(((WallIcon) panelIcon[x][y].getComponent(0)).isEcran()){
                            Element element = document.createElement("element");
                            element.setAttribute("X", x+"");
                            element.setAttribute("Y", y+"");
                            element.setAttribute("type", "ecran");  
                            racine.appendChild(element);
                        }else if(((WallIcon) panelIcon[x][y].getComponent(0)).isPorte()){
                            Element element = document.createElement("element");
                            element.setAttribute("X", x+"");
                            element.setAttribute("Y", y+"");
                            element.setAttribute("type", "porte");
                            racine.appendChild(element);
                        }         
                    }else if(panelIcon[x][y].getComponent(0) instanceof SiegeIcon){
                        if(((SiegeIcon)panelIcon[x][y].getComponent(0)).isSet()){
                            if(((SiegeIcon)panelIcon[x][y].getComponent(0)).isAllee()){
                                Element element = document.createElement("element");
                                element.setAttribute("X", x+"");
                                element.setAttribute("Y", y+"");
                                element.setAttribute("type", "allee");
                                racine.appendChild(element);
                            }else if(((SiegeIcon)panelIcon[x][y].getComponent(0)).isHand()){
                                nbSiege++;
                                Element element = document.createElement("element");
                                element.setAttribute("X", x+"");
                                element.setAttribute("Y", y+"");
                                element.setAttribute("type", "hand");
                                racine.appendChild(element);
                            }else if(((SiegeIcon)panelIcon[x][y].getComponent(0)).isNormal()){
                                nbSiege++;
                                Element element = document.createElement("element");
                                element.setAttribute("X", x+"");
                                element.setAttribute("Y", y+"");
                                element.setAttribute("type", "normal");
                                racine.appendChild(element);
                            }
                        }

                    }
                }
            }
            XMLsource = new DOMSource(document);
            System.out.println(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Export du plan au format XML
     */
    public void exportXMLFile(){
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            StreamResult xmlFile = new StreamResult(new File("plan.xml"));
            transformer.transform(XMLsource, xmlFile);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return le roomView
     */
    public RoomCreatorView getRoomView() {
        return roomView;
    }

    /**
     * @return le frame
     */
    public JFrame getFrame() {
        return frame;
    }

    @Override
    public void keyPressed(KeyEvent event) {}
    @Override
    public void keyReleased(KeyEvent event) {  }

    @Override
    public void keyTyped(KeyEvent event) {
        char c = event.getKeyChar();
        if(!Character.isDigit(c) || ((JTextField) event.getSource()).getText().length() > 1){
            event.consume();
        }
    }
}
