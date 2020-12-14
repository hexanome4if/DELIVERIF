/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.Command;

import java.util.LinkedList;

/**
 * List the commands executed by the user to apply undo and redo
 * @author zakaria
 */
public class ListOfCommands {

    /**
     * The list of commands executed by the user
     */
    private final LinkedList<Command> l;
    /**
     * The current index in the list of commands where the user is now
     */
    private int i;

    /**
     * Create a list of commands to perform undo and redo
     */
    public ListOfCommands() {
        i = -1;
        l = new LinkedList<>();
    }

    /**
     * Add a command and execute it
     *
     * @param c the command to add
     */
    public void addCommand(Command c) {
        i++;
        l.add(i, c);
        c.doCommand();
    }

    /**
     * Undo the last command
     */
    public void undo() {
        if (i >= 0) {
            l.get(i).undoCommand();
            i--;
        }
    }

    /**
     * Redo the last command
     */
    public void redo() {
        if (i+1 < l.size()) {
            i++;
            l.get(i).doCommand();   
        }
    }
}
