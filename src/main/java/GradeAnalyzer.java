import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;

public class GradeAnalyzer {
    private LinkedList<Double> values;
    private double lowerBound, upperBound;

    GradeAnalyzer(){
        this.values = new LinkedList<>();
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
        return 3;
    }

    double getMedian() throws RuntimeException{
        return 4;
    }

    LinkedList<Double> getMode() throws RuntimeException{
        LinkedList<Double> li = new LinkedList<>();
        li.add(4.4);
        li.add(12.32);

        return li;
    }


    /**
     * Returns the list of all the values in the dataset
     * <p>
     * @return all values in dataset
     * */
    LinkedList<Double> getValues(){
        return this.values;
    }
    /**
     * Returns a list of histogram data
     * <p>
     * The list returned contains the proportions of data in each tenth of the range
     *
     * @return list of histogram data
     * index                    data
     * 0-9                      Proportions of data in each tenth of the range
     * 10                       lower bound
     * 11                       upper bound
     * 12                       bin width (binSize)
     * */
    LinkedList<Double> getGraph() {
        LinkedList<Double> graphData = new LinkedList<>();
        int totalCount;
        double binSize;

        // initialize arrayList
        for(int i=0; i<13; i++){
            graphData.add(0.0);
        }

        // the bin size
        binSize = (this.upperBound - this.lowerBound) / 10.0;

        // record min, max, binSize
        graphData.set(10, this.lowerBound);
        graphData.set(11, this.upperBound);
        graphData.set(12, binSize);

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

        // Get proportion for each bin instead of count
        for(int i=0; i<10; i++){
            graphData.set(i, (graphData.get(i)/ (double) totalCount));
        }


        return graphData;
    }

    // TODO
    LinkedList<Double> getPercentiles() throws RuntimeException{
        LinkedList<Double> li = new LinkedList<>();
        li.add(1.11);
        li.add(2.22);
        li.add(3.33);
        li.add(4.44);
        li.add(5.55);
        li.add(6.66);
        li.add(7.77);
        li.add(8.88);
        li.add(9.99);
        li.add(10.10);

        return li;
    }



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
     * @throws RuntimeException - for invalid program states (out-of-range data or non-numerical data)
     *                            OR invalid file extension
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
    /**
     * Read values from a txt file with one decimal or integer value per line
     *
     * @throws RuntimeException - invalid datatype
     *                              OR value is out of range (see message of error for which)
     * @throws IOException - fails to close file
     * */
    private void readFromTxt(BufferedReader bufferedReader) throws RuntimeException, IOException{
        String line;
        int lineNum;
        double value;
        LinkedList<Double> alreadyAdded = new LinkedList<>();

        lineNum = 0;

        // read through every line
        while((line = bufferedReader.readLine()) != null){
            lineNum++; // increment line counter

            try {
                value = Double.parseDouble(line);
                this.addValue(value);    // add each value line-by-line
                alreadyAdded.add(value);
            }catch (NumberFormatException e){ // wrong data type
                for(double d : alreadyAdded){
                    this.deleteValue(d);
                }
                throw new RuntimeException("Data point is of the wrong data type. Please enter an integer or decimal value. (Value "+line+" on line "+lineNum+" is the wrong data type)");
            } catch (RuntimeException e){   // other exception
                for(double d : alreadyAdded){
                    this.deleteValue(d);
                }
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
    /**
     * Read values from a csv file with each decimal or integer value separated by commas or newlines
     *
     * @throws RuntimeException - invalid datatype
     *                              OR value is out of range (see message of error for which)
     * @throws IOException - fails to close file
     * */
    private void readFromCsv(BufferedReader bufferedReader) throws RuntimeException, IOException{
        String line,delimiter;
        int lineNum;
        String[] lineSplit;
        double value;
        LinkedList<Double> alreadyAdded = new LinkedList<>();

        delimiter = ",";
        lineNum = 0;

        // read through every line
        while((line = bufferedReader.readLine()) != null){
            lineNum++; // increment line counter

            lineSplit = line.split(delimiter);

            for(int i=0; i<lineSplit.length; i++){
                try {
                    value = Double.parseDouble(lineSplit[i]);
                    this.addValue(value);    // add each value line-by-line
                    alreadyAdded.add(value);
                } catch (NumberFormatException e) { // wrong data type
                    for(double d : alreadyAdded){
                        this.deleteValue(d);
                    }
                    throw new RuntimeException("Data point is of the wrong data type. Please enter an integer or decimal value. (Value "+line+" on line "+lineNum+" is the wrong data type)");
                } catch (RuntimeException e){
                    for(double d : alreadyAdded){
                        this.deleteValue(d);
                    }
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

    /**
     * Add this single value to the dataset
     *
     * @throws RuntimeException - value is out of range (see message of error for which)
     * */
    void addValue(double value) throws RuntimeException{
        if(value > this.upperBound || value < this.lowerBound){
            throw new RuntimeException("Input value(s) out of range.");
        }else{
            values.add(value);
        }
    }
    /**
     * Delete the first occurrence from the dataset
     *
     * @throws RuntimeException - value is out of range (see message of error for which)
     * */
    void deleteValue(double value) throws RuntimeException{
        if(value > this.upperBound || value < this.lowerBound){
            throw new RuntimeException("Input value(s) out of range.");
        }else{
            values.removeFirstOccurrence(value);
        }
    }
    /**
     * Delete all values from the dataset
     * */
    void clearValues(){
        values.clear();
    }
}
