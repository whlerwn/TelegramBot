package router;

import bot.TrackingReportsBot;
import jakarta.jws.WebService;

import java.io.*;
import java.util.Arrays;
import java.util.Base64;

@WebService(endpointInterface = "router.RouterServiceImpl")
public class RouterServiceImpl implements RouterService {

    @Override
    public String sendNotification(String untrackedUsers) {
        // ilya 497542778
        // valeria 296732256
        // ramzan 880825037 - teamlead
        // maria 261927286
        // margarita 355086790
        // yaroslav 790376269
        // konstantin 670159425 - teacher
        // TODO: HARDCODED, change chatId to TEAMLEAD
        //       either in bot(call database) or notificator(passed parameter)
        String chatId = "497542778";

        TrackingReportsBot telegramBot = new TrackingReportsBot();
        telegramBot.sendMessageToClient(chatId, untrackedUsers);

        System.out.println("Received notification.");

        return "Success";
    }

    @Override
    public String sendReport(String stream) {
        File file = saveIncomingFile(stream);
        // TODO: HARDCODED, change chatId to TEACHER
        String chatId = "497542778";

        TrackingReportsBot telegramBot = new TrackingReportsBot();
        telegramBot.sendFile(file, chatId);

        System.out.println("Received report.");

        return "Success";
    }

    private File saveIncomingFile(String stream) {
        // TODO: check path correctness
        File file = new File("src/main/java/soap/report.pdf");

        byte[] decode = Base64.getDecoder().decode(stream);
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));) {
            bos.write(decode);
        } catch (FileNotFoundException e) {
            System.out.println("File not found error." + Arrays.toString(e.getStackTrace()));
        } catch (IOException e) {
            System.out.println("I/O error." + Arrays.toString(e.getStackTrace()));
        }
        return file;
    }

}
