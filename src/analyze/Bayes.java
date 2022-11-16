package analyze;

import validation.F1;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bayes {

    private List<BigDecimal> priorProbability;
    private Map<String, BigDecimal> conditionalProbability;
    private List<Integer> ContinuousIndex;
    private Map<String, List<String>> FinalCentroidMap;
    List<List<String>> AfterKmeanTrainData;
    //p[i][j] means the probability of attribute j with classification i
    public List<List<BigDecimal>> getLikelihood(List<List<String>> trainingData, List<List<String>> testingData){

        return null;



    }

    public void train(List<List<String>> trainingData){
        Runtime.getRuntime().gc();
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
        long usedMemorySize = memoryUsage.getUsed();
        long start = System.currentTimeMillis();

        AfterKmeanTrainData = kMean(trainingData,3,0.0001);
        for (List<String> rowData:trainingData){
            rowData = classifyData(rowData);
            predictClass(trainingData,rowData);
        }

        MemoryUsage memoryUsageAfter = memoryMXBean.getHeapMemoryUsage();
        long usedMemorySizeAfter = memoryUsageAfter.getUsed();
        long end = System.currentTimeMillis();
        System.out.println("training-time consumption:: "+String.valueOf(end - start)+"ms");
        System.out.println("training-memory consumption: "+String.valueOf((usedMemorySizeAfter- usedMemorySize)/1024/1024)+"MB");

    }

    public List<String> predictLabel(List<List<String>> testingData){
        Runtime.getRuntime().gc();
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
        long usedMemorySize = memoryUsage.getUsed();
        long start = System.currentTimeMillis();
        List<String> predictList = new ArrayList<>();
        testingData.remove(0);
//        kMean(AfterKmeanTrainData,5,0.001);
        //testing
        for (List<String> tempTestData : testingData){
            classifyData(tempTestData);
            String forecastRes = predictClass(AfterKmeanTrainData,tempTestData);
            predictList.add(forecastRes);
            //TODO 写入excel or do other
        }

        MemoryUsage memoryUsageAfter = memoryMXBean.getHeapMemoryUsage();
        long usedMemorySizeAfter = memoryUsageAfter.getUsed();
        long end = System.currentTimeMillis();
        System.out.println("test-time consumption: "+String.valueOf(end - start)+"ms");
        System.out.println("test-memory consumption: "+String.valueOf((usedMemorySizeAfter- usedMemorySize)/1024/1024)+"MB");
        return predictList;
    }

    public void validBayes(List<List<String>> validData){
        Runtime.getRuntime().gc();
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
        long usedMemorySize = memoryUsage.getUsed();
        long start = System.currentTimeMillis();
        int righr = 0;
        validData.remove(0);
        List<String> predictList = new ArrayList<>();
        List<String> validDateList = new ArrayList<>();

//        kMean(trainingData,5,0.001);
        for (List<String> rowData:validData){
            String validLabel = rowData.get(rowData.size()-1);
            rowData = classifyData(rowData);
            rowData.remove(rowData.size()-1);
            String predictLabel = predictClass(AfterKmeanTrainData,rowData);
            predictList.add(predictLabel);
            validDateList.add(validLabel);
            if (validLabel.equals(predictLabel)){
                righr++;
            }
        }
        F1 f1 = new F1(predictList,validDateList);
        double re=(double) righr/(double)validData.size();
        System.out.println("test data size："+ (validData.size()));
        System.out.println("correct data amount："+(righr));
        System.out.println("accuracy："+ re);
        MemoryUsage memoryUsageAfter = memoryMXBean.getHeapMemoryUsage();
        long usedMemorySizeAfter = memoryUsageAfter.getUsed();
        long end = System.currentTimeMillis();
        System.out.println("validation-time consumption: "+String.valueOf(end - start)+"ms");
        System.out.println("validation-memory consumption: "+String.valueOf((usedMemorySizeAfter- usedMemorySize)/1024/1024)+"MB");

    }

    private List<List<String>> kMean(List<List<String>> trainData, int K, double threshold){
        trainData.remove(0);
        // key - k Cluster, value - rowData
        Map<String,List<List<String>>> kCluster = new HashMap<String,List<List<String>>>();
        // key - name of K Cluster, value - centroid data
        Map<String, List<String>> centroidMap = new HashMap<String, List<String>>();
        for (int i = 0; i < K; i++) {
            int random = (int)(Math.random()*(double) trainData.size());
            centroidMap.put(String.valueOf(i),trainData.get(random));
        }
        ContinuousIndex = new ArrayList<>();
        List<String> tempRowData = trainData.get(1);
        for (int i = 0; i < tempRowData.size()-1; i++) {
            if (isContinuousValue(tempRowData.get(i))){
                ContinuousIndex.add(i);
            }
        }

        for (int i = 0; i < 500; i++) {
            // calculate distance to the centroid, devide into k clusters and update cnetroids
            kCluster = switchCluster(centroidMap, trainData);
            Map<String, List<String>> centroidMapTemp = updateCentroidMap(kCluster);

            centroidMap = centroidMapTemp;
        }
        FinalCentroidMap = centroidMap;
        kCluster = switchCluster(FinalCentroidMap, trainData);
        //update continuous attributes of training data
        for (List<String> rowData: trainData){
            for (String key : kCluster.keySet()){
                List<List<String>> dataListInClusterK = kCluster.get(key);
                if (dataListInClusterK.contains(rowData)){
                    for (int continuousIndex:ContinuousIndex){
                        rowData.set(continuousIndex,key);
                    }
                    break;
                }
            }
        }
        return trainData;
    }

    private boolean isContinuousValue(String data){
        return data != null && data.matches("-?\\d+(\\.\\d+)?");
    }

    private List<String> classifyData(List<String> rowData){
        String kClusterName = FinalCentroidMap.keySet().toArray()[0].toString();
        double minDis = -1;
        for (Map.Entry<String, List<String>> entry : FinalCentroidMap.entrySet()){
            List<String> centroidRow = entry.getValue();
            String key = entry.getKey();
            double sum = 0;
            for (int index : ContinuousIndex){
                sum += Math.pow(Double.valueOf(rowData.get(index)) - Double.valueOf(centroidRow.get(index)),2);
            }
            if (minDis > sum || minDis == -1){
                minDis = sum;
                kClusterName = key;
            }
        }
        for (int continuousIndex : ContinuousIndex){
            rowData.set(continuousIndex,kClusterName);
        }
        return rowData;
    }

    private Map<String,List<List<String>>> switchCluster(Map<String, List<String>> centroidMap,List<List<String>> trainData){
        Map<String,List<List<String>>> kCluster = new HashMap<String,List<List<String>>>();

        for (int i = 0; i < trainData.size(); i++) {
            double minDis = -1;
            String kClusterName = centroidMap.keySet().toArray()[0].toString();
            List<String> rowData = trainData.get(i);
            for (Map.Entry<String, List<String>> entry : centroidMap.entrySet()){
                List<String> centroidRow = entry.getValue();
                String key = entry.getKey();
                double sum = 0;
                for (int index : ContinuousIndex){
                    sum += Math.pow(Double.valueOf(rowData.get(index)) - Double.valueOf(centroidRow.get(index)),2);
                }
                if (minDis > sum || minDis == -1){
                    minDis = sum;
                    kClusterName = key;
                }
            }
            if (kCluster.containsKey(kClusterName)){
                kCluster.get(kClusterName).add(rowData);
            }else {
                List<List<String>> tempData = new ArrayList<List<String>>();
                tempData.add(rowData);
                kCluster.put(kClusterName,tempData);
            }
        }

        return kCluster;
    }



    private Map<String, List<String>> updateCentroidMap(Map<String,List<List<String>>> kCluster){
        Map<String, List<String>> centroidMap = new HashMap<String, List<String>>();
        for (String key : kCluster.keySet()){
            List<List<String>> dataList = kCluster.get(key);
            List<String> centroidData = dataList.get(0);
            for (int i = 1; i < dataList.size(); i++) {
                List<String> rowData = dataList.get(i);
                for (int continuousIndex : ContinuousIndex){
                    double curr = Double.valueOf(centroidData.get(continuousIndex));
                    double update = curr + Double.valueOf(rowData.get(continuousIndex));
                    centroidData.set(continuousIndex,String.valueOf(update));
                }
            }
            for (int continuousIndex : ContinuousIndex){
                double curr = Double.valueOf(centroidData.get(continuousIndex));
                BigDecimal result = BigDecimal.valueOf(curr).divide(BigDecimal.valueOf(dataList.size()),10,BigDecimal.ROUND_HALF_UP);
                centroidData.set(continuousIndex,result.toString());
            }
            centroidMap.put(key,centroidData);
        }
        return centroidMap;
    }

    private  boolean verifyChange(Map<String, List<String>> centroidMapTemp,Map<String, List<String>> centroidMap,double threshold){
        double error = 0;
        for (Map.Entry<String, List<String>> entry : centroidMapTemp.entrySet()){
            String clusterName = entry.getKey();
            List<String> tempData = centroidMapTemp.get(clusterName);
            List<String> currData = centroidMap.get(clusterName);

            for (int continuousIndex:ContinuousIndex){
                double tempDouble = Double.valueOf(tempData.get(continuousIndex));
                double currDouble = Double.valueOf(currData.get(continuousIndex));

                error += Math.pow(tempDouble-currDouble,2);
            }
        }
        error = Math.sqrt(error);

        return error < threshold;
    }


    private Map<String, List<List<String>>> dataOfClass(List<List<String>> dataList){

        Map<String, List<List<String>>> map = new HashMap<String, List<List<String>>>();
        List<String> currRowData = null;
        String currRowDataLabel = "";

        for (int i = 0; i < dataList.size(); i++) {

            currRowData = dataList.get(i);
            currRowDataLabel = currRowData.get(currRowData.size() - 1);

            if(currRowDataLabel.length()==0) continue;

            if (map.containsKey(currRowDataLabel)) {
                map.get(currRowDataLabel).add(currRowData);
            } else {
                List<List<String>> tempData = new ArrayList<List<String>>();
                tempData.add(currRowData);
                map.put(currRowDataLabel,tempData);
            }
        }
        return map;
    }

    public String predictClass(List<List<String>> trainingData, List<String> testRow) {

        //Split Training Sets by Label
        Map<String, List<List<String>>> trainDataMap = this.dataOfClass(trainingData);
        //Get label array
        Object labelArray[] =  trainDataMap.keySet().toArray();
        double maxP = 0.00;
        int maxPIndex = -1;
        int continuousIndex =ContinuousIndex.get(0);
        //Calculate the product of all prerequisite probability( p(j|i) ) and label probability
        //Select the label having maximum probability
        for (int i = 0; i < trainDataMap.size(); i++) {
            String label = labelArray[i].toString();
            List<List<String>> dataOfCurrLabel = trainDataMap.get(label);
            BigDecimal numberOfTrainDataInCurrLabel = new BigDecimal(Double.toString(dataOfCurrLabel.size()));
            BigDecimal numberOfTrainData = new BigDecimal(Double.toString(trainingData.size()));

            double pOfCurrLabel = numberOfTrainDataInCurrLabel.divide(numberOfTrainData,10,BigDecimal.ROUND_HALF_UP).doubleValue();
            double compareValue = pOfCurrLabel;

            for (int j = 0; j < testRow.size(); j++) {
                if (j !=continuousIndex && ContinuousIndex.contains(j)){
                    continue;
                }
                // ppOfAttJInLabelI mean p(Jx | I) , 0 < x < testRow.size()
                double ppOfAttrJInLabelI = this.getPrerequisiteProbability(dataOfCurrLabel, testRow.get(j),j);
                if(ppOfAttrJInLabelI==0) ppOfAttrJInLabelI=1/(double)dataOfCurrLabel.size();
                BigDecimal b2 = new BigDecimal(Double.toString(ppOfAttrJInLabelI));

                // compareValue mean p(J1 | I) * p(J2 | I) * ...... p(Jn | I) * p(I)
                compareValue = BigDecimal.valueOf(compareValue).multiply(b2).doubleValue();
            }
            if(compareValue > maxP){
                maxP = compareValue;
                maxPIndex = i;
            }
        }
        return labelArray[maxPIndex].toString();
    }

    // get Prerequisite Probability of attribute index in current label
    private double getPrerequisiteProbability(List<List<String>> dataOfCurrLabel, String value, int index) {

        double p = 0.00;
        int count = 0;
        int total = dataOfCurrLabel.size();
        for (int i = 0; i < total; i++) {
            if (value.equals(dataOfCurrLabel.get(i).get(index))){
                count++;
            }
        }
        // both number in attribute index
        BigDecimal numberOfTestData = new BigDecimal(Double.toString(count));
        BigDecimal numberOfTotalData = new BigDecimal(Double.toString(total));

        p = numberOfTestData.divide(numberOfTotalData,10,BigDecimal.ROUND_HALF_UP).doubleValue();
        return p;
    }

    public List<List<Integer>> calculateConditionalProbability(int attributeIndex, List<List<String>> trainingData){
        List<Map<String, Integer>> classList = new ArrayList<>(6);
        for (int i=0;i<6;i++){
            classList.add(new HashMap<>());
        }
        for (int i=0;i<trainingData.size();i++){
            List<String> line = trainingData.get(i);
            int classLabel = Integer.parseInt( trainingData.get(i).get(line.size()-1));
            if (classList.get(classLabel).containsKey(line.get(attributeIndex))){
                classList.get(classLabel).put(line.get(attributeIndex),classList.get(classLabel).get(line.get(attributeIndex))+1);
            }
        }
        for (int i=0;i<trainingData.size();i++){

        }

        return null;
    }
}
