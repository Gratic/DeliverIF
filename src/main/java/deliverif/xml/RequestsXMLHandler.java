package deliverif.xml;

import deliverif.model.Address;
import deliverif.model.CityMap;
import deliverif.model.DeliveryTour;
import deliverif.model.Request;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RequestsXMLHandler extends org.xml.sax.helpers.DefaultHandler {

    private final CityMap cityMap;
    private final DeliveryTour tour;
    private boolean depotFound = false;


    public RequestsXMLHandler(DeliveryTour tour, CityMap cityMap) {
        this.tour = tour;
        this.cityMap = cityMap;
    }


    @Override
    public void endDocument() throws  SAXException{
        if (!depotFound){
            throw new SAXException("No depot found in file");
        }
    }
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        switch (qName) {
            case "depot" -> {
                depotFound = true;
                String addressString = attributes.getValue("address");
                String timeString = attributes.getValue("departureTime");

                if (addressString == null || timeString == null) {

                    throw new SAXException("XML file malformed");
                }

                try {
                    long idAddr = Long.parseLong(addressString);
                    Address addr = this.cityMap.getAddressById(idAddr);
                    if (addr == null) {
                        throw new SAXException("Address with id " + idAddr + " does not exist");
                    }
                    this.tour.addAddress(addr);

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    throw new SAXException("AddressID \"" + addressString + "\" is not a valid number");
                }



                SimpleDateFormat sdf = new SimpleDateFormat("h:m:s");
                sdf.setLenient(false);
                try {
                    this.tour.setDepartureTime(sdf.parse(timeString));
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new SAXException("Time " + timeString + " is not a valid time format");
                }

            }
            case "request" -> {
                String pickupAddString = attributes.getValue("pickupAddress");
                String deliveryAddString = attributes.getValue("deliveryAddress");
                String pickupDurString = attributes.getValue("pickupDuration");
                String deliveryDurString = attributes.getValue("deliveryDuration");

                if (pickupAddString == null || deliveryAddString == null
                        || pickupDurString == null || deliveryDurString == null) {
                    throw new SAXException("XML file malformed");
                }

                long pickIdAddr, deliIdAddr;
                int pickupDuration, deliveryDuration;

                try {
                    pickIdAddr = Long.parseLong(pickupAddString);
                    deliIdAddr = Long.parseLong(deliveryAddString);
                    pickupDuration = Integer.parseInt(pickupDurString);
                    deliveryDuration = Integer.parseInt(deliveryDurString);
                } catch (NumberFormatException e) {
                    throw new SAXException("One of the following is not a valid number:  \"" + pickupAddString +
                            "\", \"" + pickupDurString + "\", \"" +
                            deliveryAddString + "\", \"" + deliveryAddString);
                }

                Address pickupAddress = cityMap.getAddressById(pickIdAddr);
                Address deliveryAddress = cityMap.getAddressById(deliIdAddr);

                if (pickupAddress == null) {
                    throw new SAXException("Address with id " + pickIdAddr + " does not exist");
                }
                if (deliveryAddress == null) {
                    throw new SAXException("Address with id " + deliIdAddr + " does not exist");
                }

                if (deliveryDuration < 0 || pickupDuration <0){
                    throw  new SAXException("Durations must be positive integers");
                }

                Request req = new Request(pickupAddress, deliveryAddress, pickupDuration, deliveryDuration);

                Request.addRequest(req);
            }
            case "planningRequest" ->{}
            default -> throw new SAXException("Tag "+qName+" is not a valid tag");
        }
    }

}

