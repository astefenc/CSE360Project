import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class GradeAnalyzer {
    List<Double> values;

    GradeAnalyzer(){
        this.values = new LinkedList<Double>();
    }

    int getAmount() throws RuntimeException{}

    double getMaximum() throws RuntimeException{}

    double getMinimum() throws RuntimeException{}

    double getMean() throws RuntimeException{}

    double getMedian() throws RuntimeException{}

    double getMode() throws RuntimeException{}

    List<Double> getValues() throws RuntimeException{
        return this.values;
    }

    List<Double> getGraph() throws RuntimeException{}

    List<Double> getPercentiles() throws RuntimeException{}

    void loadFile(File file) throws RuntimeException{}

    void addValue(double value) throws RuntimeException{}

    void deleteValue(double value) throws RuntimeException{}

    void clearValues() throws RuntimeException{}


    /*      Runetime Exception example
                custom error messages should be as declared in user guide
     */
    void example() throws RuntimeException{
        throw new RuntimeException("CUSTOM ERROR MSG");
    }

}
