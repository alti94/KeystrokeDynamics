package sample;

import java.util.ArrayList;
import java.util.List;

public class DataSet {
    private String baseName;
    private List<FeatureVector> featureVectors;

    public DataSet(String baseName) {
        this.baseName =baseName;
        featureVectors = new ArrayList<>();
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public List<FeatureVector> getFeatureVectors() {
        return featureVectors;
    }

    public void setFeatureVectors(List<FeatureVector> featureVectors) {
        this.featureVectors = featureVectors;
    }
}
