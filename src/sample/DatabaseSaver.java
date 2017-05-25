package sample;

import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseSaver {
    public void save(List<Pair<Character, Integer>> integers, String fileName) {
        List<String> lines = integers
                .stream()
                .map(characterIntegerPair -> characterIntegerPair.getKey() + "\t" +
                        characterIntegerPair.getValue())
                .collect(Collectors.toList());

        try {
            Files.write(Paths.get("db/" + fileName), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
