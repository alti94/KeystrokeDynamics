package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;

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

    CompletableFuture<List<FeatureVector>> readDatabaseAsync;

    private boolean learningStarted;
    private LearningHelper learningHelper;

    @FXML
    public void initialize() {
        readDatabaseAsync = CompletableFuture.supplyAsync(() ->
                new DatabaseReader().read());

        learningStarted = false;
        learningHelper = new LearningHelper();
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
        learningStarted = false;
        changeUiState();

        Optional<String> name = askForFileName();

        CompletableFuture.runAsync(() -> {
            new DatabaseSaver().save(learningHelper.getKeysAndTimes(),
                    name.orElse("unknown 1"));
        });
    }

    public void identify(ActionEvent actionEvent) {
        // TODO: 25.05.17
        // TODO: 25.05.17 get result from readDatabaseAsync
        
        try {
            List<FeatureVector> featureVectors = readDatabaseAsync.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
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
            learningHelper.keyReleased();
        }
    }
}
