package com.qam2.reports;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Class representing a selection form for various reports.
 * @author Alex Hanson
 */
public class ReportSelector extends VBox {

    private final Stage stage;

    /**
     * @param owner The parent window of this form.
     */
    public ReportSelector(Stage owner) {

        super(20);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));
        buildOptions();

        stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Reports");
        stage.setScene(new Scene(this, 300, 350));
        stage.show();
    }

    private void buildOptions() {

        var empsch = new Button("Employee Schedule");
        var custsch = new Button("Customer Schedule");
        var apptsum = new Button("Appointment Summary");

        empsch.setPrefWidth(150);
        custsch.setPrefWidth(150);
        apptsum.setPrefWidth(150);

        empsch.setOnAction(e -> {
            new EmployeeSchedule(stage);
        });

        custsch.setOnAction(e -> {
            new CustomerSchedule(stage);
        });

        apptsum.setOnAction(e -> {
            new AppointmentSummary(stage);
        });

        getChildren().addAll(empsch, custsch, apptsum);
    }
}
