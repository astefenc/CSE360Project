import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GradeAnalyzer {
    private LinkedList<Double> values;
    private double lowerBound, upperBound;

    GradeAnalyzer(){
        this.values = new LinkedList<Double>();
        this.lowerBound = 0.0;
        this.upperBound = 100.0;
    }

    int getAmount() {
        return values.size();
    }

    double getMaximum() throws RuntimeException{
        return Collections.max(values);
    }

    double getMinimum() throws RuntimeException{
        return Collections.min(values);
    }

    double getMean() throws RuntimeException{
        return 0;}

    double getMedian() throws RuntimeException{
        return 0;}

    LinkedList<Double> getMode() throws RuntimeException{
        return new LinkedList<>();}

    LinkedList<Double> getValues() throws RuntimeException{
        return this.values;
    }



    LinkedList<Double> getGraph() {
        LinkedList<Double> graphData = new LinkedList<>();
        int totalCount;
        double binSize;

        // initialize arrayList
        for(int i=0; i<10; i++){
            graphData.add(0.0);
        }

        // the bin size
        binSize = (this.upperBound - this.lowerBound) / 10.0;

        // count up amount of values in each bin
        for (Double value : this.values) {
            if(value<=binSize){
                graphData.set(0, graphData.get(0) + 1);
            }else if(value<=2*binSize){
                graphData.set(1, graphData.get(1) + 1);
            }else if(value<=3*binSize){
                graphData.set(2, graphData.get(2) + 1);
            }else if(value<=4*binSize){
                graphData.set(3, graphData.get(3) + 1);
            }else if(value<=5*binSize){
                graphData.set(4, graphData.get(4) + 1);
            }else if(value<=6*binSize){
                graphData.set(5, graphData.get(5) + 1);
            }else if(value<=7*binSize){
                graphData.set(6, graphData.get(6) + 1);
            }else if(value<=8*binSize){
                graphData.set(7, graphData.get(7) + 1);
            }else if(value<=9*binSize){
                graphData.set(8, graphData.get(8) + 1);
            }else{
                graphData.set(9, graphData.get(9) + 1);
            }
        }

        // count total amount of values
        totalCount = values.size();

        // Get proportion for each index instead of count
        for(int i=0; i<10; i++){
            graphData.set(i, (graphData.get(i)/ (double) totalCount));
        }


        return graphData; // results in 10 section wide histogram
    }
/*
    LinkedList<Double> getPercentiles() throws RuntimeException{}
*/


    /**
     * Set the lower bound for the dataset
     */
    void setLowerBound(double value){
        this.lowerBound = value;
    }

    /**
     * Set the upper bound for the dataset
     */
    void setUpperBound(double value){
        this.upperBound = value;
    }

    /**
     * Load the file at filepath
     *
     * @throws IOException - if file does not exist
     * @throws RuntimeException - for invalid program states (boundaries wrong or out-of-range data)
     *                            OR invalid file extension
     *
     * @returns
     */
    void loadFile(String filepath) throws RuntimeException, IOException {
        BufferedReader bufferedReader;
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
