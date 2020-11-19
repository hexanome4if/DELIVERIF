/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.model.map;

/**
 *
 * @author zakaria
 */
public class Intersection {
    private Integer id;
    private float latitude;
    private float longitude;

    public Intersection() {
    }

    public Intersection(Integer id, float latitude, float longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getId() {
        return id;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setId(Integer id) {
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
        return "Intersection{" + "id=" + id + ", latitude=" + latitude + ", longitude=" + longitude + '}';
    }
    
    
}
