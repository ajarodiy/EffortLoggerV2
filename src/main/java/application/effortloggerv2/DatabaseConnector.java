// Author: Aditya Jarodiya

package application.effortloggerv2;

import javafx.scene.control.Alert;
import javafx.scene.control.TableView;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Properties;

// Author: Akshit Jain
public class DatabaseConnector {
    private static final String dbHost = "aws.connect.psdb.cloud";
	private static final String dbUsername = "wi9tyjj4kfjmu2g5b7f6";
	private static final String dbPassword = "pscale_pw_NZp8UREkjyS4tz9QyMAfDFJ9CqnIgmRuItYD4AdFdpI";
	private static final String dbName = "cse360project";
    private static Connection connection;

	// Author: Akshit Jain
	static {
		Properties props = new Properties();
		props.setProperty("user", dbUsername);
		props.setProperty("password", dbPassword);
		props.setProperty("useSSL", "true");

		try {
			String url = "jdbc:mysql://" + dbHost + "/" + dbName;
			connection = DriverManager.getConnection(url, props);
		} catch (SQLException e) {
			Utility.showAlert("Error", "An error occurred!", Alert.AlertType.ERROR);
			e.printStackTrace();
		}
	}

	// Author: Akshit Jain
	public static void addUser(String name, String password) {
		try {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO user_credentials (username, password) VALUES (?, ?)");
			statement.setString(1, name);
			statement.setString(2, Utility.hashPassword(password));
			statement.executeUpdate();
		} catch (SQLException e) {
			Utility.showAlert("Error", "An error occurred!", Alert.AlertType.ERROR);
			e.printStackTrace();
		}
	}

	// Author: Akshit Jain
	public static String validateUser(String name, String password) {
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM user_credentials WHERE username = ? AND password = ?");
			statement.setString(1, name);
			statement.setString(2, Utility.hashPassword(password));
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				String role = resultSet.getString(3);
				return role;
			}
		} catch (SQLException e) {
			Utility.showAlert("Error", "An error occurred!", Alert.AlertType.ERROR);
			e.printStackTrace();
		}
		return "Not Found";
	}

	// Author: Akshit Jain
	public static void logEffort(LocalDateTime startTime, LocalDateTime endTime, long timeElapsed, String project, String lifeCycle, String effortCategory, String plan) {
		try {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO EffortLog (username, start_time, end_time, time_taken, project_title, life_cycle, effort_category, plan) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			statement.setString(1, Utility.username);
			statement.setObject(2, startTime);
			statement.setObject(3, endTime);
			statement.setLong(4, timeElapsed);
			statement.setString(5, project);
			statement.setString(6, lifeCycle);
			statement.setString(7, effortCategory);
			statement.setString(8, plan);
			statement.executeUpdate();
		} catch (SQLException e) {
			Utility.showAlert("Error", "An error occurred!", Alert.AlertType.ERROR);
			e.printStackTrace();
		}
	}

	// Author: Ishan Kavdia
	public static void fetchData(TableView<EffortLogEntry> tableView) {
		try {
			Statement statement = connection.createStatement();

			// Retrieve data from EffortLog table
			ResultSet resultSet = statement.executeQuery("SELECT * FROM EffortLog");

			// Clear existing items from TableView
			tableView.getItems().clear();

			// Populate TableView with data from ResultSet
			while (resultSet.next()) {
				EffortLogEntry logEntry = new EffortLogEntry(
						resultSet.getString("username"),
						resultSet.getString("start_time"),
						resultSet.getString("end_time"),
						resultSet.getString("time_taken"),
						resultSet.getString("project_title"),
						resultSet.getString("life_cycle"),
						resultSet.getString("effort_category"),
						resultSet.getString("plan")
				);

				tableView.getItems().add(logEntry);
			}

			// Close resources
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			Utility.showAlert("Error", "An error occurred!", Alert.AlertType.ERROR);
			e.printStackTrace();
		}
	}

	// Author: Aditya Jarodiya
	public static double previousEffortsAverage(String project, String lifeCycle, String effortCategory, String plan) {
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT AVG(time_taken) AS average_time FROM EffortLog " +
					"WHERE project_title = ? AND life_cycle = ? AND effort_category = ? AND plan = ?");
			statement.setString(1, project);
			statement.setString(2, lifeCycle);
			statement.setString(3, effortCategory);
			statement.setString(4, plan);

			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getDouble("average_time");
			}

		} catch (SQLException e) {
			Utility.showAlert("Error", "An error occurred!", Alert.AlertType.ERROR);
			e.printStackTrace();
		}
		return 0;

	}

	public static void loadLists() {
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM projects");
			while (resultSet.next()) {
				Utility.projects.add(resultSet.getString("value"));
			}
			resultSet = statement.executeQuery("SELECT * FROM lifeCycles");
			while (resultSet.next()) {
				Utility.lifeCycles.add(resultSet.getString("value"));
			}
			resultSet = statement.executeQuery("SELECT * FROM effortCategories");
			while (resultSet.next()) {
				Utility.effortCategories.add(resultSet.getString("value"));
			}
			resultSet = statement.executeQuery("SELECT * FROM plans");
			while (resultSet.next()) {
				Utility.plans.add(resultSet.getString("value"));
			}
			resultSet = statement.executeQuery("SELECT * FROM completedProjects");
			while (resultSet.next()) {
				Utility.completedProjects.add(resultSet.getString("value"));
			}
		} catch (SQLException e) {
			Utility.showAlert("Error", "An error occurred!", Alert.AlertType.ERROR);
			e.printStackTrace();
		}
	}

//	public static void updateLists() {
//		try {
//			Statement statement = connection.createStatement();
//			statement.executeUpdate("TRUNCATE TABLE projects");
//			statement.executeUpdate("TRUNCATE TABLE effortCategories");
//			statement.executeUpdate("TRUNCATE TABLE lifeCycles");
//			statement.executeUpdate("TRUNCATE TABLE completedProjects");
//			statement.executeUpdate("TRUNCATE TABLE plans");
//
//			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO projects (value) VALUES (?)");
//			for (String value : Utility.projects) {
//				preparedStatement.setString(1, value);
//				preparedStatement.addBatch();
//			}
//			preparedStatement.executeBatch();
//			preparedStatement = connection.prepareStatement("INSERT INTO lifeCycles (value) VALUES (?)");
//			for (String value : Utility.lifeCycles) {
//				preparedStatement.setString(1, value);
//				preparedStatement.addBatch();
//			}
//			preparedStatement.executeBatch();
//			preparedStatement = connection.prepareStatement("INSERT INTO effortCategories (value) VALUES (?)");
//			for (String value : Utility.effortCategories) {
//				preparedStatement.setString(1, value);
//				preparedStatement.addBatch();
//			}
//			preparedStatement.executeBatch();
//			preparedStatement = connection.prepareStatement("INSERT INTO plans (value) VALUES (?)");
//			for (String value : Utility.plans) {
//				preparedStatement.setString(1, value);
//				preparedStatement.addBatch();
//			}
//			preparedStatement.executeBatch();
//			preparedStatement = connection.prepareStatement("INSERT INTO completedProjects (value) VALUES (?)");
//			for (String value : Utility.completedProjects) {
//				preparedStatement.setString(1, value);
//				preparedStatement.addBatch();
//			}
//			preparedStatement.executeBatch();
//		} catch (SQLException e) {
//			Utility.showAlert("Error", "An error occurred!", Alert.AlertType.ERROR);
//			e.printStackTrace();
//		}
//	}

	public static void updateLists(String listName, String action, String value) {
		try {
			if (action.equals("add")) {
				PreparedStatement statement = connection.prepareStatement("INSERT INTO " + listName + " (value) VALUES (?)");
				statement.setString(1, value);
				statement.executeUpdate();
			} else if (action.equals("delete")) {
				PreparedStatement statement = connection.prepareStatement("DELETE FROM " + listName + " WHERE value = ?");
				statement.setString(1, value);
				System.out.println(statement.executeUpdate());
			}
		} catch (SQLException e) {
			Utility.showAlert("Error", "An error occurred!", Alert.AlertType.ERROR);
			e.printStackTrace();
		}
	}
}
