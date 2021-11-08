package deliverif.controller.state;

import deliverif.controller.Controller;
import deliverif.model.Address;
import deliverif.model.EnumAddressType;
import deliverif.model.Request;
import deliverif.model.RoadSegment;
import pdtsp.Pair;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface IAddRequestState {
    default JPanel createAddressChoicePanel(Controller controller, List<Pair<Double, Address>> addresses, List<JRadioButton> choices) {
        JPanel optionPanel = new JPanel();
        optionPanel.add(new JLabel("There are several addresses nearby. Please select one of those, or try again:"));
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));

        ButtonGroup radioGroup = new ButtonGroup();

        // populate radio buttons
        for (Pair<Double, Address> addressPair : addresses) {
            Address address = addressPair.getY();

            String nearMessage = this.createAddressRoadsNearbyText(controller, address);
            JRadioButton radio = new JRadioButton(
                    "<html>" + address.getId() + ": " + "(" + String.format("%.2f", addressPair.getX()) + "m away)<br>" + nearMessage + "</html>"
            );

            if (choices.isEmpty()) { // means this is the first address of the list
                radio.setSelected(true);
            }

            choices.add(radio);
            radioGroup.add(radio);
            optionPanel.add(radio);
        }

        return optionPanel;
    }

    default String createAddressRoadsNearbyText(Controller controller, Address address) {
        Collection<RoadSegment> segments = controller.getCityMap().getSegmentsOriginatingFrom(address.getId());
        StringBuilder nearMessage = new StringBuilder("near<br>");
        for (RoadSegment segment : segments) {
            // &#09 is the tab character
            nearMessage.append("&#09").append(segment.getName()).append("<br>");
        }

        return nearMessage.toString();
    }

    default int getSelectedRadioIndex(List<JRadioButton> buttons) {
        int i = 0;
        for (JRadioButton radioButton : buttons) {
            if (radioButton.isSelected()) {
                return i;
            }
            i++;
        }
        return -1;
    }

    default String addressBeforeMessage(Pair<EnumAddressType, Request> address) {
        switch(address.getX()) {
            case DEPARTURE_ADDRESS -> {
                return "this tour's departure address";
            }
            case DELIVERY_ADDRESS -> {
                return "delivery address n°" + address.getY().getDeliveryAddress().getId();
            }
            case PICKUP_ADDRESS -> {
                return "pickup address n°" + address.getY().getPickupAddress().getId();
            }
            default -> { // this should never be reached
                System.exit(1);
            }
        }
        return null;
    }

    default Address showAddressChoiceForm(Controller controller, List<Pair<Double, Address>> addresses) {
        List<JRadioButton> choices = new ArrayList<>();
        JPanel optionPanel = createAddressChoicePanel(controller, addresses, choices);

        int option = JOptionPane.showConfirmDialog(
                controller.getGui().getFrame(), optionPanel, "Multiple possible addresses", JOptionPane.OK_CANCEL_OPTION
        );
        if (option == JOptionPane.CANCEL_OPTION) {
            return null; // user cancelled operation
        }

        return addresses.get(this.getSelectedRadioIndex(choices)).getY();
    }
}
