package router;

import com.google.gson.Gson;
import entity.Report;
import entity.User;
import org.telegram.telegrambots.meta.api.objects.Message;
import soap.CommandImplService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
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
        CommandImplService command = new CommandImplService();
        soap.Command commandI = command.getCommandImplPort();

        soap.User user = new soap.User();
        user.setChatId(client.getChatId());
        user.setFullName(client.getUsername());
        user.setGroup(client.getGroup());

        commandI.saveUser(user, client.getRole());
    }

    /**
     * Sends user's report to accountant service
     *
     * @param report - telegram report
     */
    public static void dispatchReport(Report report) {
        Gson gson = new Gson();

        String responseJson = gson.toJson(report);

        try {
            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofByteArray(responseJson.getBytes()))
                    // TODO: change address
                    .uri(URI.create("https://4c8f-5-101-22-143.eu.ngrok.io/accountant/tasks"))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println("ERROR: " + Arrays.toString(e.getStackTrace()));
        }

    }
}
