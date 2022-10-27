import acquireData.AcquireData;
import prepareData.CleanData;
import prepareData.FeatureScaling;

import java.util.List;

public class Solution {

    AcquireData acquireData = new AcquireData();
    CleanData cleanData = new CleanData();
    FeatureScaling featureScaling = new FeatureScaling();

    public void train(){

        List<List<String>> trainingData = acquireData.getDataAsList(AcquireData.TRAINING_DATA);

        cleanData.clean(trainingData);

        featureScaling.scale(trainingData);

        System.out.println(trainingData);


    }
}
