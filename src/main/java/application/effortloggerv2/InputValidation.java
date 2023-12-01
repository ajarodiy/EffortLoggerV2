// Author: Ishan Kavdia

package application.effortloggerv2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The InputValidation class is designed to handle input validation and sanitization
 * to ensure the security and integrity of user input in the Effort Logger application.
 */
public class InputValidation {

    /**
     * Sanitize the input to remove potential security risks.
     *
     * @param input The input string to be sanitized.
     * @return A sanitized version of the input string.
     */
    public static String sanitize(String input) {
        // Check for null input
        if (input == null) {
            return "";
        }

        // Remove leading and trailing white spaces
        input = input.trim();

        // Prevent SQL injection by removing common SQL injection characters
        input = input.replaceAll("([';\"\\\\])", "");

        // Prevent XSS (Cross-Site Scripting) attacks by converting < and > to HTML entities
        input = input.replaceAll("<", "&lt;").replaceAll(">", "&gt;");

        return input;
    }

    /**
     * Additional custom validation checks (not used currently).
     *
     * @param input The input string to be validated.
     * @return True if the input passes all validation checks, false otherwise.
     */
    private static boolean isValidInput(String input) {
        // Validate that the input contains only letters, numbers, and some special characters
        // Add more validation checks as needed

        // Define a regular expression pattern for validation
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9!@#\\$%^&*()_+\\-={}:;',./?]*$");
        Matcher matcher = pattern.matcher(input);

        // Check if the input matches the pattern
        return matcher.matches();
    }
}
