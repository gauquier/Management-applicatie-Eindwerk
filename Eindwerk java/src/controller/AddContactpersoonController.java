package controller;

import DAO.AdressDao;
import DAO.BewonerDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import model.Adress;
import model.Bewoner;
import model.Contactpersoon;

import java.io.IOException;
import java.net.URL;

public class AddContactpersoonController {
    private Contactpersoon contactpersoon = new Contactpersoon();
    private Bewoner bewoner = new Bewoner();

    @FXML
    private TextField voornaam, achternaam, identiteitskaartnr, relatie, telefoon ,email , straat, huisnr, gemeente, postcode;
    Adress a = new Adress();

    @FXML
    void addContactpersoon(ActionEvent event) {
        voornaam.getStyleClass().remove("error");
        achternaam.getStyleClass().remove("error");
        identiteitskaartnr.getStyleClass().remove("error");
        relatie.getStyleClass().remove("error");
        telefoon.getStyleClass().remove("error");
        email.getStyleClass().remove("error");
        straat.getStyleClass().remove("error");
        huisnr.getStyleClass().remove("error");
        gemeente.getStyleClass().remove("error");
        postcode.getStyleClass().remove("error");

        if (voornaam.getText() == null || voornaam.getText().trim().isEmpty() || achternaam.getText() == null || achternaam.getText().trim().isEmpty() || identiteitskaartnr.getText() == null || identiteitskaartnr.getText().trim().isEmpty() || relatie.getText() == null || relatie.getText().trim().isEmpty() || telefoon.getText() == null || telefoon.getText().trim().isEmpty()
                || email.getText() == null || email.getText().trim().isEmpty() || straat.getText() == null || straat.getText().trim().isEmpty() || huisnr.getText() == null || huisnr.getText().trim().isEmpty() || gemeente.getText() == null || gemeente.getText().trim().isEmpty() || postcode.getText() == null || postcode.getText().trim().isEmpty()){
            if (voornaam.getText() == null || voornaam.getText().trim().isEmpty())
            {
                voornaam.getStyleClass().add("error");
            }
            if (achternaam.getText() == null || achternaam.getText().trim().isEmpty())
            {
                achternaam.getStyleClass().add("error");
            }
            if (identiteitskaartnr.getText() == null || identiteitskaartnr.getText().trim().isEmpty())
            {
                identiteitskaartnr.getStyleClass().add("error");
            }
            if (relatie.getText() == null || relatie.getText().trim().isEmpty())
            {
                relatie.getStyleClass().add("error");
            }
            if (telefoon.getText() == null || telefoon.getText().trim().isEmpty())
            {
                telefoon.getStyleClass().add("error");
            }
            if (email.getText() == null || email.getText().trim().isEmpty())
            {
                email.getStyleClass().add("error");
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
            Alert alertmis = new Alert(Alert.AlertType.ERROR);
            alertmis.setTitle("Aanpassen mislukt");
            alertmis.setHeaderText(null);
            alertmis.setContentText("Gelieve alle velden in te vullen!");
            alertmis.showAndWait();
        }else {
            if (Validation.checkFirstName(voornaam.getText().toString()) == true && Validation.checkLastName(achternaam.getText().toString()) == true && Validation.checkIdentitiecard(identiteitskaartnr.getText().toString()) == true && Validation.checkAlphabetical(relatie.getText().toString()) == true && Validation.checkPhone(telefoon.getText()) == true
                    && Validation.checkEmail(email.getText()) == true && Validation.checkAlphabetical(straat.getText().toString()) == true && Validation.checkHouseNumber(huisnr.getText().toString()) == true && Validation.checkAlphabetical(gemeente.getText().toString()) == true && Validation.checkPostalCode(postcode.getText().toString()) == true) {
                Adress adres = new Adress(straat.getText().toString(), Integer.parseInt(huisnr.getText().toString()), gemeente.getText().toString(), Integer.parseInt(postcode.getText().toString()));
                contactpersoon = new Contactpersoon(bewoner, adres, voornaam.getText().toString(), achternaam.getText().toString(), Integer.parseInt(telefoon.getText().toString()), email.getText().toString(),
                        relatie.getText().toString(), identiteitskaartnr.getText().toString());
                Boolean add;
                add = BewonerDao.addContactpersoon(contactpersoon);
                if (add == true) {
                    Alert alertsuc = new Alert(Alert.AlertType.CONFIRMATION);
                    alertsuc.setTitle("Toevoegen gelukt");
                    alertsuc.setHeaderText(null);
                    alertsuc.setContentText("Contactpseroon is toegevoegd!");
                    alertsuc.show();
                    try {
                        URL paneUrl = getClass().getResource("../gui/Bewoner.fxml");
                        VBox pane = FXMLLoader.load(paneUrl);

                        BorderPane border = HomeController.getRoot();
                        border.setCenter(pane);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Alert alertmis = new Alert(Alert.AlertType.ERROR);
                    alertmis.setTitle("Toevoegen mislukt");
                    alertmis.setHeaderText(null);
                    alertmis.setContentText("Contactpersoon is niet toegeveogd! Probeer opnieuw!");
                    alertmis.showAndWait();
                }
            }else {
                if (Validation.checkFirstName(voornaam.getText().toString()) == false)
                {
                    voornaam.getStyleClass().add("error");
                }
                if (Validation.checkLastName(achternaam.getText().toString()) == false)
                {
                    achternaam.getStyleClass().add("error");
                }
                if (Validation.checkIdentitiecard(identiteitskaartnr.getText().toString()) == false)
                {
                    identiteitskaartnr.getStyleClass().add("error");
                }
                if (Validation.checkAlphabetical(relatie.getText().toString()) == false)
                {
                    relatie.getStyleClass().add("error");
                }
                if (Validation.checkPhone(telefoon.getText()) == false)
                {
                    telefoon.getStyleClass().add("error");
                }
                if (Validation.checkEmail(email.getText()) == false)
                {
                    email.getStyleClass().add("error");
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
                Alert alertmis = new Alert(Alert.AlertType.ERROR);
                alertmis.setTitle("Toevoegen mislukt");
                alertmis.setHeaderText(null);
                alertmis.setContentText("Gelieve alle velden correct in te vullen!");
                alertmis.showAndWait();
            }
        }
    }
}
