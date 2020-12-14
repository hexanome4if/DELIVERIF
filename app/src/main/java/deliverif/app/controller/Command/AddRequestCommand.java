/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.Command;

import deliverif.app.controller.GraphProcessor;
import deliverif.app.controller.MenuPageController;
import deliverif.app.model.graph.Tour;
import deliverif.app.model.request.Request;

/**
 * Command to add a request in the current tour
 * @author zakaria
 */
public class AddRequestCommand implements Command {
    /**
     * The current graph processor controller
     */
    private final GraphProcessor gp;
    /**
     * The tour before the command has been executed
     */
    private final Tour oldTour;
    /**
     * The current tour object rendered by the view
     */
    private final Tour tour;
    /**
     * The request added by the command
     */
    private final Request request;

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
        MenuPageController mpc = MenuPageController.getInstance();
        mpc.displayRequest(request);
        gp.addRequestToTour(tour, request);
        tour.notifiyObservers(null);
    }

    @Override
    public void undoCommand() {
        MenuPageController mpc = MenuPageController.getInstance();
        mpc.removeSpriteRequest(request);
        tour.getPr().removeRequest(request);
        tour.copyTour(oldTour);
        tour.notifiyObservers(null);
    }

}
