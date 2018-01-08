package views;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import models.*;

import java.io.IOException;
import java.time.*;
import java.util.Date;

public class BugDialog extends Dialog<TheBug> {
    @FXML
    private TextField tfName;
    @FXML
    private DatePicker dpDate;
    @FXML
    private ChoiceBox<Category> cbCategory;
    @FXML
    private ChoiceBox<BugType> cbType;
    @FXML
    private TextArea tfDescription;

    private TheBug current;

    public BugDialog(TheBug data) {
        current = data;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/BugDialog.fxml"));
            loader.setController(this);
            DialogPane dialog = loader.load();
            setDialogPane(dialog);
            setWidth(300);
            setHeight(200);

        } catch (IOException e) {
            e.printStackTrace();
            setResult(null);
            close();
        }
    }

    @FXML
    private void initialize() {
        tfName.setText(current.getName());
        ZonedDateTime zoned = current.getDate().toInstant().atZone(ZoneId.systemDefault());
        dpDate.setValue(LocalDate.from(zoned));
        cbCategory.setItems(FXCollections.observableArrayList(Category.values()));
        cbCategory.setValue(current.getCategory());
        cbType.setItems(FXCollections.observableArrayList(BugType.values()));
        cbType.setValue(current.getType());
        tfDescription.setText(current.getDescription());
        setResultConverter(bt -> bt != ButtonType.OK ? null : current.copy(
                current.getId(),
                tfName.getText(),
                tfDescription.getText(),
                Date.from(dpDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
                cbCategory.getValue(),
                cbType.getValue()));
    }
}
