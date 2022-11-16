package data.acquireData;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AcquireData {

    public static int TRAINING_DATA = 0;
    public static int TEST_DATA = 1;
    public static int VALIDATION_DATA = 2;
    private static List<String> PATH = Arrays.asList("resource/training.csv","resource/test.csv", "resource/validation.csv");

    public List<List<String>> getDataAsList(Integer source) {
        List<List<String>> data = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(PATH.get(source)), "UTF-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                List<String> row = Arrays.stream(line.split(",")).collect(Collectors.toList());
                data.add(row);
            }
            bufferedReader.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }
}
