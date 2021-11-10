package deliverif.gui.panel;

import deliverif.controller.state.*;
import deliverif.gui.Gui;
import deliverif.gui.utils.ColorTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class BottomPanel extends GuiPanel implements StateVisitor {
    private JLabel currentStateLabel;

    public BottomPanel(Gui gui) {
        super(gui);
        this.setBackground(ColorTheme.PANEL_1_BASE_BG);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBorder(new EmptyBorder(5, 10, 5, 10));

        currentStateLabel = new JLabel("Loading");
        this.add(currentStateLabel);
    }

    public void updateState(State currentState) {
        currentState.accept(this);
    }

    @Override
    public void visit(AddDeliveryRequestState state) {
        currentStateLabel.setText("Add a request: delivery address configuration");
    }

    @Override
    public void visit(AddPickupRequestState state) {
        currentStateLabel.setText("Add a request: pickup address configuration");
    }

    @Override
    public void visit(ComputingTourState state) {
        currentStateLabel.setText("Computing tour...");
    }

    @Override
    public void visit(DeleteRequestState state) {
        currentStateLabel.setText("Delete a request: please choose a request to remove");
    }

    @Override
    public void visit(GenerateRoadMapState state) {
        currentStateLabel.setText("Generating roadmap...");
    }

    @Override
    public void visit(InitState state) {
        currentStateLabel.setText("Welcome! Please open a map file.");
    }

    @Override
    public void visit(LoadingMapState state) {
        currentStateLabel.setText("Loading map...");
    }

    @Override
    public void visit(LoadingRequestsState state) {
        currentStateLabel.setText("Loading requests...");
    }

    @Override
    public void visit(MapLoadedState state) {
        currentStateLabel.setText("Map loaded! Please open a requests file.");
    }

    @Override
    public void visit(RequestPopupDurationState state) {
        currentStateLabel.setText("Add a request: pickup and delivery durations");
    }

    @Override
    public void visit(RequestsLoadedState state) {
        currentStateLabel.setText("Requests loaded.");
    }

    @Override
    public void visit(TourCompletedState state) {
        currentStateLabel.setText("Tour computed!");
    }

    @Override
    public void visit(TourNotOptimalState state) {
        currentStateLabel.setText("Computed tour is not optimal. Continue?");
    }
}
