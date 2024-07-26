import java.sql.*;
import java.util.*;

public class DataAnalysis {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/recess-r";
    private static final String USER = "root";
    private static final String PASS = "";

    public static void main(String[] args) {
        DataAnalysis analysis = new DataAnalysis();
        analysis.performAnalysis();
    }

    public void performAnalysis() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            List<ChallengeAttempt> challengeAttempts = fetchChallengeAttempts(conn);
            Map<String, Object> analysisResults = analyzeData(challengeAttempts);
            saveAnalysisResults(conn, analysisResults);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<ChallengeAttempt> fetchChallengeAttempts(Connection conn) throws SQLException {
        List<ChallengeAttempt> attempts = new ArrayList<>();
        String query = "SELECT * FROM challenge_attempts";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                ChallengeAttempt attempt = new ChallengeAttempt(
                        rs.getInt("attempt_id"),
                        rs.getString("username"),
                        rs.getInt("Id"),
                        rs.getString("answers"),
                        rs.getString("scores"),
                        rs.getString("time_taken"),
                        rs.getInt("total_score"),
                        rs.getLong("total_time_taken"),
                        rs.getTimestamp("attempt_date"),
                        rs.getString("school_name"),
                        rs.getInt("year_of_attempt")
                );
                attempts.add(attempt);
            }
        }
        return attempts;
    }

    private Map<String, Object> analyzeData(List<ChallengeAttempt> challengeAttempts) {
        Map<String, Object> analysisResults = new HashMap<>();

        // Most correctly answered questions
        String mostCorrectlyAnsweredQuestions = computeMostCorrectlyAnsweredQuestions(challengeAttempts);
        analysisResults.put("most_correctly_answered_questions", mostCorrectlyAnsweredQuestions);

        // School Rankings
        String schoolRankings = computeSchoolRankings(challengeAttempts);
        analysisResults.put("school_rankings", schoolRankings);

        // Performance of schools and participants
        String performanceSchoolsParticipants = computePerformanceSchoolsParticipants(challengeAttempts);
        analysisResults.put("performance_schools_participants", performanceSchoolsParticipants);

        // Percentage repetition of questions
        double percentageRepetitionQuestions = computePercentageRepetitionQuestions(challengeAttempts);
        analysisResults.put("percentage_repetition_questions", percentageRepetitionQuestions);

        // Worst performing schools for a given challenge
        String worstPerformingSchools = computeWorstPerformingSchools(challengeAttempts);
        analysisResults.put("worst_performing_schools", worstPerformingSchools);

        // Best performing schools for all challenges
        String bestPerformingSchools = computeBestPerformingSchools(challengeAttempts);
        analysisResults.put("best_performing_schools", bestPerformingSchools);

        // Participants with incomplete challenges
        String participantsIncompleteChallenges = computeParticipantsIncompleteChallenges(challengeAttempts);
        analysisResults.put("participants_incomplete_challenges", participantsIncompleteChallenges);

        // Best two participants
        String bestTwoParticipants = computeBestTwoParticipants(challengeAttempts);
        analysisResults.put("best_two_participants", bestTwoParticipants);

        // Performance of schools and participants over years
        String performanceOverYears = computePerformanceOverYears(challengeAttempts);
        analysisResults.put("performance_over_years", performanceOverYears);

        return analysisResults;
    }

    private void saveAnalysisResults(Connection conn, Map<String, Object> analysisResults) throws SQLException {
        String insertQuery = "INSERT INTO analysis (most_correctly_answered_questions, school_rankings, performance_schools_participants, " +
                "percentage_repetition_questions, worst_performing_schools, best_performing_schools, participants_incomplete_challenges, best_two_participants, performance_over_years) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setString(1, (String) analysisResults.get("most_correctly_answered_questions"));
            pstmt.setString(2, (String) analysisResults.get("school_rankings"));
            pstmt.setString(3, (String) analysisResults.get("performance_schools_participants"));
            pstmt.setDouble(4, (double) analysisResults.get("percentage_repetition_questions"));
            pstmt.setString(5, (String) analysisResults.get("worst_performing_schools"));
            pstmt.setString(6, (String) analysisResults.get("best_performing_schools"));
            pstmt.setString(7, (String) analysisResults.get("participants_incomplete_challenges"));
            pstmt.setString(8, (String) analysisResults.get("best_two_participants"));
            pstmt.setString(9, (String) analysisResults.get("performance_over_years"));
            pstmt.executeUpdate();
        }
    }

    private String computeMostCorrectlyAnsweredQuestions(List<ChallengeAttempt> attempts) {
        Map<String, Integer> questionCorrectCount = new HashMap<>();
        for (ChallengeAttempt attempt : attempts) {
            String[] answers = attempt.answers.split(",");
            String[] scores = attempt.scores.split(",");
            for (int i = 0; i < answers.length; i++) {
                if (scores[i].equals("1")) { // Assuming a score of "1" indicates a correct answer
                    questionCorrectCount.put(answers[i], questionCorrectCount.getOrDefault(answers[i], 0) + 1);
                }
            }
        }
        return questionCorrectCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No data");
    }

    private String computeSchoolRankings(List<ChallengeAttempt> attempts) {
        Map<String, Integer> schoolScores = new HashMap<>();
        for (ChallengeAttempt attempt : attempts) {
            String school = attempt.school_name;
            schoolScores.put(school, schoolScores.getOrDefault(school, 0) + attempt.total_score);
        }
        return schoolScores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .reduce((a, b) -> a + ", " + b)
                .orElse("No data");
    }

    private String computePerformanceSchoolsParticipants(List<ChallengeAttempt> attempts) {
        Map<String, List<Integer>> schoolPerformance = new HashMap<>();
        for (ChallengeAttempt attempt : attempts) {
            String school = attempt.school_name;
            schoolPerformance.putIfAbsent(school, new ArrayList<>());
            schoolPerformance.get(school).add(attempt.total_score);
        }

        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, List<Integer>> entry : schoolPerformance.entrySet()) {
            int totalScore = entry.getValue().stream().mapToInt(Integer::intValue).sum();
            int count = entry.getValue().size();
            double averageScore = (double) totalScore / count;
            result.append(entry.getKey()).append(": Average Score = ").append(String.format("%.2f", averageScore)).append("\n");
        }
        return result.toString();
    }

    private double computePercentageRepetitionQuestions(List<ChallengeAttempt> attempts) {
        Map<String, Integer> questionRepetitionCount = new HashMap<>();
        int totalQuestions = 0;
        for (ChallengeAttempt attempt : attempts) {
            String[] answers = attempt.answers.split(",");
            for (String answer : answers) {
                totalQuestions++;
                questionRepetitionCount.put(answer, questionRepetitionCount.getOrDefault(answer, 0) + 1);
            }
        }
        long repeatedQuestions = questionRepetitionCount.values().stream().filter(count -> count > 1).count();
        return (totalQuestions > 0) ? (double) repeatedQuestions / totalQuestions * 100 : 0.0;
    }

    private String computeWorstPerformingSchools(List<ChallengeAttempt> attempts) {
        Map<String, Integer> schoolScores = new HashMap<>();
        for (ChallengeAttempt attempt : attempts) {
            String school = attempt.school_name;
            schoolScores.put(school, schoolScores.getOrDefault(school, 0) + attempt.total_score);
        }
        return schoolScores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .reduce((a, b) -> a + ", " + b)
                .orElse("No data");
    }

    private String computeBestPerformingSchools(List<ChallengeAttempt> attempts) {
        Map<String, Integer> schoolScores = new HashMap<>();
        for (ChallengeAttempt attempt : attempts) {
            String school = attempt.school_name;
            schoolScores.put(school, schoolScores.getOrDefault(school, 0) + attempt.total_score);
        }
        return schoolScores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .reduce((a, b) -> a + ", " + b)
                .orElse("No data");
    }

    private String computeParticipantsIncompleteChallenges(List<ChallengeAttempt> attempts) {
        Set<String> incompleteParticipants = new HashSet<>();
        for (ChallengeAttempt attempt : attempts) {
            if (attempt.answers.contains("NA")) { // Assuming "NA" denotes an incomplete challenge
                incompleteParticipants.add(attempt.username);
            }
        }
        return String.join(", ", incompleteParticipants);
    }

    private String computeBestTwoParticipants(List<ChallengeAttempt> attempts) {
        Map<String, Integer> participantScores = new HashMap<>();
        for (ChallengeAttempt attempt : attempts) {
            participantScores.put(attempt.username, participantScores.getOrDefault(attempt.username, 0) + attempt.total_score);
        }
        return participantScores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(2)
                .map(Map.Entry::getKey)
                .reduce((a, b) -> a + ", " + b)
                .orElse("No data");
    }

    private String computePerformanceOverYears(List<ChallengeAttempt> attempts) {
        Map<Integer, Map<String, Integer>> yearSchoolPerformance = new HashMap<>();
        for (ChallengeAttempt attempt : attempts) {
            int year = attempt.year_of_attempt;
            String school = attempt.school_name;
            yearSchoolPerformance.putIfAbsent(year, new HashMap<>());
            yearSchoolPerformance.get(year).put(school, yearSchoolPerformance.get(year).getOrDefault(school, 0) + attempt.total_score);
        }

        StringBuilder result = new StringBuilder();
        for (Map.Entry<Integer, Map<String, Integer>> yearEntry : yearSchoolPerformance.entrySet()) {
            result.append("Year ").append(yearEntry.getKey()).append(":\n");
            for (Map.Entry<String, Integer> schoolEntry : yearEntry.getValue().entrySet()) {
                result.append("  ").append(schoolEntry.getKey()).append(": Total Score = ").append(schoolEntry.getValue()).append("\n");
            }
        }
        return result.toString();
    }
}

class ChallengeAttempt {
    int attempt_id;
    String username;
    int Id;
    String answers;
    String scores;
    String time_taken;
    int total_score;
    long total_time_taken;
    Timestamp attempt_date;
    String school_name;
    int year_of_attempt;

    public ChallengeAttempt(int attempt_id, String username, int Id, String answers, String scores, String time_taken, int total_score, long total_time_taken, Timestamp attempt_date, String school_name, int year_of_attempt) {
        this.attempt_id = attempt_id;
        this.username = username;
        this.Id = Id;
        this.answers = answers;
        this.scores = scores;
        this.time_taken = time_taken;
        this.total_score = total_score;
        this.total_time_taken = total_time_taken;
        this.attempt_date = attempt_date;
        this.school_name = school_name;
        this.year_of_attempt = year_of_attempt;
    }
}