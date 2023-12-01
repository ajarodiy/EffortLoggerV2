// Author: Ishan Kavdia

package application.effortloggerv2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The EffortLogDisplayPage class extends the JavaFX Application class and serves as the user interface
 * for displaying logged efforts in the Effort Logger application.
 *
 * @author Ishan Kavdia
 */
public class EffortLogDisplayPage extends Application {
    private TableView<EffortLogEntry> tableView;
    private Stage primaryStage;

    /**
     * The start method is the entry point for the EffortLogDisplayPage application.
     * It initializes the stage, sets up the UI components, and displays the Effort Log data.
     *
     * @param stage The primary stage for this application.
     */
    @Override
    public void start(Stage stage) {
        // Set the primary stage
        primaryStage = stage;
        stage.setTitle("Effort Log Display");

        // Create table columns with corresponding data bindings
        TableColumn<EffortLogEntry, String> usernameColumn = new TableColumn<>("Employee Name");
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());

        TableColumn<EffortLogEntry, String> startTimeColumn = new TableColumn<>("Start Time");
        startTimeColumn.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());

        TableColumn<EffortLogEntry, String> endTimeColumn = new TableColumn<>("End Time");
        endTimeColumn.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());

        TableColumn<EffortLogEntry, String> timeTakenColumn = new TableColumn<>("Time Taken");
        timeTakenColumn.setCellValueFactory(cellData -> cellData.getValue().timeElapsedProperty());

        TableColumn<EffortLogEntry, String> projectTitleColumn = new TableColumn<>("Project Title");
        projectTitleColumn.setCellValueFactory(cellData -> cellData.getValue().projectProperty());

        TableColumn<EffortLogEntry, String> lifeCycleColumn = new TableColumn<>("Life Cycle");
        lifeCycleColumn.setCellValueFactory(cellData -> cellData.getValue().lifeCycleProperty());

        TableColumn<EffortLogEntry, String> effortCategoryColumn = new TableColumn<>("Effort Category");
        effortCategoryColumn.setCellValueFactory(cellData -> cellData.getValue().effortCategoryProperty());

        TableColumn<EffortLogEntry, String> planColumn = new TableColumn<>("Plan");
        planColumn.setCellValueFactory(cellData -> cellData.getValue().planProperty());

        // Create TableView
        tableView = new TableView<>();
        tableView.getColumns().addAll(usernameColumn, startTimeColumn, endTimeColumn, timeTakenColumn,
                projectTitleColumn, lifeCycleColumn, effortCategoryColumn, planColumn);

        // Fetch data from the database and populate the TableView
        DatabaseConnector.fetchData(tableView);

        // Create "Home Page" button with an event handler to navigate back to the main page
        Button backButton = new Button("Home Page");
        backButton.setOnAction(event -> Utility.openPage("effortloggingpage", stage));

        // Create a vertical box layout to organize TableView and the "Home Page" button
        VBox vbox = new VBox();
        vbox.getChildren().addAll(tableView, backButton);

        // Create the scene with the specified dimensions
        Scene scene = new Scene(vbox, 1000, 800);
        stage.setScene(scene);

        // Center the stage on the screen
        stage.centerOnScreen();

        // Show the stage
        stage.show();
    }

}
