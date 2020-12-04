/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller;

import deliverif.app.model.graph.Tour;
import deliverif.app.model.request.Request;

/**
 *
 * @author zakaria
 */
public class RemoveRequest implements Command {
    GraphProcessor gp;
    Tour oldTour;
    Tour tour;
    Request request;
    
    public RemoveRequest(GraphProcessor gp, Tour t, Request r) {
        this.gp = gp;
        tour = t;
        oldTour = new Tour(t);
        request = r;
    }
    

    @Override
    public void doCommand() {
        gp.removeRequestFromTour(tour, request);
        tour.notifiyObservers(null);
    }

    @Override
    public void undoCommand() {
        tour.copyTour(oldTour);
        tour.notifiyObservers(null);
    }
    
}
