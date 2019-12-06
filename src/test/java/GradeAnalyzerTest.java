import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.LinkedList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertEquals;

public class GradeAnalyzerTest {

    // create dummy test data
    GradeAnalyzer gradeAnalyzer1 = new GradeAnalyzer();
    GradeAnalyzer gradeAnalyzer2 = new GradeAnalyzer();
    GradeAnalyzer gradeAnalyzer3 = new GradeAnalyzer();
    LinkedList<Double> values1 = new LinkedList<>();
    LinkedList<Double> values2 = new LinkedList<>();
    LinkedList<Double> values3 = new LinkedList<>();

    @Before
    public void createTestData() {
        gradeAnalyzer1.setLowerBound(0.0);
        gradeAnalyzer1.setUpperBound(100.0);

        gradeAnalyzer1.addValue(2.1);
        gradeAnalyzer1.addValue(99.8);
        gradeAnalyzer1.addValue(87.2);
        gradeAnalyzer1.addValue(73.7);
        gradeAnalyzer1.addValue(32.0113);
        gradeAnalyzer1.addValue(69.4330);
        gradeAnalyzer1.addValue(84.21);
        gradeAnalyzer1.addValue(85.4);
        gradeAnalyzer1.addValue(85.4);
        gradeAnalyzer1.addValue(87.90);
        gradeAnalyzer1.addValue(84.21);
        gradeAnalyzer1.addValue(100.00);


        gradeAnalyzer2.setLowerBound(10.0);
        gradeAnalyzer2.setUpperBound(50.0);

        gradeAnalyzer2.addValue(11.2);
        gradeAnalyzer2.addValue(14.87);
        gradeAnalyzer2.addValue(45.0);
        gradeAnalyzer2.addValue(49.99);
        gradeAnalyzer2.addValue(10.1);
        gradeAnalyzer2.addValue(32.90);
        gradeAnalyzer2.addValue(27.54);
        gradeAnalyzer2.addValue(49.6);
        gradeAnalyzer2.addValue(37.33);
        gradeAnalyzer2.addValue(48.10);

        gradeAnalyzer3.setLowerBound(0);
        gradeAnalyzer3.setUpperBound(10);

        gradeAnalyzer3.addValue(3);
        gradeAnalyzer3.addValue(7);
        gradeAnalyzer3.addValue(7);
        gradeAnalyzer3.addValue(9);
        gradeAnalyzer3.addValue(7);
        gradeAnalyzer3.addValue(5);
        gradeAnalyzer3.addValue(0);
    }

    @Test
    public void testGetAmount() {
        assertEquals(gradeAnalyzer1.getAmount(), 12, .001);
        assertEquals(gradeAnalyzer2.getAmount(), 10, .001);
        assertEquals(gradeAnalyzer3.getAmount(), 7, .001);
    }

    @Test
    public void testGetMaximum() {
        assertEquals(gradeAnalyzer1.getMaximum(), 100.00, .001);
        assertEquals(gradeAnalyzer2.getMaximum(), 49.99, .001);
    }

    @Test
    public void testGetMinimum() {
        assertEquals(gradeAnalyzer1.getMinimum(), 2.1, .001);
        assertEquals(gradeAnalyzer2.getMinimum(), 10.1, .001);
        assertEquals(gradeAnalyzer3.getMinimum(), 0, 0.0001);
    }

    @Test
    public void testGetMean() {
        assertEquals(gradeAnalyzer1.getMean(), 74.28035833, .01);
        assertEquals(gradeAnalyzer2.getMean(), 32.663, .01);
        assertEquals(gradeAnalyzer3.getMean(), 5.42857, .01);
    }

    @Test
    public void testGetMedian() {
        // Even number of entries in dataset
        assertEquals(gradeAnalyzer1.getMedian(), 84.805, .01);
        assertEquals(gradeAnalyzer2.getMedian(), 35.115, .01);
        // Odd number of entries in dataset
        assertEquals(gradeAnalyzer3.getMedian(), 7, 0);
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testGetMode() {
        //multiple modes
        assertThat(gradeAnalyzer1.getMode(), hasItems(85.4, 84.21));
        assertEquals(gradeAnalyzer1.getMode().size(), 2);

        // No mode
        gradeAnalyzer2.getMode();
        exceptionRule.expectMessage("No mode.");

        // One mode
      //  assertThat(gradeAnalyzer3.getMode(), (Matcher<? super LinkedList<Double>>) hasItems(7));
        assertEquals(gradeAnalyzer3.getMode().size(), 1);

    }
}