/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller;


import deliverif.app.model.map.*;
import deliverif.app.model.request.Request;

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
/**
 *
 * @author zakaria
 */
public class XmlReader {
    
    public Map readMap(String filename){
        
        Map map;

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

        map = new Map(intersections, segments);
        return map;
   }
   
   public Request readRequest(File file){
       Request request = new Request();
       return request;
   }
}
