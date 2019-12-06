import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
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
        Iterator<Double> iter = values.iterator();
        double total = values.getFirst();

        while(iter.hasNext()){
            total += iter.next();
        }

        return (total/values.size());
    }

    double getMedian() throws RuntimeException{
        LinkedList<Double> copyOfValues = new LinkedList<>(values);
        Collections.sort(copyOfValues);
        Iterator<Double> quickIter = copyOfValues.iterator();
        Iterator<Double> slowIter = copyOfValues.iterator();
        Iterator<Double> previousIter = copyOfValues.iterator();

        while(quickIter.hasNext()) { // quick iter moves at double the speed of slow iter so when it
            quickIter.next();    // reaches the end of the linkedlist, slow iter is in the middle
            if(quickIter.hasNext()) {
                quickIter.next();

                previousIter = slowIter;
                slowIter.next();
            }
        }

        if(!quickIter.equals(null)) { //if quick iter isn't null, the list has an odd size.
            return previousIter.next();
        }else{
            return ( previousIter.next() + slowIter.next() )/2.0;
        }
    }

    LinkedList<Double> getMode() throws RuntimeException{
        LinkedList<Double> modes = new LinkedList<Double>();

        int mostFreq = 0;
        for(Double value: this.values) {
            int freq = Collections.frequency(values, value);
            if(freq == mostFreq) {
                modes.add(value);
            }
            if(freq > mostFreq) {
                modes.clear();
                modes.add(value);
                mostFreq = freq;
            }
        }

        if(modes.equals(null)) {
            throw new RuntimeException("There is no mode.");
        }else {
            return modes;
        }
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

    LinkedList<Double> getPercentiles() throws RuntimeException{
        LinkedList<Double> sortedScores = new LinkedList<>(values);
        Collections.sort(sortedScores);
        LinkedList<Double> percentGroups = new LinkedList<Double>();
        int sizeOfColumns = sortedScores.size()/10;

        // initialize arrayList
        for(int i=0; i<10; i++){
            percentGroups.add(0.0);
        }

        for(int i = 0; i<10; i++){
            for(int j = 0; j<sizeOfColumns; j++){
                percentGroups.set(i, percentGroups.get(i) + sortedScores.pop());
            }
        }
        /*
        int[] arr;
        arr = new int[10];  // array to keep track of the number of values in each range
        int totalCount;

        //double binSize;

        // initialize arrayList
        for(int i=0; i<10; i++){
            percentGraph.add(0.0);
        }
        */
        // count up amount of total value in each range
        // also: keep track of how many values are in each range
        /*
        for (Double value : this.values) {
            if(value<10){
                percentGraph.set(0, percentGraph.get(0) + value);
                arr[0] += 1;
            }else if(value<20){
                percentGraph.set(1, percentGraph.get(1) + value);
                arr[1] += 1;
            }else if(value<30){
                percentGraph.set(2, percentGraph.get(2) + value);
                arr[2] += 1;
            }else if(value<40){
                percentGraph.set(3, percentGraph.get(3) + value);
                arr[3] += 1;
            }else if(value<50){
                percentGraph.set(4, percentGraph.get(4) + value);
                arr[4] += 1;
            }else if(value<60){
                percentGraph.set(5, percentGraph.get(5) + value);
                arr[5] += 1;
            }else if(value<70){
                percentGraph.set(6, percentGraph.get(6) + value);
                arr[6] += 1;
            }else if(value<80){
                percentGraph.set(7, percentGraph.get(7) + value);
                arr[7] += 1;
            }else if(value<90){
                percentGraph.set(8, percentGraph.get(8) + value);
                arr[8] += 1;
            }else{
                percentGraph.set(9, percentGraph.get(9) + value);
                arr[9] += 1;
            }
        }
        */
        // Get average for each index instead of the percentgraph
        for(int i=0; i<10; i++){
                percentGroups.set(i, (percentGroups.get(i)/ sizeOfColumns));
        }


        return percentGroups; // results in 10 different ranges of percentages
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
