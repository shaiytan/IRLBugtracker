package views;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.*;
import org.jetbrains.annotations.NotNull;
import presenter.*;

import java.io.IOException;
import java.util.*;

public class DesktopBugView extends Application implements View {
    @FXML
    private HBox boxBugs;
    @FXML
    private HBox boxImprovements;
    @FXML
    private HBox boxIdeas;
    @FXML
    private Label lblBug;
    @FXML
    private Label lblImprovement;
    @FXML
    private Label lblIdea;
    @FXML
    private TextArea tfBug;
    @FXML
    private TextArea tfImprovement;
    @FXML
    private TextArea tfIdea;
    @FXML
    private ListView<String> listBugs;
    @FXML
    private ListView<String> listImprovements;
    @FXML
    private ListView<String> listIdeas;

    private Presenter presenter;

    @FXML
    private void initialize() {
        presenter = new BugPresenter(new DesktopBugModel(), this);
        listBugs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listBugs.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            presenter.onBugSelected(newValue.intValue(), BugType.BUG);
            boxBugs.setDisable(newValue.equals(-1));
        });
        listImprovements.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listImprovements.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            presenter.onBugSelected(newValue.intValue(), BugType.IMPROVEMENT);
            boxImprovements.setDisable(newValue.equals(-1));
        });
        listIdeas.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listIdeas.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            presenter.onBugSelected(newValue.intValue(), BugType.IDEA);
            boxIdeas.setDisable(newValue.equals(-1));
        });
    }

    @Override
    public void setBugsList(@NotNull List<String> data) {
        listBugs.setItems(FXCollections.observableArrayList(data));
    }

    @Override
    public void setImprovementsList(@NotNull List<String> data) {
        listImprovements.setItems(FXCollections.observableArrayList(data));
    }

    @Override
    public void setIdeasList(@NotNull List<String> data) {
        listIdeas.setItems(FXCollections.observableArrayList(data));
    }

    private void setBugDetails(TheBug bug, TextArea area, Label header) {
        header.setText(bug.getName());
//        Calendar c = Calendar.getInstance(); если что, впихнуть календарь в формат
//        c.setTime(bug.getDate());
        String data = String.format("Category: %s \tRating: %d\n\n%s\n\n%4$td.%4$tm.%4$ty",
                bug.getCategory(),
                bug.getCategory().getRating(),
                bug.getDescription(),
                bug.getDate());
        area.setText(data);
    }

    @Override
    public void setBugData(@NotNull TheBug bug) {
        setBugDetails(bug, tfBug, lblBug);
    }

    @Override
    public void setIdeaData(@NotNull TheBug bug) {
        setBugDetails(bug, tfIdea, lblIdea);
    }

    @Override
    public void setImprovementData(@NotNull TheBug bug) {
        setBugDetails(bug, tfImprovement, lblImprovement);
    }

    @Override
    public void showBugForm(@NotNull TheBug bug) {
        Optional<TheBug> editedBug = new BugDialog(bug).showAndWait();
        editedBug.ifPresent(theBug -> presenter.editBug(theBug));
    }

    @Override
    public void showBugForm(@NotNull BugType type) {
        Optional<TheBug> addedBug = new BugDialog(
                new TheBug(0, "", "",
                        new Date(),
                        Category.ANNOYING,
                        type)).showAndWait();
        addedBug.ifPresent(theBug -> presenter.addBug(theBug));
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/MainWindow.fxml"));
        Stage stage = primaryStage;
        stage.setTitle("IRLBugtracker");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void onBugAdd(ActionEvent event) {
        presenter.onAdd(BugType.BUG);
    }

    public void onImprovementAdd(ActionEvent event) {
        presenter.onAdd(BugType.IMPROVEMENT);
    }

    public void onIdeaAdd(ActionEvent event) {
        presenter.onAdd(BugType.IDEA);
    }
}
