package deliverif.roadmap;

import deliverif.controller.Controller;
import deliverif.model.*;
import pdtsp.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Roadmap {

    private final Controller controller;
    private final DeliveryTour tour;
    private final CityMap map;
    private final int SPEED = 15;
    static final long ONE_SECOND_IN_MILLIS = 1000;

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

    public void WriteInFile(File file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            Date departureTime = tour.getDepartureTime();
            writer.write("Departure time: ");
            writer.write(getDateFormated(departureTime));
            writer.newLine();
            writer.write("RoadSegments : ");
            List<Address> addresses = tour.getPathAddresses();
            List<Pair<EnumAddressType, Request>> addressRequestMetadata = tour.getAddressRequestMetadata();
            Date currentDate = departureTime;
            RoadSegment previousRoadSegment = null;
            for (int i = 1; i < addressRequestMetadata.size(); i++) {
                if (addressRequestMetadata.get(i).getX().equals(EnumAddressType.PICKUP_ADDRESS)) {
                    Address lastRequestAddress = findLastRequest(addresses.get(i));
                    currentDate = computeArrivalTimeOnPoint(currentDate, lastRequestAddress, addresses.get(i));
                    writer.newLine();
                    writer.write("Estimated arrival time : " + getDateFormated(currentDate));
                    writer.newLine();
                    writer.write("Pick the parcel (duration : ");
                    writer.write(String.valueOf(addressRequestMetadata.get(i).getY().getPickupDuration()/60));
                    writer.write(" minutes)");
                    writer.newLine();
                    writer.write("Estimated departure time : " + getDateFormated(
                            computeDepartureTimeOnPoint(currentDate, addressRequestMetadata.get(i).getY().getDeliveryDuration())));
                    writer.newLine();
                    writer.newLine();


                    RoadSegment roadSegment = getRoadsegmentBetweenAdresses(addresses.get(i), addresses.get(i + 1));

                    writer.write("Then go on : ");
                    writer.write(roadSegment.getName());
                    writer.newLine();

                    previousRoadSegment = roadSegment;

                } else if (addressRequestMetadata.get(i).getX().equals(EnumAddressType.DELIVERY_ADDRESS)) {
                    Address lastRequestAddress = findLastRequest(addresses.get(i));
                    currentDate = computeArrivalTimeOnPoint(currentDate, lastRequestAddress, addresses.get(i));
                    writer.newLine();
                    writer.write("Estimated arrival time : " + getDateFormated(currentDate));
                    writer.newLine();
                    writer.write("Deliver the parcel of the request (duration : ");
                    writer.write(String.valueOf(addressRequestMetadata.get(i).getY().getDeliveryDuration()/60));
                    writer.write(" minutes)");
                    writer.newLine();
                    writer.write("Estimated departure time : " +getDateFormated(
                            computeDepartureTimeOnPoint(currentDate, addressRequestMetadata.get(i).getY().getDeliveryDuration())));
                    writer.newLine();
                    writer.newLine();
                    writer.write("Then go on : ");
                    RoadSegment roadSegment = getRoadsegmentBetweenAdresses(addresses.get(i), addresses.get(i + 1));
                    writer.write(roadSegment.getName());
                    writer.newLine();

                    previousRoadSegment = roadSegment;
                } else if (addressRequestMetadata.get(i).getX().equals(EnumAddressType.TRAVERSAL_ADDRESS)) {
                    RoadSegment roadSegment = getRoadsegmentBetweenAdresses(addresses.get(i), addresses.get(i + 1));
                    if(previousRoadSegment!=null && !previousRoadSegment.getName().equals(roadSegment.getName())&& !roadSegment.getName().equals("")) {
                        writer.write("Then turn on : ");
                        writer.write(roadSegment.getName());
                        writer.newLine();
                    }
                    previousRoadSegment = roadSegment;

                }
            }
            writer.newLine();
            Address lastRequestAddress = findLastRequest(addresses.get(addresses.size()-1));
            currentDate = computeArrivalTimeOnPoint(currentDate, lastRequestAddress, addresses.get(0));
            writer.write("Arrival time : " + getDateFormated(currentDate));


            writer.newLine();
            writer.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * @param roadSegment the roadSegment for which we want to know the travel time
     * @return The duration it will take the cyclist to cover this segment (in seconds)
     */

    public double computeDurationOnARoadSegment(RoadSegment roadSegment) {
        double duration = roadSegment.getLength() * 0.001 / SPEED;
        return duration * 60 * 60;

    }

    /**
     *
     * @param a1 the start address of the subpath we want to know the travel time of
     * @param a2 the stop address of the subpath
     * @return The duration it will take the cyclist to cover this segment (in seconds).
     */
    public double computeDurationOnSubPath(Address a1, Address a2) {
        List<RoadSegment> roadSegmentsSubPath = tour.getSubPathBetweenPoints(a1, a2);
        double duration = 0;
        for (RoadSegment roadSegment : roadSegmentsSubPath) {
            duration += computeDurationOnARoadSegment(roadSegment);
        }
        return duration;
    }

    /**
     * Computes the arrival time at a specific address, knowing the arrival time at a previous address and the
     * pickup or delivery duration if the previous address is a pickup or delivery point.
     * @param previousDate the arrival time at address a1.
     * @param a1 the previous address.
     * @param a2 the address we want to know the arrival time at.
     * @return the arrival time on the address a2.
     */
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

        return new Date(timeInMSecs
                + (int) duration * ONE_SECOND_IN_MILLIS);
    }

    public Date computeDepartureTimeOnPoint(Date previousDate, double duration) {
        long timeInMSecs = previousDate.getTime();
        return new Date(timeInMSecs
                + (int) duration * ONE_SECOND_IN_MILLIS);
    }

    public String getDateFormated(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        return sdf.format(date);
        // date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
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

    /**
     * Find the address of the last processed request of the tour, compared to a current request.
     * @param currentAddress the address of the current request.
     * @return the address of the last processed request compared to currentAddress.
     */
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
