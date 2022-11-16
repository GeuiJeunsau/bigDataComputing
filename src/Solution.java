import data.acquireData.AcquireData;
import analyze.Analyze;
import data.writeData.WriteData;
import prepareData.CleanData;
import prepareData.FeatureScaling;

import java.util.List;

public class Solution {

    AcquireData acquireData = new AcquireData();
    CleanData cleanData = new CleanData();
    FeatureScaling featureScaling = new FeatureScaling();
    Analyze analyze = new Analyze();
    WriteData writeData = new WriteData();

    public void train(){

        List<List<String>> trainingData = acquireData.getDataAsList(AcquireData.TRAINING_DATA);

        List<List<String>> testingData = acquireData.getDataAsList(AcquireData.TEST_DATA);

        List<List<String>> validData = acquireData.getDataAsList(AcquireData.VALIDATION_DATA);

        cleanData.clean(trainingData);

        featureScaling.scale(trainingData);

        List<String> predictList = analyze.analyze(trainingData, testingData, validData, Analyze.BAYES);

        writeData.writeTestResult(predictList);
//        System.out.println(trainingData);


    }

}
