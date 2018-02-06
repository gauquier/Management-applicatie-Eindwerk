package controller;

import DAO.LoginDao;
import DAO.PersoneelDao;
import DAO.RfidDao;
import DAO.RolDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.Duration;
import model.*;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddPersoneelController implements Initializable {
    @FXML
    private ComboBox<Rol> cmbRol = new ComboBox();
    ObservableList<Rol> rol = FXCollections.observableArrayList();

    @FXML
    private TextField voornaam;
    @FXML
    private TextField achternaam;
    @FXML
    private DatePicker geboortedatum;
    @FXML
    private TextField email;
    @FXML
    private TextField straat;
    @FXML
    private TextField huisnr;
    @FXML
    private TextField gemeente;
    @FXML
    private TextField postcode;
    @FXML
    private Label lblBadge;

    LocalDate date;
    Integer badge;
    Rfid rfid;
    Login login = new Login();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rol = RolDao.getAllRol();
        cmbRol.setItems(rol);

        geboortedatum.setShowWeekNumbers(false);
        geboortedatum.setValue(LocalDate.now().minusYears(16));
        geboortedatum.setEditable(false);
        final Callback<DatePicker, DateCell> beforeCurrent = new Callback<>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isAfter(LocalDate.now().minusYears(16))) {
                            setDisable(true);
                            setStyle("-fx-background-color: #EEEEEE;");
                        }
                    }
                };
            }
        };
        geboortedatum.setDayCellFactory(beforeCurrent);
    }

    @FXML
    protected void addBadge(ActionEvent event) throws IOException {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Badge toevoegen");
        dialog.setContentText("Scan uw badge");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String rf = result.get();
            if (rf.length() == 10) {
                if (LoginDao.checkLoginrfid(Integer.valueOf(rf)) == true) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Badge toevoegen niet gelukt");
                    alert.setHeaderText(null);
                    alert.setContentText("Badge wordt al gebruikt. Probeer opnieuw!");
                    alert.showAndWait();
                } else {
                    badge = Integer.valueOf(rf);
                    login.setLoginId(LoginDao.getId(login.getUsername(),login.getPassword()));
                    rfid = new Rfid(login,badge);
                    lblBadge.setText("Badge toegevoegd");
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Badge toevoegen niet gelukt");
                alert.setHeaderText(null);
                alert.setContentText("Badge niet toegevoegd. Probeer opnieuw!");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Badge toevoegen niet gelukt");
            alert.setHeaderText(null);
            alert.setContentText("Badge niet toegevoegd. Probeer opnieuw!");
            alert.showAndWait();
        }
    }

    @FXML
    void AddPersoneel(ActionEvent event) {
        voornaam.getStyleClass().remove("error");
        achternaam.getStyleClass().remove("error");
        straat.getStyleClass().remove("error");
        huisnr.getStyleClass().remove("error");
        gemeente.getStyleClass().remove("error");
        postcode.getStyleClass().remove("error");
        email.getStyleClass().remove("error");
        cmbRol.getStyleClass().remove("error");

        date = geboortedatum.getValue();
        Date sqlDate = java.sql.Date.valueOf( date );

        if (voornaam.getText() == null || voornaam.getText().trim().isEmpty() || achternaam.getText() == null || achternaam.getText().trim().isEmpty() || geboortedatum.getValue() == null || email.getText() == null || email.getText().trim().isEmpty() || cmbRol.getSelectionModel().isEmpty()
                || straat.getText() == null || straat.getText().trim().isEmpty() || huisnr.getText() == null || huisnr.getText().trim().isEmpty() || gemeente.getText() == null || gemeente.getText().trim().isEmpty() || postcode.getText() == null || postcode.getText().trim().isEmpty())
        {
            if (voornaam.getText() == null || voornaam.getText().trim().isEmpty())
            {
                voornaam.getStyleClass().add("error");
            }
            if (achternaam.getText() == null || achternaam.getText().trim().isEmpty())
            {
                achternaam.getStyleClass().add("error");
            }
            if (straat.getText() == null || straat.getText().trim().isEmpty())
            {
                straat.getStyleClass().add("error");
            }
            if (huisnr.getText() == null || huisnr.getText().trim().isEmpty())
            {
                huisnr.getStyleClass().add("error");
            }
            if (gemeente.getText() == null || gemeente.getText().trim().isEmpty())
            {
                gemeente.getStyleClass().add("error");
            }
            if (postcode.getText() == null || postcode.getText().trim().isEmpty())
            {
                postcode.getStyleClass().add("error");
            }
            if (email.getText() == null || email.getText().trim().isEmpty())
            {
                email.getStyleClass().add("error");
            }
            if (cmbRol.getSelectionModel() == null)
            {
                cmbRol.getStyleClass().add("error");
            }
            Alert alertmis = new Alert(Alert.AlertType.ERROR);
            alertmis.setTitle("Personeel toevoegen mislukt");
            alertmis.setHeaderText(null);
            alertmis.setContentText("Gelieve alle velden in te vullen!");
            alertmis.showAndWait();
        }  else {
            if (Validation.checkFirstName(voornaam.getText().toString()) == true && Validation.checkLastName(achternaam.getText().toString()) == true && Validation.checkAlphabetical(straat.getText().toString()) == true && Validation.checkHouseNumber(huisnr.getText().toString()) == true && Validation.checkAlphabetical(gemeente.getText().toString()) == true && Validation.checkPostalCode(postcode.getText().toString()) == true && Validation.checkEmail(email.getText().toString()) == true)
            {
                int output = cmbRol.getSelectionModel().getSelectedItem().getId();
                Adress adres = new Adress(straat.getText().toString(), Integer.parseInt(huisnr.getText()), gemeente.getText().toString(), Integer.parseInt(postcode.getText()));
                Rol rol = new Rol(output);
                User personeel = new User(voornaam.getText().toString(), achternaam.getText().toString(), sqlDate, email.getText().toString(), adres, rol);
                Boolean add = PersoneelDao.addPersoneel(personeel, output);
                Boolean addBadge = RfidDao.addRfid(rfid);
                if (add == true && addBadge == true)
                {
                    // Bron: https://github.com/PlusHaze/TrayNotification
                    String title = "Toevoegen gelukt";
                    String message = "Persoon is toegevoegd!";
                    TrayNotification tray = new TrayNotification(title, message, NotificationType.SUCCESS);
                    tray.showAndDismiss(Duration.seconds(4));
                }
                else {
                    Alert alertmis = new Alert(Alert.AlertType.CONFIRMATION);
                    alertmis.setTitle("Toevoegen mislukt");
                    alertmis.setHeaderText(null);
                    alertmis.setContentText("Persoon is niet toegevoegd! Probeer opnieuw!");
                    alertmis.showAndWait();
                }
            }  else {
                if (Validation.checkFirstName(voornaam.getText().toString()) == false) {
                    voornaam.getStyleClass().add("error");
                }
                if (Validation.checkLastName(achternaam.getText().toString()) == false) {
                    achternaam.getStyleClass().add("error");
                }
                if (Validation.checkAlphabetical(straat.getText().toString()) == false)
                {
                    straat.getStyleClass().add("error");
                }
                if (Validation.checkHouseNumber(huisnr.getText().toString()) == false)
                {
                    huisnr.getStyleClass().add("error");
                }
                if (Validation.checkAlphabetical(gemeente.getText().toString()) == false)
                {
                    gemeente.getStyleClass().add("error");
                }
                if (Validation.checkPostalCode(postcode.getText().toString()) == false)
                {
                    postcode.getStyleClass().add("error");
                }
                if (Validation.checkEmail(email.getText().toString()) == false)
                {
                    email.getStyleClass().add("error");
                }
                Alert alertmis = new Alert(Alert.AlertType.ERROR);
                alertmis.setTitle("Personeel toevoegen mislukt");
                alertmis.setHeaderText(null);
                alertmis.setContentText("Gelieve alle velden correct in te vullen!");
                alertmis.showAndWait();
            }
        }
    }
}
