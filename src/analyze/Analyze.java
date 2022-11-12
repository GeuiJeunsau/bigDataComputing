package analyze;

import org.omg.CORBA.StringHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Analyze {

    public static final String BAYES = "Bayes";

    Bayes bayes = new Bayes();

    public List<List<BigDecimal>> analyze(List<List<String>> trainingData, String method){
        if (BAYES.equals(method)){
            return bayes.getLikelihood(trainingData);
        } else {
            return new ArrayList<>();
        }
    }
}
