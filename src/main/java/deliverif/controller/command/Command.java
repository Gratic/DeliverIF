package deliverif.controller.command;

public interface Command {
    void doCommand() throws Exception;

    void undoCommand() throws Exception;
}
