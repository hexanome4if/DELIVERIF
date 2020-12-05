/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller;

import java.util.LinkedList;

/**
 *
 * @author zakaria
 */
public class ListOfCommands {
    private LinkedList<Command> l;
    private int i;
    public ListOfCommands() {
        i= -1;
        l = new LinkedList<Command>();
    }
    public void addCommand(Command c) {
        i++;
        l.add(i,c);
        c.doCommand();
    }
    public void undo() {
        if (i>=0) {
            l.get(i).undoCommand();
            i--;
        }
    }
    public void redo() {
        i++;
        l.get(i).doCommand();
    }
}
