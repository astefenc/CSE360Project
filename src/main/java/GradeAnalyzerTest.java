import org.hamcrest.Matcher;
import org.junit.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.LinkedList;
import java.util.List;

class GradeAnalyzerTest {

    // create dummy test data
    GradeAnalyzer gradeAnalyzer1 = new GradeAnalyzer();
    GradeAnalyzer gradeAnalyzer2 = new GradeAnalyzer();
    GradeAnalyzer gradeAnalyzer3 = new GradeAnalyzer();
    LinkedList<Double> values1 = new LinkedList<>();
    LinkedList<Double> values2 = new LinkedList<>();
    LinkedList<Double> values3 = new LinkedList<>();

    @Before
    public void createTestData() {
        values1.add(2.1);
        values1.add(99.8);
        values1.add(87.2);
        values1.add(73.7);
        values1.add(32.0113);
        values1.add(69.4330);
        values1.add(84.21);
        values1.add(85.4);
        values1.add(85.4);
        values1.add(87.90);
        values1.add(84.21);
        values1.add(100.00);

        gradeAnalyzer1.setLowerBound(0.0);
        gradeAnalyzer1.setUpperBound(100.0);
        gradeAnalyzer1.setValues(values1);

        values2.add(11.2);
        values2.add(14.87);
        values2.add(45.0);
        values2.add(49.99);
        values2.add(10.1);
        values2.add(32.90);
        values2.add(27.54);
        values2.add(49.6);
        values2.add(37.33);
        values2.add(48.10);

        gradeAnalyzer2.setLowerBound(10.0);
        gradeAnalyzer2.setUpperBound(50.0);

        gradeAnalyzer3.addValue(3);
        gradeAnalyzer3.addValue(7);
        gradeAnalyzer3.addValue(7);
        gradeAnalyzer3.addValue(9);
        gradeAnalyzer3.addValue(7);
        gradeAnalyzer3.addValue(5);
        gradeAnalyzer3.addValue(0);

        gradeAnalyzer3.setLowerBound(0);
        gradeAnalyzer3.setUpperBound(10);
        gradeAnalyzer3.setValues(values3);
    }

    @Test
    void testGetAmount() {
        assertEquals(gradeAnalyzer1.getAmount(), 12, .001);
        assertEquals(gradeAnalyzer2.getAmount(), 10, .001);
        assertEquals(gradeAnalyzer3.getAmount(), 7, .001);
    }

    @Test
    void testGetMaximum() {
        assertEquals(gradeAnalyzer1.getMaximum(), 100.00, .001);
        assertEquals(gradeAnalyzer2.getMaximum(), 49.99, .001);
    }

    @Test
    void testGetMinimum() {
        assertEquals(gradeAnalyzer1.getMinimum(), 2.1, .001);
        assertEquals(gradeAnalyzer2.getMinimum(), 10.1, .001);
        assertEquals(gradeAnalyzer3.getMinimum(), 0, 0.0001);
    }

    @Test
    void testGetMean() {
        assertEquals(gradeAnalyzer1.getMean(), 74.28035833, .01);
        assertEquals(gradeAnalyzer2.getMean(), 32.663, .01);
        assertEquals(gradeAnalyzer3.getMean(), 5.42857, .01);
    }

    @Test
    void testGetMedian() {
        // Even number of entries in dataset
        assertEquals(gradeAnalyzer1.getMedian(), 84.805, .01);
        assertEquals(gradeAnalyzer2.getMedian(), 35.115, .01);
        // Odd number of entries in dataset
        assertEquals(gradeAnalyzer3.getMedian(), 7, 0);
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    void testGetMode() {
        //multiple modes
        assertThat(gradeAnalyzer1.getMode(), hasItems(85.4, 84.21));
        assertEquals(gradeAnalyzer1.getMode().size(), 2);

        // No mode
        gradeAnalyzer2.getMode();
        exceptionRule.expectMessage("No mode.");

        // One mode
        assertThat(gradeAnalyzer3.getMode(), (Matcher<? super LinkedList<Double>>) hasItems(7));
        assertEquals(gradeAnalyzer3.getMode().size(), 1);

    }
}