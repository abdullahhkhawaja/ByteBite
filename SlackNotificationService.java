package services;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static common.Parameters.SLACK_WEBHOOK_URL;

public class SlackNotificationService {

    public static void sendNotificationToSlack(String message) throws Exception {
        String jsonPayload = "{\"text\": \"" + message + "\"}";

        URL url = new URL(SLACK_WEBHOOK_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new Exception("Failed to send Slack notification, HTTP response code: " + responseCode);
        }
    }

    public static void sendLeaveNotificationToSlack(String userName, Date leaveDate) throws Exception {
        String jsonPayload = "{\"text\": \"" + userName + " will be on leave on " + leaveDate + ".\"}";

        URL url = new URL(SLACK_WEBHOOK_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonPayload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new Exception("Failed to send Slack notification, HTTP response code: " + responseCode);
        }
    }
}
