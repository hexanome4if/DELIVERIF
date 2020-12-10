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
public class RemoveRequestCommand implements Command {

    private GraphProcessor gp;
    private Tour oldTour;
    private Tour tour;
    private Request request;

    /**
     * Create a remove request command
     *
     * @param gp the graphprocessor
     * @param t the current tour
     * @param r the request to remove from the tour
     */
    public RemoveRequestCommand(GraphProcessor gp, Tour t, Request r) {
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
        tour.getPr().addRequest(request);
        tour.copyTour(oldTour);
        tour.notifiyObservers(null);
    }

}
