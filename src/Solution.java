import acquireData.AcquireData;
import prepareData.CleanData;

import java.util.List;

public class Solution {

    AcquireData acquireData = new AcquireData();
    CleanData cleanData = new CleanData();

    public void predict(){

        List<List<String>> trainingData = acquireData.getDataAsList(AcquireData.TRAINING_DATA);

        cleanData.clean(trainingData);

        System.out.println(trainingData);


    }
}
