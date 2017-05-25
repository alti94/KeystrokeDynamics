package sample;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class LearningHelper {
    private List<Pair<Character, Integer>> letters;
    private char currentlyPressedLetter;
    private long currentlyPressedLetterStartTime;

    public LearningHelper() {
        letters = new ArrayList<>();
    }

    public void keyPressed(char letter) {
        currentlyPressedLetter = letter;
        currentlyPressedLetterStartTime = System.currentTimeMillis();
    }

    public void keyReleased() {
        Integer pressTime = Math.toIntExact(System.currentTimeMillis() -
                currentlyPressedLetterStartTime);
        letters.add(new Pair<>(currentlyPressedLetter, pressTime));
    }

    public List<Pair<Character, Integer>> getKeysAndTimes() {
        return letters;
    }
}
