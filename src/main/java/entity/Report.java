package entity;

public class Report {
    private String chatId;

    // TODO: change text -> description
    private String text;

    //TODO: uncomment
    // private int timeInMinutes;

    //TODO: generate getters and setters
    public Report() {
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Report{" +
                "chatId='" + chatId + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
