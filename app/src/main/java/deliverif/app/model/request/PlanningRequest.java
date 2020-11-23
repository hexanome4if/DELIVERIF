/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.model.request;

import java.util.List;

/**
 *
 * @author zakaria
 */
public class PlanningRequest {
    private Depot depot;
    List<Request> requests;

    public PlanningRequest() {
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

    @Override
    public String toString() {
        return "PlanningRequest{depot=" + depot + ", requests=" + requests + '}';
    }
    
}
