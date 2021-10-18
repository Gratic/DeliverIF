package xml;

import deliverif.model.Address;
import deliverif.model.CityMap;
import deliverif.model.DeliveryTour;
import deliverif.model.Request;
import deliverif.xml.MapXMLHandler;
import deliverif.xml.RequestsXMLHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;

public class TestRequestsXMLHandler {

    static SAXParserFactory factory;
    static SAXParser parser;
    static RequestsXMLHandler requestsHandler;
    static MapXMLHandler mapHandler;

    static CityMap map;
    static DeliveryTour tour;



    @BeforeAll
    static void init() {
        try {
            factory = SAXParserFactory.newInstance();
            parser = factory.newSAXParser();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void prepHandler() {
        map = new CityMap();
        tour = new DeliveryTour();
        Request.resetRequests();

        requestsHandler = new RequestsXMLHandler(tour, map);
        mapHandler = new MapXMLHandler(map);
    }

    @ParameterizedTest
    @CsvSource({
            "smallMap.xml,  requestsSmall1.xml",
            "smallMap.xml,  requestsSmall2.xml",
            "mediumMap.xml, requestsMedium3.xml",
            "mediumMap.xml, requestsMedium5.xml",
            "largeMap.xml, requestsLarge7.xml",
            "largeMap.xml, requestsLarge9.xml",
    })
    void loadRequests(String mapPath, String requestPath) {
        File fileM = new File("./resources/xml/" + mapPath);
        File fileR = new File("./resources/xml/" + requestPath);

        assertDoesNotThrow(() -> {
            parser.parse(fileM, mapHandler);
            parser.parse(fileR, requestsHandler);
        });
    }

    @Test
    void nominalTest() throws IOException, SAXException {
        File mapFile = new File("./resources/test/xml/nominalTestMap.xml");
        parser.parse(mapFile, mapHandler);

        File requestsFile = new File("./resources/test/xml/nominalRequests.xml");
        parser.parse(requestsFile, requestsHandler);

        Assertions.assertEquals(2, tour.getPathAddresses().get(0).getId());

        Date departureTime = tour.getDepartureTime();
        Assertions.assertEquals(8, departureTime.getHours());
        Assertions.assertEquals(0, departureTime.getMinutes());
        Assertions.assertEquals(0, departureTime.getSeconds());

        Collection<Request> requests = Request.getRequests();
        Assertions.assertEquals(2, requests.size());

        for (Request request : requests) {
            if (request.getPickupAddress().getId() == 6) {
                Assertions.assertEquals(4, request.getDeliveryAddress().getId());
                Assertions.assertEquals(360, request.getPickupDuration());
                Assertions.assertEquals(480, request.getDeliveryDuration());
            } else if (request.getPickupAddress().getId() == 3) {
                Assertions.assertEquals(1, request.getDeliveryAddress().getId());
                Assertions.assertEquals(480, request.getPickupDuration());
                Assertions.assertEquals(0, request.getDeliveryDuration());
            } else {
                fail("Loaded request pickup address does not correspond to the source file contents.");
            }
        }
    }

    @Test
    void invalidPickupAddressIdTest1() throws IOException, SAXException {
        File mapFile = new File("./resources/test/xml/nominalTestMap.xml");
        parser.parse(mapFile, mapHandler);

        File requestsFile = new File("./resources/test/xml/invalidRequests1.xml");
        Assertions.assertThrows(SAXException.class, () -> {
            parser.parse(requestsFile, requestsHandler);
        });
    }

    @Test
    void invalidPickupAddressIdTest2() throws IOException, SAXException {
        File mapFile = new File("./resources/test/xml/nominalTestMap.xml");
        parser.parse(mapFile, mapHandler);

        File requestsFile = new File("./resources/test/xml/invalidRequests8.xml");
        Assertions.assertThrows(SAXException.class, () -> {
            parser.parse(requestsFile, requestsHandler);
        });
    }

    @Test
    void invalidDepotAddressIdTest() throws IOException, SAXException {
        File mapFile = new File("./resources/test/xml/nominalTestMap.xml");
        parser.parse(mapFile, mapHandler);

        File requestsFile = new File("./resources/test/xml/invalidRequests2.xml");
        Assertions.assertThrows(SAXException.class, () -> {
            parser.parse(requestsFile, requestsHandler);
        });
    }

    @Test
    void invalidDepartureTimeTest1() throws IOException, SAXException {
        File mapFile = new File("./resources/test/xml/nominalTestMap.xml");
        parser.parse(mapFile, mapHandler);

        File requestsFile = new File("./resources/test/xml/invalidRequests3.xml");
        Assertions.assertThrows(SAXException.class, () -> {
            parser.parse(requestsFile, requestsHandler);
        });
    }

    @Test
    void invalidDepartureTimeTest2() throws IOException, SAXException {
        File mapFile = new File("./resources/test/xml/nominalTestMap.xml");
        parser.parse(mapFile, mapHandler);

        File requestsFile = new File("./resources/test/xml/invalidRequests4.xml");
        Assertions.assertThrows(SAXException.class, () -> {
            parser.parse(requestsFile, requestsHandler);
        });
    }

    @Test
    void malformedFileTest() throws IOException, SAXException {
        File mapFile = new File("./resources/test/xml/nominalTestMap.xml");
        parser.parse(mapFile, mapHandler);

        File requestsFile = new File("./resources/test/xml/invalidRequests5.xml");
        Assertions.assertThrows(SAXException.class, () -> {
            parser.parse(requestsFile, requestsHandler);
        });
    }

    @Test
    void noDepotTest() throws IOException, SAXException {
        File mapFile = new File("./resources/test/xml/nominalTestMap.xml");
        parser.parse(mapFile, mapHandler);

        File requestsFile = new File("./resources/test/xml/invalidRequests6.xml");
        Assertions.assertThrows(SAXException.class, () -> {
            parser.parse(requestsFile, requestsHandler);
        });
    }

    @Test
    void invalidDeliveryDurationTest() throws IOException, SAXException {
        File mapFile = new File("./resources/test/xml/nominalTestMap.xml");
        parser.parse(mapFile, mapHandler);

        File requestsFile = new File("./resources/test/xml/invalidRequests7.xml");
        Assertions.assertThrows(SAXException.class, () -> {
            parser.parse(requestsFile, requestsHandler);
        });
    }


}
