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
 *
 * @author zakaria
 */
public class SwapRequestCommand implements Command {

    private GraphProcessor gp;
    private Tour oldTour;
    private Tour tour;
    private List<Long> newOrder;

    /**
     * Create an add request command
     *
     * @param gp the graphprocessor
     * @param t the current tour
     * @param r the request to add
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