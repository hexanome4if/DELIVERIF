/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.model.request;

import deliverif.app.model.map.Intersection;

/**
 * An object representing a request of a client
 * @author zakaria
 */
public class Request {
    /**
     * Pickup address point
     */
    private Intersection pickupAddress;
    /**
     * Delivery address point
     */
    private Intersection deliveryAddress;
    /**
     * Time needed to pickup
     */
    private Integer pickupDuration;
    /**
     * Time needed to delivery
     */
    private Integer deliveryDuration;

    /**
     * Create an empty request
     */
    public Request() {
    }

    /**
     * Create a new request
     *
     * @param pickupAddress pickup address of the request as an intersection
     * @param deliveryAddress delivery address of the request as an intersection
     * @param pickupDuration duration in seconds of the pickup
     * @param deliveryDuration duration in seconds of the delivery
     */
    public Request(Intersection pickupAddress, Intersection deliveryAddress, Integer pickupDuration, Integer deliveryDuration) {
        this.pickupAddress = pickupAddress;
        this.deliveryAddress = deliveryAddress;
        this.pickupDuration = pickupDuration;
        this.deliveryDuration = deliveryDuration;
    }

    /**
     * Get pickup address as an intersection
     *
     * @return intersection representing pickup address
     */
    public Intersection getPickupAddress() {
        return pickupAddress;
    }

    /**
     * Get delivery address as an intersection
     *
     * @return intersection representing delivery address
     */
    public Intersection getDeliveryAddress() {
        return deliveryAddress;
    }

    /**
     * Get pickup duration in seconds
     *
     * @return pickup duration in seconds
     */
    public Integer getPickupDuration() {
        return pickupDuration;
    }

    /**
     * Get delivery duration in seconds
     *
     * @return delivery duration in seconds
     */
    public Integer getDeliveryDuration() {
        return deliveryDuration;
    }

    /**
     * Set pickup address for that request
     *
     * @param pickupAddress intersection representing the pickup address
     */
    public void setPickupAddress(Intersection pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    /**
     * Set delivery address for that request
     *
     * @param deliveryAddress intersection representing the delivery address
     */
    public void setDeliveryAddress(Intersection deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    /**
     * Set pickup duration in seconds for that request
     *
     * @param pickupDuration pickup duration in seconds
     */
    public void setPickupDuration(Integer pickupDuration) {
        this.pickupDuration = pickupDuration;
    }

    /**
     * Set delivery duration in seconds for that request
     *
     * @param deliveryDuration delivery duration in seconds
     */
    public void setDeliveryDuration(Integer deliveryDuration) {
        this.deliveryDuration = deliveryDuration;
    }

    @Override
    public String toString() {
        return "Request{" + "pickupAddress=" + pickupAddress + ", deliveryAddress=" + deliveryAddress + ", pickupDuration=" + pickupDuration + ", deliveryDuration=" + deliveryDuration + '}';
    }

}
