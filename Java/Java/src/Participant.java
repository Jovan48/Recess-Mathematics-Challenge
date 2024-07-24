import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Participant {
    private static final String PARTICIPANT_CSV_FILE_PATH = "participants.csv";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/recess-r";
    private static final String USER = "root";
    private static final String PASS = "";
    private static Set<String> registeredEmails = new HashSet<>();
    private static Set<String> registeredUsernames = new HashSet<>();

    public static void loadExistingRegistrations() {
        File file = new File(PARTICIPANT_CSV_FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length >= 4) {
                    registeredUsernames.add(details[0].trim());
                    registeredEmails.add(details[3].trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
    }

    public static void registerParticipant(String input, PrintWriter out) {
        try {
            String[] details = input.split(" ");
            if (details.length < 7) {
                throw new ArrayIndexOutOfBoundsException();
            }

            String username = sanitizeInput(details[0]);
            String firstname = sanitizeInput(details[1]);
            String lastname = sanitizeInput(details[2]);
            String email = sanitizeInput(details[3]);
            String date_of_birth = sanitizeInput(details[4]);
            String school_registration_number = sanitizeInput(details[5]);
            String image_path = sanitizeInput(details[6]);

            if (registeredEmails.contains(email) || registeredUsernames.contains(username)) {
                out.println("Username or email already registered. Please try again.");
                return;
            }

            if (!isValidEmail(email)) {
                out.println("Invalid email format. Please try again.");
                return;
            }

            if (!isValidDate(date_of_birth)) {
                out.println("Invalid date format. Please use YYYY-MM-DD.");
                return;
            }

            saveParticipantDetails(username, firstname, lastname, email, date_of_birth, school_registration_number, image_path);

            registeredEmails.add(email);
            registeredUsernames.add(username);

            out.println("Participant registration complete.");

            // Notify the representative
            String representativeEmail = getRepresentativeEmail(school_registration_number);
            if (representativeEmail != null) {
                Email.notifyRep(representativeEmail);
            } else {
                System.out.println("Representative email not found for registration number: " + school_registration_number);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            out.println("Missing details. Please follow the correct format and provide all required information.");
        } catch (IOException | SQLException e) {
            out.println("Error processing registration: " + e.getMessage());
        }
    }

    private static void saveParticipantDetails(String username, String firstname, String lastname, String email, String date_of_birth, String school_registration_number, String image_path) throws IOException {
        File file = new File(PARTICIPANT_CSV_FILE_PATH);
        boolean fileExists = file.exists();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (!fileExists) {
                writer.write("username,first_name,last_name,email,date_of_birth,school_registration_number,image_path");
                writer.newLine();
            }
            writer.write(String.join(",", username, firstname, lastname, email, date_of_birth, school_registration_number, image_path));
            writer.newLine();
        }
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private static boolean isValidDate(String date) {
        String dateRegex = "^\\d{4}-\\d{2}-\\d{2}$";
        Pattern pattern = Pattern.compile(dateRegex);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }

    private static String sanitizeInput(String input) {
        return input.replaceAll("[^a-zA-Z0-9@.\\-/]", "");
    }

    private static String getRepresentativeEmail(String schoolRegistrationNumber) throws SQLException {
        String query = "SELECT email_of_representative FROM schools WHERE school_registration_number = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, schoolRegistrationNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("email_of_representative");
                }
            }
        }
        return null;
    }
}