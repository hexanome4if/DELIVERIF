/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller;


import deliverif.app.model.map.Map;
import deliverif.app.model.request.Request;
import java.io.File;

/**
 *
 * @author zakaria
 */
public class XmlReader {
   public Map readMap(File file){
      Map map = new Map();
      return map;
   }
   
   public Request readRequest(File file){
       Request request = new Request();
       return request;
   }
}
