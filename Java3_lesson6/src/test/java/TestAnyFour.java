import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestAnyFour {
    Main main;

    @Before
    public void init() {
        main = new Main();
    }

    @Test
    public void testfour1() {
        Assert.assertArrayEquals(new int[]{1, 7}, main.arrayAnyOneFour(new int[]{1, 2, 4, 4, 2, 3, 4, 1, 7}));
    }

    @Test
    public void testfour2() {
        Assert.assertArrayEquals(new int[]{7, 8, 9}, main.arrayAnyOneFour(new int[]{1, 2, 3, 4, 0, 4, 7, 8, 9}));
    }

    @Test
    public void testfour3() {
        Assert.assertArrayEquals(new int[]{1, 2, 3}, main.arrayAnyOneFour(new int[]{4, 1, 2, 3}));
    }

    @Test(expected = RuntimeException.class)
    public void testfour4() {
        Assert.assertArrayEquals(new int[]{2, 3}, main.arrayAnyOneFour(new int[]{1, 1, 2, 3}));
    }

}
