package router;

import bot.TrackingReportsBot;
import jakarta.jws.WebService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Base64;


@WebService(endpointInterface = "router.RouterServiceImpl")
public class RouterServiceImpl implements RouterService {


    @Override
    public String sendNotification(String untrackedUsers) {
        // ilya 497542778
        // valeria 296732256
        // ramzan 880825037
        // maria 261927286
        // margarita 355086790
        // yaroslav 790376269
        // TODO: change chatId to TEAMLEAD or TEACHER
        String chatId = "880825037";

        TrackingReportsBot trb = new TrackingReportsBot(); // TODO: maybe consider refactoring to static?
        trb.sendMessageToClient(chatId, untrackedUsers);

        return "Success";
    }

    @Override
    public String sendReport(String stream) {

        // TODO: teacher
        String chatId = "880825037";

        // TODO: resolve path
        File file = new File("C:\\Users\\ilya\\IdeaProjects\\TelegramBot\\src\\main\\java\\soap\\report.pdf");

        try {
            byte[] decode = Base64.getDecoder().decode(stream);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(decode);
            bos.close();
        }
        catch (Exception e) {
            System.out.println("ERROR: " + Arrays.toString(e.getStackTrace()));
        }

        TrackingReportsBot trb = new TrackingReportsBot(); // TODO: maybe consider refactoring to static?
        trb.sendFile(file, chatId);

        return "Success";
    }

}
