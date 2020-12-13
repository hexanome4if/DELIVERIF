/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller;

import deliverif.app.model.map.Intersection;
import deliverif.app.model.request.Depot;
import deliverif.app.model.request.PlanningRequest;
import deliverif.app.model.request.Request;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author lea
 */
public class XmlReaderTest {
    
    public XmlReaderTest() {
    }
    //smallMap.xml 
    
    /**
     * Test of readMap method, of class XmlReader.
     * @throws java.text.ParseException
     */
    @org.junit.jupiter.api.Test
    public void testReadMap() throws ParseException {
        System.out.println("readRequest");

        XmlReader instance = new XmlReader();
        instance.readMap("./src/main/resources/deliverif/app/fichiersXML2020/wrongMediumMap.xml");
        
        Map expResult = null;
        Map result = (Map) instance.getMap();
        
        assertEquals(expResult, result);   
    }
    
    
    /**
     * Test of readRequest method, of class XmlReader.
     * @throws java.text.ParseException
     */
    @org.junit.jupiter.api.Test
    public void testReadRequest() throws ParseException {
        System.out.println("readRequest");
        String filename = "./src/main/resources/deliverif/app/fichiersXML2020/requestsSmall1.xml";
        
        XmlReader instance = new XmlReader();
        instance.readMap("./src/main/resources/deliverif/app/fichiersXML2020/smallMap.xml");
        //Depot
        Long id = Long.parseLong("342873658");  
        
        Intersection adr = instance.getMap().getIntersections().get(id);
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        Date time = hourFormat.parse("8:0:0");
        Depot depot = new Depot(adr, time);
        
        //Liste de requêtes
        List<Request> requests = new ArrayList<>();
        
        id = Long.parseLong("208769039");
        Intersection pkAdr = instance.getMap().getIntersections().get(id);
        
        id = Long.parseLong("25173820");
        Intersection dlAdr = instance.getMap().getIntersections().get(id);
        
        Integer pkD = Integer.parseInt("180");
        Integer dlD = Integer.parseInt("240");
        
        Request req = new Request(pkAdr, dlAdr, pkD, dlD);
        requests.add(req);

        PlanningRequest expResult = new PlanningRequest(depot, requests);
        PlanningRequest result = instance.readRequest(filename);
        
        System.out.println("-----------------exp");
        System.out.println(expResult);
        System.out.println("-----------------res");
        System.out.println(result);
        System.out.println("--------------------");

        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
