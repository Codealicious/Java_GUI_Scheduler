package com.qam2.reports;

import com.qam2.model.Appointment;
import com.qam2.utils.AppointmentManager;
import com.qam2.utils.ContactManager;
import com.qam2.utils.LayoutUtil;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Class representing a schedule for a selected Contact/Company Employee.
 * @author Alex Hanson
 */
public class EmployeeSchedule extends VBox {

    private ComboBox<String> empList;
    private ScrollPane container;

    /**
     * @param owner The parent window of this report.
     */
    public EmployeeSchedule(Stage owner) {

        super(10);
        setPadding(new Insets(20));

        buildReport();

        var stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Employee Schedule");
        stage.setScene(new Scene(this, 500, 500));
        stage.show();
    }

    private void buildReport() {

        empList = new ComboBox<>(FXCollections.observableList(ContactManager.getContactList()));
        getChildren().add(LayoutUtil.buildVBoxContainer(5, Pos.BASELINE_LEFT, new Insets(0,15,0,0), new Label("Select Employee"), empList));

        container = new ScrollPane();
        VBox.setMargin(container, new Insets(15,0,0,0));
        container.setMinHeight(425);
        container.setMinWidth(425);
        getChildren().add(container);

        empList.setOnAction(e-> getSchedule());
    }

    private void getSchedule() {

        int id = ContactManager.getContactID(empList.getValue());
        var appts = AppointmentManager.getInstance().getAppointmentsForCurrentYear();

        appts = appts.stream()
                .filter(a -> a.getContactID() == id)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        appts.sort(Comparator.comparing(Appointment::getStart));

        var appointments = new VBox(10);

        for(Appointment a : appts) {
            var info = new Label("\tID: " + a.getAppointmentID() + "\tTitle: " + a.getTitle() + "\tDescription: " + a.getDescription() + "\tType: " + a.getType() + "\tCustomer ID: " + a.getCustomerID());
            var times = new Label("Start: " + a.getStartDisplay() + "\t\tEnd: " + a.getEndDisplay());
            appointments.getChildren().add(LayoutUtil.buildVBoxContainer(5, Pos.BASELINE_LEFT, new Insets(5), times, info));
        }

       container.setContent(appointments);
    }
}
