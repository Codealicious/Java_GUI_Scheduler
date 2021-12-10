package com.qam2.view;

import com.qam2.model.Appointment;
import com.qam2.model.Customer;
import com.qam2.utils.AppointmentManager;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Singleton class the creates a tabular view of all Appointments in the database.
 * Provides tabular view of Appointments and CRUD operations.
 * @author Alex Hanson
 */
public final class AppointmentView extends VBox {

    private static AppointmentView instance;

    private final AppointmentManager manager;
    private final TableView<Appointment> table;
    private final ToggleGroup group;

    {
        manager = AppointmentManager.getInstance();
        table = new TableView<>();
        group = new ToggleGroup();
    }

    private AppointmentView() {
        buildFilters();
        buildView();
    }

    private void buildFilters() {

        var week = new RadioButton("Week");
        var month = new RadioButton("Month");
        var all = new RadioButton("All");

        week.setToggleGroup(group);
        month.setToggleGroup(group);
        all.setToggleGroup(group);

        group.selectedToggleProperty().addListener((ob, ovl, nvl) -> {
            updateView();
        });

        group.selectToggle(all);

        var filters = new HBox(5, all, week, month);
        VBox.setMargin(filters, new Insets(5));
        getChildren().add(filters);
    }

    private void filterAppointments(String filter) {

        switch(filter) {
            case "Week":
                table.setItems(FXCollections.observableList(manager.getAppointmentsForCurrentWeek()));
                break;
            case "Month":
                table.setItems(FXCollections.observableList(manager.getAppointmentsForCurrentMonth()));
                break;
            case "All":
                table.setItems(FXCollections.observableList(manager.getAppointments()));
                break;
        }
    }

    private boolean updateView() {
        filterAppointments(((RadioButton) group.getSelectedToggle()).getText());
        return true;
    }

    private void buildView() {

        var id = new TableColumn<Appointment, Integer>("ID");
        var title = new TableColumn<Appointment, Integer>("Title");
        var description = new TableColumn<Appointment, Integer>("Description");
        var location = new TableColumn<Appointment, Integer>("Location");
        var type = new TableColumn<Appointment, Integer>("Type");
        var start = new TableColumn<Appointment, Integer>("Start Time");
        var end = new TableColumn<Appointment, Integer>("End Time");
        var customer = new TableColumn<Appointment, Integer>("Customer");
        var contact = new TableColumn<Appointment, Integer>("Contact");

        id.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        location.setCellValueFactory(new PropertyValueFactory<>("location"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        start.setCellValueFactory(new PropertyValueFactory<>("startDisplay"));
        end.setCellValueFactory(new PropertyValueFactory<>("endDisplay"));
        customer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        contact.setCellValueFactory(new PropertyValueFactory<>("contactName"));

        id.setResizable(false);
        id.setReorderable(false);
        id.setPrefWidth(50);

        title.setResizable(false);
        title.setReorderable(false);
        title.setPrefWidth(100);

        description.setResizable(false);
        description.setReorderable(false);
        description.setPrefWidth(120);

        location.setResizable(false);
        location.setReorderable(false);
        location.setPrefWidth(100);

        type.setResizable(false);
        type.setReorderable(false);
        type.setPrefWidth(175);

        start.setResizable(false);
        start.setReorderable(false);
        start.setPrefWidth(125);

        end.setResizable(false);
        end.setReorderable(false);
        end.setPrefWidth(125);

        customer.setResizable(false);
        customer.setReorderable(false);
        customer.setPrefWidth(140);

        contact.setResizable(false);
        contact.setReorderable(false);
        contact.setPrefWidth(130);

        table.getColumns().addAll(id, title, description, location, type, start, end, customer, contact);
        getChildren().add(table);
    }

    /**
     * @return Single instance of AppointmentView.
     */
    public static AppointmentView getInstance() { return instance == null ? (instance = new AppointmentView()) : instance; }

    /**
     * @return The current Appointment selected in the tabular view.
     */
    public Appointment getSelected() { return table.getSelectionModel().getSelectedItem(); }

    /**
     * @param appt The Appointment add.
     * @return True if the Appointment was successfully added and the view updated, false otherwise.
     */
    public boolean add(Appointment appt) { return manager.add(appt) && updateView(); }

    /**
     * @param appt The Appointment to update.
     * @return True if the Appointment was successfully updated and the view updated, false otherwise.
     */
    public boolean update(Appointment appt) { return manager.update(appt) && updateView(); }

    /**
     * @return True if the currently selected Appointment in the tabular view was deleted successfully and the view updated, false otherwise.
     */
    public boolean delete() { return manager.delete(table.getSelectionModel().getSelectedItem()) && updateView(); }

    /**
     * @param c Customer for whom all Appointments are to be deleted.
     * @return True if all Appointments for given Customer were successfully deleted and the view updated, false otherwise.
     */
    public boolean deleteAllForCustomer(Customer c) { return manager.deleteAllForCustomer(c) && updateView(); }

}
