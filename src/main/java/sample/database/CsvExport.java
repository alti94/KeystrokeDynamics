package sample.database;

import sample.DataSet;
import sample.FeatureVector;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVSaver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Kamil on 31.05.2017.
 */
public class CsvExport {
    public static void export(File file) {
        List<DataSet> list = new DatabaseReader().read();

        try {
            file.delete();
            file.createNewFile();

            FileOutputStream outputStream = new FileOutputStream(file);

            for (DataSet data : list) {
                StringBuilder line = new StringBuilder(data.getBaseName());

                int[] vectorValues = new int[26];
                int[] div = new int[26];
                Arrays.fill(vectorValues, 0);
                Arrays.fill(div, 0);

                for (FeatureVector vector : data.getFeatureVectors()) {
                    List<Integer> letters = vector.getLetterPressTime();

                    for (int i = 0; i < letters.size(); i++) {
                        vectorValues[i] += letters.get(i);
                        if (letters.get(i) != 0)
                            div[i]++;
                    }
                }


                for (int i = 0; i < vectorValues.length; i++) {
                    if (div[i] > 0)
                        vectorValues[i] /= div[i];
                    line.append(',');
                    line.append(vectorValues[i]);
                }
                line.append('\n');

                outputStream.write(line.toString().getBytes());
            }

            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
