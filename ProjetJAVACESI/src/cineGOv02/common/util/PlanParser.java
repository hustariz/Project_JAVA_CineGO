/*
 * PlanParser.java                                11 mars 2016
 * CESI RILA 2015/2017
 */
package cineGOv02.common.util;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Classe permettant de parser un plan de salle, et de piocher des places libres 
 * @author Remi
 *
 */
public class PlanParser {
    /** Plan de la salle */
    private String plan;
    /** Liste des places sous forme de tableau d'Integer (x,y) */
    private ArrayList<int[]> placesInt;
    /** Liste des places de la salle sous forme textuelle */
    private ArrayList<String> placesString;
    /** Parser de doucment XML */
    private Document document;

    /**
     * Classe permettant de récupérer les places occupée d'une séance et d'en déduire les place libre
     * Permet également de parser un plan de salle en XML pour récupérer la liste des places.
     * @param plan
     * @throws Exception 
     */
    public PlanParser(String plan){
        this.plan = plan;
        try {
            document = loadXMLFromString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        placesInt = new ArrayList<int[]>();
        placesString = new ArrayList<String>();
        feedSeatLists();
    }

    /**
     * Parse le plan de la salle et récupère la liste des sièges
     */
    private void feedSeatLists() {
        NodeList nodes = document.getElementsByTagName("element");
        for (int i = 0; i < nodes.getLength(); i++) {
            String type = ((Element)nodes.item(i)).getAttribute("type");
            if(type.equals("normal") || type.equals("hand")){
                int x = Integer.parseInt(((Element)nodes.item(i)).getAttribute("X"));
                int y = Integer.parseInt(((Element)nodes.item(i)).getAttribute("Y"));
                int[] place = new int[2];
                place[0] = x;
                place[1] = y;
                placesInt.add(place);
                placesString.add(x+","+y+":");
            }
        }    
    }

    /**
     * Charge le plan de la salle et revoi un document 
     * @return le Document à parser
     * @throws Exception
     */
    public Document loadXMLFromString() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(plan));
        return builder.parse(is);
    }

    /**
     * @return le plan
     */
    public String getPlan() {
        return plan;
    }

    /**
     * @param plan le plan to set
     */
    public void setPlan(String plan) {
        this.plan = plan;
    }
    /** 
     * Récupère la liste des sièges vide sous frome 'un string et la parse. 
     * @param occupees 
     * @param nbPlaces 
     * @return une liste de place vide sous forme de String
     */
    public ArrayList<String> getFreePlace(String occupees, int nbPlaces){  
        ArrayList<String> placeResa = new ArrayList<String>();
        boolean stop = false;
        for (int i = 0; i < placesString.size() && !stop ; i++) {
            if(occupees.length() > 0){
                if(!occupees.contains(placesString.get(i)))
                    placeResa.add(placesString.get(i));
            }else{
                placeResa.add(placesString.get(i));
            }
            stop = placeResa.size() == nbPlaces ? true : false;
        }
        return placeResa;
    }
}
