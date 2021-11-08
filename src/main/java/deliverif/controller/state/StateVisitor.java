package deliverif.controller.state;

public interface StateVisitor {
    void visit(AddDeliveryRequestState state);
    void visit(AddPickupRequestState state);
    void visit(ComputingTourState state);
    void visit(DeleteRequestState state);
    void visit(GenerateRoadMapState state);
    void visit(InitState state);
    void visit(LoadingMapState state);
    void visit(LoadingRequestsState state);
    void visit(MapLoadedState state);
    void visit(RequestPopupDurationState state);
    void visit(RequestsLoadedState state);
    void visit(TourCompletedState state);
    void visit(TourNotOptimalState state);
}
