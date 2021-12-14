package com.qam2.view;

import com.qam2.model.Customer;
import com.qam2.utils.CustomerManager;

import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

/**
 * Provides tabular view of Customer records.
 * @author Alex Hanson
 */
public final class CustomerView extends VBox {

    private final TableView<Customer> table;
    private final CustomerManager manager;

    public CustomerView() {
        table = new TableView<>();
        manager = CustomerManager.getInstance();
        buildView();
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
        table.getColumns().addAll(id,name,address,postalCode,division,country,phone);
        getChildren().add(table);
    }

    public Customer getSelected() { return table.getSelectionModel().getSelectedItem(); }

    public void refreshView() {
        table.setItems(FXCollections.observableList(manager.getAll()));
        table.refresh();
    }

}
