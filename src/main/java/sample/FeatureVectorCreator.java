package sample;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FeatureVectorCreator {
    public FeatureVector createFeatureVectorFromLines(List<String> lines) {
        List<List<Integer>> pressTime = prepareList();

        for (String line : lines) {
            Pair<Character, Integer> lineParts = splitLine(line);
            pressTime.get(lineParts.getKey() - 'A').add(lineParts.getValue());
        }

        return new FeatureVector(calculateFeatureVector(pressTime));
    }

    public FeatureVector createFeatureVectorFromKeys(List<Pair<Character, Integer>> keys) {
        List<List<Integer>> pressTime = prepareList();

        for (Pair<Character, Integer> key : keys) {
            pressTime.get(key.getKey() - 'A').add(key.getValue());
        }

        return new FeatureVector(calculateFeatureVector(pressTime));
    }

    private List<List<Integer>> prepareList() {
        List<List<Integer>> list = new ArrayList<>(26);
        IntStream.rangeClosed(0, 25).forEach(i -> list.add(new ArrayList<>()));
        return list;
    }

    private Pair<Character, Integer> splitLine(String line) {
        String[] parts = line.split("\t");
        return new Pair<>(parts[0].charAt(0), Integer.valueOf(parts[1]));
    }

    public List<Integer> calculateFeatureVector(List<List<Integer>> pressTime) {
        return pressTime.stream()
                .mapToInt(letterTimes -> {
                    if (letterTimes.size() == 0) {
                        return 0;
                    } else {
                        return letterTimes.stream()
                                .mapToInt(Integer::intValue)
                                .sum() / letterTimes.size();
                    }
                })
                .boxed()
                .collect(Collectors.toList());
    }
}
