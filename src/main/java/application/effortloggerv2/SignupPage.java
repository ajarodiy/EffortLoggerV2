package application.effortloggerv2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// Author: Mishika Chhabra
public class SignupPage extends Application {
    private Stage primaryStage;
    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        stage.setTitle("EffortLogger Signup Page");

        // Create a grid for the signup form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(40, 40, 40, 40));

        // Username, password, and confirm password fields
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Label confirmPasswordLabel = new Label("Confirm Password:");
        PasswordField confirmPasswordField = new PasswordField();

        // Signup button
        Button signupButton = new Button("Sign Up");

        // Log in text
        Text loginText = new Text("Already have an account? Log in!");
        loginText.setStyle("-fx-fill: blue;");
        loginText.setOnMouseEntered(event -> loginText.setUnderline(true));
        loginText.setOnMouseExited(event -> loginText.setUnderline(false));
        loginText.setOnMouseClicked(event -> Utility.openPage("loginpage", stage));

        // Handle signup button action
        signupButton.setOnAction(event -> {
            String username = InputValidation.sanitize(usernameField.getText());
            String password = InputValidation.sanitize(passwordField.getText());
            String confirmPassword = InputValidation.sanitize(confirmPasswordField.getText());

            if (!password.equals(confirmPassword)) {
                Utility.showAlert("Password Mismatch", "The entered passwords do not match", Alert.AlertType.ERROR);
            }
            else if (username.isEmpty() || password.isEmpty()) {
                Utility.showAlert("Empty Fields", "Username and password cannot be empty", Alert.AlertType.ERROR);
            }
            else {
                DatabaseConnector.addUser(username, password);
                Utility.showAlert("Successfully Registered", "You have been successfully registered", Alert.AlertType.INFORMATION);
                Utility.username = username;
                Utility.role = "developer";
                Utility.openPage("effortloggingpage", primaryStage);
            }
        });

        // Add elements to the grid
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(confirmPasswordLabel, 0, 2);
        grid.add(confirmPasswordField, 1, 2);
        grid.add(signupButton, 0, 3, 2, 1);
        grid.add(loginText, 0, 4, 2, 1);

        // Create a container for the grid
        VBox container = new VBox(20);
        container.getChildren().addAll(grid);

        Scene scene = new Scene(container, 400, 300);

        stage.setScene(scene);
        stage.show();
    }
}
