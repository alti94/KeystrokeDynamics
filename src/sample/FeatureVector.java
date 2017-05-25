package sample;

import java.util.ArrayList;
import java.util.List;

public class FeatureVector {
    private String id;
    private List<Integer> letterPressTime;

    public FeatureVector() {
        letterPressTime = new ArrayList<>(26);
    }

    public FeatureVector(String id, List<Integer> letterPressTime) {
        this.id = id;
        this.letterPressTime = letterPressTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.id = id;
    }

    public List<Integer> getLetterPressTime() {
        return letterPressTime;
    }

    public void setLetterPressTime(List<Integer> letterPressTime) {
        if (letterPressTime == null || letterPressTime.size() < 26) {
            throw new IllegalArgumentException();
        }

        this.letterPressTime = letterPressTime;
    }
}
