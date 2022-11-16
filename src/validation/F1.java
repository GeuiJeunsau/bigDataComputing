package validation;

import java.math.BigDecimal;
import java.util.*;

public class F1 {

    // key - attribute, value - TP number
    Map<String,Integer> TPMap = new HashMap<String,Integer>();

    // key - attribute, value - FP number
    Map<String,Integer> FPMap = new HashMap<String,Integer>();

    // key - attribute, value - FN number
    Map<String,Integer> FNMap = new HashMap<String,Integer>();



    Set<String> labelSet = new HashSet<String>();

    public F1(List<String> predictDataList, List<String> validDataList) {


        //init TP FP FN Map
        //TP means True Positive, FP means False Positive, FN means False Negative
        for (int i = 0; i < predictDataList.size(); i++) {
            String validData = validDataList.get(i);
            String predictData = predictDataList.get(i);
            labelSet.add(validData);
            if (validData.equals(predictData)){
                // 当 校验数据 跟 预测数据相同时， 更新 当前校验数据类的TP Map
                if (TPMap.containsKey(validData)){
                    TPMap.put(validData,TPMap.get(validData)+1);
                }else {
                    TPMap.put(validData,1);
                }
            }else {
                // 当 校验数据 跟 预测数据不相同时
                // 更新 当前校验类的FN Map
                if (FNMap.containsKey(validData)){
                    FNMap.put(validData,FNMap.get(validData)+1);
                }else {
                    FNMap.put(validData,1);
                }
                // 更新 当前预测类的 FP Map
                if (FPMap.containsKey(predictData)){
                    FPMap.put(predictData,FPMap.get(predictData)+1);
                }else {
                    FPMap.put(predictData,1);
                }
            }
        }
    }


    public Double calculateF1Value(double precision, double recall){
        BigDecimal precisionB = BigDecimal.valueOf(precision);
        BigDecimal recallB = BigDecimal.valueOf(recall);

        // F1 = 2 * p*r / (p+r)
        BigDecimal numerator  = precisionB.multiply(recallB);
        BigDecimal denominator = precisionB.add(recallB);
        if (numerator.doubleValue() == 0){
            return 0.0;
        }
        return 2*(numerator.divide(denominator,4,BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    public Double micro_F1(){
        // 将所有类的TP,FP,FN加在一起，最后算出F1，此F1就是 micro_F1
        int totalTP = 0;
        int totalFP = 0;
        int totalFN = 0;
        double precision = 0;
        double recall = 0;

        for (Map.Entry<String,Integer> entry:TPMap.entrySet()){
            totalTP += entry.getValue();
        }

        for (Map.Entry<String,Integer> entry:FPMap.entrySet()){
            totalFP += entry.getValue();
        }

        for (Map.Entry<String,Integer> entry:FNMap.entrySet()){
            totalFN += entry.getValue();
        }
        BigDecimal totalTPB = BigDecimal.valueOf(totalTP);
        BigDecimal totalFPB = BigDecimal.valueOf(totalFP);
        BigDecimal totalFNB= BigDecimal.valueOf(totalFN);

        precision = calculatePrecision(totalTPB,totalFPB);
        recall = calculateRecall(totalTPB,totalFNB);

        return calculateF1Value(precision,recall);
    }

    public Double macro_F1(){
        // 分别计算每个类的F1，最后所有F1 求 算数平均值，此平均值就是 macro_F1

        Double sumF1 = 0.000;
        for (String label : labelSet){
            int TP = TPMap.get(label) == null?0:TPMap.get(label);
            int FP = FPMap.get(label) == null?0:FPMap.get(label);
            int FN = FNMap.get(label) == null?0:FNMap.get(label);
            double precision = 0;
            double recall = 0;
            BigDecimal TPB = BigDecimal.valueOf(TP);
            BigDecimal FPB = BigDecimal.valueOf(FP);
            BigDecimal FNB= BigDecimal.valueOf(FN);

            precision = calculatePrecision(TPB,FPB);
            recall = calculateRecall(TPB,FNB);
            sumF1 += calculateF1Value(precision,recall);
        }

        return BigDecimal.valueOf(sumF1).divide(BigDecimal.valueOf(labelSet.size()),4,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private double calculatePrecision(BigDecimal TP, BigDecimal FP){
        if (TP.doubleValue() == 0 && FP.doubleValue() == 0){
            return 0;
        }
        return TP.divide(TP.add(FP),4,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private double calculateRecall(BigDecimal TP, BigDecimal FN){
        if (TP.doubleValue() == 0 && FN.doubleValue() == 0){
            return 0;
        }
        return TP.divide(TP.add(FN),4,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
