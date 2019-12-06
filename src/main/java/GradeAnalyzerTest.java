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
    List<Double> values1 = new LinkedList<>();
    List<Double> values2 = new LinkedList<>();
    List<Double> values3 = new LinkedList<>();

    //TODO:Ange; Make test lists more robust, then calculate actual values, and use those in tests.


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
        assertEquals(gradeAnalyzer1.getAmount(), 11, .001);
        assertEquals(gradeAnalyzer2.getAmount(), 10, .001);
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
    }

    @Test
    void testGetMean() {
        //TODO - Ange; should probably just hardcode actual mean value
        double mean1 = 0;
        double mean2 = 0;
        double sum1 = 0;
        for (Double value : values1) {
            sum1 += value;
        }
        assertEquals(gradeAnalyzer1.getMean(), mean1, .001);
        assertEquals(gradeAnalyzer2.getMean(), mean2, .001);
    }

    @Test
    void testGetMedian() {
        double median1 = 0;
        double median2 = 0;

        assertEquals(gradeAnalyzer1.getMedian(), median1, .001);
        assertEquals(gradeAnalyzer2.getMedian(), median2, .001);
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    void testGetMode() {
        //multiple modes
        List<Double> actualModes1 = gradeAnalyzer1.getMode();
        assertThat(actualModes1, hasItems(85.4, 84.21));
        assertEquals(actualModes1.size(), 2);

        // No mode
        gradeAnalyzer2.getMode();
        exceptionRule.expectMessage("No mode.");

        // One mode
        assertThat(gradeAnalyzer3.getMode(), hasItems(7));
        assertEquals(gradeAnalyzer3.getMode().size(), 1);

    }

    @Test
    void testAddValue() {
        //TODO - where are values getting added (front or end of list)? Try one case where value added is legal

        gradeAnalyzer2.addValue(63);
        exceptionRule.expectMessage("Input value(s) out of range.");
    }



}