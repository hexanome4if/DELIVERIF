/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller;


import deliverif.app.model.map.*;
import deliverif.app.model.request.*;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.DocumentBuilder;  
import org.w3c.dom.Document;  
import org.w3c.dom.NodeList;  
import org.w3c.dom.Node;  
import org.w3c.dom.Element;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.text.SimpleDateFormat;  
import java.util.Date;  

/**
 *
 * @author zakaria
 */
public class XmlReader {
    
    private Map map;
    
    /**
     * XmlReader constructor
     */
    public XmlReader(){
        this.map = null;
    }

    /**
     * Getter
     * @return Map
     */
    public Map getMap() {
        return map;
    }
    
    /**
     * Read a xml file to create a instance of the Map class
     * @param filename path of a xml file
     * @return boolean
     */
    public boolean readMap(String filename){

        List<Segment> segments = new ArrayList<>(); 
        HashMap<Long, Intersection> intersections = new HashMap<>();
       
        try {  
            //creating a constructor of file class and parsing an XML file  
            File file = new File(filename);

            //an instance of factory that gives a document builder  
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  

            //an instance of builder to parse the specified xml file  
            DocumentBuilder db = dbf.newDocumentBuilder();  

            Document doc = db.parse(file);  
            doc.getDocumentElement().normalize();  
            if(!doc.getDocumentElement().getNodeName().equals("map")) return false;
            
            NodeList nodeList = doc.getElementsByTagName("intersection");

            for (int itr = 0; itr < nodeList.getLength(); itr++) { 

                Node node = nodeList.item(itr);  

                if (node.getNodeType() == Node.ELEMENT_NODE){  
                    Element eElement = (Element) node;  
                    Long id = Long.parseLong(eElement.getAttribute("id"));
                    float lat = Float.parseFloat(eElement.getAttribute("latitude"));
                    float lon = Float.parseFloat(eElement.getAttribute("longitude"));

                    Intersection inter = new Intersection(id, lat, lon);
                    intersections.put(id, inter);
                }
            }

            nodeList = doc.getElementsByTagName("segment");

            for (int itr = 0; itr < nodeList.getLength(); itr++) { 

                Node node = nodeList.item(itr);  
                if (node.getNodeType() == Node.ELEMENT_NODE){  

                    Element eElement = (Element) node;
                    Long origin = Long.parseLong(eElement.getAttribute("origin"));  
                    Long dest = Long.parseLong(eElement.getAttribute("destination"));
                    float len = Float.parseFloat(eElement.getAttribute("length"));
                    String name = eElement.getAttribute("name");

                    Segment  seg = new Segment(intersections.get(origin),intersections.get(dest), len, name);
                    segments.add(seg);

                }
            }	
            this.map = new Map(intersections, segments);
            
        }catch (Exception e){  
            //e.printStackTrace(); 
            System.out.println("/!\\ Errors on map file");
            return false;
        }
        
        return this.map != null;
   }

    /**
     * Read a xml file to create an instance of PlanningRequest class
     * @param filename path of a xml file
     * @return
     */
    public PlanningRequest readRequest(String filename){
        
        PlanningRequest pr = null;
        List<Request> requests = new ArrayList<Request>();
        Depot depot = null;
        
        
        try {  
            //creating a constructor of file class and parsing an XML file (attention de le changer //) 
            File file = new File(filename);  

            //an instance of factory that gives a document builder  
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  

            //an instance of builder to parse the specified xml file  
            DocumentBuilder db = dbf.newDocumentBuilder();  

            Document doc = db.parse(file);  
            doc.getDocumentElement().normalize();  
            
            if(!doc.getDocumentElement().getNodeName().equals("planningRequest")) return null;
            
            NodeList nodeList = doc.getElementsByTagName("depot");  
            SimpleDateFormat hourFormat =new SimpleDateFormat("HH:mm:ss"); 
            
            // nodeList is not iterable, so we are using for loop  
            for (int itr = 0; itr < nodeList.getLength(); itr++) { 

                    Node node = nodeList.item(itr);  

                    if (node.getNodeType() == Node.ELEMENT_NODE){  
                        Element eElement = (Element) node;  
                        Long adrId = Long.parseLong(eElement.getAttribute("address"));
                        Intersection adr = map.getIntersections().get(adrId);
                        
                        if(adr == null){
                            break;
                        }
                        Date departure = hourFormat.parse(eElement.getAttribute("departureTime"));
                        
                        depot = new Depot (adr, departure); 
                    }  
            } 
            
            nodeList = doc.getElementsByTagName("request");  
            
            for (int itr = 0; itr < nodeList.getLength(); itr++) { 

                Node node = nodeList.item(itr);  

                if (node.getNodeType() == Node.ELEMENT_NODE){  
                    Element eElement = (Element) node;  
                    Long pkId = Long.parseLong(eElement.getAttribute("pickupAddress"));
                    Long dlId;
                    
                    if(!"".equals(eElement.getAttribute("deliveryAddress")))
                        dlId= Long.parseLong(eElement.getAttribute("deliveryAddress"));
                    else
                        dlId= Long.parseLong(eElement.getAttribute("adresseLivraison"));
                    
                    Integer pkD = Integer.parseInt(eElement.getAttribute("pickupDuration"));
                    Integer dlD = Integer.parseInt(eElement.getAttribute("deliveryDuration"));
                    
                    Intersection pkAdr = map.getIntersections().get(pkId);
                    Intersection dlAdr = map.getIntersections().get(dlId);
                    
                    if(pkAdr == null || dlAdr == null){
                        requests.clear();
                        break;
                    }
                    Request request = new Request(pkAdr, dlAdr, pkD, dlD);
                    requests.add(request); 
                
                }
            }
            pr = new PlanningRequest (depot, requests);
            
        }catch (Exception e){  
            //e.printStackTrace();  
            System.out.println("/!\\ Errors on request file");
        }  
        
        return pr;
    }
}
