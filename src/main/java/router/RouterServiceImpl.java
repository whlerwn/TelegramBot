package router;

import bot.TrackingReportsBot;
import jakarta.jws.WebService;


@WebService(endpointInterface = "router.RouterServiceImpl")
public class RouterServiceImpl implements RouterService {


    @Override
    public String sendNotification(String untrackedUsers) {
        // ilya 497542778
        // valeria 296732256
        // ramzan 880825037
        // TODO: change chatId to TEAMLEAD or TEACHER
        String chatId = "880825037";

        TrackingReportsBot trb = new TrackingReportsBot(); // TODO: maybe consider refactoring to static?
        trb.sendMessageToClient(chatId, untrackedUsers);

        return "Success";
    }

}
