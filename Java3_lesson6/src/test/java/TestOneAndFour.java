import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestOneAndFour {
    private int[] a;
    private boolean check;
    Main main;

    @Before
    public void init() {
        main = new Main();
    }

    public TestOneAndFour(int[] a, boolean check) {
        this.a = a;
        this.check = check;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new int[]{1, 1, 1, 1, 1, 1}, false},
                {new int[]{1,1,1,4,4,1,4,4},true},
                {new int[]{1,4,4,1,1,4,3},false},
                {new int[]{4,4,4,4},false}
        });
    }

    @Test
    public void testOneAndFour() {
        Assert.assertEquals(main.oneOrFour(a), check);
    }
}
