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
 * Command to remove a request from the current tour
 * @author zakaria
 */
public class RemoveRequestCommand implements Command {

    /**
     * The graph processor controller
     */
    private final GraphProcessor gp;
    /**
     * The tour before the command has been executed
     */
    private final Tour oldTour;
    /**
     * The current tour rendered by the view
     */
    private final Tour tour;
    /**
     * The request removed from the tour
     */
    private final Request request;

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
        MenuPageController mpc = MenuPageController.getInstance();
        mpc.removeSpriteRequest(request);
        gp.removeRequestFromTour(tour, request);
        tour.notifiyObservers(null);
    }

    @Override
    public void undoCommand() {
        MenuPageController mpc = MenuPageController.getInstance();
        mpc.displayRequest(request);
        tour.getPr().addRequest(request);
        tour.copyTour(oldTour);
        tour.notifiyObservers(null);
    }

}
