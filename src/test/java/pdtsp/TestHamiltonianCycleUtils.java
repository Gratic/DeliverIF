package pdtsp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TestHamiltonianCycleUtils {
    @Test
    void twoOptMoveTest() {
        List<Integer> H = new ArrayList<>();

        for (int i = 1; i < 5; i++)
            H.add(i);

        H = HamiltonianCycleUtils.twoOptMove(H, 2, 4);

        List<Integer> expectedResult = new ArrayList<>();
        expectedResult.add(1);
        expectedResult.add(2);
        expectedResult.add(4);
        expectedResult.add(3);

        for (int i = 0; i < H.size(); i++) {
            Assertions.assertEquals(expectedResult.get(i), H.get(i));
        }
    }

    @Test
    void threeOptMove0Test() {
        List<Integer> H = new ArrayList<>();

        for (int i = 1; i < 7; i++)
            H.add(i);

        H = HamiltonianCycleUtils.threeOptMove(H, 2, 4, 6, 0);

        List<Integer> expectedResult = new ArrayList<>();
        expectedResult.add(1);
        expectedResult.add(2);
        expectedResult.add(5);
        expectedResult.add(6);
        expectedResult.add(3);
        expectedResult.add(4);

        for (int i = 0; i < H.size(); i++) {
            Assertions.assertEquals(expectedResult.get(i), H.get(i));
        }
    }

    @Test
    void threeOptMove1Test() {
        List<Integer> H = new ArrayList<>();

        for (int i = 1; i < 7; i++)
            H.add(i);

        H = HamiltonianCycleUtils.threeOptMove(H, 2, 4, 6, 1);

        List<Integer> expectedResult = new ArrayList<>();
        expectedResult.add(1);
        expectedResult.add(2);
        expectedResult.add(6);
        expectedResult.add(5);
        expectedResult.add(3);
        expectedResult.add(4);

        for (int i = 0; i < H.size(); i++) {
            Assertions.assertEquals(expectedResult.get(i), H.get(i));
        }
    }

    @Test
    void threeOptMove2Test() {
        List<Integer> H = new ArrayList<>();

        for (int i = 1; i < 7; i++)
            H.add(i);

        H = HamiltonianCycleUtils.threeOptMove(H, 2, 4, 6, 2);

        List<Integer> expectedResult = new ArrayList<>();
        expectedResult.add(1);
        expectedResult.add(2);
        expectedResult.add(5);
        expectedResult.add(6);
        expectedResult.add(4);
        expectedResult.add(3);

        for (int i = 0; i < H.size(); i++) {
            Assertions.assertEquals(expectedResult.get(i), H.get(i));
        }
    }

    @Test
    void threeOptMove3Test() {
        List<Integer> H = new ArrayList<>();

        for (int i = 1; i < 7; i++)
            H.add(i);

        H = HamiltonianCycleUtils.threeOptMove(H, 2, 4, 6, 3);

        List<Integer> expectedResult = new ArrayList<>();
        expectedResult.add(1);
        expectedResult.add(2);
        expectedResult.add(4);
        expectedResult.add(3);
        expectedResult.add(6);
        expectedResult.add(5);

        for (int i = 0; i < H.size(); i++) {
            Assertions.assertEquals(expectedResult.get(i), H.get(i));
        }
    }

    @Test
    void threeOptMove4Test() {
        List<Integer> H = new ArrayList<>();

        for (int i = 1; i < 7; i++)
            H.add(i);

        H = HamiltonianCycleUtils.threeOptMove(H, 2, 4, 6, 4);

        List<Integer> expectedResult = new ArrayList<>();
        expectedResult.add(1);
        expectedResult.add(2);
        expectedResult.add(3);
        expectedResult.add(4);
        expectedResult.add(6);
        expectedResult.add(5);

        for (int i = 0; i < H.size(); i++) {
            Assertions.assertEquals(expectedResult.get(i), H.get(i));
        }
    }

    @Test
    void threeOptMove5Test() {
        List<Integer> H = new ArrayList<>();

        for (int i = 1; i < 7; i++)
            H.add(i);

        H = HamiltonianCycleUtils.threeOptMove(H, 2, 4, 6, 5);

        List<Integer> expectedResult = new ArrayList<>();
        expectedResult.add(1);
        expectedResult.add(2);
        expectedResult.add(4);
        expectedResult.add(3);
        expectedResult.add(5);
        expectedResult.add(6);

        for (int i = 0; i < H.size(); i++) {
            Assertions.assertEquals(expectedResult.get(i), H.get(i));
        }
    }

    @Test
    void threeOptMove6Test() {
        List<Integer> H = new ArrayList<>();

        for (int i = 1; i < 7; i++)
            H.add(i);

        H = HamiltonianCycleUtils.threeOptMove(H, 2, 4, 6, 6);

        List<Integer> expectedResult = new ArrayList<>();
        expectedResult.add(1);
        expectedResult.add(2);
        expectedResult.add(6);
        expectedResult.add(5);
        expectedResult.add(4);
        expectedResult.add(3);

        for (int i = 0; i < H.size(); i++) {
            Assertions.assertEquals(expectedResult.get(i), H.get(i));
        }
    }
}
