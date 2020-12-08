/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.model.map;

/**
 *
 * @author zakaria
 */
public class Intersection {

    private Long id;
    private float latitude;
    private float longitude;

    /**
     * Create an empty intersection
     */
    public Intersection() {
    }

    /**
     * Create an intersection
     *
     * @param id
     */
    public Intersection(Long id) {
        this.id = id;
    }

    public Intersection(Long id, float latitude, float longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Intersection{(" + id + ") latitude=" + latitude + ", longitude=" + longitude + "}\n";
    }

}
