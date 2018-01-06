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

    private void setDefaultView(String headerData, Label header, String areaData, TextArea area) {
        header.setText(headerData);
        area.setText(areaData);
    }
    @FXML
    private void initialize() {
        presenter = new BugPresenter(new DesktopBugModel(), this);
        listBugs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listBugs.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            boolean noneSelected = newValue.equals(-1);
            boxBugs.setDisable(noneSelected);
            if (noneSelected) setDefaultView("Bug", lblBug, "Описание бага", tfBug);
            else presenter.onBugSelected(newValue.intValue(), BugType.BUG);
        });
        listImprovements.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listImprovements.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            boolean noneSelected = newValue.equals(-1);
            boxImprovements.setDisable(noneSelected);
            if (noneSelected)
                setDefaultView("Improvement", lblImprovement, "Описание улучшения", tfImprovement);
            else presenter.onBugSelected(newValue.intValue(), BugType.IMPROVEMENT);
        });
        listIdeas.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listIdeas.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            boolean noneSelected = newValue.equals(-1);
            boxIdeas.setDisable(noneSelected);
            if (noneSelected) setDefaultView("Idea", lblIdea, "Описание идеи", tfIdea);
            else presenter.onBugSelected(newValue.intValue(), BugType.IDEA);
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
        primaryStage.setTitle("IRLBugtracker");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    @FXML
    private void onBugAdd() {
        presenter.onAdd(BugType.BUG);
    }

    @FXML
    private void onImprovementAdd() {
        presenter.onAdd(BugType.IMPROVEMENT);
    }

    @FXML
    private void onIdeaAdd() {
        presenter.onAdd(BugType.IDEA);
    }

    @FXML
    private void onBugFix() {
        presenter.onFix(listBugs.getSelectionModel().getSelectedIndex(), BugType.BUG);
    }

    @FXML
    private void onImprovementFix() {
        presenter.onFix(listImprovements.getSelectionModel().getSelectedIndex(), BugType.IMPROVEMENT);
    }

    @FXML
    private void onIdeaFix() {
        presenter.onFix(listIdeas.getSelectionModel().getSelectedIndex(), BugType.IDEA);
    }

    @FXML
    private void onBugDelete() {
        presenter.onDelete(listBugs.getSelectionModel().getSelectedIndex(), BugType.BUG);
    }

    @FXML
    private void onImprovementDelete() {
        presenter.onDelete(listImprovements.getSelectionModel().getSelectedIndex(), BugType.IMPROVEMENT);
    }

    @FXML
    private void onIdeaDelete() {
        presenter.onDelete(listIdeas.getSelectionModel().getSelectedIndex(), BugType.IDEA);
    }

    @FXML
    private void onBugEdit() {
        presenter.onEdit(listBugs.getSelectionModel().getSelectedIndex(), BugType.BUG);
    }

    @FXML
    private void onImprovementEdit() {
        presenter.onEdit(listImprovements.getSelectionModel().getSelectedIndex(), BugType.IMPROVEMENT);
    }

    @FXML
    private void onIdeaEdit() {
        presenter.onEdit(listIdeas.getSelectionModel().getSelectedIndex(), BugType.IDEA);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
