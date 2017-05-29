package sample;

import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Classification {
    private List<DataSet> dataSetsFromFiles;

    public Classification(List<DataSet> dataSetsFromFiles) {
        this.dataSetsFromFiles = dataSetsFromFiles;
    }

    public Optional<Integer> start(FeatureVector currentTyping) {
        List<String> names = getNames();

        Instances dataSets = new Instances("letters", createAttributes(names), countSamples());

        for (DataSet dataSet : dataSetsFromFiles) {
            for (FeatureVector featureVector : dataSet.getFeatureVectors()) {
                DenseInstance sample = new DenseInstance(27);
                for (int d = 0; d < 26; d++) {
                    sample.setValue(d, featureVector.getLetterPressTime().get(d));
                }

                Attribute nameAttribute = new Attribute("name", names, 26);
                sample.setValue(nameAttribute, dataSet.getBaseName());
                dataSets.add(sample);
            }
        }
        dataSets.setClassIndex(26);

        Instances currentDataSet = new Instances("letters", createAttributes(names), 1);
        DenseInstance denseInstance = new DenseInstance(27);
        for (int d = 0; d < 26; d++) {
            denseInstance.setValue(d, currentTyping.getLetterPressTime().get(d));
        }
        currentDataSet.add(denseInstance);
        currentDataSet.setClassIndex(26);

        Classifier ibk = new IBk(6);
        try {
            ibk.buildClassifier(dataSets);
            return Optional.of((int) ibk.classifyInstance(currentDataSet.instance(0)));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private int countSamples() {
        int size = 0;
        for (DataSet dataSet : dataSetsFromFiles) {
            for (FeatureVector featureVector : dataSet.getFeatureVectors()) {
                size += 1;
            }
        }

        return size;
    }

    private List<String> getNames() {
        return dataSetsFromFiles.stream()
                .map(DataSet::getBaseName)
                .collect(Collectors.toList());
    }

    private ArrayList<Attribute> createAttributes(List<String> names) {
        ArrayList<Attribute> attributes = new ArrayList<>(27);

        IntStream.rangeClosed(0, 25).forEach(value -> {
            attributes.add(new Attribute(Character.toString((char) ('A' + value)), value));
        });

        Attribute nameAttribute = new Attribute("name", names, 26);
        attributes.add(nameAttribute);

        return attributes;
    }
}
