package deliverif.controller.command;

import deliverif.controller.Controller;
import javax.swing.*;
import java.util.LinkedList;

public class ListOfCommands {

    private Controller controller;

    private final LinkedList<Command> commands;
    private int lastCommandIndex;

    public ListOfCommands(Controller controller) {
        lastCommandIndex = -1;
        commands = new LinkedList<>();
        this.controller = controller;
    }

    public void add(Command command) throws Exception {
        if(commands.size() > lastCommandIndex + 1) {
            // clearing subList view of the list allows removal of elements from the list
            commands.subList(lastCommandIndex + 1, commands.size()).clear();
        }

        lastCommandIndex++; // WARN: Increment first
        // add command to the end of the list
        commands.add(lastCommandIndex, command);
        command.doCommand();

        updateGuiUndoRedoButtons();
    }

    public void undo() {
        if (lastCommandIndex >= 0) {
            try {
                commands.get(lastCommandIndex).doCommand();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,"An error occured during undo");
                e.printStackTrace();
            }
            lastCommandIndex--; // WARN: Decrement after
        }

        updateGuiUndoRedoButtons();
    }

    public void redo() {
        if(isRedoPossible()) {
            try {
                lastCommandIndex++;
                commands.get(lastCommandIndex).doCommand();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,"An error occured during redo");
                e.printStackTrace();
            }
        }

        updateGuiUndoRedoButtons();
    }

    public int getLastCommandIndex() {
        return lastCommandIndex;
    }

    private boolean isRedoPossible() {
        return commands.size() > 0 && lastCommandIndex < (commands.size() - 1);
    }

    private void updateGuiUndoRedoButtons() {
        // check if Undo button should be active or not
        controller.getGui().getControlPanel().undoButton.setEnabled(commands.size() > 0);

        // check if Redo button should be active or not
        controller.getGui().getControlPanel().redoButton.setEnabled(isRedoPossible());
    }

}
