package pdtsp;

import deliverif.model.CityMap;
import deliverif.model.DeliveryTour;
import deliverif.model.Request;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;

public class TestPDTSPWrapper {

    @ParameterizedTest
    @CsvSource({
            "smallMap.xml,  requestsSmall1.xml,5478.643769799999",
            "smallMap.xml,  requestsSmall2.xml,4378.9992717000005",
            "mediumMap.xml, requestsMedium3.xml,15056.395194100001",
            "mediumMap.xml, requestsMedium5.xml,16081.2154806",
            "largeMap.xml, requestsLarge7.xml,28247.547062600002",
            "largeMap.xml, requestsLarge9.xml,39088.947161",
            "largeMap.xml,  requestsSmall1.xml,5478.643769799999",
            "largeMap.xml,  requestsSmall2.xml,4378.9992717000005",
            "largeMap.xml, requestsMedium3.xml,15056.395194100001",
            "largeMap.xml, requestsMedium5.xml,16054.058871099998",
    })
    void testWithAllFiles(String mapPath, String requestPath, String expectedSolutionCostStr) {
        File fileM = new File("./resources/xml/" + mapPath);
        File fileR = new File("./resources/xml/" + requestPath);
        double expectedSolutionCost = Double.parseDouble(expectedSolutionCostStr);

        CityMap cityMap = new CityMap();
        DeliveryTour deliveryTour = new DeliveryTour();

        try {
            cityMap.loadMapFromFile(fileM);
            deliveryTour.loadRequestsFromFile(fileR, cityMap);
        } catch (Exception e) {
            fail();
        }

        PDTSPWrapper wrapper = new PDTSPWrapper(cityMap, deliveryTour, 9999999);

        assertDoesNotThrow(() -> {
            wrapper.prepare();
            wrapper.compute();
            wrapper.updateDeliveryTour();
        });

        Assertions.assertTrue(wrapper.isSolutionFound());
        Assertions.assertEquals(expectedSolutionCost, wrapper.getSolutionCost());
    }

    @Test
    void testSolutionCost_Stub1() {
        long addressId = 0L;
        double expectedSolutionCost = 6d;

        List<Object> stub = StubPDTSPWrapper.stub1();
        CityMap cityMap = (CityMap) stub.get(0);
        DeliveryTour deliveryTour = (DeliveryTour) stub.get(1);

        // Deux requêtes : une de 1 vers 2 et une de 2 vers 1.
        deliveryTour.addRequest(new Request(cityMap.getAddressById(1L), cityMap.getAddressById(2L), 0, 0));
        deliveryTour.addRequest(new Request(cityMap.getAddressById(2L), cityMap.getAddressById(1L), 0, 0));

        deliveryTour.addAddress(cityMap.getAddressById(addressId));

        PDTSPWrapper wrapper = new PDTSPWrapper(cityMap, deliveryTour, 9999999);

        assertDoesNotThrow(() -> {
            wrapper.prepare();
            wrapper.compute();
            wrapper.updateDeliveryTour();
        });

        Assertions.assertTrue(wrapper.isSolutionFound());
        Assertions.assertEquals(expectedSolutionCost, wrapper.getSolutionCost());
    }

    @ParameterizedTest
    @CsvSource({
            "0,8",
            "1,6",
            "4,5"
    })
    void testSolutionCost_Stub2(String addressIdStr, String expectedSolutionCostStr) {
        long addressId = Long.parseLong(addressIdStr);
        double expectedSolutionCost = Double.parseDouble(expectedSolutionCostStr);

        List<Object> stub = StubPDTSPWrapper.stub2();
        CityMap cityMap = (CityMap) stub.get(0);
        DeliveryTour deliveryTour = (DeliveryTour) stub.get(1);

        // Deux requêtes : une de 1 vers 2 et une de 2 vers 1.
        deliveryTour.addRequest(new Request(cityMap.getAddressById(2L), cityMap.getAddressById(3L), 0, 0));

        deliveryTour.addAddress(cityMap.getAddressById(addressId));

        PDTSPWrapper wrapper = new PDTSPWrapper(cityMap, deliveryTour, 9999999);

        assertDoesNotThrow(() -> {
            wrapper.prepare();
            wrapper.compute();
            wrapper.updateDeliveryTour();
        });

        Assertions.assertEquals(expectedSolutionCost, wrapper.getSolutionCost());
    }

    @ParameterizedTest
    @CsvSource({
            "0,9",
            "1,7",
            "3,6",
            "4,6",
            "5,8",
            "7,7",
            "8,8",
            "9,7",
            "10,8"
    })
    void testSolutionCost_Stub3(String addressIdStr, String expectedSolutionCostStr) {
        long addressId = Long.parseLong(addressIdStr);
        double expectedSolutionCost = Double.parseDouble(expectedSolutionCostStr);

        List<Object> stub = StubPDTSPWrapper.stub3();
        CityMap cityMap = (CityMap) stub.get(0);
        DeliveryTour deliveryTour = (DeliveryTour) stub.get(1);

        // Deux requêtes : une de 1 vers 2 et une de 2 vers 1.
        deliveryTour.addRequest(new Request(cityMap.getAddressById(6L), cityMap.getAddressById(2L), 0, 0));

        deliveryTour.addAddress(cityMap.getAddressById(addressId));

        PDTSPWrapper wrapper = new PDTSPWrapper(cityMap, deliveryTour, 9999999);

        assertDoesNotThrow(() -> {
            wrapper.prepare();
            wrapper.compute();
            wrapper.updateDeliveryTour();
        });

        Assertions.assertTrue(wrapper.isSolutionFound());
        Assertions.assertEquals(expectedSolutionCost, wrapper.getSolutionCost());
    }

    @ParameterizedTest
    @CsvSource({
            "0,13",
            "1,11"
    })
    void testSolutionCost_Stub3_2(String addressIdStr, String expectedSolutionCostStr) {
        long addressId = Long.parseLong(addressIdStr);
        double expectedSolutionCost = Double.parseDouble(expectedSolutionCostStr);

        List<Object> stub = StubPDTSPWrapper.stub3();
        CityMap cityMap = (CityMap) stub.get(0);
        DeliveryTour deliveryTour = (DeliveryTour) stub.get(1);

        // Deux requêtes : une de 1 vers 2 et une de 2 vers 1.
        deliveryTour.addRequest(new Request(cityMap.getAddressById(9L), cityMap.getAddressById(2L), 0, 0));
        deliveryTour.addRequest(new Request(cityMap.getAddressById(5L), cityMap.getAddressById(4L), 0, 0));

        deliveryTour.addAddress(cityMap.getAddressById(addressId));

        PDTSPWrapper wrapper = new PDTSPWrapper(cityMap, deliveryTour, 9999999);

        assertDoesNotThrow(() -> {
            wrapper.prepare();
            wrapper.compute();
            wrapper.updateDeliveryTour();
        });

        Assertions.assertTrue(wrapper.isSolutionFound());
        Assertions.assertEquals(expectedSolutionCost, wrapper.getSolutionCost());
    }
}