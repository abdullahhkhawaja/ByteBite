package services;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;

import java.net.URI;
import java.util.Date;

public class SQSService {

    private static final String SQS_QUEUE_URL = "http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/slack-notification-queue";  // LocalStack endpoint
    private static final String ACCESS_KEY = "79";
    private static final String SECRET_KEY = "79";

    private AmazonSQS sqsClient;

    public SQSService() {
        sqsClient = AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY)))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", "us-east-1"))
                .build();
    }

    public void sendMessageToSQS(String message) {
        try {
            SendMessageRequest sendMessageRequest = new SendMessageRequest()
                    .withQueueUrl(SQS_QUEUE_URL)
                    .withMessageBody(message)
                    .withDelaySeconds(1);

            sqsClient.sendMessage(sendMessageRequest);
            System.out.println("Message sent to SQS: " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receiveMessageFromSQS() {
        try {
            Thread.sleep(2000);
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(SQS_QUEUE_URL);
            receiveMessageRequest.setMaxNumberOfMessages(1);

            ReceiveMessageResult result = sqsClient.receiveMessage(receiveMessageRequest);

            result.getMessages().forEach(message -> {
                String body = message.getBody();
                System.out.println("Received message: " + body);
                try {
                    SlackNotificationService.sendNotificationToSlack(body);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
