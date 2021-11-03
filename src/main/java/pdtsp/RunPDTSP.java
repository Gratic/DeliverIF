package pdtsp;

import deliverif.model.*;

import java.util.*;

public class RunPDTSP {

    public static List<Object> case1()
    {
        // Deux requêtes : une de 1 vers 2 et une de 2 vers 1.
        Collection<Pair<Long, Long>> requests = new ArrayList<>();
        requests.add(new Pair<>(1L, 2L));
        requests.add(new Pair<>(2L, 1L));

        // Définition de la map, forme un triangle entre 0,1,2. Graphe complet.
        // Utilisation d'une ArrayList comme pair : faute de définition
        Map<Long, Collection<Pair<Long, Double>>> map = new HashMap<>();
        Collection<Pair<Long, Double>> arcs0 = new ArrayList<>();
        arcs0.add(new Pair<>(1L, 2d));
        arcs0.add(new Pair<>(2L, 3d));

        Collection<Pair<Long, Double>> arcs1 = new ArrayList<>();
        arcs1.add(new Pair<>(0L, 2d));
        arcs1.add(new Pair<>(2L, 1d));

        Collection<Pair<Long, Double>> arcs2 = new ArrayList<>();
        arcs2.add(new Pair<>(0L, 3d));
        arcs2.add(new Pair<>(1L, 1d));

        map.put(0L, arcs0);
        map.put(1L, arcs1);
        map.put(2L, arcs2);

        return new ArrayList<>() {{
            add(map);
            add(requests);
            add(0L);
        }};
    }

    public static List<Object> case1real()
    {
        // Définition de la map, forme un triangle entre 0,1,2. Graphe complet.
        // Utilisation d'une ArrayList comme pair : faute de définition
        CityMap cityMap = new CityMap();
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

        for(RoadSegment rd : roadSegments)
        {
            cityMap.addSegment(rd);
        }

        // Deux requêtes : une de 1 vers 2 et une de 2 vers 1.
        Collection<Request> requests = new ArrayList<>();
        requests.add(new Request(add1, add2, 0, 0));
        requests.add(new Request(add2, add1, 0, 0));

        return new ArrayList<>() {{
            add(cityMap);
            add(requests);
            add(add0);
        }};
    }

    public static List<Object> case2()
    {
        // Deux requêtes : une de 1 vers 2
        Collection<Pair<Long, Long>> requests = new ArrayList<>();
        requests.add(new Pair<>(1L, 2L));

        // Définition de la map, forme un triangle entre 0,1,2. Graphe complet.
        // Utilisation d'une ArrayList comme pair : faute de définition
        Map<Long, Collection<Pair<Long, Double>>> map = new HashMap<>();
        Collection<Pair<Long, Double>> arcs0 = new ArrayList<>();
        arcs0.add(new Pair<>(1L, 2d));
        arcs0.add(new Pair<>(2L, 3d));

        Collection<Pair<Long, Double>> arcs1 = new ArrayList<>();
        arcs1.add(new Pair<>(0L, 2d));
        arcs1.add(new Pair<>(2L, 1d));

        Collection<Pair<Long, Double>> arcs2 = new ArrayList<>();
        arcs2.add(new Pair<>(0L, 3d));
        arcs2.add(new Pair<>(1L, 1d));

        map.put(0L, arcs0);
        map.put(1L, arcs1);
        map.put(2L, arcs2);

        return new ArrayList<>() {{
            add(map);
            add(requests);
            add(0L);
        }};
    }

    public static List<Object> case3()
    {
        // Deux requêtes : une de 1 vers 2 et une de 2 vers 1.
        Collection<Pair<Long, Long>> requests = new ArrayList<>();
        requests.add(new Pair<>(1L, 2L));
        requests.add(new Pair<>(5L, 2L));
        requests.add(new Pair<>(2L, 4L));

        // Définition de la map, forme un triangle entre 0,1,2. Graphe complet.
        // Utilisation d'une ArrayList comme pair : faute de définition
        Map<Long, Collection<Pair<Long, Double>>> arcs = new HashMap<>();

        Collection<Pair<Long, Double>> arc0 = new ArrayList<>();
        arc0.add(new Pair<>(1L, 1d));
        arc0.add(new Pair<>(3L, 2d));

        Collection<Pair<Long, Double>> arc1 = new ArrayList<>();
        arc1.add(new Pair<>(0L, 1d));
        arc1.add(new Pair<>(2L, 3d));
        arc1.add(new Pair<>(3L, 1d));

        Collection<Pair<Long, Double>> arc2 = new ArrayList<>();
        arc2.add(new Pair<>(1L, 3d));
        arc2.add(new Pair<>(4L, 1d));
        arc2.add(new Pair<>(5L, 3d));

        Collection<Pair<Long, Double>> arc3 = new ArrayList<>();
        arc3.add(new Pair<>(0L, 2d));
        arc3.add(new Pair<>(1L, 1d));
        arc3.add(new Pair<>(4L, 4d));

        Collection<Pair<Long, Double>> arc4 = new ArrayList<>();
        arc4.add(new Pair<>(2L, 1d));
        arc4.add(new Pair<>(3L, 4d));
        arc4.add(new Pair<>(5L, 1d));

        Collection<Pair<Long, Double>> arc5 = new ArrayList<>();
        arc5.add(new Pair<>(2L, 3d));
        arc5.add(new Pair<>(4L, 1d));

        arcs.put(0L, arc0);
        arcs.put(1L, arc1);
        arcs.put(2L, arc2);
        arcs.put(3L, arc3);
        arcs.put(4L, arc4);
        arcs.put(5L, arc5);

        return new ArrayList<>() {{
            add(arcs);
            add(requests);
            add(3L);
        }};
    }

    public static List<Object> case4()
    {
        // Deux requêtes : une de 1 vers 2
        Collection<Pair<Long, Long>> requests = new ArrayList<>();
        requests.add(new Pair<>(100L, 200L));

        // Définition de la map, forme un triangle entre 0,1,2. Graphe complet.
        // Utilisation d'une ArrayList comme pair : faute de définition
        Map<Long, Collection<Pair<Long, Double>>> map = new HashMap<>();
        Collection<Pair<Long, Double>> arcs0 = new ArrayList<>();
        arcs0.add(new Pair<>(100L, 200d));
        arcs0.add(new Pair<>(200L, 300d));

        Collection<Pair<Long, Double>> arcs1 = new ArrayList<>();
        arcs1.add(new Pair<>(0L, 200d));
        arcs1.add(new Pair<>(200L, 100d));

        Collection<Pair<Long, Double>> arcs2 = new ArrayList<>();
        arcs2.add(new Pair<>(0L, 300d));
        arcs2.add(new Pair<>(100L, 100d));

        map.put(0L, arcs0);
        map.put(100L, arcs1);
        map.put(200L, arcs2);

        return new ArrayList<>() {{
            add(map);
            add(requests);
            add(0L);
        }};
    }

    public static void main(String[] args) {
        List<Object> example = case1real();

        CityMap cityMap = (CityMap)example.get(0);
        List<Request> requests = (List<Request>)example.get(1);
        Address startPoint = (Address)example.get(2);

        DeliveryTour deliveryTour = new DeliveryTour();

        PDTSPWrapper wrapper = new PDTSPWrapper(cityMap, requests, deliveryTour, startPoint, 10000);
        wrapper.prepare();
//        while(!wrapper.isSolutionOptimal())
//        {
//            System.out.println("not Optimal");
            while(!wrapper.isSolutionFound())
            {
                System.out.println("computing ...");
                wrapper.compute();
            }
//        }

        List<Long> realPath = wrapper.getPath();

        System.out.println("Realpath:");
        System.out.print("[");
        for(int i = 0; i < realPath.size(); i++)
        {
            Long node = realPath.get(i);
            if(i == realPath.size()-1)
            {
                System.out.print(node);
            }
            else
            {
                System.out.print(node+",");
            }
        }
        System.out.println("]");

        wrapper.updateDeliveryTour();

        System.out.print("[");
        for(int i = 0; i < deliveryTour.getPath().size(); i++)
        {
            RoadSegment rs = deliveryTour.getPath().get(i);
            if(i != deliveryTour.getPath().size() - 1)
            {
                if(rs == null)
                {
                    System.out.print("same node,");
                }
                else
                {
                    System.out.print(rs.getOrigin().getId() + "->" + rs.getDestination().getId() + ",");
                }
            }
            else
            {
                if(rs == null)
                {
                    System.out.print("same node");
                }
                else
                {
                    System.out.print(rs.getOrigin().getId() + "->" + rs.getDestination().getId());
                }
            }
        }
        System.out.println("]");

        System.out.print("[");
        for(int i = 0; i < deliveryTour.getPathAddresses().size(); i++)
        {
            Address address = deliveryTour.getPathAddresses().get(i);
            String type = null;
            Request request = null;

            if(deliveryTour.getAddressRequestMetadata().get(address) != null)
            {
                Map.Entry<Request, EnumAddressType> pair = deliveryTour.getAddressRequestMetadata().get(address);
                request = pair.getKey();

                if(pair.getValue() == EnumAddressType.PICKUP_ADDRESS)
                {
                    type = "pickup";
                }
                else if(pair.getValue() == EnumAddressType.DELIVERY_ADDRESS)
                {
                    type = "delivery";
                }
            }

            if(type != null)
            {
                long pickupAddressId = request.getPickupAddress().getId();
                long deliveryAddressId = request.getDeliveryAddress().getId();

                if(i != deliveryTour.getPathAddresses().size() - 1)
                {
                    System.out.print(type + " {" + pickupAddressId + ":" + deliveryAddressId + "},");
                }
                else
                {
                    System.out.print(type + " {" + pickupAddressId + ":" + deliveryAddressId + "}");
                }
            }

        }
        System.out.println("]");
    }
}
