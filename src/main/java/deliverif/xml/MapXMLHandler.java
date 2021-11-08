package deliverif.xml;
import deliverif.model.Address;
import deliverif.model.CityMap;
import deliverif.model.RoadSegment;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;



public class MapXMLHandler extends org.xml.sax.helpers.DefaultHandler {
    private final CityMap cityMap;



    public MapXMLHandler(CityMap cityMap) {
        this.cityMap = cityMap;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        switch (qName) {
            case "intersection":
                String idString = attributes.getValue("id");
                String latString = attributes.getValue("latitude");
                String lonString = attributes.getValue("longitude");

                if (idString == null || latString == null || lonString == null) {
                    throw new SAXException("XML file malformed");
                }

                long id;
                try {
                    id = Long.parseLong(idString);
                } catch (NumberFormatException e) {
                    throw new SAXException("Address id \"" +
                            idString + "\" is not a valid number");
                }

                if (cityMap.getAddressById(id) != null) {
                    throw new SAXException("Duplicate address id \"" + idString + "\"");
                }

                double latitude, longitude;
                try {
                    latitude = Double.parseDouble(latString);
                    longitude = Double.parseDouble(lonString);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    throw new SAXException("One of the following is not a valid floating point number: \""
                            + latString + "\", \"" + lonString + "\" ");
                }

                Address address = new Address(id, latitude, longitude);

                cityMap.getAddresses().put(id, address);
                break;
            case "segment":
                String destString = attributes.getValue("destination");
                String origString = attributes.getValue("origin");
                String lenString = attributes.getValue("length");
                String name = attributes.getValue("name");



                if (destString == null || origString == null || lenString == null || name == null) {
                    throw new SAXException("XML file malformed");
                }

                long destId, origId;
                try {
                    destId = Long.parseLong(destString);
                    origId = Long.parseLong(origString);

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    throw new SAXException("One of the following is not a valid number : \""
                            + destString + "\", \"" + origString + "\"");
                }

                Address destination = cityMap.getAddressById(destId);
                Address origin = cityMap.getAddressById(origId);

                if (destination == null) {
                    throw new SAXException("Address with id " + destId + "does not exist");
                }
                if (origin == null) {
                    throw new SAXException("Address with id " + origId + "does not exist");
                }

                double length;
                try {
                    length = Double.parseDouble(lenString);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    throw new SAXException("Length " + lenString + " is not a valid floating point number");
                }

                RoadSegment segment = new RoadSegment(origin, destination, name, length);
                cityMap.addSegment(segment);
                break;
            case "map":
                break;
            default:
                throw new SAXException(qName);
        }
    }
}
