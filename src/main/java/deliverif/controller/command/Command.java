package deliverif.controller.command;

public interface Command {
    void doCommand();

    void undoCommand();
}
