package sample;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class LearningHelper {
    private List<Pair<Character, Integer>> letters;
    private boolean[] currentlyPressedLetter = new boolean[26];
    private long[] currentPressedLetterTimes = new long[26];

    public LearningHelper() {
        letters = new ArrayList<>();
    }

    public void keyPressed(char letter) {
        int id = letter - 'A';

        if (!currentlyPressedLetter[id]) {
            currentlyPressedLetter[id] = true;
            currentPressedLetterTimes[id] = System.currentTimeMillis();
        }
    }

    public void keyReleased(char letter) {
        int id = letter - 'A';

        currentlyPressedLetter[id] = false;

        int pressTime = (int)(System.currentTimeMillis() -
                currentPressedLetterTimes[id]);
        letters.add(new Pair<>(letter, pressTime));
    }

    public List<Pair<Character, Integer>> getKeysAndTimes() {
        return letters;
    }
}
