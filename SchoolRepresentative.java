import java.io.*;
import java.sql.*;
import java.util.Arrays;

public class SchoolRepresentative {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/recess-r";
    private static final String USER = "root";
    private static final String PASS = "";
    private static final String PARTICIPANT_CSV_FILE_PATH = "D:\\Math\\participants.csv";

    public static void loginSchoolRepresentative(String username, String email, PrintWriter out) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            if (validateSchoolRepresentative(conn, username, email)) {
                out.println("Login successful. Welcome " + username + "!");
            } else {
                out.println("Invalid credentials. Please try again.");
            }
        } catch (SQLException e) {
            out.println("Database error: " + e.getMessage());
        }
    }

    private static boolean validateSchoolRepresentative(Connection conn, String username, String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM schools WHERE name_of_representative = ? AND email_of_representative = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public static void viewApplicants(PrintWriter out) {
        System.out.println("debufjfjfjffkkfkfkfkkfk");
        try (BufferedReader reader = new BufferedReader(new FileReader(PARTICIPANT_CSV_FILE_PATH))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                for (String detail : details) {
                    System.out.println(detail);
                }
                out.println("username: " + details[0] + ", first_name: " + details[1] + ", last _name: " + details[2] + ", email: " + details[3] + ", date_of_birth: " + details[4] + ", school_registration_number: " + details[5] + ", image_path: " + details[6]);
            }
            out.println("End of list");
        } catch (IOException e) {
            out.println("Error reading CSV file: " + e.getMessage());
        }
    }

    public static void processApplicant(String command, String username, PrintWriter out) {
        System.out.println(command);
        File inputFile = new File(PARTICIPANT_CSV_FILE_PATH);
        File tempFile = new File("participants_temp.csv");
        boolean userFound = false;
        String[] userDetails = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            writer.write(reader.readLine()); // Write header
            writer.newLine();
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                System.out.println(details[0]);
                if (details[0].equalsIgnoreCase(username)) {
                    userFound = true;
                    userDetails = details;
                    out.println("Found user: " + username);
                    break;
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            out.println("Error processing applicant: " + e.getMessage());
            return;
        }

        if (userFound) {
            out.println("Processing command: " + command);
            if (command.equalsIgnoreCase("confirm")) {
                out.println("Applicant accepted.");
                saveToDatabase(userDetails, "participants");
                Email.notifyParticipant(userDetails[3], "confirm");
            } else if (command.equalsIgnoreCase("reject")) {
                out.println("Applicant rejected.");
                saveToDatabase(userDetails, "rejected__participants");
                Email.notifyParticipant(userDetails[3], "reject");
            } else {
                out.println("Unknown command: " + command);
            }
        } else {
            out.println("Applicant not found.");
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            out.println("Error updating participant records.");
        } else {
            out.println("Participant records updated successfully.");
        }
    }

    private static void saveToDatabase(String[] details, String tableName) {
        for (String detail : details) {
            System.out.println(detail);
        }
        String query = "INSERT INTO " + tableName + " (username, first_name, last_name, email, date_of_birth, school_registration_number) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, details[0]); // username
            pstmt.setString(2, details[1]); // firstname
            pstmt.setString(3, details[2]); // lastname
            pstmt.setString(4, details[3]); // email
            pstmt.setString(5, details[4]); // date_of_birth
            pstmt.setString(6, details[5]); // school_registration_number
            // pstmt.setString(7, details[6]); // image_path
            pstmt.executeUpdate();
            System.out.println("Saved to database: " + Arrays.toString(details));
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}