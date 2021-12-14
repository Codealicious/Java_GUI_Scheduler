package com.qam2;

import com.qam2.forms.AppointmentForm;
import com.qam2.forms.CustomerForm;
import com.qam2.forms.LoginForm;
import com.qam2.model.Appointment;
import com.qam2.reports.ReportSelector;
import com.qam2.utils.CustomerManager;
import com.qam2.utils.LayoutUtil;
import com.qam2.utils.AppointmentManager;
import com.qam2.utils.UserManager;
import com.qam2.view.AppointmentView;
import com.qam2.view.CustomerView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main dashboard display for schedule program.
 * Provides tabular views for customer and appointment data.
 * Provides buttons to: generate reports; add, delete, or update customer and appointment records; logout.
 * @author Alex Hanson
 * @version 2.0
 */
public final class MainDisplay extends VBox {

    private Stage stage;
    private Tab customerTab ;
    private CustomerView cv;
    private AppointmentView av;

    /**
     * Creates a new MainDisplay that acts as the main GUI component of the scheduler application
     */
    public MainDisplay() {

        setPadding(new Insets(50));
        cv = new CustomerView();
        av = new AppointmentView();

        buildLogoutBtn();
        buildReportsBtn();
        buildRecordsTabView();
        buildCrudButtons();

        stage = new Stage();
        stage.setTitle("Schedule Dashboard");
        stage.setScene(new Scene(this, 825,550));
        stage.show();

        upcomingAppointment();
    }

    private void buildReportsBtn() {

        var records = new Button("Reports");

        records.setOnAction(e -> {
            new ReportSelector(stage);
        });

        getChildren().add(LayoutUtil.buildHBoxContainer(0, Pos.CENTER_LEFT, new Insets(10,5,25,0), records));

    }

    private void buildLogoutBtn() {

        var logout = new Button("Logout");

        logout.setOnAction(e -> {
            UserManager.logout();
            stage.close();
            new LoginForm();
        });

        getChildren().add(LayoutUtil.buildHBoxContainer(0, Pos.CENTER_RIGHT, new Insets(10, 0, 25 ,5), logout));
    }

    private void buildRecordsTabView() {

        var mainView = new TabPane();
        var appointmentsTab = new Tab("Appointments", av);
        customerTab = new Tab("Customers", cv);

        customerTab.setClosable(false);
        appointmentsTab.setClosable(false);

        mainView.getTabs().addAll(customerTab, appointmentsTab);
        mainView.setTabDragPolicy(TabPane.TabDragPolicy.FIXED);

        getChildren().add(mainView);
    }

    private void buildCrudButtons() {

        var add = new Button("Add");
        var update = new Button("Update");
        var delete = new Button("Delete");

        add.setPrefWidth(70);
        update.setPrefWidth(70);
        delete.setPrefWidth(70);

        add.requestFocus();

        add.setOnAction(e -> {
            if(customerTab.isSelected()) {
                new CustomerForm(stage, null).display();
                cv.refreshView();
            }else {
                new AppointmentForm(stage, null).display();
                av.refreshView();
            }
        });

        update.setOnAction(e -> {
            if(customerTab.isSelected() && cv.getSelected() != null) {
                new CustomerForm(stage, cv.getSelected()).display();
                cv.refreshView();
            }else if(av.getSelected() != null) {
                new AppointmentForm(stage, av.getSelected()).display();
            }

            av.refreshView();
        });

        delete.setOnAction(e -> {
           if(customerTab.isSelected() ) {
               deleteCustomer();
               cv.refreshView();

           }else {
               deleteAppointment();
           }

            av.refreshView();
        });

        getChildren().add(LayoutUtil.buildHBoxContainer(10, Pos.TOP_RIGHT, new Insets(15, 0, 0, 0), add, update, delete));
    }

    private void deleteCustomer() {

        var cust = cv.getSelected();

        if(cust != null) {

            var confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setHeaderText("DELETE");
            confirm.setContentText("Delete Customer?" + "\t\tID: " + cust.getCustomerID() + "\t\tName: " + cust.getCustomerName());
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    if (CustomerManager.getInstance().delete(cust)) {
                        var success = new Alert(Alert.AlertType.INFORMATION);
                        success.setHeaderText("SUCCESS!");
                        success.setContentText("Customer Successfully Deleted.\nName: " + cust.getCustomerName());
                        success.showAndWait();
                    } else {
                        var failure = new Alert(Alert.AlertType.ERROR);
                        failure.setHeaderText("Opps Something Went Wrong!");
                        failure.setContentText("Customer Could Not Be Deleted.");
                        failure.showAndWait();
                    }
                }
            });
        }
    }

    private void deleteAppointment() {

        var appt = av.getSelected();

        if(appt != null) {

            var confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setHeaderText("DELETE");
            confirm.setContentText("Delete Appointment?" + "\t\tID: " + appt.getAppointmentID());
            confirm.showAndWait().ifPresent(response -> {
                if(response == ButtonType.OK) {
                    if (AppointmentManager.getInstance().delete(appt)) {
                        var success = new Alert(Alert.AlertType.INFORMATION);
                        success.setHeaderText("SUCCESS!");
                        success.setContentText("Appointment Cancelled:\nID: " + appt.getAppointmentID() + "\nType: " + appt.getType());
                        success.showAndWait();
                    } else {
                        var failure = new Alert(Alert.AlertType.ERROR);
                        failure.setHeaderText("Opps Something Went Wrong!");
                        failure.setContentText("Appointment Could Not Be Deleted.");
                        failure.showAndWait();
                    }
                }
            });
        }
    }

    private void upcomingAppointment() {

        Appointment a = AppointmentManager.getInstance().upcomingAppointment();
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Welcome " + UserManager.getCurrentUserName());

        if(a != null) {
            alert.setContentText("Upcoming Appointment:\n" +
                                    "Appointment ID: " + a.getAppointmentID() +
                                    "\n" + a.getStartDisplay() +
                                    "\n" + a.getTitle() +
                                    "\nCustomer: " + CustomerManager.getInstance().getCustomerName(a.getCustomerID()));

        }else {
            alert.setContentText("No Upcoming Appointments");
        }

        alert.showAndWait();
    }
}
