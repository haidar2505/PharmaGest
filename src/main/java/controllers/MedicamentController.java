package controllers;

import DAO.FormeDAO;
import DAO.FamilleDAO;
import DAO.FournisseurDAO;
import DAO.MedicamentDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Famille;
import models.Forme;
import models.Fournisseur;
import models.Medicament;
import utils.Utils;
import utils.ValidationUtils;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class MedicamentController {
    Utils sceneLoader = new Utils();

    @FXML public Button maintenanceButton;

    public void maintenanceButtonOnAction(ActionEvent e) throws IOException {
        sceneLoader.loadScene("Maintenance.fxml", "Maintenance", maintenanceButton);
    }

    //Table columns
    @FXML private TableView<Medicament> medicamentTable;
    @FXML private TableColumn<Medicament, String> idColumn;
    @FXML private TableColumn<Medicament, String> dciColumn;
    @FXML private TableColumn<Medicament, String> formeColumn;
    @FXML private TableColumn<Medicament, String> familleColumn;
    @FXML private TableColumn<Medicament, String> dosageColumn;
    @FXML private TableColumn<Medicament, Double> puachatColumn;
    @FXML private TableColumn<Medicament, Double> puventeColumn;
    @FXML private TableColumn<Medicament, String> qtestockeColumn;
    @FXML private TableColumn<Medicament, String> ordonnanceColumn;
    @FXML private TableColumn<Medicament, String> fournisseurColumn;

    //ComboBox
    @FXML private ComboBox<String> searchComboBox;
    @FXML private ComboBox<String> formeComboBox;
    @FXML private ComboBox<String> familleComboBox;
    @FXML private ComboBox<String> fournisseurComboBox;
    @FXML private ComboBox<String> uniteComboBox;
    @FXML private ComboBox<String> ordonnanceComboBox;

    //ComboBox values
    private final ObservableList<String> columns = FXCollections.observableArrayList("Médicament", "Forme", "Famille", "Stock", "Fournisseur");
    private final ObservableList<String> unite = FXCollections.observableArrayList("g", "mg");
    private final ObservableList<String> ordonnace = FXCollections.observableArrayList("Oui", "Non");

    public void initialize() throws SQLException{
        if (medicamentTable != null && searchComboBox != null && formeComboBox != null && familleComboBox != null) {

            //Call DAO
            FamilleDAO familleDAO = new FamilleDAO();
            FormeDAO formeDAO = new FormeDAO();
            FournisseurDAO fournisseurDAO = new FournisseurDAO();

            //Set up ComboBox
            searchComboBox.setItems(columns);
            uniteComboBox.setItems(unite);
            ordonnanceComboBox.setItems(ordonnace);

            ObservableList<Forme> allFormes = formeDAO.getAllForme();
            ObservableList<String> formeNames = FXCollections.observableArrayList();
            for (Forme forme : allFormes) {
                formeNames.add(forme.getNomForme());
            }

            ObservableList<Famille> familles = familleDAO.getAllFamille();
            ObservableList<String> familleNames = FXCollections.observableArrayList();
            for (Famille famille : familles) {
                familleNames.add(famille.getNomFamille());
            }

            ObservableList<Fournisseur> fournisseurs = fournisseurDAO.getAllFounisseur();
            ObservableList<String> fournisseursNames = FXCollections.observableArrayList();
            for (Fournisseur fournisseur : fournisseurs) {
                fournisseursNames.add(fournisseur.getNom());
            }

            formeComboBox.setItems(formeNames);
            familleComboBox.setItems(familleNames);
            fournisseurComboBox.setItems(fournisseursNames);

            //Table columns
            idColumn.setCellValueFactory(new PropertyValueFactory<>("idMedicament"));
            dciColumn.setCellValueFactory(new PropertyValueFactory<>("dci"));
            formeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getForme().getNomForme()));
            familleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFamille().getNomFamille()));
            dosageColumn.setCellValueFactory(new PropertyValueFactory<>("dosage"));
            puachatColumn.setCellValueFactory(new PropertyValueFactory<>("puAchat"));
            puventeColumn.setCellValueFactory(new PropertyValueFactory<>("puVente"));
            qtestockeColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
            familleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFournisseur().getNom()));
            ordonnanceColumn.setCellValueFactory(new PropertyValueFactory<>("ordonnance"));
            ordonnanceColumn.setCellValueFactory(cellData -> {
                boolean isOrdonnance = cellData.getValue().isOrdonnance();
                return new SimpleStringProperty(isOrdonnance ? "Oui" : "Non");
            });

            //Selection row
            medicamentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {

                    idField.setText(String.valueOf(newSelection.getIdMedicament()));
                    medicamentField.setText(newSelection.getDci());
                    formeComboBox.setValue(newSelection.getForme().getNomForme());
                    familleComboBox.setValue(newSelection.getFamille().getNomFamille());
                    dosageField.setText(newSelection.getDosage());
                    puachatField.setText(String.valueOf(newSelection.getPuAchat()));
                    puventeField.setText(String.valueOf(newSelection.getPuVente()));
                    qtestockeField.setText(String.valueOf(newSelection.getStock()));
                    ordonnanceComboBox.setValue(String.valueOf(newSelection.isOrdonnance()));
                    fournisseurComboBox.setValue(newSelection.getFournisseur().getNom());
                }
            });

            //Load data
            loadMedicamentData();
        }
    }

    public boolean isRowSelected() {
        return medicamentTable.getSelectionModel().getSelectedItem() != null;
    }

    private ObservableList<Medicament> medicamentList = FXCollections.observableArrayList();

    //Load medicament data
    private void loadMedicamentData() throws SQLException {
        MedicamentDAO medicamentDAO = new MedicamentDAO();
        medicamentList = medicamentDAO.getAllMedicament();
        medicamentTable.setItems(medicamentList);
    }

    @FXML private TextField searchField;

    public void searchButtonOnAction(ActionEvent e) throws SQLException {

        //Filter list and call DAO
        ObservableList<Medicament> filteredList = FXCollections.observableArrayList();
        MedicamentDAO medicamentDAO = new MedicamentDAO();
        medicamentList = medicamentDAO.getAllMedicament();

        String search = searchField.getText().trim();
        String lowerCaseFilter = search.toLowerCase();


        if(search.isEmpty() || searchComboBox.getValue() == null){
            loadMedicamentData();
        } else {
            String selectedComboBox = searchComboBox.getValue().trim();

            if (selectedComboBox.isEmpty()) {
                deleteError.setText("Choisir une colonne");
            } else {
                for (Medicament medicament : medicamentList) {
                    boolean matches = false;

                    switch (selectedComboBox) {
                        case "Médicament":
                            matches = medicament.getDci().toLowerCase().contains(lowerCaseFilter);
                            break;
                        case "Forme":
                            matches = medicament.getForme().getNomForme().toLowerCase().contains(lowerCaseFilter);
                            break;
                        case "Famille":
                            matches = medicament.getFamille().getNomFamille().toLowerCase().contains(lowerCaseFilter);
                            break;
                        case "Fournisseur":
                            matches = medicament.getFournisseur().getNom().toLowerCase().contains(lowerCaseFilter);
                            break;
                        case "Qte stocké":
                            try {
                                matches = medicament.getStock() <= medicament.getStock();
                            } catch (NumberFormatException event) {
                                matches = false;
                            }
                            break;
                    }
                    if (matches) {
                        filteredList.add(medicament);
                    }
                }
            }
        }
        medicamentTable.setItems(filteredList);
    }

    public void searchButtonAnnulerOnAction(ActionEvent e) throws SQLException {
        searchComboBox.getSelectionModel().clearSelection();
        searchField.clear();
        loadMedicamentData();
    }

    @FXML private TextField idField;
    @FXML private TextField medicamentField;
    @FXML private TextField puachatField;
    @FXML private TextField puventeField;
    @FXML private TextField qtestockeField;
    @FXML private TextField stockMinField;
    @FXML private TextField stockMaxField;
    @FXML private TextField dosageField;

    @FXML private Label medicamentFieldError;
    @FXML private Label familleFieldError;
    @FXML private Label formeFieldError;
    @FXML private Label dosageFieldError;
    @FXML private Label uniteFieldError;
    @FXML private Label puachatFieldError;
    @FXML private Label puventeFieldError;
    @FXML private Label qtestockeFieldError;
    @FXML private Label stockMinFieldError;
    @FXML private Label stockMaxFieldError;
    @FXML private Label fournisseurFieldError;
    @FXML private Label ordonnanceFieldError;

    public void medicamentAddButtonOnAction(ActionEvent e) throws SQLException{
        MedicamentDAO medicamentDAO = new MedicamentDAO();

        String verifydci = medicamentField.getText().trim();
        String verifyforme = formeComboBox.getValue();
        String verifydosage = dosageField.getText().trim() + uniteComboBox.getValue();


        boolean isInvalid = false;
        boolean isOrdonnace = false;

        if(medicamentDAO.verifyExistingMedicament(verifydci, verifydosage, verifyforme)){
            medicamentFieldError.setText("Médicament existant");
            isInvalid = true;
        }else{
            if (ValidationUtils.validateDCI(medicamentField, medicamentFieldError)) isInvalid = true;
        }

        if (ValidationUtils.validatePrice(puachatField, puachatFieldError)) isInvalid = true;
        if (ValidationUtils.validatePrice(puventeField, puventeFieldError)) isInvalid = true;
        if (ValidationUtils.validateQteStock(qtestockeField, qtestockeFieldError)) isInvalid = true;
        if (ValidationUtils.validateQteStock(stockMinField, stockMinFieldError)) isInvalid = true;
        if (ValidationUtils.validateQteStock(stockMaxField, stockMaxFieldError)) isInvalid = true;

        if (dosageField.getText().isEmpty()) {
            dosageFieldError.setText("Entrez le dosage du médicament en mg ou g");
            isInvalid = true;
        } else {
            dosageFieldError.setText("");
        }

        if (uniteComboBox.getValue() == null) {
            uniteFieldError.setText("Choisir l'unité du dosage");
            isInvalid = true;
        } else {
            uniteFieldError.setText("");
        }

        if (formeComboBox.getValue() == null) {
            formeFieldError.setText("Choisir la forme du médicament");
            isInvalid = true;
        } else {
            formeFieldError.setText("");
        }

        if (familleComboBox.getValue() == null) {
            familleFieldError.setText("Choisir la famille du médicament");
            isInvalid = true;
        } else {
            familleFieldError.setText("");
        }

        if (fournisseurComboBox.getValue() == null) {
            fournisseurFieldError.setText("Choisir un fournisseur");
            isInvalid = true;
        } else {
            fournisseurFieldError.setText("");
        }

        if (ordonnanceComboBox.getValue() != null) {
            String selectedAdmin = ordonnanceComboBox.getValue().trim();
            isOrdonnace = selectedAdmin.equals("Admin");
            ordonnanceFieldError.setText("");
        } else {
            ordonnanceFieldError.setText("Définir l'ordonnance");
            isInvalid = true;
        }

        if (!isInvalid) {
            FormeDAO formeDAO = new FormeDAO();
            FamilleDAO familleDAO = new FamilleDAO();
            FournisseurDAO fournisseurDAO = new FournisseurDAO();

            String dci = medicamentField.getText().trim();
            String nomforme = formeComboBox.getValue();
            String nomFamille = familleComboBox.getValue();
            String nomFournisseur = fournisseurComboBox.getValue();
            double puAchat = Double.parseDouble(puachatField.getText().trim());
            double puVente = Double.parseDouble(puventeField.getText().trim());
            int qteStock = Integer.parseInt(qtestockeField.getText().trim());
            int stockMin = Integer.parseInt(stockMinField.getText().trim());
            int stockMax = Integer.parseInt(stockMaxField.getText().trim());
            String dosage = dosageField.getText().trim() + uniteComboBox.getValue();

            int idForme = formeDAO.getFormeId(nomforme);
            int idFamille = familleDAO.getFamilleId(nomFamille);
            int idFournisseur = fournisseurDAO.getFournisseurId(nomFournisseur);

            Medicament newMedicament = new Medicament(dci, dosage, puAchat, puVente, qteStock, stockMin, stockMax, isOrdonnace, idForme, idFamille, idFournisseur);
            if (medicamentDAO.addMedicament(newMedicament)) {
                loadMedicamentData();
                clearForm();
            }
        }
    }

    public void medicamentModifierButtonOnAction(ActionEvent e) throws SQLException {
        if(isRowSelected()) {

            MedicamentDAO medicamentDAO = new MedicamentDAO();

            boolean isInvalid = false;
            boolean isOrdonnace = false;

            if (ValidationUtils.validateDCI(medicamentField, medicamentFieldError)) isInvalid = true;
            if (ValidationUtils.validatePrice(puachatField, puachatFieldError)) isInvalid = true;
            if (ValidationUtils.validatePrice(puventeField, puventeFieldError)) isInvalid = true;
            if (ValidationUtils.validateQteStock(qtestockeField, qtestockeFieldError)) isInvalid = true;
            if (ValidationUtils.validateQteStock(stockMinField, stockMinFieldError)) isInvalid = true;
            if (ValidationUtils.validateQteStock(stockMaxField, stockMaxFieldError)) isInvalid = true;

            if (dosageField.getText().isEmpty()) {
                dosageFieldError.setText("Entrez le dosage du médicament en mg ou g");
                isInvalid = true;
            } else {
                dosageFieldError.setText("");
            }

            if (uniteComboBox.getValue() == null) {
                uniteFieldError.setText("Choisir l'unité du dosage");
                isInvalid = true;
            } else {
                uniteFieldError.setText("");
            }

            if (formeComboBox.getValue() == null) {
                formeFieldError.setText("Choisir la forme du médicament");
                isInvalid = true;
            } else {
                formeFieldError.setText("");
            }

            if (familleComboBox.getValue() == null) {
                familleFieldError.setText("Choisir la famille du médicament");
                isInvalid = true;
            } else {
                familleFieldError.setText("");
            }

            if (fournisseurComboBox.getValue() == null) {
                fournisseurFieldError.setText("Choisir un fournisseur");
                isInvalid = true;
            } else {
                fournisseurFieldError.setText("");
            }

            if (ordonnanceComboBox.getValue() != null) {
                String selectedAdmin = ordonnanceComboBox.getValue().trim();
                isOrdonnace = selectedAdmin.equals("Admin");
                ordonnanceFieldError.setText("");
            } else {
                ordonnanceFieldError.setText("Définir l'ordonnance");
                isInvalid = true;
            }

            if(!isInvalid){
                FormeDAO formeDAO = new FormeDAO();
                FamilleDAO familleDAO = new FamilleDAO();
                FournisseurDAO fournisseurDAO = new FournisseurDAO();

                int idDci = Integer.parseInt(idField.getText().trim());
                String dci = medicamentField.getText().trim();
                String nomforme = formeComboBox.getValue();
                String nomFamille = familleComboBox.getValue();
                String nomFournisseur = fournisseurComboBox.getValue();
                double puAchat = Double.parseDouble(puachatField.getText().trim());
                double puVente = Double.parseDouble(puventeField.getText().trim());
                int qteStock = Integer.parseInt(qtestockeField.getText().trim());
                int stockMin = Integer.parseInt(stockMinField.getText().trim());
                int stockMax = Integer.parseInt(stockMaxField.getText().trim());
                String dosage = dosageField.getText().trim() + uniteComboBox.getValue();

                int idForme = formeDAO.getFormeId(nomforme);
                int idFamille = familleDAO.getFamilleId(nomFamille);
                int idFournisseur = fournisseurDAO.getFournisseurId(nomFournisseur);

                Medicament modifierMedicament = new Medicament(dci, dosage, puAchat, puVente, qteStock, stockMin, stockMax, isOrdonnace, idForme, idFamille, idFournisseur);
                if (medicamentDAO.modifierMedicament(idDci, modifierMedicament)) {
                    loadMedicamentData();
                    clearForm();
                }
            }
        }else{
            clearForm();
            medicamentFieldError.setText("Choisir un médicament");
        }
    }

    @FXML private Label deleteError;

    public void supprimerMedicamentButtonOnAction(ActionEvent e) throws SQLException {
        if(isRowSelected()) {
            int idDci = Integer.parseInt(idField.getText().trim());
            String dci = medicamentField.getText().trim();

            if (dci.isEmpty()) {
                deleteError.setText("Choisir un médicament");
            } else {
                deleteError.setText("");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Supprimer médicament");
                alert.setHeaderText("Vous vouliez supprimer le médicament : " + dci);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    MedicamentDAO medicamentDAO = new MedicamentDAO();
                    medicamentDAO.deleteMedicament(idDci);
                    clearForm();
                    loadMedicamentData();
                } else {
                    loadMedicamentData();
                    clearForm();
                }
            }
        }else{
            deleteError.setText("Choisir un médicament");
        }
    }

    public void medicamentAnnulerButtonOnAction(ActionEvent e) {
        clearForm();
    }

    private void clearForm(){
        medicamentTable.getSelectionModel().clearSelection();

        idField.clear();
        medicamentField.clear();
        formeComboBox.getSelectionModel().clearSelection();
        familleComboBox.getSelectionModel().clearSelection();
        puventeField.clear();
        puachatField.clear();
        dosageField.clear();
        uniteComboBox.getSelectionModel().clearSelection();
        ordonnanceComboBox.getSelectionModel().clearSelection();
        fournisseurComboBox.getSelectionModel().clearSelection();
        qtestockeField.clear();
        stockMaxField.clear();
        stockMinField.clear();

        medicamentFieldError.setText("");
        formeFieldError.setText("");
        familleFieldError.setText("");
        puventeFieldError.setText("");
        puachatFieldError.setText("");
        dosageFieldError.setText("");
        uniteFieldError.setText("");
        qtestockeFieldError.setText("");
        deleteError.setText("");
        stockMaxFieldError.setText("");
        stockMinFieldError.setText("");
        fournisseurFieldError.setText("");
        ordonnanceFieldError.setText("");
    }
}