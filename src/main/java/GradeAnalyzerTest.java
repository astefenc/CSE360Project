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

    @Before
    public void createTestData() {
        values1.add(2.1);
        values1.add(99.8);
        values1.add(87.2);
        values1.add(73.7);
        values1.add(32.0113);
        values1.add(69.4330);
        values1.add(84.21);
        values1.add(100.00);

        gradeAnalyzer1.setLowerBound(0.0);
        gradeAnalyzer1.setUpperBound(100.0);
        gradeAnalyzer1.setValues(values1);

        values2.add(10.0);
        values2.add(11.2);
        values2.add(14.87);
        values2.add(45.0);
        values2.add(49.99);
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
        assertEquals(gradeAnalyzer1.getAmount(), 8);
        assertEquals(gradeAnalyzer2.getAmount(), 10);
    }

    @Test
    void testGetMaximum() {
        assertEquals(gradeAnalyzer1.getMaximum(), 100.00);
        assertEquals(gradeAnalyzer2.getMaximum(), 49.99);
    }

}