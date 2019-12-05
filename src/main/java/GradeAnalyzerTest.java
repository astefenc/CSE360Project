import org.junit.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

class GradeAnalyzerTest {

    // create dummy test data
    GradeAnalyzer gradeAnalyzer1 = new GradeAnalyzer();
    GradeAnalyzer gradeAnalyzer2 = new GradeAnalyzer();
    List<Double> values1 = new LinkedList<>();
    List<Double> values2 = new LinkedList<>();

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

    @Test
    void testGetMode() {
        double mode1 = 0;
        double mode2 = 0;

        assertEquals(gradeAnalyzer1.getMode(), mode1, .001);
        assertEquals(gradeAnalyzer2.getMode(), mode2, .001);
    }

    @Test
    void testAddValue() {


    }



}