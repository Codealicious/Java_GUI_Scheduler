package com.qam2;

import com.qam2.db.DBConnectionManager;
import com.qam2.forms.LoginForm;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Scheduler application entry point
 */
//  A minor change!
public class Main extends Application {

    /**
     * Launches application login form.
     * @param primaryStage Stage provided to method by Application super class, not used in this implementation.
     */

    @Override
    public void start(Stage primaryStage) { new LoginForm(); }

    /**
     * Stops the application and closes Database resource.
     */

    @Override
    public void stop() {
        try {
            super.stop();
        }catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            DBConnectionManager.getInstance().close();
        }
    }

    /**
     * Starts the application.
     * @param args Optional command line arguments, not used in this implementation.
     */
    public static void main(String[] args) { launch(args); }
}

