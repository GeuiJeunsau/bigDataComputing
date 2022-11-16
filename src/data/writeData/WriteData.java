package data.writeData;

import java.io.*;
import java.util.List;

public class WriteData {

    private final String READ_PATH = "resource/test.csv";

    private final String WRITE_PATH = "resource/1.csv";

    public void writeTestResult(List<String> predictList){
        File readFile = new File(READ_PATH);
        File writeFile = new File(WRITE_PATH);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(readFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(writeFile));

            //先写入标题
            String title = reader.readLine();
            writer.write(title);
            writer.newLine();

            String lineData;
            int count = 0;
            while((lineData = reader.readLine()) != null && count < predictList.size()){
                String writeData = lineData+predictList.get(count)+",";
                writer.write(writeData);
                writer.newLine();
                count++;
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("happen io exception");
        }
    }

}
