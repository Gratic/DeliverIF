package pdtsp;

import deliverif.model.Address;
import deliverif.model.CityMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class TestShortestPathWrapper {

    @Test
    public void test1() {
        CityMap map = (CityMap) StubPDTSPWrapper.stub1().get(0);

        Address add0 = map.getAddressById(0);
        Address add1 = map.getAddressById(1);
        Address add2 = map.getAddressById(2);

        ShortestPathWrapper spw = new ShortestPathWrapper(map);


        List<Address> path01 = spw.shortestPathBetween(add0, add1);
        List<Address> path10 = spw.shortestPathBetween(add1, add0);
        List<Address> path02 = spw.shortestPathBetween(add0, add2);
        List<Address> path20 = spw.shortestPathBetween(add2, add0);
        List<Address> path12 = spw.shortestPathBetween(add1, add2);
        List<Address> path21 = spw.shortestPathBetween(add2, add1);

        Assertions.assertEquals(2, path01.size());
        Assertions.assertEquals(2, path10.size());
        Assertions.assertEquals(2, path02.size());
        Assertions.assertEquals(2, path20.size());
        Assertions.assertEquals(2, path12.size());
        Assertions.assertEquals(2, path21.size());

        Assertions.assertEquals(add0, path01.get(0));
        Assertions.assertEquals(add1, path01.get(1));
        Assertions.assertEquals(add1, path10.get(0));
        Assertions.assertEquals(add0, path10.get(1));
        Assertions.assertEquals(add0, path02.get(0));
        Assertions.assertEquals(add2, path02.get(1));
        Assertions.assertEquals(add2, path20.get(0));
        Assertions.assertEquals(add0, path20.get(1));
        Assertions.assertEquals(add1, path12.get(0));
        Assertions.assertEquals(add2, path12.get(1));
        Assertions.assertEquals(add2, path21.get(0));
        Assertions.assertEquals(add1, path21.get(1));

    }

    @Test
    public void test2() {
        CityMap map = (CityMap) StubPDTSPWrapper.stub2().get(0);

        Address add0 = map.getAddressById(0);
        Address add1 = map.getAddressById(1);
        Address add2 = map.getAddressById(2);
        Address add3 = map.getAddressById(3);
        Address add4 = map.getAddressById(4);

        ShortestPathWrapper spw = new ShortestPathWrapper(map);

        List<Address> path01 = spw.shortestPathBetween(add0, add1);
        List<Address> path02 = spw.shortestPathBetween(add0, add2);
        List<Address> path03 = spw.shortestPathBetween(add0, add3);
        List<Address> path04 = spw.shortestPathBetween(add0, add4);
        List<Address> path12 = spw.shortestPathBetween(add1, add2);
        List<Address> path13 = spw.shortestPathBetween(add1, add3);
        List<Address> path14 = spw.shortestPathBetween(add1, add4);
        List<Address> path23 = spw.shortestPathBetween(add2, add3);
        List<Address> path24 = spw.shortestPathBetween(add2, add4);
        List<Address> path34 = spw.shortestPathBetween(add3, add4);

        Assertions.assertEquals(2, path01.size());
        Assertions.assertEquals(3, path02.size());
        Assertions.assertEquals(2, path12.size());
        Assertions.assertEquals(2, path03.size());
        Assertions.assertEquals(3, path13.size());
        Assertions.assertEquals(2, path23.size());
        Assertions.assertEquals(3, path04.size());
        Assertions.assertEquals(3, path14.size());
        Assertions.assertEquals(2, path24.size());
        Assertions.assertEquals(2, path34.size());

        Assertions.assertTrue(path02.get(1) == add1 || path02.get(1) == add3);
        Assertions.assertEquals(add2, path13.get(1));
        Assertions.assertEquals(add3, path04.get(1));
        Assertions.assertEquals(add2, path14.get(1));
    }

    @Test
    public void test3() {
        CityMap map = (CityMap) StubPDTSPWrapper.stub3().get(0);

        Address add0 = map.getAddressById(0);
        Address add1 = map.getAddressById(1);
        Address add2 = map.getAddressById(2);
        Address add3 = map.getAddressById(3);
        Address add4 = map.getAddressById(4);
        Address add5 = map.getAddressById(5);
        Address add6 = map.getAddressById(6);
        Address add7 = map.getAddressById(7);
        Address add8 = map.getAddressById(8);
        Address add9 = map.getAddressById(9);
        Address add10 = map.getAddressById(10);

        ShortestPathWrapper spw = new ShortestPathWrapper(map);

        List<Address> path01 = spw.shortestPathBetween(add0, add1);
        List<Address> path02 = spw.shortestPathBetween(add0, add2);
        List<Address> path03 = spw.shortestPathBetween(add0, add3);
        List<Address> path04 = spw.shortestPathBetween(add0, add4);
        List<Address> path05 = spw.shortestPathBetween(add0, add5);
        List<Address> path28 = spw.shortestPathBetween(add2, add8);
        List<Address> path73 = spw.shortestPathBetween(add7, add3);
        List<Address> path610 = spw.shortestPathBetween(add6, add10);

        Assertions.assertArrayEquals(path01.toArray(),
                new Object[]{add0, add1});
        Assertions.assertTrue(
                Arrays.equals(path02.toArray(), new Object[]{add0, add1, add7, add2}) ||
                        Arrays.equals(path02.toArray(), new Object[]{add0, add1, add9, add2})
        );
        Assertions.assertArrayEquals(path03.toArray(),
                new Object[]{add0, add6, add4, add3});
        Assertions.assertArrayEquals(path04.toArray(),
                new Object[]{add0, add6, add4});
        Assertions.assertArrayEquals(path05.toArray(),
                new Object[]{add0, add6, add5});
        Assertions.assertArrayEquals(path28.toArray(),
                new Object[]{add2, add8});
        Assertions.assertArrayEquals(path73.toArray(),
                new Object[]{add7, add3});
        Assertions.assertArrayEquals(path610.toArray(),
                new Object[]{add6, add4, add3, add10});

    }
};
