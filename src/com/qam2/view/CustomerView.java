package com.qam2.view;

import com.qam2.model.Customer;
import com.qam2.utils.CustomerManager;

import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

/**
 * Singleton class that provides a tabular view of all Customers in the database.
 * Provides tabular view of all Customers and CRUD operations.
 * @author Alex Hanson
 */
public final class CustomerView extends VBox {

    private static CustomerView instance;

    private final CustomerManager manager;
    private final TableView<Customer> table;

    {
        manager = CustomerManager.getInstance();
        table = new TableView<>();
    }

    private CustomerView() {
        buildView();
        getChildren().add(table);
        refreshView();
    }

    private void buildView() {

        var id = new TableColumn<Customer, Integer>("ID");
        var name = new TableColumn<Customer, String>("Name");
        var address = new TableColumn<Customer, String>("Address");
        var postalCode = new TableColumn<Customer, String>("Postal Code");
        var division = new TableColumn<Customer,String>("Division");
        var country = new TableColumn<Customer, String>("Country");
        var phone = new TableColumn<Customer, String>("Phone");

        id.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        name.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        division.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
        country.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        id.setReorderable(false);
        id.setResizable(false);
        id.setPrefWidth(50);

        name.setReorderable(false);
        name.setResizable(false);
        name.setPrefWidth(250);

        address.setReorderable(false);
        address.setResizable(false);
        address.setPrefWidth(250);

        postalCode.setReorderable(false);
        postalCode.setResizable(false);
        postalCode.setPrefWidth(100);

        division.setReorderable(false);
        division.setResizable(false);
        division.setPrefWidth(150);

        country.setReorderable(false);
        country.setResizable(false);
        country.setPrefWidth(150);

        phone.setReorderable(false);
        phone.setResizable(false);
        phone.setPrefWidth(110);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(id, name, address, postalCode, division, country, phone);
    }

    /**
     * @return Single instance of CustomerView.
     */
    public static CustomerView getInstance() {
        return instance == null ? (instance = new CustomerView()) : instance;
    }

    /**
     * @return Currently selected Customer in the tabular view.
     */
    public Customer getSelected() { return table.getSelectionModel().getSelectedItem(); }

    /**
     * Updates the view when any change is made to a customer.
     * Updates to this view will trigger updates to the appointment since it displays associated customer data that
     * may have changed.
     * Always returns true so that this method call can easily be added into conditional expressions
     * dependent on the boolean result of a database controller method.
     * @return True
     */
    public boolean refreshView() {
        table.setItems(FXCollections.observableList(manager.getCustomers()));
        table.refresh();
        AppointmentView.getInstance().refreshView();
        return true;
    }

    /**
     * @param c Customer to add.
     * @return True if the Customer was added successfully and the view updated, false otherwise.
     */
    public boolean add(Customer c) { return manager.add(c) && refreshView(); }

    /**
     * @param c Customer to update.
     * @return True if the Customer was successfully updated and the view updated, false otherwise.
     */
    public boolean update(Customer c) { return manager.update(c) && refreshView(); }

    /**
     * @return True if the currently selected Customer was deleted successfully and the view updated, false otherwise.
     */
    public boolean delete() { return manager.delete(table.getSelectionModel().getSelectedItem()) && refreshView(); }
}
