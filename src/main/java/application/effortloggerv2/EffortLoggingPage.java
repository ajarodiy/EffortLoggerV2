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

// Author: Aditya Jarodiya and Akshit Jain
public class EffortLoggingPage extends Application {
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
        primaryStage = stage;
        stage.setTitle("Effort Logging Page");

        // Author: Mihir Kataria
        clockStatus = new Label("Clock is stopped");
        clockStatus.setStyle("-fx-background-color: red; -fx-padding: 5px;");
        clockStatus.setTextFill(Color.WHITE);

        startButton = new Button("Start this Activity");

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

        stopButton = new Button("Stop this Activity");
        stopButton.setDisable(true);

        logsArea = new TextArea();
        logsArea.setEditable(false);

        startButton.setOnAction(e -> {
            // Author: Mihir Kataria
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

        stopButton.setOnAction(e -> {
            // Author: Mihir Kataria
            clockStatus.setText("Clock is stopped");
            clockStatus.setStyle("-fx-background-color: red; -fx-padding: 5px;");
            clockStatus.setTextFill(Color.WHITE);
            startButton.setDisable(false);
            stopButton.setDisable(true);

            // Stop the timer logic
            LocalDateTime endTime = LocalDateTime.now();

            // Log the end time and calculate the time taken
            long timeElapsed = calculateTimeElapsed(startTime, endTime);

            log("Stopped at: " + formatTime(endTime) +
                    "\nTime taken: " + formatTime(timeElapsed) +
                    "\nProject: " + projectDropdown.getValue() +
                    "\nLife Cycle Step: " + lifeCycleDropdown.getValue() +
                    "\nEffort Category: " + effortCategoryDropdown.getValue() +
                    "\nPlan: " + plansDropdown.getValue());

            // Author: Aditya Jarodiya
            double averageTimeForTask = DatabaseConnector.previousEffortsAverage(projectDropdown.getValue(), lifeCycleDropdown.getValue(),
                    effortCategoryDropdown.getValue(), plansDropdown.getValue());
            if (averageTimeForTask == 0) {
                log("No records for this task were found. Thus your efficiency for this task was 100%.");
            } else {
                double efficiency = averageTimeForTask / timeElapsed;
                log("Your efficiency for this task was " + Utility.formatAsPercentage(efficiency));
            }

            DatabaseConnector.logEffort(startTime, endTime, timeElapsed, projectDropdown.getValue(), lifeCycleDropdown.getValue(),
                    effortCategoryDropdown.getValue(), plansDropdown.getValue());
            log("Effort logged successfully.");
        });

        effortLogsViewButton = new Button("View Effort Logs");
        effortLogsViewButton.setOnAction(e -> Utility.openPage("effortlogdisplaypage", stage));

        editTasksButton = new Button("Edit Tasks");
        editTasksButton.setOnAction(e -> Utility.openPage("taskeditorpage", stage));

        Button logOutButton = new Button("Log Out");
        logOutButton.setOnAction(e -> Utility.openPage("loginpage", stage));

        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(effortLogsViewButton, editTasksButton, logOutButton);

        VBox root = new VBox(10);
        root.getChildren().addAll(clockStatus, startButton, projectDropdown, lifeCycleDropdown, effortCategoryDropdown, plansDropdown,  stopButton, logsArea, hbox);

        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
    }

    // Author: Mihir Kataria
    private String formatTime(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return time.format(formatter);
    }

    // Author: Mihir Kataria
    private String formatTime(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }

    // Author: Mihir Kataria
    private long calculateTimeElapsed(LocalDateTime startTime, LocalDateTime endTime) {
        return startTime.until(endTime, java.time.temporal.ChronoUnit.SECONDS);
    }

    private void log(String logEntry) {
        logsArea.appendText("\n\n" + logEntry);
    }
}
