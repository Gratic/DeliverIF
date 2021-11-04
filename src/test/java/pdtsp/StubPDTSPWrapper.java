package pdtsp;

import deliverif.model.Address;
import deliverif.model.CityMap;
import deliverif.model.DeliveryTour;
import deliverif.model.RoadSegment;

import java.util.ArrayList;
import java.util.List;

/*
    Stub classes to have predefined CityMap and DeliveryTour.
 */
public class StubPDTSPWrapper {
    static List<Object> stub1() {
        // Définition de la map, forme un triangle entre 0,1,2. Graphe complet.

        List<Object> stub1 = new ArrayList<>();
        CityMap cityMap = new CityMap();
        DeliveryTour deliveryTour = new DeliveryTour();

        Address add0 = new Address(0L, 0d, 0d);
        Address add1 = new Address(1L, 10d, 10d);
        Address add2 = new Address(2L, 20d, 20d);

        cityMap.getAddresses().put(add0.getId(), add0);
        cityMap.getAddresses().put(add1.getId(), add1);
        cityMap.getAddresses().put(add2.getId(), add2);

        List<RoadSegment> roadSegments = new ArrayList<>();
        roadSegments.add(new RoadSegment(add0, add1, "01", 2d));
        roadSegments.add(new RoadSegment(add0, add2, "02", 3d));

        roadSegments.add(new RoadSegment(add1, add0, "10", 2d));
        roadSegments.add(new RoadSegment(add1, add2, "12", 1d));

        roadSegments.add(new RoadSegment(add2, add0, "20", 3d));
        roadSegments.add(new RoadSegment(add2, add1, "21", 1d));

        for (RoadSegment rd : roadSegments) {
            cityMap.addSegment(rd);
        }

        stub1.add(cityMap);
        stub1.add(deliveryTour);

        return stub1;
    }

    static List<Object> stub2() {
        //  (0) --3-- (1)
        //   |       / 1
        //   2   2-(2)
        //   |  /    \ 1
        //  (3)----2---(4)

        List<Object> stub2 = new ArrayList<>();
        CityMap cityMap = new CityMap();
        DeliveryTour deliveryTour = new DeliveryTour();

        Address add0 = new Address(0L, 0d, 0d);
        Address add1 = new Address(1L, 10d, 10d);
        Address add2 = new Address(2L, 20d, 20d);
        Address add3 = new Address(3L, 30d, 30d);
        Address add4 = new Address(4L, 40d, 40d);

        cityMap.getAddresses().put(add0.getId(), add0);
        cityMap.getAddresses().put(add1.getId(), add1);
        cityMap.getAddresses().put(add2.getId(), add2);
        cityMap.getAddresses().put(add3.getId(), add3);
        cityMap.getAddresses().put(add4.getId(), add4);

        List<RoadSegment> roadSegments = new ArrayList<>();
        roadSegments.add(new RoadSegment(add0, add1, "01", 3d));
        roadSegments.add(new RoadSegment(add0, add3, "03", 2d));

        roadSegments.add(new RoadSegment(add1, add0, "10", 3d));
        roadSegments.add(new RoadSegment(add1, add2, "12", 1d));

        roadSegments.add(new RoadSegment(add2, add1, "21", 1d));
        roadSegments.add(new RoadSegment(add2, add3, "23", 2d));
        roadSegments.add(new RoadSegment(add2, add4, "24", 1d));

        roadSegments.add(new RoadSegment(add3, add0, "30", 2d));
        roadSegments.add(new RoadSegment(add3, add2, "32", 2d));
        roadSegments.add(new RoadSegment(add3, add4, "34", 2d));

        roadSegments.add(new RoadSegment(add4, add2, "42", 1d));
        roadSegments.add(new RoadSegment(add4, add3, "43", 2d));

        for (RoadSegment rd : roadSegments) {
            cityMap.addSegment(rd);
        }

        stub2.add(cityMap);
        stub2.add(deliveryTour);

        return stub2;
    }

    static List<Object> stub3() {
        List<Object> stub3 = new ArrayList<>();
        CityMap cityMap = new CityMap();
        DeliveryTour deliveryTour = new DeliveryTour();

        Address add0 = new Address(0L, 0d, 0d);
        Address add1 = new Address(1L, 10d, 10d);
        Address add2 = new Address(2L, 20d, 20d);
        Address add3 = new Address(3L, 30d, 30d);
        Address add4 = new Address(4L, 40d, 40d);
        Address add5 = new Address(5L, 50d, 50d);
        Address add6 = new Address(6L, 60d, 60d);
        Address add7 = new Address(7L, 70d, 70d);
        Address add8 = new Address(8L, 80d, 80d);
        Address add9 = new Address(9L, 90d, 90d);
        Address add10 = new Address(10L, 100d, 100d);

        cityMap.getAddresses().put(add0.getId(), add0);
        cityMap.getAddresses().put(add1.getId(), add1);
        cityMap.getAddresses().put(add2.getId(), add2);
        cityMap.getAddresses().put(add3.getId(), add3);
        cityMap.getAddresses().put(add4.getId(), add4);
        cityMap.getAddresses().put(add5.getId(), add5);
        cityMap.getAddresses().put(add6.getId(), add6);
        cityMap.getAddresses().put(add7.getId(), add7);
        cityMap.getAddresses().put(add8.getId(), add8);
        cityMap.getAddresses().put(add9.getId(), add9);
        cityMap.getAddresses().put(add10.getId(), add10);

        List<RoadSegment> roadSegments = new ArrayList<>();
        roadSegments.add(new RoadSegment(add0, add1, "", 1d));
        roadSegments.add(new RoadSegment(add0, add6, "", 2d));

        roadSegments.add(new RoadSegment(add1, add0, "", 1d));
        roadSegments.add(new RoadSegment(add1, add6, "", 1d));
        roadSegments.add(new RoadSegment(add1, add7, "", 2d));
        roadSegments.add(new RoadSegment(add1, add9, "", 1d));

        roadSegments.add(new RoadSegment(add2, add3, "", 1d));
        roadSegments.add(new RoadSegment(add2, add7, "", 1d));
        roadSegments.add(new RoadSegment(add2, add8, "", 1d));
        roadSegments.add(new RoadSegment(add2, add9, "", 2d));

        roadSegments.add(new RoadSegment(add3, add2, "", 1d));
        roadSegments.add(new RoadSegment(add3, add4, "", 1d));
        roadSegments.add(new RoadSegment(add3, add7, "", 2d));
        roadSegments.add(new RoadSegment(add3, add8, "", 2d));
        roadSegments.add(new RoadSegment(add3, add10, "", 1d));

        roadSegments.add(new RoadSegment(add4, add3, "", 1d));
        roadSegments.add(new RoadSegment(add4, add5, "", 2d));
        roadSegments.add(new RoadSegment(add4, add6, "", 1d));
        roadSegments.add(new RoadSegment(add4, add7, "", 2d));

        roadSegments.add(new RoadSegment(add5, add4, "", 2d));
        roadSegments.add(new RoadSegment(add5, add6, "", 1d));

        roadSegments.add(new RoadSegment(add6, add0, "", 2d));
        roadSegments.add(new RoadSegment(add6, add1, "", 1d));
        roadSegments.add(new RoadSegment(add6, add4, "", 1d));
        roadSegments.add(new RoadSegment(add6, add5, "", 1d));
        roadSegments.add(new RoadSegment(add6, add7, "", 4d));

        roadSegments.add(new RoadSegment(add7, add1, "", 2d));
        roadSegments.add(new RoadSegment(add7, add2, "", 1d));
        roadSegments.add(new RoadSegment(add7, add3, "", 2d));
        roadSegments.add(new RoadSegment(add7, add4, "", 2d));
        roadSegments.add(new RoadSegment(add7, add6, "", 4d));
        roadSegments.add(new RoadSegment(add7, add9, "", 3d));

        roadSegments.add(new RoadSegment(add8, add2, "", 1d));
        roadSegments.add(new RoadSegment(add8, add3, "", 2d));
        roadSegments.add(new RoadSegment(add8, add10, "", 3d));

        roadSegments.add(new RoadSegment(add9, add1, "", 1d));
        roadSegments.add(new RoadSegment(add9, add2, "", 2d));
        roadSegments.add(new RoadSegment(add9, add7, "", 3d));

        roadSegments.add(new RoadSegment(add10, add3, "", 1d));
        roadSegments.add(new RoadSegment(add10, add8, "", 3d));

        for (RoadSegment rd : roadSegments) {
            cityMap.addSegment(rd);
        }

        stub3.add(cityMap);
        stub3.add(deliveryTour);

        return stub3;
    }
}
