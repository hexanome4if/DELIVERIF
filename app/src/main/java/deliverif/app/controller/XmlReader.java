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
    
    public XmlReader(){
        this.map = null;
    }

    public Map getMap() {
        return map;
    }
    
    public boolean readMap(String filename){

        List<Segment> segments = new ArrayList<Segment>(); 
        HashMap<Long, Intersection> intersections = new HashMap<Long, Intersection>();
       
        try {  
            //creating a constructor of file class and parsing an XML file  
            File file = new File(filename);

            //an instance of factory that gives a document builder  
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  

            //an instance of builder to parse the specified xml file  
            DocumentBuilder db = dbf.newDocumentBuilder();  

            Document doc = db.parse(file);  
            doc.getDocumentElement().normalize();  

            //System.out.println("Root element: " + doc.getDocumentElement().getNodeName());  
            NodeList nodeList = doc.getElementsByTagName("intersection");  
            // nodeList is not iterable, so we are using for loop  



            for (int itr = 0; itr < nodeList.getLength(); itr++) { 

                Node node = nodeList.item(itr);  
                //System.out.println("Node Name :" + node.getNodeName());

                if (node.getNodeType() == Node.ELEMENT_NODE){  
                    Element eElement = (Element) node;  
                    Long id = Long.parseLong(eElement.getAttribute("id"));
                    float lat = Float.parseFloat(eElement.getAttribute("latitude"));
                    float lon = Float.parseFloat(eElement.getAttribute("longitude"));

                    //System.out.println("Request id : " + eElement.getAttribute("id")); 

                    Intersection inter = new Intersection(lat, lon);
                    intersections.put(id, inter);

                }
            }

            nodeList = doc.getElementsByTagName("segment");

            for (int itr = 0; itr < nodeList.getLength(); itr++) { 

                Node node = nodeList.item(itr);  
                //System.out.println("\nNode Name :" + node.getNodeName());

                if (node.getNodeType() == Node.ELEMENT_NODE){  

                    Element eElement = (Element) node;
                    Long origin = Long.parseLong(eElement.getAttribute("origin"));  
                    Long dest = Long.parseLong(eElement.getAttribute("destination"));
                    float len = Float.parseFloat(eElement.getAttribute("length"));
                    String name = eElement.getAttribute("name");

                    //System.out.println("Request addres : " + eElement.getAttribute("name")); 

                    //destination length name origin
                    Segment  seg = new Segment(intersections.get(origin),intersections.get(dest), len, name);
                    segments.add(seg);

                }
            }	

        }catch (Exception e){  
            e.printStackTrace();  
        }
        
        this.map = new Map(intersections, segments);
        
        return this.map != null;
   }

    public PlanningRequest readRequest(String filename){
        
        PlanningRequest pr;
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

            NodeList nodeList = doc.getElementsByTagName("depot");  
            SimpleDateFormat hourFormat =new SimpleDateFormat("HH:mm:ss"); 
            
            // nodeList is not iterable, so we are using for loop  
            for (int itr = 0; itr < nodeList.getLength(); itr++) { 

                    Node node = nodeList.item(itr);  

                    if (node.getNodeType() == Node.ELEMENT_NODE){  
                        Element eElement = (Element) node;  
                        //System.out.println("address : " + eElement.getAttribute("address"));
                        //System.out.println("departureTime : " + eElement.getAttribute("departureTime"));
                        
                        Long adrId = Long.parseLong(eElement.getAttribute("address"));
                        Intersection adr = map.getIntersections().get(adrId);
                        
                        Date departure = hourFormat.parse(eElement.getAttribute("departureTime"));
                        
                        depot = new Depot (adr, departure); 
                    }  
            } 
            

            nodeList = doc.getElementsByTagName("request");  
            // nodeList is not iterable, so we are using for loop  
            for (int itr = 0; itr < nodeList.getLength(); itr++) { 

                Node node = nodeList.item(itr);  

                if (node.getNodeType() == Node.ELEMENT_NODE){  
                    Element eElement = (Element) node;  
                    /*System.out.println("pickupAddress :" + eElement.getAttribute("pickupAddress")); 
                    System.out.println("deliveryAddress :" + eElement.getAttribute("deliveryAddress"));
                    System.out.println("pickupDuration :" + eElement.getAttribute("pickupDuration")); 
                    System.out.println("deliveryDuration :" + eElement.getAttribute("deliveryDuration"));*/
                    
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
                            
                    Request request = new Request(pkAdr, dlAdr, pkD, dlD);
                    requests.add(request); 
                
                }
            }

        }catch (Exception e){  
            e.printStackTrace();  
        }  
        
        pr = new PlanningRequest (depot, requests);
        return pr;
    }
}
