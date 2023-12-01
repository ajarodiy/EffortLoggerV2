package application.effortloggerv2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The EffortLoggingPage class extends Application and is used for creating a JavaFX GUI.
 * This class is part of a time tracking and effort logging application. It allows users
 * to record the time spent on various tasks, categorized by project, life cycle, effort category,
 * and plans.
 */
// Author: Aditya Jarodiya and Akshit Jain
public class EffortLoggingPage extends Application {
    // Declare UI elements and time tracking variables
    private Stage primaryStage;
    private Label clockStatus;
    private Button startButton;
    private ComboBox<String> projectDropdown;
    private ComboBox<String> lifeCycleDropdown;
    private ComboBox<String> effortCategoryDropdown;
    private ComboBox<String> plansDropdown;
    private Button stopButton;
    private TextArea logsArea;
    private Button effortLogsViewButton;
    private Button editTasksButton;

    private LocalDateTime startTime;

    @Override
    public void start(Stage stage) {
        // Basic setup of the application stage
        primaryStage = stage;
        stage.setTitle("Effort Logging Page");

        // Initializing and setting up UI components
        // clockStatus indicates the current state of the timer
        clockStatus = new Label("Clock is stopped");
        clockStatus.setStyle("-fx-background-color: red; -fx-padding: 5px;");
        clockStatus.setTextFill(Color.WHITE);

        // Buttons for controlling the application's functionality
        startButton = new Button("Start this Activity");

        // Dropdowns for selecting project, lifecycle step, effort category, and plan
        projectDropdown = new ComboBox<>();
        projectDropdown.setPromptText("Select Project");
        projectDropdown.getItems().addAll(Utility.projects);

        lifeCycleDropdown = new ComboBox<>();
        lifeCycleDropdown.setPromptText("Select Life Cycle Step");
        lifeCycleDropdown.getItems().addAll(Utility.lifeCycles);

        effortCategoryDropdown = new ComboBox<>();
        effortCategoryDropdown.setPromptText("Select Effort Category");
        effortCategoryDropdown.getItems().addAll(Utility.effortCategories);

        plansDropdown = new ComboBox<>();
        plansDropdown.setPromptText("Select Plan");
        plansDropdown.getItems().addAll(Utility.plans);

        // Button to stop the activity
        stopButton = new Button("Stop this Activity");
        stopButton.setDisable(true);

        // TextArea for logging activities and events
        logsArea = new TextArea();
        logsArea.setEditable(false);

        // Event handling for the start button
        startButton.setOnAction(e -> {
            // Validations and actions on start event
            if (projectDropdown.getValue() == null) {
                Utility.showAlert("Empty", "Please select a project to work on", Alert.AlertType.WARNING);
                return;
            }
            clockStatus.setText("Clock is running");
            clockStatus.setStyle("-fx-background-color: green; -fx-padding: 5px;");
            clockStatus.setTextFill(Color.WHITE);
            startButton.setDisable(true);
            stopButton.setDisable(false);

            // Start the timer logic
            startTime = LocalDateTime.now();

            // Log the start time and selected details
            log("Started at: " + formatTime(startTime) +
                    "\nProject: " + projectDropdown.getValue() +
                    "\nLife Cycle Step: " + lifeCycleDropdown.getValue() +
                    "\nEffort Category: " + effortCategoryDropdown.getValue() +
                    "\nPlan: " + plansDropdown.getValue());
        });

        // Event handling for the stop button
        stopButton.setOnAction(e -> {
            clockStatus.setText("Clock is stopped");
            clockStatus.setStyle("-fx-background-color: red; -fx-padding: 5px;");
            clockStatus.setTextFill(Color.WHITE);
            startButton.setDisable(false);
            stopButton.setDisable(true);

            // Stop the timer logic and calculate elapsed time
            LocalDateTime endTime = LocalDateTime.now();
            long timeElapsed = calculateTimeElapsed(startTime, endTime);

            // Log the end time and the duration of the activity
            log("Stopped at: " + formatTime(endTime) +
                    "\nTime taken: " + formatTime(timeElapsed) +
                    "\nProject: " + projectDropdown.getValue() +
                    "\nLife Cycle Step: " + lifeCycleDropdown.getValue() +
                    "\nEffort Category: " + effortCategoryDropdown.getValue() +
                    "\nPlan: " + plansDropdown.getValue());

            // Additional functionality to calculate and log efficiency
            double averageTimeForTask = DatabaseConnector.previousEffortsAverage(projectDropdown.getValue(), lifeCycleDropdown.getValue(),
                    effortCategoryDropdown.getValue(), plansDropdown.getValue());
            if (averageTimeForTask == 0) {
                log("No records for this task were found. Thus your efficiency for this task was 100%.");
            } else {
                double efficiency = averageTimeForTask / timeElapsed;
                log("Your efficiency for this task was " + Utility.formatAsPercentage(efficiency));
            }

            // Logging the effort details in the database
            DatabaseConnector.logEffort(startTime, endTime, timeElapsed, projectDropdown.getValue(), lifeCycleDropdown.getValue(),
                    effortCategoryDropdown.getValue(), plansDropdown.getValue());
            log("Effort logged successfully.");
        });

        // Buttons to view effort logs and edit tasks
        effortLogsViewButton = new Button("View Effort Logs");
        effortLogsViewButton.setOnAction(e -> Utility.openPage("effortlogdisplaypage", stage));

        editTasksButton = new Button("Edit Tasks");
        editTasksButton.setOnAction(e -> Utility.openPage("taskeditorpage", stage));

        // Button for logging out of the application
        Button logOutButton = new Button("Log Out");
        logOutButton.setOnAction(e -> Utility.openPage("loginpage", stage));

        // HBox and VBox for layout management
        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(effortLogsViewButton, editTasksButton, logOutButton);

        VBox root = new VBox(10);
        root.getChildren().addAll(clockStatus, startButton, projectDropdown, lifeCycleDropdown, effortCategoryDropdown, plansDropdown,  stopButton, logsArea, hbox);

        // Scene setup for the primary stage
        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
    }

    /**
     * Formats a LocalDateTime object into a string representation.
     * @param time LocalDateTime object to be formatted.
     * @return String representing formatted date and time.
     */
    private String formatTime(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return time.format(formatter);
    }

    /**
     * Formats a duration in seconds into a string representation of hours, minutes, and seconds.
     * @param seconds Duration in seconds to be formatted.
     * @return String representing the formatted duration.
     */
    private String formatTime(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }

    /**
     * Calculates the time elapsed between two LocalDateTime objects in seconds.
     * @param startTime Start time of the activity.
     * @param endTime End time of the activity.
     * @return Duration in seconds between startTime and endTime.
     */
    private long calculateTimeElapsed(LocalDateTime startTime, LocalDateTime endTime) {
        return startTime.until(endTime, java.time.temporal.ChronoUnit.SECONDS);
    }

    /**
     * Appends a log entry to the TextArea component.
     * @param logEntry String containing the information to be logged.
     */
    private void log(String logEntry) {
        logsArea.appendText("\n\n" + logEntry);
    }
}
