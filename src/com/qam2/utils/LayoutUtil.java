package com.qam2.utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Provides static functions to build common layout configurations.
 * @author Alex Hanson
 */
public abstract class LayoutUtil {

    /**
     * Creates common form field layout used throughout application.
     * Lambda expression was used to implement ChangeListener in order to add a
     * listener to the TextField representing a form field.
     * @param tf TextField .
     * @param prompt Prompt for TextField.
     * @param lbl Associated Label for TextField.
     * @return VBox node.
     */
    public static VBox configureTextFieldDisplay(TextField tf, String prompt, Label lbl) {
        tf.setPromptText(prompt);
        tf.getStyleClass().add("input-field");
        tf.textProperty().addListener((ob, ovl, nvl) -> {
            if(!nvl.isEmpty()) {
                tf.getStyleClass().remove("error");
                lbl.setVisible(false);
            }
        });

        lbl.setText("please provide " + prompt.toLowerCase());
        lbl.getStyleClass().add("error-label");
        lbl.setVisible(false);

        return new VBox(tf, lbl);
    }

    /**
     * Creates common HBox layout used throughout application.
     * @param spacing Spacing between HBox child nodes.
     * @param alignment Alignment of HBox child nodes.
     * @param insets The HBox's margin.
     * @param nodes Vararg of children to add to HBox.
     * @return HBox node.
     */
    public static HBox buildHBoxContainer(int spacing, Pos alignment, Insets insets, Node... nodes) {
        HBox container = new HBox(spacing);
        container.setAlignment(alignment);
        VBox.setMargin(container, insets);
        container.getChildren().addAll(nodes);
        return container;
    }

    /**
     * Creates common VBox layout used throughout application.
     * @param spacing Spacing between VBox child nodes.
     * @param alignment Alignment of VBox child nodes.
     * @param insets The VBox's margin.
     * @param nodes Vararg of children to add to VBox.
     * @return VBox node.
     */
    public static VBox buildVBoxContainer(int spacing, Pos alignment, Insets insets, Node... nodes) {
        VBox container = new VBox(spacing);
        container.setAlignment(alignment);
        VBox.setMargin(container, insets);
        container.getChildren().addAll(nodes);
        return container;
    }

    /**
     * Creates common text node layout used throughout application.
     * @param text The text to display.
     * @param pos The alignment of the text within HBox container.
     * @param styleClass CSS style class.
     * @return HBox node.
     */
    public static HBox getTextElement(String text, Pos pos, String styleClass) {
        var node = new HBox(new Text(text));
        node.setAlignment(pos);
        node.getStyleClass().add(styleClass);
        return node;
    }
}
