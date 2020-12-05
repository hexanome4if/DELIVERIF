/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.model.request;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author zakaria
 */
public class Observable {
    private Collection<Observer> obs;
    public Observable () {
        obs = new ArrayList<Observer>();
    }
    public void addObserver(Observer o) {
        if(!obs.contains(o)) obs.add(o);
    }
    public void notifiyObservers(Object arg){
        for (Observer o : obs)
            o.update(this, arg);
    }
}
