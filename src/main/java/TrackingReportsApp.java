import bot.TrackingReportsBot;
import jakarta.xml.ws.Endpoint;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import router.RouterServiceImpl;

public class TrackingReportsApp {
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TrackingReportsBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
       Endpoint.publish("http://localhost:8081/", new RouterServiceImpl());
    }
}
