package application.effortloggerv2;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utility {
    public static String role = "";
    public static String username = "";

    public static List<String> projects = new ArrayList<>();
    public static List<String> lifeCycles = new ArrayList<>();
    public static List<String> effortCategories = new ArrayList<>();
    public static List<String> plans = new ArrayList<>();
    public static List<String> completedProjects = new ArrayList<>();





    // Author: Mishika Chhabra
    public static void openPage(String pageName, Stage stage) {
        switch (pageName) {
            case "loginpage":
                LoginPage loginPage = new LoginPage();
                loginPage.start(stage);
                break;
            case "signuppage":
                SignupPage signupPage = new SignupPage();
                signupPage.start(stage);
                break;
            case "effortloggingpage":
                EffortLoggingPage effortLoggingPage = new EffortLoggingPage();
                effortLoggingPage.start(stage);
                break;
            case "effortlogdisplaypage":
                EffortLogDisplayPage effortLogDisplayPage = new EffortLogDisplayPage();
                effortLogDisplayPage.start(stage);
                break;
            case "taskeditorpage":
                TaskEditorPage taskEditorPage = new TaskEditorPage();
                taskEditorPage.start(stage);
                break;
        }
    }

    // Author: Mishika Chhabra
    public static String hashPassword(String plainPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(plainPassword.getBytes());

            StringBuilder hashedPassword = new StringBuilder();
            for (byte b : hashedBytes) {
                hashedPassword.append(String.format("%02x", b));
            }
            return hashedPassword.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    // Author: Aditya Jarodiya
    public static void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Author: Aditya Jarodiya
    public static String formatAsPercentage(double value) {
        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMinimumFractionDigits(2);
        return percentFormat.format(value);
    }

    public static String dbUsername = "lewtn53opij8mfwl0m41";
    public static String dbPassword1 = "pscale_pw_Q5Er";
    public static String dbPassword2 = "oCN9ClpstdMb1MSRRqt";
    public static String dbPassword3 = "ErXaZTxS5xUFT0G9jkm3";
}
