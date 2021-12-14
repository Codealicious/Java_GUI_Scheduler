package com.qam2.forms;

import com.qam2.MainDisplay;
import com.qam2.utils.LayoutUtil;
import com.qam2.utils.time.TimeUtil;
import com.qam2.utils.time.TimeZone;
import com.qam2.utils.UserManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;

/**
 * Provides login form for scheduler application.
 * @author Alex Hanson
 */
public final class LoginForm extends VBox {

    private final Stage stage;
    private final ResourceBundle rb;

    /**
     * Creates the login form.
     */
    public LoginForm() {

        super(20);

        setPadding(new Insets(20,25,0,25));

        rb = ResourceBundle.getBundle("com.qam2.utils.localizationResources.resource");

        getChildren().add(LayoutUtil.getTextElement("[ " + TimeZone.LOCAL.getID() + " ]", Pos.TOP_LEFT, "tagline"));
        getChildren().add(LayoutUtil.getTextElement("Global INC.", Pos.CENTER, "company-name"));

        buildGrid();
        buildTaglines();

        getStylesheets().add(LoginForm.class.getResource("../styles/LoginForm.css").toExternalForm());

        stage = new Stage();
        stage.setScene(new Scene(this, 350, 420));
        stage.show();
    }

    /**
     * Lambda used to implement EventHandler for login button.
     */
    public void buildGrid() {

        var grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,20,25,25));
        VBox.setMargin(grid, new Insets(20,0,5,0));

        var title= LayoutUtil.getTextElement(rb.getString("headline"), Pos.CENTER, "headline");

        var userNameText = new TextField();
        var passwordText = new PasswordField();

        userNameText.setPromptText(rb.getString("username"));
        passwordText.setPromptText(rb.getString("password"));

        var btn = new Button(rb.getString("login"));

        btn.setOnAction(e -> {
            try( var writer = new PrintWriter(new FileWriter("login_activity.txt", true)); ) {

                String outcome = "Login: [ UserName: " + userNameText.getText() + " ] AT " +
                                  TimeUtil.toDateTimeStringWithZone(TimeUtil.getUTCDateTimeNow());

                if (UserManager.authenticate(userNameText.getText(), passwordText.getText())) {
                    writer.println( outcome + " [ Attempt Successful ]");
                    stage.close();
                    new MainDisplay();
                } else {
                    writer.println( outcome + " [ Attempt Unsuccessful ]");
                    passwordText.clear();
                    var error = new Alert(Alert.AlertType.ERROR);
                    error.setHeaderText(rb.getString("error_header"));
                    error.setContentText(rb.getString("error_message"));
                    error.showAndWait();
                }

            }catch(IOException err) {
                System.out.println(err);
            }
        });

        grid.add(title, 0,0, 2,1);
        grid.add(new Label(rb.getString("username")), 0,1);
        grid.add(new Label(rb.getString("password")), 0,2);
        grid.add(userNameText, 1,1);
        grid.add(passwordText, 1,2);
        grid.add(LayoutUtil.buildHBoxContainer(0, Pos.CENTER_RIGHT, new Insets(0), btn),1,3);

        getChildren().add(grid);
    }
    private void buildTaglines() {

        var tagline1 = LayoutUtil.getTextElement(rb.getString("tagline_1"), Pos.CENTER_LEFT, "tagline");
        var tagline2 = LayoutUtil.getTextElement(rb.getString("tagline_2"), Pos.CENTER_RIGHT, "tagline");

        VBox.setMargin(tagline1,new Insets(0,0,0,35));
        VBox.setMargin(tagline2,new Insets(0,35,0,0));

        getChildren().add(tagline1);
        getChildren().add(tagline2);
    }
}
