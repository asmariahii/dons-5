package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import entities.Dons;
import javafx.stage.Stage;
import service.DonsService;

import java.text.SimpleDateFormat;
import java.util.List;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class AfficherDonsController {
    private Stage primaryStage;
    private DonsService donsService;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setDonsService(DonsService donsService) {
        this.donsService = donsService;
    }

    @FXML
    private TableView<Dons> donsTable;

    @FXML
    private TableColumn<Dons, String> nomUserColumn;

    @FXML
    private TableColumn<Dons, String> prenomUserColumn;

    @FXML
    private TableColumn<Dons, String> emailUserColumn;

    @FXML
    private TableColumn<Dons, String> numTelColumn;

    @FXML
    private TableColumn<Dons, Integer> nbPointsColumn;

    @FXML
    private TableColumn<Dons, String> dateAjoutColumn;

    @FXML
    private TableColumn<Dons, String> etatStatutDonsColumn;

    public AfficherDonsController() {
        donsService = new DonsService();
    }

    @FXML
    private void handleAjouterDon() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterDons.fxml"));
            Parent root = loader.load();
            AjouterDonsController ajouterDonsController = loader.getController();
            ajouterDonsController.initialize();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter Don");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la vue pour ajouter un don.");
        }
    }

    @FXML
    private void handleSupprimerDon() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SupprimerDons.fxml"));
            Parent root = loader.load();
            SupprimerDonsController supprimerDonsController = loader.getController();
            supprimerDonsController.setDonsService(donsService);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Supprimer Don");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la vue pour supprimer un don.");
        }
    }

    @FXML
    void initialize() {
        nomUserColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNomUser()));
        prenomUserColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrenomUser()));
        emailUserColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmailUser()));
        numTelColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNumTel()));
        nbPointsColumn.setCellValueFactory(new PropertyValueFactory<>("nbPoints"));
        dateAjoutColumn.setCellValueFactory(data -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return new SimpleStringProperty(dateFormat.format(data.getValue().getDate_ajout()));
        });
        etatStatutDonsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEtatStatutDons()));

        addSupprimerButtonToTable();

        loadDons();
    }

    private void addSupprimerButtonToTable() {
        TableColumn<Dons, Void> colSupprimer = new TableColumn<>("Supprimer");
        Callback<TableColumn<Dons, Void>, TableCell<Dons, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Dons, Void> call(final TableColumn<Dons, Void> param) {
                final TableCell<Dons, Void> cell = new TableCell<>() {
                    private final Button btn = new Button("Supprimer");

                    {
                        btn.setOnAction((event) -> {
                            Dons don = getTableView().getItems().get(getIndex());
                            supprimerDon(don);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        colSupprimer.setCellFactory(cellFactory);
        donsTable.getColumns().add(colSupprimer);
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadDons() {
        donsTable.getItems().clear();
        List<Dons> donsList = donsService.getAllDonsWithUserDetails();
        donsTable.getItems().addAll(donsList);
    }

    private void supprimerDon(Dons don) {
        boolean success = donsService.supprimerDons(don);
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "Le don a été supprimé avec succès.");
            loadDons();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur de suppression", "Impossible de supprimer le don.");
        }
    }
}
