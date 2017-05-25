package sample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseReader {
    public List<FeatureVector> read() {
        List<Path> filesInDatabase = getFilesInDatabase("db");
        List<List<Path>> filesGroupedByName = groupFilesByName(filesInDatabase);

        return filesGroupedByName
                .stream()
                .map(group -> new FeatureVectorCreator().createFeatureVectorFromLines(
                        readLinesFromFiles(group), getNameFromGroup(group)))
                .collect(Collectors.toList());
    }

    private List<Path> getFilesInDatabase(String directory) {
        try {
            return Files.walk(Paths.get(directory))
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private List<List<Path>> groupFilesByName(List<Path> filesInDatabase) {
        sortAlphabetically(filesInDatabase);
        List<List<Path>> groups = new ArrayList<>(filesInDatabase.size());
        int groupId = 0;

        for (int i = 0; i < filesInDatabase.size(); i++) {
            groups.add(new ArrayList<>());
            groups.get(groupId).add(filesInDatabase.get(i));
            String baseName = getBaseName(filesInDatabase.get(i));

            for (int j = i + 1; j < filesInDatabase.size(); j++, i++) {
                if (filesInDatabase.get(j).getFileName().toString().startsWith(baseName)) {
                    groups.get(groupId).add(filesInDatabase.get(j));
                } else {
                    groupId += 1;
                    break;
                }
            }
        }

        return groups;
    }

    private void sortAlphabetically(List<Path> filesInDatabase) {
        filesInDatabase.sort(Comparator.comparing(path -> path.getFileName().toString()));
    }

    private String getBaseName(Path path) {
        return path.getFileName().toString().split("\t")[0];
    }

    private List<String> readLinesFromFiles(List<Path> group) {
        List<String> lines = new ArrayList<>();

        for (Path path : group) {
            try {
                lines.addAll(Files.readAllLines(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lines;
    }

    private String getNameFromGroup(List<Path> group) {
        return group.get(0).getFileName().toString().split(" ")[0];
    }
}
