package analyze;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Bayes {

    private List<BigDecimal> priorProbability;
    private Map<String, BigDecimal> conditionalProbability;

    //p[i][j] means the probability of attribute j with classification i
    public List<List<BigDecimal>> getLikelihood(List<List<String>> trainingData){

        List<Integer> classList = new ArrayList<>(6);

        for (int i=0;i<6;i++){
            classList.add(0);
        }

        for (int i=1;i<trainingData.size();i++){
            int classIndex = Integer.parseInt(trainingData.get(i).get(trainingData.get(i).size()-1));
            classList.set(classIndex, classList.get(classIndex) + 1);
        }


//https://zhuanlan.zhihu.com/p/370809150
        //https://blog.csdn.net/Yan456jie/article/details/52649788?fps=1&locationNum=5&utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-1-52649788-blog-51052753.pc_relevant_3mothn_strategy_recovery&spm=1001.2101.3001.4242.2&utm_relevant_index=4






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
    }
}
