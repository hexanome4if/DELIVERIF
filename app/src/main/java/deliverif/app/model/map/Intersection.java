/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.model.map;

/**
 * Represents an intersection on the map
 * @author zakaria
 */
public class Intersection {
    /**
     * Intersection unique id
     */
    private Long id;
    /**
     * Latitude position
     */
    private float latitude;
    /**
     * Longitude position
     */
    private float longitude;

    /**
     * Create an empty intersection
     */
    public Intersection() {
    }

    /**
     * Create an intersection with only an id
     *
     * @param id intersection id
     */
    public Intersection(Long id) {
        this.id = id;
    }

    /**
     * Create an intersection
     *
     * @param id intersection id
     * @param latitude intersection latitude
     * @param longitude intersection longitude
     */
    public Intersection(Long id, float latitude, float longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Get intersection id
     *
     * @return intersection id
     */
    public Long getId() {
        return id;
    }

    /**
     * Get intersection latitude
     *
     * @return intersection latitude
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * Get intersection longitude
     *
     * @return intersection longitude
     */
    public float getLongitude() {
        return longitude;
    }

    /**
     * Set intersection id
     *
     * @param id new intersection id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Set intersection latitude
     *
     * @param latitude new intersection latitude
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    /**
     * Set intersection longitude
     *
     * @param longitude new intersection longitude
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Intersection{(" + id + ") latitude=" + latitude + ", longitude=" + longitude + "}\n";
    }

}
