/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.Command;

import deliverif.app.controller.State.SwapRequestState;

/**
 * Command to select a sprite on the map
 * @author zakaria
 */
public class SelectSpriteCommand implements Command {
    /**
     * The current state which must be a SwapRequestState
     */
    private final SwapRequestState srs;
    /**
     * Whether the selected node is the first or the second
     */
    private final boolean isFirst;
    /**
     * The selected node id
     */
    private final String id;

    /**
     * Create a select node command
     *
     * @param srs the current state
     * @param isFirst is the selected node first or second
     * @param id the selected node id
     */
    public SelectSpriteCommand(SwapRequestState srs, boolean isFirst, String id) {
        this.srs = srs;
        this.isFirst = isFirst;
        this.id = id;
    }

    @Override
    public void doCommand() {
        if (isFirst) {
            srs.setFirstId(id);
        } else {
            srs.setSecondId(id);
        }
    }

    @Override
    public void undoCommand() {
        if (isFirst) {
            srs.setFirstId(null);
        } else {
            srs.setSecondId(null);
        }
    }

}

