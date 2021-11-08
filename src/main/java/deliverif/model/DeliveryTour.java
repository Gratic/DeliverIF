package deliverif.model;

import deliverif.exception.RequestsLoadException;
import deliverif.observer.Observable;
import deliverif.xml.RequestsXMLHandler;
import org.xml.sax.SAXException;
import pdtsp.Pair;
import pdtsp.ShortestPathWrapper;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeliveryTour extends Observable {
    /**
     * Path
     */
    private final List<RoadSegment> path;
    private Date departureTime;

    /**
     * Addresses on the path (departure, delivery, pickup)
     */
    private final List<Address> pathAddresses;

    /**
     * Metadata of addresses (basically associated request and address type)
     */
    private final List<Pair<EnumAddressType, Request>> addressRequestMetadata;

    /**
     * List of all requests in tour
     */
    private final List<Request> requests;

    /**
     * Index of the currently selected element:
     * -2 -> none
     * -1 -> departureAddress,
     * >=0 -> index of the request in the arraylist
     */

    private int selectedElement;

    public DeliveryTour() {
        requests = new ArrayList<>();
        path = new ArrayList<>();
        pathAddresses = new ArrayList<>();
        addressRequestMetadata = new ArrayList<>();
        departureTime = new Date();
        selectedElement = -2;
    }

    public List<RoadSegment> getPath() {
        return path;
    }

    public List<Address> getPathAddresses() {
        return pathAddresses;
    }

    public List<Pair<EnumAddressType, Request>> getAddressRequestMetadata() {
        return addressRequestMetadata;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
        this.notifyObservers();
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public Address getDepartureAddress() {
        if (this.pathAddresses.size() > 0) {
            return this.pathAddresses.get(0);
        }
        return null;
    }

    public boolean isSelected(Request request) {
        if (selectedElement >= 0) {
            return requests.get(selectedElement).equals(request);
        } else {
            return false;
        }
    }

    public Request getSelectedRequest() {
        if(selectedElement >= 0) {
            return requests.get(selectedElement);
        }
        return null;
    }

    public boolean isDepartureSelected() {
        return selectedElement == -1;
    }

    public void addAddress(Address addr, EnumAddressType type, Request req) {
        this.addAddressNoNotify(addr, type, req);
        this.notifyObservers();
    }

    public void addAddressNoNotify(Address addr, EnumAddressType type, Request req) {
        this.pathAddresses.add(addr);
        this.addressRequestMetadata.add(new Pair<>(type, req));
    }

    public void addAddressNoNotify(Address addr) {
        this.pathAddresses.add(addr);
    }

    public void addAddress(Address addr) {
        this.addAddressNoNotify(addr);
        this.notifyObservers();
    }

    public void loadRequestsFromFile(File requestsFile, CityMap map) throws RequestsLoadException {
        try {
            this.clear();

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            RequestsXMLHandler handler = new RequestsXMLHandler(this, map);
            parser.parse(requestsFile, handler);

            this.notifyObservers("requests");
        } catch (SAXException | IOException | ParserConfigurationException exception) {
            throw new RequestsLoadException(exception);
        }
    }

    public void clear(boolean clearRequests) {
        if (clearRequests) {
            requests.clear();
            selectedElement = -2;
        }
        path.clear();
        pathAddresses.clear();
        addressRequestMetadata.clear();
        this.notifyObservers();
    }

    public void clear() {
        clear(true);
    }

    public void addRequest(Request request) {
        requests.add(request);
        this.notifyObservers();
    }
    private void updatePath(CityMap map){
        path.clear();
        Address prevAddress =null;
        for (Address address : pathAddresses){
            if (prevAddress != null){
                if (map.findSegment(prevAddress.getId(), address.getId()) != null) {
                    path.add(map.findSegment(prevAddress.getId(), address.getId()));
                }else {
                    System.out.println(prevAddress.getId()+" - "+address.getId());
                }
            }
            prevAddress = address;
        }
        this.notifyObservers();
    }
    public void addPointToPath( EnumAddressType type, Request request, Pair<EnumAddressType, Request> pairPrevious, CityMap map) {

        int indexAddressPrevious = this.addressRequestMetadata.indexOf(pairPrevious);
        Address address = request.getAddress(type);
        Address addressPrevious;
        if (pairPrevious.getX()==EnumAddressType.DEPARTURE_ADDRESS){
            addressPrevious = getDepartureAddress();
        }else {
        addressPrevious =pairPrevious.getY().getAddress(pairPrevious.getX());
        }
        int indexAddressNext = indexAddressPrevious + 1;

        //visit addressRequestMetadata until we find an address that isn't transversal
        while (this.addressRequestMetadata.get(indexAddressNext).getX() == EnumAddressType.TRAVERSAL_ADDRESS) {
            indexAddressNext++;
        }

        Pair<EnumAddressType, Request> addressNextPair = this.addressRequestMetadata.get(indexAddressNext);
        System.out.println(pairPrevious.getX()+" - "+(requests.indexOf(pairPrevious.getY())+1));
        System.out.println(addressNextPair.getX()+" - "+(requests.indexOf(addressNextPair.getY())+1 +" - "+indexAddressNext));

        Address addressNext;
        if (addressNextPair.getX()==EnumAddressType.DEPARTURE_ADDRESS){
            addressNext = getDepartureAddress();
        }else {
            addressNext = addressNextPair.getY().getAddress(addressNextPair.getX());
        }

        ShortestPathWrapper wrapper = new ShortestPathWrapper(map);

        List<Address> newPath = wrapper.shortestPathBetween(addressPrevious, address);
        newPath.remove(newPath.size()-1);
        int indexMiddle = newPath.size() ;

        newPath.addAll(wrapper.shortestPathBetween(address, addressNext));


        for (int i = 0; i < indexAddressNext-indexAddressPrevious+1; i++) {
            this.pathAddresses.remove(indexAddressPrevious);
            this.addressRequestMetadata.remove(indexAddressPrevious);
        }
        int indexToAdd = indexAddressPrevious ;

        for (int j = 0; j < newPath.size(); j++) {
            this.pathAddresses.add(indexToAdd, newPath.get(j));
            if (j == 0) {
                this.addressRequestMetadata.add(indexToAdd, pairPrevious);
            } else if (j == indexMiddle) {
                this.addressRequestMetadata.add(indexToAdd, new Pair<>(type,request));
            } else if (j == newPath.size() - 1) {
                this.addressRequestMetadata.add(indexToAdd, addressNextPair);
            } else {
                this.addressRequestMetadata.add(indexToAdd, new Pair<>(EnumAddressType.TRAVERSAL_ADDRESS, null));
            }
            indexToAdd++;
        }
    }

    // Request modifs from User
    public void addRequestRecompute(Request request, Pair<EnumAddressType, Request> pickupPrevious,
                                    Pair<EnumAddressType, Request> deliveryPrevious, CityMap map) throws Exception {
        if(addressRequestMetadata.indexOf(pickupPrevious) > addressRequestMetadata.indexOf(deliveryPrevious)){
            throw new Exception("Delivery before pickup");
        }
        requests.add(request);

        addPointToPath(EnumAddressType.PICKUP_ADDRESS, request, pickupPrevious, map);

        if (pickupPrevious.equals(deliveryPrevious)){
            deliveryPrevious = new Pair<>(EnumAddressType.PICKUP_ADDRESS,request);
        }
        addPointToPath(EnumAddressType.DELIVERY_ADDRESS, request, deliveryPrevious, map);

        updatePath(map);
    }

    public void deletePointFromPath(Address address, Request request, CityMap map) {

        int indexAddress = this.pathAddresses.indexOf(address);

        //visit addressRequestMetadata to find previous point
        int indexPreviousAddress = indexAddress;
        while (this.addressRequestMetadata.get(indexAddress).getX() == EnumAddressType.TRAVERSAL_ADDRESS) {
            indexPreviousAddress--;
        }
        Address previousAddress = pathAddresses.get(indexPreviousAddress);

        //visit addressRequestMetadata to find next point
        int indexNextAddress = indexAddress;
        while (this.addressRequestMetadata.get(indexAddress).getX() == EnumAddressType.TRAVERSAL_ADDRESS) {
            indexNextAddress++;
        }
        Address nextAddress = pathAddresses.get(indexNextAddress);

        ShortestPathWrapper wrapper = new ShortestPathWrapper(map);
        List<Address> newPath = wrapper.shortestPathBetween(previousAddress, nextAddress);

        for (int i = indexPreviousAddress + 1; i < indexNextAddress; i++) {
            this.pathAddresses.remove(i);
            this.addressRequestMetadata.remove(i);
        }
        int indexToAdd = indexPreviousAddress + 1;

        for (int j = 0; j < newPath.size(); j++) {

            this.pathAddresses.add(indexToAdd, newPath.get(j));

            if (j == 0) {
                this.addressRequestMetadata.add(indexToAdd, this.addressRequestMetadata.get(indexPreviousAddress));
            } else if (j == newPath.size() - 1) {
                this.addressRequestMetadata.add(indexToAdd, this.addressRequestMetadata.get(indexNextAddress));
            } else {
                this.addressRequestMetadata.add(indexToAdd, new Pair<>(EnumAddressType.TRAVERSAL_ADDRESS, null));
            }
            indexToAdd++;
        }
    }


    public void deleteRequestRecompute(Request request, CityMap map) {
        deletePointFromPath(request.getPickupAddress(), request, map);
        deletePointFromPath(request.getDeliveryAddress(), request, map);

        updatePath(map);

    }

    public List<Request> getRequests() {
        return requests;
    }

    private Pair<Double, Integer> getClosestRequest(Coord coords) {

        int closest = 0;
        double minDist = Integer.MAX_VALUE;
        int i = 0;
        for (Request request : requests) {
            if (request.getDeliveryAddress().dist(coords) < minDist) {
                closest = i;
                minDist = request.getDeliveryAddress().dist(coords);
            }
            if (request.getPickupAddress().dist(coords) < minDist) {
                closest = i;
                minDist = request.getPickupAddress().dist(coords);
            }
            i++;
        }


        return new Pair<>(minDist, closest);
    }

    public List<Pair<Double, Pair<EnumAddressType, Request>>> getClosestRequestsFrom(Coord pos, double maxDist) {
        List<Pair<Double, Pair<EnumAddressType, Request>>> res = new ArrayList<>();
        for (Request req : requests) {
            res.add(new Pair<>(req.getPickupAddress().dist(pos), new Pair<>(EnumAddressType.PICKUP_ADDRESS, req)));
            res.add(new Pair<>(req.getDeliveryAddress().dist(pos), new Pair<>(EnumAddressType.DELIVERY_ADDRESS, req)));
        }
        res.add(new Pair<>(this.getDepartureAddress().dist(pos), new Pair<>(EnumAddressType.DEPARTURE_ADDRESS, null)));

        res.sort(Pair::compareTo);
        int index = 0;
        for (Pair<Double, Pair<EnumAddressType, Request>> p : res) {
            if (p.getX() > maxDist) {
                break;
            }
            index++;
        }
        return res.subList(0, index);
    }

    private final static double THRESHOLD = 50d;

    private void toggleSelect(int n) {
        selectedElement = selectedElement != n ? n : -2;

        this.notifyObservers();
    }

    public void selectElement(Coord coords, double threshold) {
        Pair<Double, Integer> res = getClosestRequest(coords);
        double distDeparture = Double.MAX_VALUE;
        Address departure = getDepartureAddress();
        if (departure != null) {
            distDeparture = departure.dist(coords);
        }

        if (distDeparture < res.getX() && distDeparture < threshold) {
            toggleSelect(-1);

        } else if (res.getX() < threshold) {
            toggleSelect(res.getY());
        }
        else {
            this.selectedElement = -2;
        }
    }

    public void selectElement(Coord coords) {
        selectElement(coords, THRESHOLD);
    }

    public void selectDepartureAddress() {
        toggleSelect(-1);
    }

    public void selectRequest(Request request) {
        int index = requests.indexOf(request);
        if ((index < 0)) {
            System.err.println("Request not present");
        } else {
            toggleSelect(index);
        }
    }


    public List<RoadSegment> getSubPathBetweenPoints(Address a1, Address a2) {
        List<Address> addresses = this.pathAddresses;
        int indexAddress1 = getIndexOfAddress(a1);
        int indexAddress2 = getIndexOfAddress(a2);
        return path.subList(indexAddress1, indexAddress2 - 1);
    }

    public int getIndexOfAddress(Address address) {
        List<Address> addresses = this.pathAddresses;
        int index = 0;
        for (int i = 0; i < addresses.size(); i++) {
            if (addresses.get(i).equals(address)) {
                index = i;
            }
        }
        return index;
    }


}
