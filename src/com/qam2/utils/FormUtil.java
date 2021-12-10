package com.qam2.utils;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Provides functions for validating forms.
 * Static functions for validating add and update forms for both Customers and Appointments.
 * @author Alex Hanson
 */
public abstract class FormUtil {

    /**
     * Determine if a given TextField contains a value.
     * If field is empty apply error styles to both the field and its associated label.
     * If field is not empty remove any error styles for both the field and label.
     * @param tf The TextField to check.
     * @param lbl The Label associated with the TextField.
     * @return True if the field contains a value, false otherwise.
     */
    public static boolean validateTextField(TextField tf, Label lbl) {

        if(tf.getText().isEmpty()) {
            tf.getStyleClass().add("error");
            lbl.setVisible(true);
            return false;
        }else {
            tf.getStyleClass().remove("error");
            lbl.setVisible(false);
            return true;
        }
    }

    /**
     * Determine if a given ComboBox has a selected value.
     * If the box has a selected value remove error styles from both the box and the label.
     * If the box does not have a selected value, add error styles to both the box and the label.
     * @param cb The ComboBox to check.
     * @param lbl The Label associated with the ComboBox.
     * @return True if the box has a selected value, false otherwise.
     */
    public static boolean validateComboBox(ComboBox<String> cb, Label lbl) {

        if(cb.valueProperty().get() == null) {
            cb.getStyleClass().add("error");
            lbl.getStyleClass().add("error-label");
            return false;
        }else {
            cb.getStyleClass().remove("error");
            lbl.getStyleClass().remove("error-label");
            return true;
        }
    }

}
