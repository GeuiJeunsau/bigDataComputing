package analyze;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Analyze {

    public static final String BAYES = "Bayes";

    Bayes bayes = new Bayes();

    public List<String> analyze(List<List<String>> trainingData, List<List<String>> testingData,List<List<String>> validData, String method){
        if (BAYES.equals(method)){
            bayes.train(trainingData);
            bayes.validBayes(validData);
            List<String> predictList = bayes.predictLabel(testingData);
            return predictList;
        } else {
            return new ArrayList<>();
        }
    }
}
