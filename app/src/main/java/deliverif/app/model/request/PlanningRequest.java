/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.model.request;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zakaria
 */
public class PlanningRequest {
    private Depot depot;
    List<Request> requests;

    public PlanningRequest() {
        this.requests = new ArrayList<Request>();
    }

    public PlanningRequest(Depot depot, List<Request> requests) {
        this.depot = depot;
        this.requests = requests;
    }

    public Depot getDepot() {
        return depot;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setDepot(Depot depot) {
        this.depot = depot;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }
    
    public void addRequest(Request r) {
        this.requests.add(r);
    }
    
    public void removeRequest(Request r){
        this.requests.remove(r);
    }
    @Override
    public String toString() {
        return "PlanningRequest{depot=" + depot + ", requests=" + requests + '}';
    }
    
}
