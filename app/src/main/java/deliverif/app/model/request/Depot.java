/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.model.request;

import deliverif.app.model.map.Intersection;
import java.util.Date;

/**
 *
 * @author zakaria
 */
public class Depot {

    private Intersection address;
    private Date departureTime;

    /**
     * Create an empty depot
     */
    public Depot() {
    }

    /**
     * Create a new depot
     *
     * @param address address of the depot represented by an intersection
     * @param departureTime departure time from the depot
     */
    public Depot(Intersection address, Date departureTime) {
        this.address = address;
        this.departureTime = departureTime;
    }

    /**
     * Get depot address
     *
     * @return depot address as an intersection
     */
    public Intersection getAddress() {
        return address;
    }

    /**
     * Get depot departure time
     *
     * @return depot depoarture time as a date object
     */
    public Date getDepartureTime() {
        return departureTime;
    }

    /**
     * Set depot address
     *
     * @param address depot address as an intersection
     */
    public void setAddress(Intersection address) {
        this.address = address;
    }

    /**
     * Set depot departure time
     *
     * @param departureTime depot departure time as a date object
     */
    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    @Override
    public String toString() {
        return "Depot{" + "address=" + address + ", departureTime=" + departureTime + '}';
    }

}
