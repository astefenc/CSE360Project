import java.io.*;
import java.util.ArrayList;
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

    int getAmount() throws RuntimeException{
        return values.size();
    }
/*
    double getMaximum() throws RuntimeException{}

    double getMinimum() throws RuntimeException{}

    double getMean() throws RuntimeException{}

    double getMedian() throws RuntimeException{}

    double getMode() throws RuntimeException{}

    List<Double> getValues() throws RuntimeException{
        return this.values;
    }

    /*
        Returns a 10 bin wide histogram depicting the data

        index  |    what
        0      |    MINIMUM PERMITTED VALUE
        1      |    MAXIMUM PERMITTED VALUE
        2      |    Bin size
        3-12   |    values for each bin
     */
    List<Double> getGraph() {
        ArrayList<Double> graphData = new ArrayList<>();
        int binSize, bin, dataIndex, totalCount;

        // initialize arrayList
        for(int i=0; i<13; i++){
            graphData.add(0.0);
        }

        // mark upper and lower bounds
        graphData.set(0, this.lowerBound);
        graphData.set(1, this.upperBound);

        // and the bin size
        binSize = ((int) Math.ceil((this.upperBound - this.lowerBound) / 10.0));
        graphData.set(2, (double) binSize);

        // count up amount of values in each bin
        for(int i=0; i<this.values.size(); i++){
            bin = (int)(this.values.get(i)/binSize);
            dataIndex = bin+2;

            graphData.set(dataIndex, graphData.get(dataIndex)+1); // increment value by 1
        }

        // count total amount of values
        totalCount = values.size();

        // Get proportion for each index instead of count
        for(int i=3; i<13; i++){
            graphData.set(i, (graphData.get(i)/ (double) totalCount));
        }


        return graphData; // results in 10 section wide histogram
    }
/*
    List<Double> getPercentiles() throws RuntimeException{}
*/
    void setLowerBound(double value){
        this.lowerBound = value;
    }

    void setUpperBound(double value){
        this.upperBound = value;
    }

    void loadFile(String filepath) throws RuntimeException, IOException {
        FileReader fileReader;
        BufferedReader bufferedReader;
        String line;
        int lengthOfFilePath;


        // Try to find the file
        try {
            bufferedReader = new BufferedReader(new FileReader(filepath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File does not exist. You may have an invalid file path.");
        }


        lengthOfFilePath = filepath.length();


        if(lengthOfFilePath<4){
            throw new RuntimeException("Invalid file extension.");      // filepath too short to fit proper extension
        }else if(filepath.substring(lengthOfFilePath-4).equals(".txt")){
            readFromTxt(bufferedReader);
        }else if(filepath.substring(lengthOfFilePath-4).equals(".csv")){
            readFromCsv(bufferedReader);
        }else{
            throw new RuntimeException("Invalid file extension.");      // wrong extension
        }



    }
    private void readFromTxt(BufferedReader bufferedReader) throws RuntimeException, IOException{
        String line;
        int lineNum;

        lineNum = 0;

        // read through every line
        while((line = bufferedReader.readLine()) != null){
            lineNum++; // increment line counter

            try {
                this.addValue(Double.parseDouble(line));    // add each value line-by-line
            }catch (NumberFormatException e){ // wrong data type
                throw new RuntimeException("Data point is of the wrong data type. Please enter an integer or decimal value. (Value "+line+" on line "+lineNum+" is the wrong data type)");
            } catch (RuntimeException e){   // other exception
                if(e.getMessage().contains("out of range")){
                    throw new RuntimeException("Input value(s) out of range.  (Value "+line+" on line "+lineNum+" is out of range)");   // out of range - add line info
                }else{
                    throw e; // throw others
                }
            }
        }

        // Close file
        bufferedReader.close();
    }
    private void readFromCsv(BufferedReader bufferedReader) throws RuntimeException, IOException{
        String line,delimiter;
        int lineNum;
        String[] lineSplit;

        delimiter = ",";
        lineNum = 0;

        // read through every line
        while((line = bufferedReader.readLine()) != null){
            lineNum++; // increment line counter

            lineSplit = line.split(delimiter);

            for (String s : lineSplit) {
                try {
                    this.addValue(Double.parseDouble(s));    // add each value

                } catch (NumberFormatException e) { // wrong data type
                    throw new RuntimeException("Data point is of the wrong data type. Please enter an integer or decimal value. (Value "+line+" on line "+lineNum+" is the wrong data type)");
                } catch (RuntimeException e){
                    if(e.getMessage().contains("out of range")){
                        throw new RuntimeException("Input value(s) out of range.  (Value "+line+" on line "+lineNum+" is out of range)");   // out of range - add line info
                    }else{
                        throw e; // throw others
                    }
                }
            }
        }

        // Close file
        bufferedReader.close();
    }

    void addValue(double value) throws RuntimeException{
        if(value > this.upperBound || value < this.lowerBound){
            throw new RuntimeException("Input value(s) out of range.");
        }else{
            this.values.add(value);
        }
    }

    void deleteValue(double value) throws RuntimeException{}

    void clearValues() throws RuntimeException{
        values.clear();
    }


    /*      Runetime Exception example
                custom error messages should be as declared in user guide
     */
    void example() throws RuntimeException{
        throw new RuntimeException("CUSTOM ERROR MSG");
    }

}
