package controller;

import deliverif.controller.Controller;
import deliverif.controller.command.AddRequestCommand;
import deliverif.controller.command.ListOfCommands;
import deliverif.gui.utils.Assets;
import deliverif.model.Address;
import deliverif.model.DeliveryTour;
import deliverif.model.EnumAddressType;
import deliverif.model.Request;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pdtsp.Pair;

public class TestUndoRedo {

    public static Controller controller;

    @BeforeAll
    static void init() {
        Assets.init();
        controller = new Controller();
    }

    @Test
    void controllerTest() {
        Assertions.assertNotNull(controller);
    }

    @Test
    void noCommandRedoTest() {
        ListOfCommands listOfCommands = new ListOfCommands(controller);

        Assertions.assertEquals(-1, listOfCommands.getLastCommandIndex());

        listOfCommands.redo();
        listOfCommands.redo();

        Assertions.assertEquals(-1, listOfCommands.getLastCommandIndex());
    }

    @Test
    void oneAddCommandTest() {
        ListOfCommands listOfCommands = new ListOfCommands(controller);
        Assertions.assertEquals(-1, listOfCommands.getLastCommandIndex());

        // add 1 command
        DeliveryTour tour = new DeliveryTour();
        Request request0 = new Request(
                new Address(1, 10.0, 10.0),
                new Address(2, 20.0, 20.0),
                300, 300
        );

        try {
            listOfCommands.add(new AddRequestCommand(
                    tour, request0, controller.getCityMap(),
                    new Pair<>(EnumAddressType.DEPARTURE_ADDRESS, null),
                    new Pair<>(EnumAddressType.DEPARTURE_ADDRESS, null)
            ));
        } catch (Exception exception) {
            System.out.println("Exception while adding a new command. Should not have happened");
        }


        Assertions.assertEquals(0, listOfCommands.getLastCommandIndex());

        // 1 redo (must do nothing)
        listOfCommands.redo();
        Assertions.assertEquals(0, listOfCommands.getLastCommandIndex());
        System.out.println("Expected 0, got " + listOfCommands.getLastCommandIndex());

        // 1 undo (must work)
        listOfCommands.undo();
        Assertions.assertEquals(-1, listOfCommands.getLastCommandIndex());
        System.out.println("Expected -1, got " + listOfCommands.getLastCommandIndex());

        // 1 more undo (must do nothing)
        listOfCommands.undo();
        Assertions.assertEquals(-1, listOfCommands.getLastCommandIndex());
        System.out.println("Expected -1, got " + listOfCommands.getLastCommandIndex());

        // 1 redo (must work)
        listOfCommands.redo();
        Assertions.assertEquals(0, listOfCommands.getLastCommandIndex());
        System.out.println("Expected 0, got " + listOfCommands.getLastCommandIndex());

    }
}
