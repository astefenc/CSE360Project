import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class GradeAnalyzer {
    List<Double> values;
    double lowerBound, upperBound;

    GradeAnalyzer(){
        this.values = new LinkedList<Double>();
        this.lowerBound = 0.0;
        this.upperBound = 100.0;
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

    List<Double> getGraph() throws RuntimeException{
        //WIP - Robert
        List<Double> graphData = new LinkedList<>();
        int binSize, bin, dataIndex;

        for(int i=0; i<12; i++){
            graphData.set(i,0.0);
        }





        for(int i=0; i<this.values.size(); i++){
            bin = (int)(this.values.get(i)/10.0);
            dataIndex = bin+2;

            graphData.set(dataIndex, graphData.get(dataIndex)+1);

        }
    }

    List<Double> getPercentiles() throws RuntimeException{}

    void setLowerBound(double value){} // needed so we can throw error if entered/loaded is outside range

    void setUpperBound(double value){} // needed so we can throw error if entered/loaded is outside range

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
