/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.model.request;

/**
 *
 * @author zakaria
 */
public class Request {
    private Integer pickupAddress;
    private Integer deliveryAddress;
    private Integer pickupDuration;
    private Integer deliveryDuration;

    public Request() {
    }

    public Request(Integer pickupAddress, Integer deliveryAddress, Integer pickupDuration, Integer deliveryDuration) {
        this.pickupAddress = pickupAddress;
        this.deliveryAddress = deliveryAddress;
        this.pickupDuration = pickupDuration;
        this.deliveryDuration = deliveryDuration;
    }

    public Integer getPickupAddress() {
        return pickupAddress;
    }

    public Integer getDeliveryAddress() {
        return deliveryAddress;
    }

    public Integer getPickupDuration() {
        return pickupDuration;
    }

    public Integer getDeliveryDuration() {
        return deliveryDuration;
    }

    public void setPickupAddress(Integer pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public void setDeliveryAddress(Integer deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setPickupDuration(Integer pickupDuration) {
        this.pickupDuration = pickupDuration;
    }

    public void setDeliveryDuration(Integer deliveryDuration) {
        this.deliveryDuration = deliveryDuration;
    }

    @Override
    public String toString() {
        return "Request{" + "pickupAddress=" + pickupAddress + ", deliveryAddress=" + deliveryAddress + ", pickupDuration=" + pickupDuration + ", deliveryDuration=" + deliveryDuration + '}';
    }
    
    
}
