package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;
import sample.database.CsvExport;
import sample.database.DatabaseReader;
import sample.database.DatabaseSaver;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Controller {
    @FXML
    private Button startLearningButton;
    @FXML
    private Button addToDatabaseButton;
    @FXML
    private Button identifyButton;
    @FXML
    private TextArea learningTextArea;

    CompletableFuture<List<DataSet>> readDatabaseAsync;

    private boolean learningStarted;
    private LearningHelper learningHelper;

    @FXML
    public void initialize() {
        readDatabaseAsync = CompletableFuture.supplyAsync(() ->
                new DatabaseReader().read());

        learningStarted = false;
        learningHelper = new LearningHelper();

        CsvExport.export(new File("export.csv"));
    }

    public void startLearning(ActionEvent actionEvent) {
        learningStarted = true;
        changeUiState();
    }

    private void changeUiState() {
        startLearningButton.setDisable(!startLearningButton.isDisabled());
        learningTextArea.setDisable(!learningTextArea.isDisabled());
        addToDatabaseButton.setDisable(!addToDatabaseButton.isDisabled());
        identifyButton.setDisable(!identifyButton.isDisabled());
    }

    public void stopLearning(ActionEvent actionEvent) {
        Optional<String> name = askForFileName();

        new DatabaseSaver().save(learningHelper.getKeysAndTimes(),
                name.orElse("unknown 1"));

        reset();
    }

    public void identify(ActionEvent actionEvent) {
        List<DataSet> featureVectors = null;
        try {
            featureVectors = readDatabaseAsync.get();
        } catch (InterruptedException | ExecutionException e1) {
            e1.printStackTrace();
        }

        Classification classification = new Classification(featureVectors);
        Optional<Integer> nameId = classification.start(new FeatureVectorCreator().
                createFeatureVectorFromKeys(learningHelper.getKeysAndTimes()));
        if (nameId.isPresent()) {
            displayResult(nameId.get());
        } else {
            displayErrorMessage();
        }

        reset();
    }

    private void reset() {
        learningStarted = false;
        changeUiState();
        learningTextArea.clear();

        learningHelper = new LearningHelper();
        readDatabaseAsync = CompletableFuture.supplyAsync(() ->
                new DatabaseReader().read());
    }

    private void displayResult(Integer nameId) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Wynik klasyfikacji");
        try {
            alert.setHeaderText("Największe prawdopodobieństwo dla osoby: " +
                    readDatabaseAsync.get().get(nameId).getBaseName());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        alert.showAndWait();
    }

    private void displayErrorMessage() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText("Wystąpił błąd");
        alert.showAndWait();
    }

    private Optional<String> askForFileName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nazwa");
        dialog.setHeaderText("Podaj nazwę");
        dialog.setContentText("Np. Zuzia 1");
        return dialog.showAndWait();
    }

    public void learningKeyPressed(KeyEvent keyEvent) {
        if (learningStarted) {
            try {
                char letter = keyEvent.getText().toUpperCase().charAt(0);
                if (Character.isLetter(letter)) {
                    learningHelper.keyPressed(letter);
                }
            } catch (Exception e) {
                //Not a letter
                //No one cares what it is :)
            }
        }
    }

    public void learningKeyReleased(KeyEvent keyEvent) {
        if (learningStarted) {
            if (keyEvent.getCode().isLetterKey()) {
                char letter = keyEvent.getCode().getName().toUpperCase().charAt(0);
                learningHelper.keyReleased(letter);
            }
        }
    }
}
