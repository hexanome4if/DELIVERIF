/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.model.request;

import java.util.Date;

/**
 *
 * @author zakaria
 */
public class Depot {
    private Integer address;
    private Date departureTime;

    public Depot() {
    }

    public Depot(Integer address, Date departureTime) {
        this.address = address;
        this.departureTime = departureTime;
    }

    public Integer getAddress() {
        return address;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setAddress(Integer address) {
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
