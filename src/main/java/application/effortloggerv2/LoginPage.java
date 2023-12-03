// Authors: Aditya Jarodiya, Mishika Chhabra, Ishan Kavdia, Mihir Kataria, Akshit Jain, Rishikumar Senthilvel

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
public class LoginPage extends Application {
	private Stage primaryStage;

	@Override
	public void start(Stage stage) {
		try {
			this.primaryStage = stage;

			primaryStage.setTitle("EffortLogger Login Page");
	        // Create a grid for the login form
	        GridPane grid = new GridPane();
	        grid.setHgap(10);
	        grid.setVgap(10);
	        grid.setPadding(new Insets(40, 40, 40, 40));

	        // Username and password fields
	        Label usernameLabel = new Label("Username:");
	        TextField usernameField = new TextField();
	        Label passwordLabel = new Label("Password:");
	        PasswordField passwordField = new PasswordField();

	        // Login button
	        Button loginButton = new Button("Login");

			// Sign up text
			Text signupText = new Text("Don't have an account? Sign up!");
			signupText.setStyle("-fx-fill: blue;");
			signupText.setOnMouseEntered(event -> signupText.setUnderline(true));
			signupText.setOnMouseExited(event -> signupText.setUnderline(false));
			signupText.setOnMouseClicked(event -> Utility.openPage("signuppage", primaryStage));

	        // Handle login button action
	        loginButton.setOnAction(event -> {
	            String username = InputValidation.sanitize(usernameField.getText());
	            String password = InputValidation.sanitize(passwordField.getText());

				String role = DatabaseConnector.validateUser(username, password);
				if (role.equals("Not Found")) {
					Utility.showAlert("Incorrect Credentials", "Invalid username or password", Alert.AlertType.ERROR);
				} else {
					Utility.role = role;
					Utility.username = username;
					Utility.openPage("effortloggingpage", primaryStage);
				}
	        });

	        // Add elements to the grid
	        grid.add(usernameLabel, 0, 0);
	        grid.add(usernameField, 1, 0);
	        grid.add(passwordLabel, 0, 1);
	        grid.add(passwordField, 1, 1);
	        grid.add(loginButton, 0, 3, 2, 1);
			grid.add(signupText, 0, 4, 2, 1);

	        // Create a container for the grid
	        VBox container = new VBox(20);
	        container.getChildren().addAll(grid);

	        Scene scene = new Scene(container, 400, 300);

	        primaryStage.setScene(scene);
	        primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		DatabaseConnector.loadLists();
		launch(args);
	}
}
