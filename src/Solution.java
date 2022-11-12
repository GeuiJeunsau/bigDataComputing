import acquireData.AcquireData;
import analyze.Analyze;
import prepareData.CleanData;
import prepareData.FeatureScaling;

import java.util.List;

public class Solution {

    AcquireData acquireData = new AcquireData();
    CleanData cleanData = new CleanData();
    FeatureScaling featureScaling = new FeatureScaling();
    Analyze analyze = new Analyze();

    public void train(){

        List<List<String>> trainingData = acquireData.getDataAsList(AcquireData.TRAINING_DATA);

        cleanData.clean(trainingData);

        featureScaling.scale(trainingData);

        analyze.analyze(trainingData, Analyze.BAYES);

        System.out.println(trainingData);


    }
}
