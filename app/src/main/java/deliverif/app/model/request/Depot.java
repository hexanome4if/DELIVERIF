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

    public Depot() {
    }

    public Depot(Intersection address, Date departureTime) {
        this.address = address;
        this.departureTime = departureTime;
    }

    public Intersection getAddress() {
        return address;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setAddress(Intersection address) {
        this.address = address;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    @Override
    public String toString() {
        return "Depot{" + "address=" + address + ", departureTime=" + departureTime + '}';
    }
    
}
