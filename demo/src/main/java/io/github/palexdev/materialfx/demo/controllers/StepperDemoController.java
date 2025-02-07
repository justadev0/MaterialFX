/*
 * Copyright (C) 2021 Parisi Alessandro
 * This file is part of MaterialFX (https://github.com/palexdev/MaterialFX).
 *
 * MaterialFX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MaterialFX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with MaterialFX.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.palexdev.materialfx.demo.controllers;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.demo.MFXDemoResourcesLoader;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import io.github.palexdev.materialfx.utils.BindingUtils;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StepperDemoController implements Initializable {
    private final MFXTextField loginField;
    private final MFXPasswordField passwordField;
    private final MFXTextField firstNameField;
    private final MFXTextField lastNameField;
    private final MFXComboBox<String> genderCombo;
    private final MFXCheckbox checkbox;

    @FXML
    private MFXStepper stepper;

    @FXML
    private MFXButton unlock;

    public StepperDemoController() {
        loginField = new MFXTextField();
        passwordField = new MFXPasswordField();
        firstNameField = new MFXTextField();
        lastNameField = new MFXTextField();
        genderCombo = new MFXComboBox<>();
        checkbox = new MFXCheckbox("Confirm Data?");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginField.setPromptText("Username...");
        loginField.getValidator().add(BindingUtils.toProperty(loginField.textProperty().length().greaterThanOrEqualTo(6)), "The username must be at least 6 characters long");
        loginField.setValidated(true);
        loginField.setIcon(new MFXIconWrapper(new MFXFontIcon("mfx-user", 16, Color.web("#4D4D4D")), 24));
        passwordField.setPromptText("Password...");
        passwordField.getValidator().add(BindingUtils.toProperty(passwordField.passwordProperty().length().greaterThanOrEqualTo(8)), "The password must be at least 8 characters long");
        passwordField.setValidated(true);

        firstNameField.setPromptText("First Name...");
        lastNameField.setPromptText("Last Name...");
        genderCombo.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));

        checkbox.setMarkType("mfx-variant7-mark");

        List<MFXStepperToggle> stepperToggles = createSteps();
        stepper.getStepperToggles().addAll(stepperToggles);

        unlock.visibleProperty().bind(stepper.mouseTransparentProperty());
        unlock.setOnAction(event -> stepper.setMouseTransparent(false));
    }

    private List<MFXStepperToggle> createSteps() {
        FontIcon key = new FontIcon("fas-key");
        key.setIconSize(16);
        key.setIconColor(Color.web("#f1c40f"));
        MFXStepperToggle step1 = new MFXStepperToggle("Step 1", key);
        VBox step1Box = new VBox(40, loginField, passwordField);
        step1Box.setAlignment(Pos.CENTER);
        step1.setContent(step1Box);
        step1.getValidator().addDependencies(loginField.getValidator(), passwordField.getValidator());

        MFXStepperToggle step2 = new MFXStepperToggle("Step 2", new MFXFontIcon("mfx-user", 16, Color.web("#49a6d7")));
        VBox step2Box = new VBox(40, firstNameField, lastNameField, genderCombo);
        step2Box.setAlignment(Pos.CENTER);
        step2.setContent(step2Box);

        MFXStepperToggle step3 = new MFXStepperToggle("Step 3", new MFXFontIcon("mfx-variant7-mark", 16, Color.web("#85CB33")));
        Node step3Grid = createGrid();
        step3.setContent(step3Grid);
        step3.getValidator().add(checkbox.selectedProperty(), "Data must be confirmed");

        return List.of(step1, step2, step3);
    }

    private Node createGrid() {
        MFXLabel usernameLabel1 = createLabel("Username: ");
        MFXLabel usernameLabel2 = createLabel("");
        usernameLabel2.textProperty().bind(loginField.textProperty());

        MFXLabel firstNameLabel1 = createLabel("First Name: ");
        MFXLabel firstNameLabel2 = createLabel("");
        firstNameLabel2.textProperty().bind(firstNameField.textProperty());

        MFXLabel lastNameLabel1 = createLabel("Last Name: ");
        MFXLabel lastNameLabel2 = createLabel("");
        lastNameLabel2.textProperty().bind(lastNameField.textProperty());

        MFXLabel genderLabel1 = createLabel("Gender: ");
        MFXLabel genderLabel2 = createLabel("");
        genderLabel2.textProperty().bind(Bindings.createStringBinding(
                () -> genderCombo.getSelectedValue() != null ? genderCombo.getSelectedValue() : "",
                genderCombo.selectedValueProperty()
        ));

        MFXLabel completedLabel = new MFXLabel("Completed!!");
        completedLabel.setFont(Font.font("Open Sans Bold", 24));
        completedLabel.setTextFill(Color.web("#85CB33"));

        HBox b1 = new HBox(usernameLabel1, usernameLabel2);
        HBox b2 = new HBox(firstNameLabel1, firstNameLabel2);
        HBox b3 = new HBox(lastNameLabel1, lastNameLabel2);
        HBox b4 = new HBox(genderLabel1, genderLabel2);

        b1.setMaxWidth(Region.USE_PREF_SIZE);
        b2.setMaxWidth(Region.USE_PREF_SIZE);
        b3.setMaxWidth(Region.USE_PREF_SIZE);
        b4.setMaxWidth(Region.USE_PREF_SIZE);

        VBox box = new VBox(10, b1, b2, b3, b4, checkbox);
        box.getStylesheets().setAll(MFXDemoResourcesLoader.load("css/StepperDemo.css"));
        box.setAlignment(Pos.CENTER);
        StackPane.setAlignment(box, Pos.CENTER);

        stepper.setOnLastNext(event -> {
            box.getChildren().setAll(completedLabel);
            stepper.setMouseTransparent(true);
        });
        stepper.setOnBeforePrevious(event -> {
            if (stepper.isLastToggle()) {
                checkbox.setSelected(false);
                box.getChildren().setAll(b1, b2, b3, b4, checkbox);
            }
        });

        return box;
    }

    private MFXLabel createLabel(String text) {
        MFXLabel label = new MFXLabel(text);
        label.setAlignment(Pos.CENTER_LEFT);
        label.setPrefWidth(200);
        label.setMinWidth(Region.USE_PREF_SIZE);
        label.setMaxWidth(Region.USE_PREF_SIZE);
        return label;
    }
}
