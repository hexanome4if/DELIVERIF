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
import java.util.List;

/**
 * Command to swap two points in the current tour
 * @author zakaria
 */
public class SwapRequestCommand implements Command {
    /**
     * Current graph processor controller
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
     * The new order of points on the tour
     */
    private final List<Long> newOrder;

    /**
     * Create a swap request
     * @param gp the graph processor
     * @param t the tour to update
     * @param newOrder the new request order
     */
    public SwapRequestCommand(GraphProcessor gp, Tour t, List<Long> newOrder) {
        this.gp = gp;
        tour = t;
        oldTour = new Tour(t);
        this.newOrder = newOrder;
    }

    @Override
    public void doCommand() {  
        tour.copyTour(gp.changeOrder(tour, newOrder));
        tour.notifiyObservers(null);
    }

    @Override
    public void undoCommand() {
        tour.copyTour(oldTour);
        tour.notifiyObservers(null);
    }

}