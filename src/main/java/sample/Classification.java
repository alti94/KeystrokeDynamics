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
        ArrayList<Attribute> attributes = createAttributes(names);

        Instances dataSet = new Instances("letters", attributes,
                dataSetsFromFiles.get(i).getFeatureVectors().size());

        for (int g = 0; g < dataSetsFromFiles.size(); g++) {
            for (int s = 0; s < dataSetsFromFiles.get(g).getFeatureVectors().size(); s++) {

            }
        }

        dataSet.setClassIndex(26);
        for (int i = 0; i < names.size(); i++) {


            for (FeatureVector featureVector : dataSetsFromFiles.get(i).getFeatureVectors()) {
                DenseInstance data = new DenseInstance(27);
                for (int d = 0; d < 26; d++) {
                    data.setValue(i, featureVector.getLetterPressTime().get(i));
                }

                Attribute nameAttribute = new Attribute("name", names, 26);
                data.setValue(nameAttribute, dataSetsFromFiles.get(i).getBaseName());
                dataSet.add(data);
            }


            dataSets[i] = dataSet;
        }

        Instances currentDataSet = new Instances("letters", attributes, 1);
        DenseInstance denseInstance = new DenseInstance(27);
        for (int d = 0; d < 26; d++) {
            denseInstance.setValue(d, currentTyping.getLetterPressTime().get(d));
        }
        currentDataSet.add(denseInstance);
        currentDataSet.setClassIndex(26);

        Classifier ibk = new IBk(1);
        try {
            ibk.buildClassifier(dataSets[1]);
            return Optional.of((int) ibk.classifyInstance(currentDataSet.instance(0)));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
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
