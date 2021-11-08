package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.gui.Gui;
import deliverif.model.Address;
import deliverif.model.EnumAddressType;
import deliverif.model.Request;
import deliverif.model.RoadSegment;
import pdtsp.Pair;

import javax.annotation.processing.SupportedSourceVersion;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChooseRequestToDeleteState implements State {

    @Override
    public void run(Controller controller, Gui gui) {

        List<Request> requests = controller.getTour().getRequests();
        List<JRadioButton> choices;
        JPanel optionPanel = new JPanel();
        optionPanel.add(new JLabel("There are several requests nearby. Please select one of those, or try again:"));
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));

        ButtonGroup radioGroup = new ButtonGroup();

        // populate radio buttons
        for (Request request : requests) {
            JRadioButton radio = new JRadioButton(
            );
            radioGroup.add(radio);
            optionPanel.add(radio);
        }
    }


    public String createAddressRoadsNearbyText(Controller controller, Address address) {
        Collection<RoadSegment> segments = controller.getCityMap().getSegmentsOriginatingFrom(address.getId());
        StringBuilder nearMessage = new StringBuilder("near<br>");
        for (RoadSegment segment : segments) {
            // &#09 is the tab character
            nearMessage.append("&#09").append(segment.getName()).append("<br>");
        }

        return nearMessage.toString();
    }

    @Override
    public void addressClick(Controller controller, Gui gui, Address addressClicked) {
        //TODO: Find a way to detect overlapping points
        //case delete request
        controller.setCurrentState(controller.deleteRequest);
        //case choose associated request
    }

    public void cancelButtonClick(Controller controller, Gui gui) {

        controller.popState();
    }

    @Override
    public void accept(StateVisitor visitor) {
        visitor.visit(this);
    }
}
