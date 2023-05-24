package router;

import com.google.gson.Gson;
import entity.Report;
import entity.User;
import soap.CommandImplService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;

public class Dispatcher {

    /**
     * Sends user's data to command service
     *
     * @param client - user entity
     */
    public static void dispatchUser(User client) {
        soap.Command commandI = new CommandImplService().getCommandImplPort();

        soap.User user = new soap.User();
        user.setChatId(client.getChatId());
        user.setFullName(client.getUsername());
        user.setGroup(client.getGroup());

        commandI.saveUser(user, client.getRole());
        System.out.println("Sent user.");
    }

    /**
     * Sends user's report to accountant service
     *
     * @param report - telegram report
     */
    public static void dispatchReport(Report report) {
        String responseJson = new Gson().toJson(report);

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofByteArray(responseJson.getBytes()))
                // TODO: change ACCOUNTANT address
                .uri(URI.create("https://4c8f-5-101-22-143.eu.ngrok.io/accountant/tasks"))
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            System.out.println("I/O error." + Arrays.toString(e.getStackTrace()));
        } catch (InterruptedException e) {
            System.out.println("Interrupted." + Arrays.toString(e.getStackTrace()));
        }
        System.out.println("Sent report.");
    }
}
