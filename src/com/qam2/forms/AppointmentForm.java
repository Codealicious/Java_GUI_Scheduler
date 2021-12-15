package com.qam2.forms;

import com.qam2.model.Appointment;
import com.qam2.utils.CustomerManager;
import com.qam2.utils.LayoutUtil;
import com.qam2.utils.AppointmentManager;
import com.qam2.utils.ContactManager;
import com.qam2.utils.FormUtil;
import com.qam2.utils.time.TimeUtil;
import com.qam2.utils.time.TimeZone;
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

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * Provides a form for adding or updating an Appointment record.
 * @author Alex Hanson.
 */
public class AppointmentForm extends VBox {

    private final Stage stage;
    private TextField id;
    private TextField title;
    private TextField description;
    private TextField location;
    private TextField type;
    private ComboBox<String> start;
    private ComboBox<String> end;
    private DatePicker date;
    private ComboBox<String> customers;
    private ComboBox<String> contacts;
    private ComboBox<String> users;
    private Label startLabel;
    private Label endLabel;
    private Label customerLabel;
    private Label contactLabel;
    private Label userLabel;
    private Label titleError;
    private Label descriptionError;
    private Label locationError;
    private Label typeError;

    private ZonedDateTime creationDate;
    private String createdBy;

    private final Appointment appointment;

    /**
     * Creates a form for adding or updating an Appointment.
     * @param owner The parent window of this form.
     * @param appointment If not null, an Appointment to be updated.
     */
    public AppointmentForm(Stage owner, Appointment appointment) {

        super(10);
        setPadding(new Insets(50,40,20,40));

        this.appointment = appointment;

        if (appointment != null) {
            creationDate = appointment.getCreateDate();
            createdBy = appointment.getCreatedBy();
        }

        configureTextFields();
        configureComboBoxes();
        configureButtons();

        getStylesheets().add(AppointmentForm.class.getResource("../styles/Form.css").toExternalForm());

        stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Appointment Form");
        stage.setScene(new Scene(this, 400,650));
    }

    private void configureTextFields() {

        title = new TextField();
        description = new TextField();
        location = new TextField();
        type = new TextField();

        titleError = new Label();
        descriptionError = new Label();
        locationError = new Label();
        typeError =  new Label();

        getChildren().addAll(
                LayoutUtil.configureTextFieldDisplay(title, "Title", titleError),
                LayoutUtil.configureTextFieldDisplay(description, "Description", descriptionError),
                LayoutUtil.configureTextFieldDisplay(location, "Location", locationError),
                LayoutUtil.configureTextFieldDisplay(type, "type", typeError));

        populateFormFields();
    }

    private void populateFormFields() {

        if(appointment != null) {

            id = new TextField(Integer.toString(appointment.getAppointmentID()));
            id.setDisable(true);
            id.setPrefWidth(50);
            var idLabel = new Label("ID:");
            HBox.setMargin(idLabel, new Insets(5,0,0,0));
            getChildren().add(0, new HBox(5, idLabel, id));

            title.setText(appointment.getTitle());
            description.setText(appointment.getDescription());
            location.setText(appointment.getLocation());
            type.setText(appointment.getType());
        }
    }

    /**
     * Lambdas used to implement EventHandler(s) for the forms ComboBoxes and DatePicker.
     */
    private void configureComboBoxes() {

        customers = new ComboBox<>();
        contacts = new ComboBox<>();
        customerLabel = new Label("Customer");
        contactLabel = new Label("Contact");

        users = new ComboBox<>();
        userLabel = new Label("User");

        date = new DatePicker(LocalDate.now());

        start = new ComboBox<>();
        end = new ComboBox<>();
        startLabel = new Label("Start Time");
        endLabel = new Label("End Time");

        customers.setOnAction(e -> validateComboBox(customers, customerLabel));

        users.setOnAction(e -> validateComboBox(users, userLabel));

        date.setOnAction(e -> getAvailableTimes());

        contacts.setOnAction(e -> validateComboBox(contacts, contactLabel));

        start.setOnAction(e -> getAvailableEndTimes());

        end.setOnAction(e -> clearComboBoxError(end, endLabel));

        contacts.setItems(FXCollections.observableList(ContactManager.getContactList()));
        customers.setItems(FXCollections.observableList(CustomerManager.getInstance().getCustomerList()));
        users.setItems(FXCollections.observableList(UserManager.getUserNameList()));

        getChildren().addAll(
                new HBox(20, new VBox(10, customerLabel, customers), new VBox(10, userLabel, users)),
                new VBox(10, contactLabel, contacts),
                date,
                new HBox(30, new VBox(10, startLabel, start), new VBox(10, endLabel, end))
        );

        populateComboBoxes();
    }

    private void populateComboBoxes() {

        if(appointment != null) {

            customers.getSelectionModel().select(appointment.getCustomerName());
            users.getSelectionModel().select(appointment.getUserName());
            contacts.getSelectionModel().select(appointment.getContactName());
            date.setValue(appointment.getStart().toLocalDate());

            getAvailableTimes();

            start.getSelectionModel().select(appointment.getLocalStartString());

            getAvailableEndTimes();

            end.getSelectionModel().select(appointment.getLocalEndString());
        }
    }

    private void validateComboBox(ComboBox<String> cb, Label lbl) {
        getAvailableTimes();
        clearComboBoxError(cb, lbl);
    }

    private void clearComboBoxError(ComboBox<String> cb, Label lbl) {
        cb.getStyleClass().remove("error");
        lbl.getStyleClass().remove("error-label");
    }

    private void getAvailableTimes() {

        if(date.getValue() != null && users.getValue() != null && customers.getValue() != null && contacts.getValue() != null) {
            start.setItems(FXCollections.observableList(AppointmentManager.getAvailableStartTimes(customers.getValue(), contacts.getValue(), users.getValue(), date.getValue(), appointment)));
        }else {
            start.setItems(null);
        }
    }

    private void getAvailableEndTimes() {

        if(start.getValue() != null) {
            end.setItems(FXCollections.observableList(AppointmentManager.getAvailableEndTimes(new ArrayList<>(start.getItems()), start.getValue())));
            clearComboBoxError(start, startLabel);
        }else {
            end.setItems(null);
        }
    }

    /**
     * Lambdas used to implement EventHandler(s) for the form's add/update and cancel buttons.
     */
    public void configureButtons() {

        var add = new Button("Add");
        var cancel = new Button("Cancel");

        add.setPrefWidth(70);
        cancel.setPrefWidth(70);

        cancel.requestFocus();

        if(appointment == null) {
            add.setOnAction( e-> {
                if(isValid()) {
                    add();
                    stage.close();
                }
            });
        }else {
            add.setText("Update");
            add.setOnAction(e -> {
                if(isValid() && isChanged())
                    update();
            });
        }

        cancel.setOnAction( e -> stage.close());

        getChildren().add(LayoutUtil.buildHBoxContainer(10, Pos.CENTER_RIGHT, new Insets(40, 0, 40,0), cancel, add));
    }

    private boolean isValid() {

        // DatePicker always has a date.

        var valid = true;

        if(!FormUtil.validateTextField(title, titleError)) valid = false;

        if(!FormUtil.validateTextField(description, descriptionError)) valid = false;

        if(!FormUtil.validateTextField(location, locationError)) valid = false;

        if(!FormUtil.validateTextField(type, typeError)) valid = false;

        if(!FormUtil.validateComboBox(customers, customerLabel)) valid = false;

        if(!FormUtil.validateComboBox(contacts, contactLabel)) valid = false;

        if(!FormUtil.validateComboBox(users, userLabel)) valid = false;

        if(!FormUtil.validateComboBox(start, startLabel)) valid = false;

        if(!FormUtil.validateComboBox(end, endLabel)) valid = false;

        return valid;
    }

    private boolean isChanged() {

        if(appointment != null)
            return !(title.getText().trim().equals(appointment.getTitle()) &&
                    description.getText().trim().equals(appointment.getDescription()) &&
                    location.getText().trim().equals(appointment.getLocation()) &&
                    type.getText().trim().equals(appointment.getType()) &&
                    customers.getValue().equals(appointment.getCustomerName()) &&
                    contacts.getValue().equals(appointment.getContactName()) &&
                    users.getValue().equals(appointment.getUserName()) &&
                    start.getValue().equals(appointment.getLocalStartString()) &&
                    end.getValue().equals(appointment.getLocalEndString()) &&
                    date.getValue().compareTo(appointment.getStart().toLocalDate()) == 0);

        return true;
    }

    private void add() {

        if(AppointmentManager.getInstance().add(addAppointment())) {
            var success = new Alert(Alert.AlertType.INFORMATION);
            success.setHeaderText("SUCCESS!");
            success.setContentText("Appointment successfully created.");
            success.showAndWait();
        }else {
            var failure = new Alert(Alert.AlertType.ERROR);
            failure.setHeaderText("Opps Something Went Wrong!");
            failure.setContentText("Appointment Could Not Be Created.");
            failure.showAndWait();
        }
    }

    /**
     * Lambda used to implement Consumer functional interface taken as a parameter to the
     * ifPresent() method of the Optional object returned by the showAndWait() method of Alert.
     * The Consumer implementation launches another Alert dialog if the user clicks the OK button.
     * The dialog displayed is based on the outcome of updating the Appointment.
     */
    public void update() {

        var confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setHeaderText("UPDATE");
        confirm.setContentText("Update Appointment?" + "\t\tID: " + id.getText());
        confirm.showAndWait().ifPresent(response -> {
            if(response == ButtonType.OK) {
                if(AppointmentManager.getInstance().update(updateAppointment())) {
                    var success = new Alert(Alert.AlertType.INFORMATION);
                    success.setHeaderText("SUCCESS!");
                    success.setContentText("Appointment Successfully Updated.");
                    success.showAndWait();
                    stage.close();
                }else {
                    var failure = new Alert(Alert.AlertType.ERROR);
                    failure.setHeaderText("Opps Something Went Wrong!");
                    failure.setContentText("Appointment Could Not Be updated.");
                    failure.showAndWait();
                }
            }
        });
    }

    private Appointment addAppointment() {
        return new Appointment(
                title.getText().trim(),
                description.getText().trim(),
                location.getText().trim(),
                type.getText().trim(),
                TimeUtil.convertTime(ZonedDateTime.of(date.getValue(), TimeUtil.parseLocalTime(start.getValue()), TimeZone.LOCAL.getID()), TimeZone.UTC),
                TimeUtil.convertTime(ZonedDateTime.of(date.getValue(), TimeUtil.parseLocalTime(end.getValue()), TimeZone.LOCAL.getID()), TimeZone.UTC),
                TimeUtil.getUTCDateTimeNow(),
                UserManager.getCurrentUserName(),
                TimeUtil.getUTCDateTimeNow(),
                UserManager.getCurrentUserName(),
                CustomerManager.getInstance().getCustomerID(customers.getValue()),
                UserManager.getCurrentUserID(),
                ContactManager.getContactID(contacts.getValue())
        );
    }

    private Appointment updateAppointment() {

        return new Appointment(
                Integer.parseInt(id.getText()),
                title.getText().trim(),
                description.getText().trim(),
                location.getText().trim(),
                type.getText().trim(),
                TimeUtil.convertTime(ZonedDateTime.of(date.getValue(), TimeUtil.parseLocalTime(start.getValue()), TimeZone.LOCAL.getID()), TimeZone.UTC),
                TimeUtil.convertTime(ZonedDateTime.of(date.getValue(), TimeUtil.parseLocalTime(end.getValue()), TimeZone.LOCAL.getID()), TimeZone.UTC),
                creationDate,
                createdBy,
                TimeUtil.getUTCDateTimeNow(),
                UserManager.getCurrentUserName(),
                CustomerManager.getInstance().getCustomerID(customers.getValue()),
                UserManager.getCurrentUserID(),
                ContactManager.getContactID(contacts.getValue())
        );
    }
}
