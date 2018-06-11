package controller;

import DAO.BewonerDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.Bewoner;
import model.Verpleegdossier;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VerpleegDossierToevoegenController implements Initializable{
    private Verpleegdossier dossier = new Verpleegdossier();
    private Bewoner bewoner = new Bewoner();

    @FXML
    private TextField Wondzorg, Bloedafname, VroegerBeroep, Specifiekewensen;

    @FXML
    private RadioButton SuikerziekteJa, SuikerziekteNee;

    ToggleGroup suikerziekte =new ToggleGroup();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SuikerziekteJa.setToggleGroup(suikerziekte);
        SuikerziekteJa.setUserData("True");
        SuikerziekteNee.setToggleGroup(suikerziekte);
        SuikerziekteNee.setUserData("False");
    }

    @FXML
    void addVerpleegDossier(ActionEvent event) {
        Wondzorg.getStyleClass().remove("error");
        Bloedafname.getStyleClass().remove("error");
        VroegerBeroep.getStyleClass().remove("error");
        Specifiekewensen.getStyleClass().remove("error");

        if (Wondzorg.getText() == null || Wondzorg.getText().trim().isEmpty() || Bloedafname.getText() == null || Bloedafname.getText().trim().isEmpty() || VroegerBeroep.getText() == null || VroegerBeroep.getText().trim().isEmpty() || Specifiekewensen.getText() == null || Specifiekewensen.getText().trim().isEmpty()){
            if (Wondzorg.getText() == null || Wondzorg.getText().trim().isEmpty())
            {
                Wondzorg.getStyleClass().add("error");
            }
            if (Bloedafname.getText() == null || Bloedafname.getText().trim().isEmpty())
            {
                Bloedafname.getStyleClass().add("error");
            }
            if (VroegerBeroep.getText() == null || VroegerBeroep.getText().trim().isEmpty())
            {
                VroegerBeroep.getStyleClass().add("error");
            }
            if (Specifiekewensen.getText() == null || Specifiekewensen.getText().trim().isEmpty())
            {
                Specifiekewensen.getStyleClass().add("error");
            }
            Alert alertmis = new Alert(Alert.AlertType.ERROR);
            alertmis.setTitle("Toevoegen mislukt");
            alertmis.setHeaderText(null);
            alertmis.setContentText("Gelieve alle velden in te vullen!");
            alertmis.showAndWait();
        }else {
            if (Validation.checkAlphabetical(Wondzorg.getText()) == true && Validation.checkAlphabetical(Bloedafname.getText()) == true  && Validation.checkAlphabetical(VroegerBeroep.getText()) == true  && Validation.checkText(Specifiekewensen.getText()) == true){
                Verpleegdossier dossier = new Verpleegdossier(Bewoner.getSelectedId(), Wondzorg.getText(), Bloedafname.getText(),Boolean.valueOf(suikerziekte.getSelectedToggle().getUserData().toString()), VroegerBeroep.getText(), Specifiekewensen.getText());
                Boolean add;
                add = BewonerDao.addVerpleegDossier(dossier);
                if (add == true)
                {
                    // PlusHaze. (2016, 3 maart). Tray notification. Geraadpleegd op 29 januari 2018, van https://github.com/PlusHaze/TrayNotification
                    String title = "Toevoegen gelukt";
                    String message = "Verpleegdossier is toegevoegd!";
                    TrayNotification tray = new TrayNotification(title, message, NotificationType.SUCCESS);
                    tray.showAndDismiss(Duration.seconds(4));
                    try {
                        URL paneUrl = getClass().getResource("../gui/Bewoner.fxml");
                        VBox pane = FXMLLoader.load(paneUrl);

                        BorderPane border = HomeController.getRoot();
                        border.setCenter(pane);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Alert alertmis = new Alert(Alert.AlertType.ERROR);
                    alertmis.setTitle("Toevoegen mislukt");
                    alertmis.setHeaderText(null);
                    alertmis.setContentText("Verpleegdossier is niet toegevoegd! Probeer opnieuw!");
                    alertmis.showAndWait();
                }
            }else {
                if (Validation.checkAlphabetical(Wondzorg.getText()) == false)
                {
                    Wondzorg.getStyleClass().add("error");
                }
                if (Validation.checkAlphabetical(Bloedafname.getText()) == false)
                {
                    Bloedafname.getStyleClass().add("error");
                }
                if (Validation.checkAlphabetical(VroegerBeroep.getText()) == false)
                {
                    VroegerBeroep.getStyleClass().add("error");
                }
                if (Validation.checkText(Specifiekewensen.getText()) == false)
                {
                    Specifiekewensen.getStyleClass().add("error");
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
