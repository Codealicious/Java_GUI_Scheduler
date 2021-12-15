package com.qam2.forms;

import com.qam2.model.Customer;
import com.qam2.utils.LayoutUtil;
import com.qam2.utils.FormUtil;
import com.qam2.utils.CountriesAndDivisions;
import com.qam2.utils.CustomerManager;
import com.qam2.utils.time.TimeUtil;
import com.qam2.utils.UserManager;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.ZonedDateTime;

/**
 * Provides a form for adding or updating a Customer record.
 * @author Alex Hanson.
 */
public class CustomerForm extends VBox {

    // Form IO controls
    private final Stage stage;
    private TextField id;
    private TextField name;
    private TextField address;
    private TextField postalCode;
    private TextField phone;
    private ComboBox<String> countries;
    private ComboBox<String> divisions;

    private Label nameError;
    private Label addressError;
    private Label postalCodeError;
    private Label phoneError;
    private Label countryLabel;
    private Label divisionLabel;

    // Database info required by Customer model for update
    private ZonedDateTime creationDate;
    private String createdBy;

    private final Customer customer;

    /**
     * Creates a form for adding or updating a Customer.
     * @param owner The parent window of this form.
     * @param customer If not null, a Customer to be updated.
     */
    public CustomerForm( Stage owner, Customer customer) {

        super(10);
        setPadding(new Insets(50, 40, 20, 40));

        this.customer = customer;

        if(this.customer != null) {
            creationDate = customer.getCreateDate();
            createdBy = customer.getCreatedBy();
        }

        configureTextFields();
        configureComboBoxes();
        configureButtons();

        getStylesheets().add(CustomerForm.class.getResource("../styles/Form.css").toExternalForm());

        stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Customer Form");
        stage.setScene(new Scene(this, 400, 460));
        stage.showAndWait();
    }

    private void configureTextFields() {

        name = new TextField();
        address = new TextField();
        postalCode = new TextField();
        phone = new TextField();

        nameError = new Label();
        addressError = new Label();
        postalCodeError = new Label();
        phoneError = new Label();

        getChildren().addAll(
                LayoutUtil.configureTextFieldDisplay(name, "Customer Name", nameError),
                LayoutUtil.configureTextFieldDisplay(address, "Address", addressError),
                LayoutUtil.configureTextFieldDisplay(postalCode, "Postal Code", postalCodeError),
                LayoutUtil.configureTextFieldDisplay(phone, "Phone", phoneError));

        postalCode.setMaxWidth(100);
        phone.setMaxWidth(150);

        populateFormFields();
    }

    private void populateFormFields() {

        if(customer != null) {

            id = new TextField(Integer.toString(customer.getCustomerID()));
            id.setDisable(true);
            id.setPrefWidth(50);
            var idLabel = new Label("ID:");
            HBox.setMargin(idLabel, new Insets(5, 0, 0, 0));
            getChildren().add(0, new HBox(5, idLabel, id));

            name.setText(customer.getCustomerName());
            postalCode.setText(customer.getPostalCode());
            phone.setText(customer.getPhone());
            address.setText(customer.getAddress());
        }
    }

    /**
     * Lambdas used to implement EventHandlers for forms ComboBoxes.
     */
    public void configureComboBoxes() {

        countries = new ComboBox<>();
        divisions = new ComboBox<>();
        countryLabel = new Label("Country");
        divisionLabel = new Label("Province/State/Territory");

        countries.setItems(FXCollections.observableList(CountriesAndDivisions.getCountryList()));

        countries.setOnAction(e -> {
            populateDivisions();
            countries.getStyleClass().remove("error");
            countryLabel.getStyleClass().remove("error-label");
        });

        divisions.setOnAction(e -> {
            divisions.getStyleClass().remove("error");
            divisionLabel.getStyleClass().remove("error-label");
        });

        if(customer != null) {
            countries.getSelectionModel().select(customer.getCountryName());
            populateDivisions();
            divisions.getSelectionModel().select(customer.getDivisionName());
        }

        getChildren().add(new HBox(20,
                new VBox(10, countryLabel, countries),
                new VBox(10, divisionLabel, divisions)));
    }

    private void populateDivisions() {

        divisions.setItems(FXCollections.observableList(CountriesAndDivisions.getDivisionsForCountry(countries.getValue())));
    }

    /**
     * Lambdas used to configure EventHandlers for forms add and cancel buttons.
     */
    public void configureButtons() {

        var add = new Button("Add");
        var cancel = new Button("Cancel");

        add.setPrefWidth(70);
        cancel.setPrefWidth(70);

        cancel.requestFocus();

        if(customer == null) {
            add.setOnAction(e -> {
                if (isValid()) {
                    add();
                    stage.close();
                }
            });
        } else {
            add.setText("Update");
            add.setOnAction(e -> {
                if (isValid() && isChanged()) {
                    update();
                    stage.close();
                }
            });
        }

        cancel.setOnAction( e -> stage.close());

        getChildren().add(LayoutUtil.buildHBoxContainer(10, Pos.CENTER_RIGHT, new Insets(40, 0, 40,0), cancel, add));
    }

    private boolean isValid() {

        var valid = true;

        if(!FormUtil.validateTextField(name, nameError)) valid = false;

        if(!FormUtil.validateTextField(address, addressError)) valid = false;

        if(!FormUtil.validateTextField(postalCode, postalCodeError)) valid = false;

        if(!FormUtil.validateTextField(phone, phoneError)) valid = false;

        if(!FormUtil.validateComboBox(countries, countryLabel)) valid = false;

        if(!FormUtil.validateComboBox(divisions, divisionLabel)) valid = false;

        return valid;
    }

    private boolean isChanged() {

        if(customer != null)
            return !(name.getText().trim().equals(customer.getCustomerName()) &&
                    address.getText().trim().equals(customer.getAddress()) &&
                    postalCode.getText().trim().equals(customer.getPostalCode()) &&
                    phone.getText().trim().equals(customer.getPhone()) &&
                    (divisions.getValue() != null && CountriesAndDivisions.getDivisionID(divisions.getValue()) == customer.getDivisionID()));

        return true;
    }

    private void add() {

        if(CustomerManager.getInstance().add(addCustomer())) {
            var success = new Alert(Alert.AlertType.INFORMATION);
            success.setHeaderText("SUCCESS!");
            success.setContentText("Customer Successfully Created.");
            success.showAndWait();
        } else {
            var failure = new Alert(Alert.AlertType.ERROR);
            failure.setHeaderText("Opps Something Went Wrong!");
            failure.setContentText("Customer Could Not Be Created.");
            failure.showAndWait();
        }
    }

    /**
     * Lambda used to implement Consumer functional interface taken as a parameter to the
     * ifPresent() method of the Optional object returned by the showAndWait() method of Alert.
     * The Consumer implementation launches another Alert dialog if the user clicks the OK button.
     * The dialog displayed is based on the outcome of updating the Customer.
     */
    public void update() {

        var confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setHeaderText("UPDATE");
        confirm.setContentText("Update Customer?" + "\t\tID: " + id.getText());
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if(CustomerManager.getInstance().update(updateCustomer())) {
                    var success = new Alert(Alert.AlertType.INFORMATION);
                    success.setHeaderText("SUCCESS!");
                    success.setContentText("Customer Successfully Updated.");
                    success.showAndWait();
                } else {
                    var failure = new Alert(Alert.AlertType.ERROR);
                    failure.setHeaderText("Opps Something Went Wrong!");
                    failure.setContentText("Customer Could Not Be Updated.");
                    failure.showAndWait();
                }
            }
        });
    }

    private Customer addCustomer() {

        return new Customer(
                name.getText().trim(),
                address.getText().trim(),
                postalCode.getText().trim(),
                phone.getText().trim(),
                TimeUtil.getUTCDateTimeNow(),
                UserManager.getCurrentUserName(),
                TimeUtil.getUTCDateTimeNow(),
                UserManager.getCurrentUserName(),
                CountriesAndDivisions.getDivisionID(divisions.getValue())
        );
    }

    private Customer updateCustomer() {

        return new Customer(
                Integer.parseInt(id.getText()),
                name.getText(),
                address.getText(),
                postalCode.getText(),
                phone.getText(),
                creationDate,
                createdBy,
                TimeUtil.getUTCDateTimeNow(),
                UserManager.getCurrentUserName(),
                CountriesAndDivisions.getDivisionID(divisions.getValue())
        );
    }
}
