/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.Observer;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author zakaria
 */
public abstract class Observable {

    private Collection<Observer> obs;

    /**
     * Create an observable
     */
    public Observable() {
        obs = new ArrayList<>();
    }

    /**
     * Add an observer to be notified on any change
     *
     * @param o the observer to add
     */
    public void addObserver(Observer o) {
        if (!obs.contains(o)) {
            obs.add(o);
        }
    }

    /**
     * Notify every observers of the object that a change occured on the object
     *
     * @param arg data about the update
     */
    public void notifiyObservers(Object arg) {
        for (Observer o : obs) {
            o.update(this, arg);
        }
    }
}
