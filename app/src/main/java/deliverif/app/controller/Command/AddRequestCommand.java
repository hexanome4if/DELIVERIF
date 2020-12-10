/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.Command;

import deliverif.app.controller.GraphProcessor;
import deliverif.app.model.graph.Tour;
import deliverif.app.model.request.Request;

/**
 *
 * @author zakaria
 */
public class AddRequestCommand implements Command {

    private GraphProcessor gp;
    private Tour oldTour;
    private Tour tour;
    private Request request;

    /**
     * Create an add request command
     *
     * @param gp the graphprocessor
     * @param t the current tour
     * @param r the request to add
     */
    public AddRequestCommand(GraphProcessor gp, Tour t, Request r) {
        this.gp = gp;
        tour = t;
        oldTour = new Tour(t);
        request = r;
    }

    @Override
    public void doCommand() {
        gp.addRequestToTour(tour, request);
        tour.notifiyObservers(null);
    }

    @Override
    public void undoCommand() {
        tour.getPr().removeRequest(request);
        tour.copyTour(oldTour);
        tour.notifiyObservers(null);
    }

}
