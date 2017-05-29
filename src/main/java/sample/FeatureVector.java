package sample;

import java.util.ArrayList;
import java.util.List;

public class FeatureVector {
    private List<Integer> letterPressTime;

    public FeatureVector() {
        letterPressTime = new ArrayList<>(26);
    }

    public FeatureVector(List<Integer> letterPressTime) {
        this.letterPressTime = letterPressTime;
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
