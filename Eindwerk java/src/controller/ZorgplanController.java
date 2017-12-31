package controller;

import DAO.BewonerDao;
import DAO.LoginDao;
import DAO.MediactieDao;
import DAO.ZorgplanDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import model.*;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;


public class ZorgplanController implements Initializable {
    @FXML
    private ComboBox<Bewoner> cmbBewoner = new ComboBox();
    ObservableList bewoner = FXCollections.observableArrayList();

    @FXML
    private ComboBox<Medicatie> cmbMedicatie = new ComboBox();
    ObservableList medicatie = FXCollections.observableArrayList();

    @FXML
    private ComboBox<Zorgtaak> cmbZorgtaak = new ComboBox();
    ObservableList zorgtaak = FXCollections.observableArrayList();

    @FXML
    private TextField zorgTaak;
    @FXML
    private TextArea txtOpmerking;

    Medicatie med = new Medicatie();
    Bewoner bew = new Bewoner();
    Zorgtaak taak = new Zorgtaak();
    User user = new User();
    Login login = new Login();

    String gebruikersnaam;
    String pas;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bewoner = BewonerDao.getAllBewoners();
        cmbBewoner.setItems(bewoner);

        medicatie = MediactieDao.getAllMedicatie();
        cmbMedicatie.setItems(medicatie);

        zorgtaak = ZorgplanDao.getAllZorgtaak();
        cmbZorgtaak.setItems(zorgtaak);

    }

    @FXML
    void addZorgplan(ActionEvent event) {
        cmbBewoner.getStyleClass().remove("error");
        cmbMedicatie.getStyleClass().remove("error");
        cmbZorgtaak.getStyleClass().remove("error");

        bew = cmbBewoner.getSelectionModel().getSelectedItem();
        med = cmbMedicatie.getSelectionModel().getSelectedItem();
        taak = cmbZorgtaak.getSelectionModel().getSelectedItem();
        String opmerking = txtOpmerking.getText().toString();

        if (bew == null || bew.equals("")) {
            cmbBewoner.getStyleClass().add("error");
            Alert notSelected = new Alert(Alert.AlertType.ERROR);
            notSelected.setTitle("Geen bewoner gekozen");
            notSelected.setHeaderText(null);
            notSelected.setContentText("Gelieve een bewoner te selecteren!");
            notSelected.show();
        } else {
            if ((med == null || med.equals("")) && (taak == null || taak.equals("")))
            {
                if (med == null || med.equals("")) {
                    cmbMedicatie.getStyleClass().add("error");
                }
                if (taak == null || taak.equals("")) {
                    cmbZorgtaak.getStyleClass().add("error");
                }
                Alert notSelected = new Alert(Alert.AlertType.ERROR);
                notSelected.setTitle("Geen medicatie of zorgtaak gekozen");
                notSelected.setHeaderText(null);
                notSelected.setContentText("Gelieve een zorgtaak en/of medicatie te selecteren!");
                notSelected.show();
            }else {
                Dialog<Pair<String, String>> dialog = new Dialog<>();
                dialog.setTitle("Teken");
                ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

                GridPane gridPane = new GridPane();
                gridPane.setHgap(10);
                gridPane.setVgap(10);
                gridPane.setPadding(new Insets(20, 150, 10, 10));

                TextField username = new TextField();
                Label usern = new Label("Gebruikersnaam:");
                username.setPromptText("Gebruikersnaam");
                PasswordField password = new PasswordField();
                Label pass = new Label("Paswoord: ");
                password.setPromptText("Paswoord");

                gridPane.add(usern, 0, 0);
                gridPane.add(username, 1, 0);
                gridPane.add(pass, 0, 1);
                gridPane.add(password, 1, 1);

                dialog.getDialogPane().setContent(gridPane);

                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == loginButtonType) {
                        return new Pair<>(username.getText(), password.getText());
                    }
                    return null;
                });

                Optional<Pair<String, String>> result = dialog.showAndWait();

                result.ifPresent(pair -> {
                    gebruikersnaam = pair.getKey();
                    pas = pair.getValue();
                });

                if (LoginDao.checkLogin(gebruikersnaam.toString(), pas.toString()) == true) {
                    int userId = LoginDao.getUserId(gebruikersnaam.toString(), pas.toString());
                    user.setCurrentUser(userId);
                    Zorgplan zorgplan = new Zorgplan(med, taak, user, opmerking, bew);
                    Boolean check = ZorgplanDao.addZorgplan(zorgplan);

                    if (check == true) {
                        Alert added = new Alert(Alert.AlertType.INFORMATION);
                        added.setTitle("Toevoegen gelukt");
                        added.setHeaderText(null);
                        added.setContentText("Zorgplan is getekend!");
                        added.show();
                    } else {
                        Alert mislukt = new Alert(Alert.AlertType.ERROR);
                        mislukt.setTitle("Toevoegen niet gelukt");
                        mislukt.setHeaderText(null);
                        mislukt.setContentText("Zorgplan is niet getekend!");
                        mislukt.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Login niet gelukt");
                    alert.setHeaderText(null);
                    alert.setContentText("Gebruikersnaam of wachtwoord komen niet overeen. Probeer opnieuw!");
                    alert.showAndWait();
                }
            }
        }
    }

    @FXML
    void addZorgplanViaRFID(ActionEvent event) {
        cmbBewoner.getStyleClass().remove("error");
        cmbMedicatie.getStyleClass().remove("error");
        cmbZorgtaak.getStyleClass().remove("error");

        bew = cmbBewoner.getSelectionModel().getSelectedItem();
        med = cmbMedicatie.getSelectionModel().getSelectedItem();
        taak = cmbZorgtaak.getSelectionModel().getSelectedItem();
        String opmerking = txtOpmerking.getText().toString();

        if (bew == null || bew.equals("")) {
            cmbBewoner.getStyleClass().add("error");
            Alert notSelected = new Alert(Alert.AlertType.ERROR);
            notSelected.setTitle("Geen bewoner gekozen");
            notSelected.setHeaderText(null);
            notSelected.setContentText("Gelieve een bewoner te selecteren!");
            notSelected.show();
        } else {
            if ((med == null || med.equals("")) && (taak == null || taak.equals("")))
            {
                if (med == null || med.equals("")) {
                    cmbMedicatie.getStyleClass().add("error");
                }
                if (taak == null || taak.equals("")) {
                    cmbZorgtaak.getStyleClass().add("error");
                }
                Alert notSelected = new Alert(Alert.AlertType.ERROR);
                notSelected.setTitle("Geen medicatie of zorgtaak gekozen");
                notSelected.setHeaderText(null);
                notSelected.setContentText("Gelieve een zorgtaak en/of medicatie te selecteren!");
                notSelected.show();
            }else {
                TextInputDialog dialog = new TextInputDialog("");
                dialog.setTitle("Text Input Dialog");
                dialog.setContentText(" scan your badge");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    String rfid = result.get();
                    if (rfid.length() == 10) {
                        if (LoginDao.checkLoginrfid(Integer.valueOf(rfid)) == true) {
                            int loginId = LoginDao.getLoginId(Integer.valueOf(rfid));
                            int userId = LoginDao.getUserIdByLoginId(loginId);

                            user.setCurrentUser(userId);
                            Zorgplan zorgplan = new Zorgplan(med, taak, user, opmerking, bew);
                            Boolean check = ZorgplanDao.addZorgplan(zorgplan);

                            if (check == true) {
                                Alert added = new Alert(Alert.AlertType.INFORMATION);
                                added.setTitle("Toevoegen gelukt");
                                added.setHeaderText(null);
                                added.setContentText("Zorgplan is getekend!");
                                added.show();
                            } else {
                                Alert mislukt = new Alert(Alert.AlertType.ERROR);
                                mislukt.setTitle("Toevoegen niet gelukt");
                                mislukt.setHeaderText(null);
                                mislukt.setContentText("Zorgplan is niet getekend!");
                                mislukt.show();
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Login niet gelukt");
                            alert.setHeaderText(null);
                            alert.setContentText("Badge niet gevonden. Probeer opnieuw!");
                            alert.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Login niet gelukt");
                        alert.setHeaderText(null);
                        alert.setContentText("Badge niet gevonden. Probeer opnieuw!");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Login niet gelukt");
                    alert.setHeaderText(null);
                    alert.setContentText("Badge niet gevonden. Probeer opnieuw!");
                    alert.showAndWait();
                }
            }
        }
    }

    @FXML
    void addZorgtaak(ActionEvent event) {
        Zorgtaak zorgtaak = new Zorgtaak(zorgTaak.getText());
        if (ZorgplanDao.addZorgtaak(zorgtaak) == true) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Toevoegen gelutk");
            alert.setHeaderText(null);
            alert.setContentText("Het toevoegen van de zorgtaak is gelukt!");
            alert.showAndWait();
            zorgTaak.setText("");
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Toevoegen mislukt");
            alert.setHeaderText(null);
            alert.setContentText("Het toevoegen van de zorgtaak is niet gelukt! Probeer opnieuw.");
            alert.showAndWait();
        }
    }
}