package deliverif.controller.command;

import java.util.LinkedList;

public class ListOfCommands {
    private LinkedList<Command> commands;
    private int lastCommandIndex;

    public ListOfCommands() {
        lastCommandIndex = -1;
        commands = new LinkedList<Command>();
    }

    public void add(Command command) {
        lastCommandIndex++; // WARN: Increment first
        // add command to the end of the list
        commands.add(lastCommandIndex, command);
        command.doCommand();
    }

    public void undo() {
        if(lastCommandIndex >= 0) {
            commands.get(lastCommandIndex).undoCommand();
            lastCommandIndex--; // WARN: Decrement after
        }
    }

    public void redo() {
        lastCommandIndex++; // WARN: Increment first
        commands.get(lastCommandIndex).doCommand();
    }
}
