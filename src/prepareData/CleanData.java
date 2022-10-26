package prepareData;


import java.util.*;

public class CleanData {

    private static Map<String, List<String>> validValues = new HashMap<>(15);
    private static Map<String, List<Double>> validBoundary = new HashMap<>(15);

    public void clean(List<List<String>> trainingData){
        List<String> header = trainingData.get(0);
        trainingData.removeIf(td -> !(checkValid(td, header)
                && checkOutlier(td, header)
        )
                && !header(td, header));
    }

    //check if data of current line all valid
    private Boolean checkValid(List<String> line, List<String> header){
        for(int i=0;i<line.size()-1;i++){
            if (!(!validValues.containsKey(header.get(i)) || validValues.get(header.get(i)).contains(line.get(i)))){
                return false;
            }
        }
        return true;
    }

    private Boolean checkOutlier(List<String> line, List<String> header){
        for (int i=0;i< line.size()-1;i++){
            if (validBoundary.containsKey(header.get(i))){
                try {
                    Double d = new Double(line.get(i));
                    if (Double.compare(d, validBoundary.get(header.get(i)).get(0)) < 0 || Double.compare(d, validBoundary.get(header.get(i)).get(1)) > 0){
                        return false;
                    }
                } catch (Exception e){
                    //means current data is not a digit
                    return false;
                }
            }
        }
        return true;
    }


    public CleanData() {
        // initial valid value of each attribute
        validValues.put("attribute 1", Arrays.asList("yes", "no"));
        validValues.put("attribute 4", Arrays.asList("Always", "Frequently", "no", "Sometimes"));
        validValues.put("attribute 5", Arrays.asList("yes", "no"));
        validValues.put("attribute 10", Arrays.asList("Always", "Frequently", "no", "Sometimes"));
        validValues.put("attribute 11", Arrays.asList("A", "B", "M", "P", "W"));
        validValues.put("attribute 15", Arrays.asList("yes", "no"));

        //initial valid boundary of each attribute
        //question?
        validBoundary.put("attribute 2", Arrays.asList(1.0, 3.0));
        validBoundary.put("attribute 3", Arrays.asList(1.0, 4.0));
        validBoundary.put("attribute 6", Arrays.asList(1.23, 3.69));
        validBoundary.put("attribute 8", Arrays.asList(0.0, 3.0));
        validBoundary.put("attribute 12", Arrays.asList(14.0, 61.0));
        validBoundary.put("attribute 13", Arrays.asList(1.45, 1.98));
        validBoundary.put("attribute 14", Arrays.asList(15000.00, 69200.00));
    }

    //to see if current line is header
    private boolean header(List<String> line, List<String> header){
        if (line == header){
            return true;
        }
        return false;
    }
}
