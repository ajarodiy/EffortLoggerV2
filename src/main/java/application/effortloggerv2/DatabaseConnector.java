// Author: Aditya Jarodiya

package application.effortloggerv2;

import javafx.scene.control.Alert;
import javafx.scene.control.TableView;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Properties;

// Author: Akshit Jain
public class DatabaseConnector {

	// Database connection parameters
	private static final String dbHost = "aws.connect.psdb.cloud";
	private static final String dbName = "cse360project";

	// JDBC Connection object
	private static Connection connection;

	// Static block to initialize the database connection when the class is loaded
	static {
		// Set database connection properties
		Properties props = new Properties();
		props.setProperty("user", Utility.dbUsername);
		props.setProperty("password", Utility.dbPassword1 + Utility.dbPassword2 + Utility.dbPassword3);
		props.setProperty("useSSL", "true");

		try {
			// Construct the JDBC URL for the database connection
			String url = "jdbc:mysql://" + dbHost + "/" + dbName;

			// Establish the database connection using DriverManager
			connection = DriverManager.getConnection(url, props);
		} catch (SQLException e) {
			// Handle any SQL exception that may occur during connection establishment
			Utility.showAlert("Error", "An error occurred!", Alert.AlertType.ERROR);
			e.printStackTrace();
		}
	}


	// Author: Akshit Jain
	public static void addUser(String name, String password) {
		try {
			// Prepare the SQL statement to insert user credentials into the database
			PreparedStatement statement = connection.prepareStatement("INSERT INTO user_credentials (username, password) VALUES (?, ?)");

			// Set the parameters in the prepared statement
			statement.setString(1, name);
			statement.setString(2, Utility.hashPassword(password));

			// Execute the update to insert the new user
			statement.executeUpdate();
		} catch (SQLException e) {
			// Handle any SQL exception that may occur during user addition
			Utility.showAlert("Error", "An error occurred!", Alert.AlertType.ERROR);
			e.printStackTrace();
		}
	}

	// Author: Akshit Jain
	public static String validateUser(String name, String password) {
		try {
			// Prepare the SQL statement to select user credentials from the database
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM user_credentials WHERE username = ? AND password = ?");

			// Set the parameters in the prepared statement
			statement.setString(1, name);
			statement.setString(2, Utility.hashPassword(password));

			// Execute the query to retrieve user information
			ResultSet resultSet = statement.executeQuery();

			// Check if a user with the provided credentials exists
			if (resultSet.next()) {
				// Retrieve and return the role of the user
				String role = resultSet.getString(3);
				return role;
			}
		} catch (SQLException e) {
			// Handle any SQL exception that may occur during user validation
			Utility.showAlert("Error", "An error occurred!", Alert.AlertType.ERROR);
			e.printStackTrace();
		}

		// Return "Not Found" if user validation fails
		return "Not Found";
	}

	// Author: Akshit Jain
	public static void logEffort(
			LocalDateTime startTime,
			LocalDateTime endTime,
			long timeElapsed,
			String project,
			String lifeCycle,
			String effortCategory,
			String plan
	) {
		try {
			// Prepare the SQL statement to insert effort log into the EffortLog table
			PreparedStatement statement = connection.prepareStatement("INSERT INTO EffortLog (username, start_time, end_time, time_taken, project_title, life_cycle, effort_category, plan) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

			// Set the parameters in the prepared statement
			statement.setString(1, Utility.username);
			statement.setObject(2, startTime);
			statement.setObject(3, endTime);
			statement.setLong(4, timeElapsed);
			statement.setString(5, project);
			statement.setString(6, lifeCycle);
			statement.setString(7, effortCategory);
			statement.setString(8, plan);

			// Execute the update to log the effort
			statement.executeUpdate();
		} catch (SQLException e) {
			// Handle any SQL exception that may occur during effort logging
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
			// Prepare the SQL statement to calculate the average time taken from the EffortLog table
			PreparedStatement statement = connection.prepareStatement("SELECT AVG(time_taken) AS average_time FROM EffortLog " +
					"WHERE project_title = ? AND life_cycle = ? AND effort_category = ? AND plan = ?");

			// Set the parameters in the prepared statement
			statement.setString(1, project);
			statement.setString(2, lifeCycle);
			statement.setString(3, effortCategory);
			statement.setString(4, plan);

			// Execute the query to retrieve the average time
			ResultSet resultSet = statement.executeQuery();

			// Check if a result is obtained
			if (resultSet.next()) {
				// Return the average time taken
				return resultSet.getDouble("average_time");
			}

		} catch (SQLException e) {
			// Handle any SQL exception that may occur during average calculation
			Utility.showAlert("Error", "An error occurred!", Alert.AlertType.ERROR);
			e.printStackTrace();
		}

		// Return 0 if there is no data or an error occurs
		return 0;
	}

	public static void loadLists() {
		try {
			// Create a statement for executing SQL queries
			Statement statement = connection.createStatement();

			// Load projects list from the 'projects' table
			ResultSet resultSet = statement.executeQuery("SELECT * FROM projects");
			while (resultSet.next()) {
				Utility.projects.add(resultSet.getString("value"));
			}

			// Load life cycles list from the 'lifeCycles' table
			resultSet = statement.executeQuery("SELECT * FROM lifeCycles");
			while (resultSet.next()) {
				Utility.lifeCycles.add(resultSet.getString("value"));
			}

			// Load effort categories list from the 'effortCategories' table
			resultSet = statement.executeQuery("SELECT * FROM effortCategories");
			while (resultSet.next()) {
				Utility.effortCategories.add(resultSet.getString("value"));
			}

			// Load plans list from the 'plans' table
			resultSet = statement.executeQuery("SELECT * FROM plans");
			while (resultSet.next()) {
				Utility.plans.add(resultSet.getString("value"));
			}

			// Load completed projects list from the 'completedProjects' table
			resultSet = statement.executeQuery("SELECT * FROM completedProjects");
			while (resultSet.next()) {
				Utility.completedProjects.add(resultSet.getString("value"));
			}

		} catch (SQLException e) {
			// Handle any SQL exception that may occur during list loading
			Utility.showAlert("Error", "An error occurred!", Alert.AlertType.ERROR);
			e.printStackTrace();
		}
	}


	public static void updateLists(String listName, String action, String value) {
		try {
			// Check the action to determine whether to add or delete from the list
			if (action.equals("add")) {
				// Prepare the SQL statement to insert a new value into the specified list
				PreparedStatement statement = connection.prepareStatement("INSERT INTO " + listName + " (value) VALUES (?)");
				statement.setString(1, value);
				statement.executeUpdate();
			} else if (action.equals("delete")) {
				// Prepare the SQL statement to delete a value from the specified list
				PreparedStatement statement = connection.prepareStatement("DELETE FROM " + listName + " WHERE value = ?");
				statement.setString(1, value);

				// Execute the update and print the number of rows affected (for debugging purposes)
				System.out.println(statement.executeUpdate());
			}
		} catch (SQLException e) {
			// Handle any SQL exception that may occur during list updating
			Utility.showAlert("Error", "An error occurred!", Alert.AlertType.ERROR);
			e.printStackTrace();
		}
	}
}
