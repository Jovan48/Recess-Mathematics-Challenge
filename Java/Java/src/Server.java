import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Server {
    private static final int INACTIVITY_LIMIT = 30 * 60 * 1000; // 30 minutes in milliseconds
    private static Timer inactivityTimer;

    public static void main(String[] args) {
        Participant.loadExistingRegistrations();
        startServer();
    }

    private static void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server started on port 12345.");
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    startInactivityTimer(); // Ensure timer starts when a new client connects
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);

                    String input;
                    while ((input = in.readLine()) != null) {
                        resetInactivityTimer();
                        processClientRequest(input, out);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error starting server: " + e.getMessage());
        }
    }

    private static void processClientRequest(String input, PrintWriter out) {
        String[] parts = input.split(" ", 2);
        String command = parts[0];
        String data = parts.length > 1 ? parts[1] : "";

        switch (command.toLowerCase()) {
            case "register":
                Participant.registerParticipant(data, out);
                break;
            case "login":
                String[] loginDetails = data.split(" ");
                if (loginDetails.length == 2) {
                    SchoolRepresentative.loginSchoolRepresentative(loginDetails[0], loginDetails[1], out);
                } else {
                    out.println("Invalid login details. Please try again.");
                }
                break;
            case "viewapplicants":
                System.out.println("debufjfjfjffkkfkfkfkkfk");
                SchoolRepresentative.viewApplicants(out);
                System.out.println("debufjfjfjffkkfkfkfkkfk");
                break;
            case "confirm":


            case "reject":
                System.out.println(data);
                String[] applicantDetails = data.split(",");
                if (applicantDetails.length == 1) {
                    SchoolRepresentative.processApplicant(command, applicantDetails[0], out);
                } else {
                    out.println("Invalid command. Please use 'confirm <username>' or 'reject <username>'.");
                }
                break;
            default:
                out.println("Unknown command. Please try again.");
        }
    }

    private static void startInactivityTimer() {
        if (inactivityTimer != null) {
            inactivityTimer.cancel();
        }
        inactivityTimer = new Timer(true);
        inactivityTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Session timed out due to inactivity. Exiting the system.");
                System.exit(0);
            }
        }, INACTIVITY_LIMIT, INACTIVITY_LIMIT);
    }

    private static void resetInactivityTimer() {
        if (inactivityTimer != null) {
            inactivityTimer.cancel();
        }
        startInactivityTimer();
    }
}