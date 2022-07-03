package entity;

public class Report {
    private String chatId;
    private String text;

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
