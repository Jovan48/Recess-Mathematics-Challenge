import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("Type 'exit' to quit or select your role:");
                System.out.println("1. Participant");
                System.out.println("2. School Representative");

                String input = scanner.nextLine().trim().toLowerCase();
                if (input.equals("exit")) {
                    System.out.println("Exiting the system. Goodbye!");
                    break;
                }

                try {
                    int role = Integer.parseInt(input);
                    switch (role) {
                        case 1:
                            handleParticipant(scanner, out, in);
                            break;
                        case 2:
                            loginSchoolRepresentative(scanner, out, in);
                            break;
                        default:
                            System.out.println("Invalid selection. Please choose a valid role.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number corresponding to your role or 'exit' to quit.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }

    private static void handleParticipant(Scanner scanner, PrintWriter out, BufferedReader in) throws IOException {
        System.out.println("Are you registered or unregistered? (Enter 'registered' or 'unregistered')");
        String status = scanner.nextLine().trim().toLowerCase();

        if (status.equals("registered")) {
            handleRegisteredParticipant(scanner);
        } else if (status.equals("unregistered")) {
            promptParticipantRegistration(scanner, out, in);
        } else {
            System.out.println("Invalid input. Please enter 'registered' or 'unregistered'.");
            handleParticipant(scanner, out, in);
        }
    }

    private static void promptParticipantRegistration(Scanner scanner, PrintWriter out, BufferedReader in) throws IOException {
        System.out.println("To register as Participant, type 'Register' followed by your details:");
        System.out.println("Format: Register <username> <firstname> <lastname> <emailAddress> <date_of_birth (YYYY-MM-DD)> <school_registration_number> <image_path>");
        System.out.print("Enter your registration details: ");

        String input = scanner.nextLine();
        if (input.startsWith("Register ")) {
            out.println("register " + input.substring(9).trim());
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
                if (response.startsWith("Participant registration complete.")) {
                    break;
                }
            }
        } else {
            System.out.println("Invalid format. Please start with 'Register'.");
            promptParticipantRegistration(scanner, out, in);
        }
    }

    private static void loginSchoolRepresentative(Scanner scanner, PrintWriter out, BufferedReader in) throws IOException {
        System.out.println("School Representative Login:");
        System.out.print("Enter your username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine().trim();

        out.println("login " + username + " " + email);

        String response;
        while ((response = in.readLine()) != null) {
            System.out.println(response);
            if (response.startsWith("Login successful.")) {
                handleSchoolRepresentativeCommands(scanner, out, in);
                break;
            }
        }
    }

    private static void handleSchoolRepresentativeCommands(Scanner scanner, PrintWriter out, BufferedReader in) throws IOException {
        while (true) {
            System.out.println("Enter command (viewApplicants, confirm <username>, reject <username>, exit):");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("exit")) {
                System.out.println("Exiting the system. Goodbye!");
                break;
            }

            if (input.equals("viewapplicants") || input.startsWith("confirm") || input.startsWith("reject")) {
                out.println(input);
                String response;
                while ((response = in.readLine()) != null) {
                    System.out.println(response);
                    if (response.equals("End of list") || response.startsWith("Applicant") || response.equals("Applicant accepted.") || response.equals("Applicant rejected.")) {
                        break;
                    }
                }
            } else {
                System.out.println("Invalid command. Please try again.");
            }
        }
    }

    private static void handleRegisteredParticipant(Scanner scanner) {
        ParticipantDAO participantDAO = new ParticipantDAO();
        ChallengeDAO challengeDAO = new ChallengeDAO();
        QuestionDAO questionDAO = new QuestionDAO();
        ChallengeAttemptDAO challengeAttemptDAO = new ChallengeAttemptDAO();

        // Authenticate participant
        System.out.println("Welcome to the Math Competition!");
        System.out.println("Enter username:");
        String username = scanner.nextLine();

        System.out.println("Enter your email:");
        String email = scanner.nextLine();

        Participant participant = participantDAO.findByUsernameAndEmail(username, email);
        if (participant == null) {
            System.out.println("Invalid credentials.");
            return;
        }

        boolean attemptAnotherChallenge;
        do {
            // Display prompt for commands
            System.out.println("Enter command (view-challenges or attempt-challenge):");
            String command = scanner.nextLine().toLowerCase();

            if (command.equals("view-challenges")) {
                // Display challenges
                List<Challenge> challenges = challengeDAO.findAllChallenges();
                System.out.println("Available Challenges:");
                for (Challenge challenge : challenges) {
                    System.out.println("Challenge ID: " + challenge.getId());
                }
            } else if (command.equals("attempt-challenge")) {
                System.out.println("Enter the Challenge ID you want to attempt:");
                int challengeId = Integer.parseInt(scanner.nextLine());

                int attemptCount = 0;
                final int maxAttempts = 3;
                final long maxTimeMillis = 60000; // 1 minute for each attempt

                while (attemptCount < maxAttempts) {
                    attemptCount++;
                    List<Question> questions = questionDAO.findQuestionsById(challengeId);
                    Collections.shuffle(questions); // Randomize questions

                    List<String> answers = new ArrayList<>();
                    List<Integer> scores = new ArrayList<>();
                    List<Long> timeTaken = new ArrayList<>();
                    int totalScore = 0;
                    long remainingTimeMillis = maxTimeMillis;

                    for (Question question : questions) {
                        long startTime = System.currentTimeMillis();

                        System.out.println("Question: " + question.getQuestionText());
                        System.out.println("Remaining Questions: " + (questions.size() - answers.size() - 1));
                        System.out.println("Enter your answer (or '-' if unsure):");
                        String answer = scanner.nextLine();

                        long endTime = System.currentTimeMillis();
                        long durationMillis = endTime - startTime;
                        remainingTimeMillis -= durationMillis;
                        timeTaken.add(durationMillis);

                        if (answer.equals("-")) {
                            scores.add(0);
                        } else if (answer.equalsIgnoreCase(question.getCorrectAnswer())) {
                            scores.add(10); // Assuming each question is worth 10 marks
                            totalScore += 10;
                        } else {
                            scores.add(-3); // Deduct 3 marks for wrong answer
                            totalScore -= 3;
                            System.out.println("Wrong answer! 3 marks have been deducted.");
                        }

                        answers.add(answer);

                        // Show remaining time in seconds
                        long remainingTimeSecs = remainingTimeMillis / 1000;
                        System.out.println("Remaining time for attempt to close: " + remainingTimeSecs + " seconds");

                        if (remainingTimeMillis <= 0) {
                            System.out.println("Time's up for this attempt.");
                            break;
                        }
                    }

                    long totalTimeMillis = timeTaken.stream().mapToLong(Long::longValue).sum();
                    long totalTimeSecs = totalTimeMillis / 1000;

                    // Create ChallengeAttempt instance
                    ChallengeAttempt attempt = new ChallengeAttempt(username, challengeId, answers, scores, timeTaken);

                    // Submit attempt
                    try {
                        challengeAttemptDAO.submitAttempt(attempt);
                    } catch (Exception e) {
                        System.out.println("An error occurred while submitting the attempt.");
                        return;
                    }

                    System.out.println("Attempt " + attemptCount + " completed.");
                    System.out.println("Total Scores: " + totalScore);
                    System.out.println("Time Taken for each question (ms): " + timeTaken);
                    System.out.println("Total Time Taken: " + totalTimeSecs + " seconds");

                    if (attemptCount < maxAttempts) {
                        System.out.println("Do you wish to attempt this challenge again? (yes/no)");

                        String response = scanner.nextLine();
                        if (!response.equalsIgnoreCase("yes")) {
                            break;
                        }
                    } else {
                        System.out.println("Maximum attempts reached for this challenge.");
                    }
                }
            } else {
                System.out.println("Invalid command. Please enter 'view-challenges' or 'attempt-challenge'.");
            }

            System.out.println("Do you wish to attempt a challenge? (yes/no)");
            String response = scanner.nextLine();
            attemptAnotherChallenge = response.equalsIgnoreCase("yes");
        } while (attemptAnotherChallenge);

        System.out.println("Logging out. Goodbye!");
    }

    private static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/recess-r";
        String username = "root";
        String password = "";
        return DriverManager.getConnection(url, username, password);
    }

    public static class Challenge {
        private final int Id;

        public Challenge(int Id) {
            this.Id = Id;
        }

        public int getId() {
            return Id;
        }
    }

    public static class Question {
        private final int Question_id;
        private final int Id;
        private final String questionText;
        private final String correctAnswer;

        public Question(int Question_id, int Id, String questionText, String correctAnswer) {
            this.Question_id = Question_id;
            this.Id = Id;
            this.questionText = questionText;
            this.correctAnswer = correctAnswer;
        }

        public int getQuestion_id() {
            return Question_id;
        }

        public int getId() {
            return Id;
        }

        public String getQuestionText() {
            return questionText;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }
    }

    public static class ChallengeAttempt {
        private final String username;
        private final int Id;
        private final List<String> answers;
        private final List<Integer> scores;
        private final List<Long> timeTaken;
        private final int totalScore;
        private final long totalTimeTaken;

        public ChallengeAttempt(String username, int Id, List<String> answers, List<Integer> scores, List<Long> timeTaken) {
            this.username = username;
            this.Id = Id;
            this.answers = answers;
            this.scores = scores;
            this.timeTaken = timeTaken;
            this.totalScore = scores.stream().mapToInt(Integer::intValue).sum();
            this.totalTimeTaken = timeTaken.stream().mapToLong(Long::longValue).sum();
        }

        public String getUsername() {
            return username;
        }

        public int getId() {
            return Id;
        }

        public List<String> getAnswers() {
            return answers;
        }

        public List<Integer> getScores() {
            return scores;
        }

        public List<Long> getTimeTaken() {
            return timeTaken;
        }

        public int getTotalScore() {
            return totalScore;
        }

        public long getTotalTimeTaken() {
            return totalTimeTaken;
        }
    }

    public static record Participant(String username, String email) {}

    public static class ChallengeAttemptDAO {
        public void submitAttempt(ChallengeAttempt attempt) throws SQLException {
            String query = "INSERT INTO challenge_attempt (username, Id, answers, scores, time_taken, total_score, total_time_taken) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, attempt.getUsername());
                statement.setInt(2, attempt.getId());
                statement.setString(3, attempt.getAnswers().toString());
                statement.setString(4, attempt.getScores().toString());
                statement.setString(5, attempt.getTimeTaken().toString());
                statement.setInt(6, attempt.getTotalScore());
                statement.setLong(7, attempt.getTotalTimeTaken());

                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int attemptId = generatedKeys.getInt(1);
                        System.out.println("Challenge attempt ID: " + attemptId);
                    }
                }
            }
        }
    }

    public static class ChallengeDAO {
        public List<Challenge> findAllChallenges() {
            List<Challenge> challenges = new ArrayList<>();
            try (Connection connection = getConnection()) {
                String query = "SELECT * FROM challenge";
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    challenges.add(new Challenge(resultSet.getInt("id")));
                }
            } catch (SQLException e) {
                System.err.println("Error finding challenges: " + e.getMessage());
            }
            return challenges;
        }
    }

    public static class QuestionDAO {
        public List<Question> findQuestionsById(int Id) {
            List<Question> questions = new ArrayList<>();
            try (Connection connection = getConnection()) {
                String query = "SELECT * FROM question WHERE Id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, Id);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    questions.add(new Question(
                            resultSet.getInt("Question_id"),
                            resultSet.getInt("Id"),
                            resultSet.getString("question_text"),
                            resultSet.getString("correct_answer")
                    ));
                }
            } catch (SQLException e) {
                System.err.println("Error finding questions: " + e.getMessage());
            }
            return questions;
        }
    }

    public static class ParticipantDAO {
        public Participant findByUsernameAndEmail(String username, String email) {
            try (Connection connection = getConnection()) {
                String query = "SELECT * FROM participants WHERE username = ? AND email = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, username);
                statement.setString(2, email);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    return new Participant(resultSet.getString("username"), resultSet.getString("email"));
                }
            } catch (SQLException e) {
                System.err.println("An error occurred while finding the participant:");
                System.err.println("Username: " + username);
                System.err.println("Email: " + email);
                System.err.println("Error: " + e.getMessage());
            }
            return null;
        }
    }
}