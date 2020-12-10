/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.model.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author zakaria
 */
public class PlanningRequest {

    private Depot depot;
    private List<Request> requests;

    /**
     * Create an empty planning request
     */
    public PlanningRequest() {
        this.requests = new ArrayList<>();
    }

    /**
     * Create a new planning request
     *
     * @param depot the planning depot
     * @param requests planning's requests
     */
    public PlanningRequest(Depot depot, List<Request> requests) {
        this.depot = depot;
        this.requests = requests;
    }

    /**
     * Get planning depot
     *
     * @return planning depot
     */
    public Depot getDepot() {
        return depot;
    }

    /**
     * Get the requests composing the planning
     *
     * @return planning's requests
     */
    public List<Request> getRequests() {
        return requests;
    }

    /**
     * Set the planning depot
     *
     * @param depot new planning depot
     */
    public void setDepot(Depot depot) {
        this.depot = depot;
    }

    /**
     * Set the requests composing the planning
     *
     * @param requests planning's requests
     */
    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    /**
     * Add a request to the planning
     *
     * @param r the request to add
     */
    public void addRequest(Request r) {
        this.requests.add(r);
    }

    /**
     * Remove a request from the planning
     *
     * @param r the request to remove
     */
    public void removeRequest(Request r) {
        this.requests.remove(r);
    }

    public Request findRequestByAddress(Long idItsc){
        for(Request rq : requests){
            if(Objects.equals(rq.getDeliveryAddress().getId(), idItsc) 
            || Objects.equals(rq.getDeliveryAddress().getId(), idItsc)){
                return rq;
            }
        }
        return null;
    }
    
    public int getDuration(Long idItsc){
        for(Request rq : requests){
            if(Objects.equals(rq.getPickupAddress().getId(), idItsc)){
                return rq.getPickupDuration();
            } else if(Objects.equals(rq.getDeliveryAddress().getId(), idItsc)){
                return rq.getDeliveryDuration();
            }
        }
        return -1;
    }

    /**
     * Get intersection type from requests
     *
     * @param id intersection id
     * @return intersection type can be either "Delivery" or "Pickup"
     */
    public String researchTypeIntersection(Long id) {
        
        String res = "";
        for (Request r : requests) {
            if (Objects.equals(r.getDeliveryAddress().getId(), id)) {
                res = "Delivery";
                break;
            }

            if (Objects.equals(r.getPickupAddress().getId(), id)) {
                res = "Pickup";
                break;
            }
        }
        return res;
    }

    @Override
    public String toString() {
        return "PlanningRequest{depot=" + depot + ", requests=" + requests + '}';
    }

    public boolean isEmpty() {
        return requests.isEmpty() || depot == null;
    }

}
