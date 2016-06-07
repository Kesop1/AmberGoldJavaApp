package sample;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static java.lang.Character.isDigit;

class PasswordChange {

    void change(User user) {
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Password change");
        window.setMinWidth(250);
        window.setResizable(false);

        Label label = new Label("Please change your current password");
        Button closeButton = new Button("Continue");
        Label errorLabel = new Label();

        Label newPassLabel = new Label("New password");
        Label newPassLabel2 = new Label("Confirm new password");
        PasswordField newPassText = new PasswordField();
        PasswordField newPassText2 = new PasswordField();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setHgap(10);
        gridPane.getChildren().addAll(label, newPassLabel, newPassLabel2, newPassText, newPassText2, errorLabel, closeButton);
        GridPane.setColumnSpan(label, 2);
        GridPane.setColumnSpan(errorLabel, 2);
        GridPane.setConstraints(label, 0, 0);
        GridPane.setConstraints(newPassLabel, 0, 1);
        GridPane.setConstraints(newPassText, 1, 1);
        GridPane.setConstraints(newPassLabel2, 0, 2);
        GridPane.setConstraints(newPassText2, 1, 2);
        GridPane.setConstraints(closeButton, 1, 3);
        GridPane.setConstraints(errorLabel, 0, 4);

        closeButton.setOnAction(e -> {
            String message;
            boolean digit = false;
            char[] digitChar = newPassText.getText().toCharArray();
            for (Character character : digitChar) {
                if (isDigit(character))
                    digit = true;
            }
            if ((newPassText.getText().length() <= 6) || (!digit))
                message = "Password must be at least 6 characters long and contain at least one digit";
            else {
                if (newPassText.getText().equals(user.getPassword())) {
                    message = "Password must be different";
                } else {
                    if (newPassText.getText().equals(newPassText2.getText())) {
                        user.setChangePassword(false);
                        user.setPassword(newPassText.getText());
                        message = "Password successfully changed for " + user.getId();
                        window.close();
                    } else {
                        message = "Passwords don't match";
                    }
                }
            }
            errorLabel.setText(message);
            System.out.println(message);

        });


        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(gridPane);
        window.setScene(scene);
        window.showAndWait();
    }


}
