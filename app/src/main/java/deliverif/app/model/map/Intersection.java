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
    
    private float latitude;
    private float longitude;

    public Intersection() {
    }

    public Intersection(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Intersection{latitude=" + latitude + ", longitude=" + longitude + "}\n";
    }
    
    
}
