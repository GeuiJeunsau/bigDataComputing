package prepareData;

import java.util.List;

public class FeatureScaling {

    //log scaling
    public void scale(List<List<String>> trainingData){
        //attribute 12 & attribute 14 need to do feature scaling

        for (int i=1;i<trainingData.size();i++){
            //log scaling attribute 12
            logScaling(trainingData.get(i), 11);
            //log scaling attribute 14
            logScaling(trainingData.get(i), 13);
        }

    }

    private void logScaling(List<String> data, int attributeNo){
        String logScaling = ((Double) Math.log(new Double(data.get(attributeNo)))).toString();
        data.set(attributeNo, logScaling);
    }

}
