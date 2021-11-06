package deliverif.roadmap;

import deliverif.controller.Controller;
import deliverif.model.*;
import pdtsp.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Roadmap {

    private final Controller controller;
    private final DeliveryTour tour;
    private final CityMap map;
    private final int SPEED = 15;
    static final long ONE_MINUTE_IN_MILLIS = 60000;

    public Roadmap(Controller controller) {
        this.controller = controller;
        this.tour = controller.getTour();
        this.map = controller.getCityMap();

    }

    public void createFile() {
        try {
            File roadmap = new File("resources/roadmap/roadmap.txt");
            if (roadmap.createNewFile()) {
                System.out.println("File created: " + roadmap.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void WriteInFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("resources/roadmap/roadmap.txt", false));
            Date departureTime = tour.getDepartureTime();
            writer.write("Departure time: ");
            writer.write(departureTime.toString());
            writer.newLine();
            writer.write("RoadSegments : ");
            List<Address> addresses = tour.getPathAddresses();
            List<Pair<EnumAddressType, Request>> addressRequestMetadata = tour.getAddressRequestMetadata();
            Date currentDate = departureTime;
            for (int i = 1; i < addressRequestMetadata.size(); i++) {
                if (addressRequestMetadata.get(i).getX().equals(EnumAddressType.PICKUP_ADDRESS)) {
                    Address lastRequestAddress = findLastRequest(addresses.get(i));
                    currentDate = computeArrivalTimeOnPoint(currentDate, lastRequestAddress, addresses.get(i));
                    writer.write("Estimated arrival time : " + currentDate);
                    writer.newLine();
                    writer.write("Pick the parcel (duration : ");
                    writer.newLine();
                    writer.write(addressRequestMetadata.get(i).getY().getPickupDuration());
                    writer.write(" )");
                    writer.newLine();
                    writer.write("Then go on : ");
                    RoadSegment roadSegment = getRoadsegmentBetweenAdresses(addresses.get(i), addresses.get(i + 1));
                    writer.write(roadSegment.getName());
                } else if (addressRequestMetadata.get(i).getX().equals(EnumAddressType.DELIVERY_ADDRESS)) {
                    Address lastRequestAddress = findLastRequest(addresses.get(i));
                    currentDate = computeArrivalTimeOnPoint(currentDate, lastRequestAddress, addresses.get(i));
                    writer.write("Estimated arrival time : " + currentDate);
                    writer.newLine();
                    writer.write("Deliver the parcel of the request (duration : ");
                    writer.newLine();
                    writer.write(addressRequestMetadata.get(i).getY().getDeliveryDuration());
                    writer.write(" )");
                    writer.newLine();
                    writer.write("Then go on : ");
                    RoadSegment roadSegment = getRoadsegmentBetweenAdresses(addresses.get(i), addresses.get(i + 1));
                    writer.write(roadSegment.getName());
                    writer.newLine();
                } else if (addressRequestMetadata.get(i).getX().equals(EnumAddressType.TRAVERSAL_ADDRESS)) {
                    RoadSegment roadSegment = getRoadsegmentBetweenAdresses(addresses.get(i), addresses.get(i + 1));
                    writer.write("Turn on ");
                    writer.write(roadSegment.getName());
                    writer.newLine();
                }
            }
            writer.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    public double computeDurationOnARoadSegment(RoadSegment roadSegment) {
        double duration = roadSegment.getLength() * 0.001 / SPEED;
        return duration * 60; //Return duration in minutes

    }

    public double computeDurationOnSubPath(Address a1, Address a2) {
        List<RoadSegment> roadSegmentsSubPath = tour.getSubPathBetweenPoints(a1, a2);
        double duration = 0;
        for (RoadSegment roadSegment : roadSegmentsSubPath) {
            duration += computeDurationOnARoadSegment(roadSegment);
        }
        return duration;
    }

    public Date computeArrivalTimeOnPoint(Date previousDate, Address a1, Address a2) {
        long timeInMSecs = previousDate.getTime();
        double duration = computeDurationOnSubPath(a1, a2);
        List<Pair<EnumAddressType, Request>> addressRequestMetadata = tour.getAddressRequestMetadata();
        int indexA1 = tour.getIndexOfAddress(a1);
        if (addressRequestMetadata.get(indexA1).getX().equals(EnumAddressType.DELIVERY_ADDRESS)) {
            duration += addressRequestMetadata.get(indexA1).getY().getDeliveryDuration();
        } else if (addressRequestMetadata.get(indexA1).getX().equals(EnumAddressType.PICKUP_ADDRESS)) {
            duration += addressRequestMetadata.get(indexA1).getY().getPickupDuration();
        }
        Date afterAddingMins = new Date(timeInMSecs
                + ((int) duration * ONE_MINUTE_IN_MILLIS));

        return afterAddingMins;
    }

    public RoadSegment getRoadsegmentBetweenAdresses(Address a1, Address a2) {
        Collection<RoadSegment> roadSegmentsOriginating = map.getSegmentsOriginatingFrom(a1.getId());
        RoadSegment finalRoadSegment = null;
        for (RoadSegment roadSeg : roadSegmentsOriginating) {
            if (roadSeg.getDestination().equals(a2)) {
                finalRoadSegment = roadSeg;
            }
        }
        return finalRoadSegment;
    }

    public Address findLastRequest(Address currentAddress) {
        List<Address> addresses = tour.getPathAddresses();
        List<Pair<EnumAddressType, Request>> addressRequestMetadata = tour.getAddressRequestMetadata();
        int stopIndex = tour.getIndexOfAddress(currentAddress);
        Address lastAddress = null;
        for (int i = 0; i < stopIndex; i++) {
            if (addressRequestMetadata.get(i).getX().equals(EnumAddressType.PICKUP_ADDRESS) ||
                    addressRequestMetadata.get(i).getX().equals(EnumAddressType.DELIVERY_ADDRESS)) {
                lastAddress = addresses.get(i);
            }
        }
        return lastAddress;
    }

}
