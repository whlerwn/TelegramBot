package bot;

import entity.Command;
import entity.Report;
import entity.Role;
import entity.User;
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
import service.ReportService;
import service.RoleModeService;
import service.UserService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TrackingReportsBot extends TelegramLongPollingBot {

    private final RoleModeService roleModeService = RoleModeService.getInstance();
    private final CommandModeService commandModeService = CommandModeService.getInstance();
    private final ReportService reportService = ReportService.getInstance();
    private final UserService userService = UserService.getInstance();

    private User admin = new User();
    private User client = new User();
    private Report report = new Report();

    /**
     * Данный метод нужен для того, чтобы сконнектиться с телеграм-ботом
     * @return
     */
    @Override
    public String getBotUsername() {
        return "TrackingReportsBot";
    }

    /**
     * Данный метод нужен для того, чтобы сконнектиться с телеграм-ботом
     * @return
     */
    @Override
    public String getBotToken() {
        return "5459953599:AAGzJmz5IZEaNcTAADcSxY3fhDy0PQwk74c";
    }

    /**
     * Главный метод телеграм-бота
     * @param update любое действие пользователя в телеграм-боте
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {

            handleMessage(update.getMessage());

        } else if (update.hasCallbackQuery() && afterGroupHandleCallback(update.getCallbackQuery())) {

            handleCallback(update.getCallbackQuery());

        } else if (update.hasCallbackQuery() && afterStartHandleCallback(update.getCallbackQuery())) {

            startHandleCallback(update.getCallbackQuery());

         } else if (update.hasCallbackQuery() && afterRegistrationHandleCallback(update.getCallbackQuery())) {
            registrationHandleCallback(update.getCallbackQuery());

        }
    }

    /**
     * Данный метод проверяет, была ли нажата кнопка BLUE при присвоении группы.
     * @param callbackQuery вызов нажатия кнопки
     * @return true, если кнопка была нажата
     */
    private boolean afterGroupHandleCallback(CallbackQuery callbackQuery) {
        return callbackQuery.getData().equals("BLUE");
    }

    /**
     * Данный метод проверяет, была ли нажата кнопка ADMIN или CLIENT при выборе группы.
     * @param callbackQuery вызов нажатия кнопки
     * @return true, если кнопка была нажата
     */
    private boolean afterStartHandleCallback(CallbackQuery callbackQuery) {
        Role role = Role.valueOf(callbackQuery.getData());
        return role == Role.ADMIN || role == Role.CLIENT;
    }

    /**
     * Данный метод проверяет, была ли нажата кнопка TEACHER, LEAD или STUDENT при присвоении роли.
     * @param callbackQuery вызов нажатия кнопки
     * @return true, если кнопка была нажата
     */
    private boolean afterRegistrationHandleCallback(CallbackQuery callbackQuery) {
        Role role = Role.valueOf(callbackQuery.getData());
        return role == Role.TEACHER || role == Role.LEAD || role == Role.STUDENT;
    }

    /**
     * Данный метод - действие бота после нажатия кнопки BLUE: <br>
     * На данном этапе регистрация лида или студента заканчивается - информация о пользователях передаётся в базу данных <br>
     * Телеграм-бот уведомляет лида или студента о том, что админ их зарегистрировал
     * @param callbackQuery вызов нажатия кнопки
     */
    private void handleCallback(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        userService.setGroup(client, "BLUE");
        simpleBotAnswer(message, "На этом этапе регистрация клиента завершена. Сейчас напишу об этом клиенту!");
        if (client.getRole().equals("LEAD")) {
            sendMessageToClient(client.getChatId(),
                    "Привет, " + client.getUsername() + ", ты зарегистрирован как лид. " +
                            "Теперь ты можешь отправлять каждый день отчёт, используя команду /setreport. " +
                            "А если студент не затрекался в течение 24 часов, я уведомлю тебя об этом.");
        } else if (client.getRole().equals("STUDENT")) {
            sendMessageToClient(client.getChatId(),
                    "Привет, " + client.getUsername() + ", ты зарегистрирован как студент. " +
                            "Теперь ты можешь отправлять каждый день отчёт, используя команду /setreport.");
        }
        System.out.println(client.toString());
        System.out.println(admin.toString());
    }

    /**
     * Данный метод - действие бота после нажатия кнопки TEACHER, LEAD или STUDENT:<br>
     * На данном этапе присваивается роль клиенту
     * @param callbackQuery вызов нажатия кнопки
     */
    private void registrationHandleCallback(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        Role newRole = Role.valueOf(callbackQuery.getData());
        if (newRole == Role.TEACHER) {
            simpleBotAnswer(message, "На этом этапе регистрация лектора завершена. " +
                    "Клиент " + client.getUsername() + " теперь лектор. \uD83D\uDC68\u200D\uD83C\uDFEB" +
                    "\nСейчас я напишу лектору об этом.");
            userService.setRole(client, Role.TEACHER.toString());
            sendMessageToClient(client.getChatId(), "Привет, " + client.getUsername() + ", ты зарегистрирован как лектор! " +
                    "Теперь тебе будет приходить каждый день в 22:00 отчёт об активностях студентов. " +
                    "А если студент не затрекался в течение 3 дней, я уведомлю тебя об этом.");
            System.out.println(client.toString());
        } else if (newRole == Role.LEAD) {
            simpleBotAnswer(message, "Роль присвоена. Теперь " + client.getUsername() + " - лид. \uD83E\uDDD1\uD83C\uDFFB\u200D\uD83D\uDCBB Не забудь добавить группу!");
            userService.setRole(client, Role.LEAD.toString());
        } else if (newRole == Role.STUDENT) {
            simpleBotAnswer(message, "Роль присвоена. Теперь " + client.getUsername() + " - студент. \uD83E\uDDD1\uD83C\uDFFB\u200D\uD83D\uDCBB Не забудь добавить группу!");
            userService.setRole(client, Role.STUDENT.toString());
        }
    }

    /**
     * При вызове данного метода телеграм-бот отправляет сообщение выбранному пользователю
     * @param chatId уникальный номер пользователя, которому бот будет отправлять сообщение
     * @param string сообщение, которое будет отправлено пользователю
     */
    private void sendMessageToClient(String chatId, String string) {
        try {
            execute(
                    SendMessage.builder()
                            .chatId(chatId)
                            .text(string)
                            .build()
            );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Данный метод - действие бота после нажатия кнопки ADMIN или CLIENT:<br>
     * На данном этапе бот уведомляет админа о том, какие действия от него требуются <br>
     * Клиенту приходит сообщение с его уникальным номером, который необходимо передать админу
     * @param callbackQuery вызов нажатия кнопки
     */
    private void startHandleCallback(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        Role newRole = Role.valueOf(callbackQuery.getData());
        roleModeService.setRole(message.getChatId(), newRole);
        System.out.println(message.getChatId());
        if (newRole == Role.ADMIN) {
            simpleBotAnswer(message, """
                            Теперь ты админ. Чтобы задать своё имя, используй команду /setadminname.   
                            Как зарегистрировать клиента (выполнить поэтапно):
                            1. Команда /registration и ввод уникального номера клиента (получить при запуске бота клиентом)
                            2. Команда /setusername для ввода имени клиента
                            3. Команда /setrole для присвоения роли клиенту
                            4. Команда /setgroup для присвоения группы клиенту.
                            После этого клиент будет зарегистрирован.

                            *Остальные команды и их описание можно найти в меню или с помощью символа "/".""");
            System.out.println(newRole);
            userService.setChatId(admin, String.valueOf(message.getChatId()));
            userService.setRole(admin, Role.ADMIN.toString());
        } else if (newRole == Role.CLIENT) {
            simpleBotAnswer(message, "Твой уникальный код: " + message.getChatId().toString() +
                    ". Передай его администратору для регистрации.");
            System.out.println(newRole);
            userService.setChatId(client, String.valueOf(message.getChatId()));
        }
    }

    /**
     * Метод обрабатывает сообщения, которые получает бот <br>
     * Поддерживается функция приема текстового сообщения и команд, прописанных в BotFather
     * @param message сообщения, которые приходят боту
     */
    private void handleMessage(Message message) {
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity = message.getEntities().stream()
                    .filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                String command = message.getText().substring
                        (commandEntity.get().getOffset(), commandEntity.get().getLength());
                switch (command) {
                    case "/start" ->
                        caseStart(message);
                    case "/setadminname" ->
                        botAnswerWithCommand(message,"Введи Имя и Фамилию:", Command.SETADMINNAME);
                    case "/registration" ->
                        botAnswerWithCommand(message, "Введи уникальный код клиента:", Command.REGISTRATION);
                    case "/setusername" ->
                        botAnswerWithCommand(message, "Введи Имя и Фамилию клиента:", Command.SETUSERNAME);
                    case "/setrole" ->
                        caseSetRole(message);
                    case "/setgroup" ->
                        caseSetGroup(message);
                    case "/setreport" ->
                        botAnswerWithCommand(message, "Жду трекинг:", Command.SETREPORT);
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
                && commandModeService.getCommand(message.getChatId()) == Command.SETREPORT) {
            actionSetReport(message);
        }
    }

    /**
     * Метод сохраняет отчет (текст и уникальный номер пользователя, кто пишет отчет)
     * @param message обрабатываемое сообщение
     */
    private void actionSetReport(Message message) {
        reportService.setReport(report, message.getChatId().toString() ,message.getText());
        simpleBotAnswer(message, "Отчет сохранён. \uD83D\uDC4C\uD83C\uDFFB");
        System.out.println(report.toString());
    }

    /**
     * Метод записывает имя клиента
     * @param message обрабатываемое сообщение
     */
    private void actionSetUserName(Message message) {
        userService.setUserName(client, message.getText());
        simpleBotAnswer(message, "Записал! Идём дальше.");
    }

    /**
     * Метод записывает уникальный номер клиента
     * @param message обрабатываемое сообщение
     */
    private void actionRegistration(Message message) {
        userService.setChatId(client, subSringChatId(message.getText()));
        simpleBotAnswer(message, "Пользователь с ID " + client.getChatId()
                + " найден. Можно регистрировать дальше.");
    }

    /**
     * Метод нужен для того, чтобы в корректном формате получить уникальный номер пользователя
     * @param string текст сообщения боту
     * @return текст без пробелов
     */
    private String subSringChatId(String string) {
        String[] sub = string.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String s : sub) {
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * Метод записывает имя админа
     * @param message обрабатываемое сообщение
     */
    private void actionSetAdminName(Message message) {
        userService.setUserName(admin, message.getText());
        simpleBotAnswer(message, "Отлично, " + admin.getUsername() +", я запомнил твоё имя. \uD83D\uDC4D\uD83C\uDFFB");
    }

    /**
     * После выбора команды /setgroup метод отправляет сообщение от бота и кнопку BLUE
     * @param message обрабатываемое сообщение
     */
    private void caseSetGroup(Message message) {
        commandModeService.setCommand(message.getChatId(), Command.SETGROUP);
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text("BLUE")
                        .callbackData("BLUE")
                        .build()));
        try {
            execute(
                    SendMessage.builder()
                            .text("Выбери группу:")
                            .chatId(message.getChatId().toString())
                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                            .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * После выбора команды /setrole метод отправляет сообщение от бота и кнопки TEACHER, LEAD и STUDENT
     * @param message обрабатываемое сообщение
     */
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

    /**
     * После выбора команды /start метод отправляет сообщение от бота и кнопки ADMIN и CLIENT
     * @param message обрабатываемое сообщение
     */
    private void caseStart (Message message) {
        System.out.println(message.getChatId());
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

    /**
     * Метод отправляет сообщение от бота и задает команду
     * @param message обрабатываемое сообщение
     * @param text текст ответа
     * @param command команда, используемая в момент отправки сообщения
     */
    private void botAnswerWithCommand(Message message, String text, Command command) {
        commandModeService.setCommand(message.getChatId(), command);
        try {
            execute(
                    SendMessage.builder()
                            .chatId(message.getChatId().toString())
                            .text(text)
                            .build()
            );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод отправляет сообщение от бота
     * @param message обрабатываемое сообщение
     * @param text текст ответа
     */
    private void simpleBotAnswer(Message message, String text) {
        try {
            execute(
                    SendMessage.builder()
                            .chatId(message.getChatId().toString())
                            .text(text)
                            .build()
            );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
