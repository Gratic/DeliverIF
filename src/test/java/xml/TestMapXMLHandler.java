package xml;

import deliverif.model.Address;
import deliverif.model.CityMap;
import deliverif.model.RoadSegment;
import deliverif.xml.MapXMLHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;

public class TestMapXMLHandler {

    static SAXParserFactory factory;
    static SAXParser parser;
    static MapXMLHandler handler;

    static CityMap map;

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
        handler = new MapXMLHandler(map);
    }

    @ParameterizedTest
    @ValueSource(strings = {"smallMap.xml", "mediumMap.xml", "largeMap.xml"})
    void loadMap(String path) {
        File file = new File("./resources/xml/" + path);
        assertDoesNotThrow(() -> parser.parse(file, handler));
    }

    @Test
    void nominalTest() throws IOException, SAXException {
        File mapFile = new File("./resources/test/xml/nominalTestMap.xml");
        parser.parse(mapFile, handler);

        Assertions.assertEquals(6, map.getAddresses().size());
        Assertions.assertEquals(6, map.getSegments().size());  // adjacency list size

        Assertions.assertEquals(2, map.getSegmentsOriginatingFrom(1).size());
        Assertions.assertEquals(3, map.getSegmentsOriginatingFrom(2).size());
        Assertions.assertEquals(2, map.getSegmentsOriginatingFrom(3).size());
        Assertions.assertEquals(2, map.getSegmentsOriginatingFrom(4).size());
        Assertions.assertEquals(1, map.getSegmentsOriginatingFrom(5).size());
        Assertions.assertEquals(1, map.getSegmentsOriginatingFrom(6).size());

        Address addr1 = map.getAddressById(1);
        Assertions.assertEquals(1.0, addr1.getLatitude());
        Assertions.assertEquals(-1.0, addr1.getLongitude());

        Address addr6 = map.getAddressById(6);
        Assertions.assertEquals(0.0, addr6.getLatitude());
        Assertions.assertEquals(-2.0, addr6.getLongitude());

        for (RoadSegment segment : map.getSegmentsOriginatingFrom(1)) {
            if (segment.getDestination().getId() == 2) {
                Assertions.assertEquals(2.0, segment.getLength());
                Assertions.assertEquals("Rue Placard", segment.getName());
            } else if (segment.getDestination().getId() == 6) {
                Assertions.assertEquals(1.0, segment.getLength());
                Assertions.assertEquals("Rue plouf", segment.getName());
            }
        }

        for (RoadSegment segment : map.getSegmentsOriginatingFrom(4)) {
            if (segment.getDestination().getId() == 3) {
                Assertions.assertEquals(1.0, segment.getLength());
                Assertions.assertEquals(0, segment.getName().length());
            } else if (segment.getDestination().getId() == 5) {
                Assertions.assertEquals(2.0, segment.getLength());
                Assertions.assertEquals("Allée du meuble à chaussures", segment.getName());
            } else {
                fail("An unknown segment was loaded.");
            }
        }
    }

    @Test
    void invalidMapOnlySegmentsTest() {
        File mapFile = new File("./resources/test/xml/invalidTestMap1.xml");
        Assertions.assertThrows(SAXException.class, () -> {
            parser.parse(mapFile, handler);
        });
    }

    @Test
    void invalidMapEmptyFileTest() {
        File mapFile = new File("./resources/test/xml/invalidTestMap2.xml");
        Assertions.assertThrows(SAXException.class, () -> {
            parser.parse(mapFile, handler);
        });
    }

    @Test
    void invalidMapMalformedFileTest1() {
        File mapFile = new File("./resources/test/xml/invalidTestMap3.xml");
        Assertions.assertThrows(SAXException.class, () -> {
            parser.parse(mapFile, handler);
        });
    }

    @Test
    void invalidMapMalformedFileTest2() {
        File mapFile = new File("./resources/test/xml/invalidTestMap4.xml");
        Assertions.assertThrows(SAXException.class, () -> {
            parser.parse(mapFile, handler);
        });
    }

    @Test
    void invalidMapInvalidAddressIdTest() {
        File mapFile = new File("./resources/test/xml/invalidTestMap5.xml");
        Assertions.assertThrows(SAXException.class, () -> {
            parser.parse(mapFile, handler);
        });
    }
}
