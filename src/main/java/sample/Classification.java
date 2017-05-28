package sample;

import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Classification {
    private List<DataSet> dataSetsFromFiles;

    public Classification(List<DataSet> dataSetsFromFiles) {
        this.dataSetsFromFiles = dataSetsFromFiles;
    }

    public void start(FeatureVector currentTyping) {
        List<String> names = dataSetsFromFiles.stream()
                .map(DataSet::getBaseName)
                .collect(Collectors.toList());
        ArrayList<Attribute> attributes = createAttributes(names);

        Instances[] dataSets = new Instances[names.size()];
        for (int i = 0; i < names.size(); i++) {
            int[] countPerPerson = new int[dataSetsFromFiles.get(i).getFeatureVectors().size()];
            Instances dataSet = new Instances("letters", attributes, 27);

            for (FeatureVector featureVector : dataSetsFromFiles.get(i).getFeatureVectors()) {
                DenseInstance data = new DenseInstance(27);
                for (int d = 0; d < 26; d++) {
                    data.setValue(i, featureVector.getLetterPressTime().get(i));
                }

                Attribute nameAttribute = new Attribute("name", names, 26);
                data.setValue(nameAttribute, dataSetsFromFiles.get(i).getBaseName());
                dataSet.add(data);
            }

            dataSet.setClassIndex(26);
            dataSets[i] = dataSet;
        }

        Classifier ibk = new IBk(10);
        try {
            ibk.buildClassifier(dataSets[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int dataSetSize = dataSets[0].size();


//                for (int instanceNumber = 0; instanceNumber < datasetSize; instanceNumber++) {
//                    Instance test = datasets[d].instance(instanceNumber);
//                    datasets[d].remove(instanceNumber);
//
//                    Classifier ibk = new IBk(k);
//                    ibk.buildClassifier(datasets[d]);
//                    int classification = (int) ibk.classifyInstance(test);
//                    //System.out.println(k + ", " + instanceNumber + ": " + ibk.classifyInstance(test));
//                    if (classification != (instanceNumber / (datasetSize / names.size()))) {
//                        //System.out.println(k + ", " + instanceNumber + ", " + test);
//                        errors++;
//                    }
//
//                    datasets[d].add(instanceNumber, test);
//                }
//                System.out.println("K: " + k + ", errors = " + errors + "/" + datasetSize + " (" + (100.0 * errors / datasetSize) + "%)");
//            }
    }


    public ArrayList<Attribute> createAttributes(List<String> names) {
        ArrayList<Attribute> attributes = new ArrayList<>(27);

        IntStream.rangeClosed(0, 25).forEach(value -> {
            attributes.add(new Attribute(Character.toString((char) ('A' + value)), value));
        });

        Attribute nameAttribute = new Attribute("name", names, 26);
        attributes.add(nameAttribute);

        return attributes;
    }
}
