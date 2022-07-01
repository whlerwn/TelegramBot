package bot;

import entity.Command;
import entity.Role;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.CommandModeService;
import service.RoleModeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TrackingReportsBot extends TelegramLongPollingBot {

    private final RoleModeService roleModeService = RoleModeService.getInstance();
    private final CommandModeService commandModeService = CommandModeService.getInstance();

    @Override
    public String getBotUsername() {
        return "TrackingReportsBot";
    }

    @Override
    public String getBotToken() {
        return "5459953599:AAGzJmz5IZEaNcTAADcSxY3fhDy0PQwk74c";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            handleMessage(update.getMessage());
        } else if (update.hasCallbackQuery() && afterStartHandleCallback(update.getCallbackQuery())) {
             try {
                 startHandleCallback(update.getCallbackQuery());
             } catch (TelegramApiException e) {
                 e.printStackTrace();
             }
         } else if (update.hasCallbackQuery() && afterRegistrationHandleCallback(update.getCallbackQuery())) {
            try {
                registrationHandleCallback(update.getCallbackQuery());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean afterStartHandleCallback(CallbackQuery callbackQuery) {
        Role role = Role.valueOf(callbackQuery.getData());
        return role == Role.ADMIN || role == Role.CLIENT;
    }

    private boolean afterRegistrationHandleCallback(CallbackQuery callbackQuery) {
        Role role = Role.valueOf(callbackQuery.getData());
        return role == Role.TEACHER || role == Role.LEAD || role == Role.STUDENT;
    }

    private void registrationHandleCallback(CallbackQuery callbackQuery) throws TelegramApiException {
        Message message = callbackQuery.getMessage();
        Role newRole = Role.valueOf(callbackQuery.getData());
        // TODO: добавить роль КЛИЕНТА в базу
        if (newRole == Role.TEACHER) {
            execute(SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text("На этом этапе регистрация лектора завершена. Клиент *Имя Фамилия* теперь учитель. \uD83D\uDC68\u200D\uD83C\uDFEB")
                    .build());
        } else if (newRole == Role.LEAD) {
            execute(SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text("Роль присвоена. Теперь *Имя Фамилия* - лид. Не забудь добавить группу!")
                    .build());
        } else if (newRole == Role.STUDENT) {
            execute(SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text("Роль присвоена. Теперь *Имя Фамилия* - студент. Не забудь добавить группу!")
                    .build());
        }
    }

    private void startHandleCallback(CallbackQuery callbackQuery) throws TelegramApiException {
        Message message = callbackQuery.getMessage();
        Role newRole = Role.valueOf(callbackQuery.getData());
        roleModeService.setRole(message.getChatId(), newRole);
        if (newRole == Role.ADMIN) {
            //TODO: добавить роль АДМИНА в базу данных
            execute(SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text("""
                            Теперь ты админ. Чтобы задать своё имя, используй команду /setadminname.
                            Как зарегистрировать клиента (выполнить поэтапно):
                            1. Команда /registration и ввод уникального номера клиента (получить при запуске бота клиентом)
                            2. Команда /setusername для ввода имени клиента
                            3. Команда /setrole для присвоения роли клиенту
                            4. Команда /setgroup для присвоения группы клиенту.
                            После этого клиент будет зарегистрирован.

                            *Остальные команды и их описание можно найти в меню или с помощью символа "/".""")
                    .build());
            System.out.println(newRole);
        } else if (newRole == Role.CLIENT) {
            execute(SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text("Твой уникальный код: " + message.getChatId().toString() +
                            ". Передай его администратору для регистрации.")
                    .build());
            System.out.println(newRole);
        }
    }

    private void handleMessage(Message message) {
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity = message.getEntities().stream()
                    .filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                String command = message.getText().substring
                        (commandEntity.get().getOffset(), commandEntity.get().getLength());
                switch (command) {
                    case "/start" -> {
                        caseStart(message);
                        return;
                    }
                    case "/setadminname" -> {
                        caseSetAdminName(message);
                        return;
                    }
                    case "/registration" -> {
                        caseRegistration(message);
                        return;
                    }
                    case "/setusername" -> {
                        caseSetUserName(message);
                        return;
                    }
                    case "/setrole" -> {
                        caseSetRole(message);
                        return;
                    }
                    case "/setgroup" -> {
                        caseSetGroup(message);
                        return;
                    }
                }
            }
        } else if (message.hasText() && !message.hasEntities() &&
                commandModeService.getCommand(message.getChatId()) == Command.SETADMINNAME) {
            actionSetAdminName(message);
        } else if (message.hasText() && !message.hasEntities()
                && commandModeService.getCommand(message.getChatId()) == Command.REGISTRATION) {
            actionRegistration(message);
        } else if (message.hasText() && !message.hasEntities()
                && commandModeService.getCommand(message.getChatId()) == Command.SETUSERNAME) {
            actionSetUserName(message);
        } else if (message.hasText() && !message.hasEntities()
                && commandModeService.getCommand(message.getChatId()) == Command.SETGROUP) {
            actionSetGroup(message);
        }
    }

    private void actionSetGroup(Message message) {
        //TODO: закинуть группу КЛИЕНТА в базу данных
        try {
            execute(SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text("На этом этапе регистрация клиента завершена. Сейчас напишу об этом клиенту!")
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void actionSetUserName(Message message) {
        //TODO: закинуть имя и фамилию КЛИЕНТА в базу данных
        try {
            execute(SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text("Записал! Идём дальше.")
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void actionRegistration(Message message) {
        //TODO: проверить, есть ли chatID КЛИЕНТА в базе
        try {
            execute(SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text("Пользователь с ID " + message.getText() + " найден. Можно регистрировать дальше.")
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void actionSetAdminName(Message message) {
        //TODO: закинуть имя и фамилию АДМИНА в базу данных
        try {
            execute(SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text("Отлично, " + message.getText() +", я запомнил твоё имя. \uD83D\uDC4D\uD83C\uDFFB")
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void caseSetGroup(Message message) {
        commandModeService.setCommand(message.getChatId(), Command.SETGROUP);
        try {
            execute(
                    SendMessage.builder()
                            .chatId(message.getChatId().toString())
                            .text("Введи название группы:")
                            .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void caseSetRole(Message message) {
        commandModeService.setCommand(message.getChatId(), Command.SETROLE);
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(Arrays.asList(
                InlineKeyboardButton.builder()
                        .text(Role.TEACHER.toString())
                        .callbackData("TEACHER")
                        .build(),
                InlineKeyboardButton.builder()
                        .text(Role.LEAD.toString())
                        .callbackData("LEAD")
                        .build(),
                InlineKeyboardButton.builder()
                        .text(Role.STUDENT.toString())
                        .callbackData("STUDENT")
                        .build()));
        try {
            execute(
                    SendMessage.builder()
                            .text("Выбери роль клиента:")
                            .chatId(message.getChatId().toString())
                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                            .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void caseSetUserName(Message message) {
        commandModeService.setCommand(message.getChatId(), Command.SETUSERNAME);
        try {
            execute(
                    SendMessage.builder()
                            .chatId(message.getChatId().toString())
                            .text("Введи Имя и Фамилию клиента:")
                            .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void caseRegistration(Message message) {
        commandModeService.setCommand(message.getChatId(), Command.REGISTRATION);
        try {
            execute(
                    SendMessage.builder()
                            .chatId(message.getChatId().toString())
                            .text("Введи уникальный код клиента:")
                            .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void caseSetAdminName(Message message) {
        commandModeService.setCommand(message.getChatId(), Command.SETADMINNAME);
        try {
            execute(
                    SendMessage.builder()
                            .chatId(message.getChatId().toString())
                            .text("Введи Имя и Фамилию:")
                            .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void caseStart (Message message) {
        //TODO: добавить chatId в базу
        commandModeService.setCommand(message.getChatId(), Command.START);
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(Arrays.asList(
                InlineKeyboardButton.builder()
                        .text(Role.ADMIN.toString())
                        .callbackData("ADMIN")
                        .build(),
                InlineKeyboardButton.builder()
                        .text(Role.CLIENT.toString())
                        .callbackData("CLIENT")
                        .build()));
        try {
            execute(
                    SendMessage.builder()
                            .text("Выбери роль:")
                            .chatId(message.getChatId().toString())
                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                            .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
