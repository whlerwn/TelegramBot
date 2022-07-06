package entity;

public class Report {
    private String chatId;

    private String description;
    private int timeInMinutes;

    public Report() {
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTimeInMinutes() {
        return timeInMinutes;
    }

    public void setTimeInMinutes(int timeInMinutes) {
        this.timeInMinutes = timeInMinutes;
    }

    @Override
    public String toString() {
        return "Report{" +
                "chatId='" + chatId + '\'' +
                ", description='" + description + '\'' +
                ", timeInMinutes=" + timeInMinutes +
                '}';
    }
}
