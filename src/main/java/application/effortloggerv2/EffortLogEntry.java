package application.effortloggerv2;

import javafx.beans.property.SimpleStringProperty;

// Author: Aditya Jarodiya
public class EffortLogEntry {
    public SimpleStringProperty username, startTime, endTime, timeElapsed, project, lifeCycle, effortCategory, plan;

    public EffortLogEntry(String username, String startTime, String endTime, String timeElapsed, String project, String lifeCycle, String effortCategory, String plan) {
        this.username = new SimpleStringProperty(username);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.timeElapsed = new SimpleStringProperty(formatTime(Integer.parseInt(timeElapsed)));
        this.project = new SimpleStringProperty(project);
        this.lifeCycle = new SimpleStringProperty(lifeCycle);
        this.effortCategory = new SimpleStringProperty(effortCategory);
        this.plan = new SimpleStringProperty(plan);
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }
    public SimpleStringProperty startTimeProperty() {
        return startTime;
    }

    public SimpleStringProperty endTimeProperty() {
        return endTime;
    }

    public SimpleStringProperty timeElapsedProperty() {
        return timeElapsed;
    }

    public SimpleStringProperty projectProperty() {
        return project;
    }

    public SimpleStringProperty lifeCycleProperty() {
        return lifeCycle;
    }

    public SimpleStringProperty effortCategoryProperty() {
        return effortCategory;
    }

    public SimpleStringProperty planProperty() {
        return plan;
    }

    // Author: Mihir Kataria
    private String formatTime(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int remainingSeconds = seconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }

}
