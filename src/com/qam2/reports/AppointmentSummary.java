package com.qam2.reports;

import com.qam2.model.Appointment;
import com.qam2.utils.AppointmentManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Class representing a summary report of appointments for current year.
 * Provides summary of total for each month and total appointments by type for each month.
 * @author Alex Hanson
 */
public class AppointmentSummary extends VBox {

    /**
     * @param owner The parent window of this report.
     */
    public AppointmentSummary(Stage owner) {

        super(20);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));

        buildReport();

        var stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Appointment Summary");
        stage.setScene(new Scene(this, 325, 375));
        stage.show();
    }

    /**
     * The Appointments for each month in the current year are sorted by type of Appointment. This
     * is achieved by passing a Comparator to ArrayList.sort(). The Comparator is created using the static method
     * comparing which is passed a method reference Appointment::getType which implements the Function interface
     * telling the sort method which Appointment field to sort on.
     */
    public void buildReport() {

        var appointments = AppointmentManager.getInstance().getAppointmentsForCurrentYear();

        Month [] months = Month.values();

        var output = new VBox(10);

        for(int i = 0; i < months.length; i++) {

            Month m = months[i];

            ArrayList<Appointment> apptsForMonth = AppointmentManager.filterAppointmentsByMonth(appointments, m.getValue());
            apptsForMonth.sort(Comparator.comparing(Appointment::getType));

            var month = new HBox(new Label(m.toString()));
            month.setPrefWidth(75);
            month.setAlignment(Pos.CENTER);

            var header = new HBox(new Label("-----------"), month, new Label("-----------"));
            VBox.setMargin(header, new Insets(15, 0,0,0));

            output.getChildren().add(header);
            output.getChildren().add(new Label("\tTotal Appointments: " + apptsForMonth.size()));

            int j = 0, count = 1;

            while(j < apptsForMonth.size()) {

                int k = j;
                String type = apptsForMonth.get(j).getType();

                while(k + 1 < apptsForMonth.size() && apptsForMonth.get(k).getType().compareTo(apptsForMonth.get(k+1).getType()) == 0) {
                    k++;
                    count++;
                }

                output.getChildren().add(new Label("\t\t\t" + type + ": " + count + "\n"));

                count = 1;
                j = k > j ? k + 1 : ++j;
            }
        }

        var container = new ScrollPane(output);
        container.setPadding(new Insets(0,0,20,0));
        container.setMinHeight(225);
        container.setMinWidth(225);
        getChildren().add(container);
    }
}
