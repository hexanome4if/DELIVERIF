/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.Command;

import java.util.LinkedList;

/**
 *
 * @author zakaria
 */
public class ListOfCommands {

    private LinkedList<Command> l;
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
     * @param c
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
        i++;
        l.get(i).doCommand();
    }
}
