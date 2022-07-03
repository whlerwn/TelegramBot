package entity;

public class User {
    private String chatId;
    private String username;
    private String group;
    private String role;

    public User() {
    }

    public User(String chatId, String username) {
        this.chatId = chatId;
        this.username = username;
    }

    public User(String chatId, String username, String group, String role) {
        this.chatId = chatId;
        this.username = username;
        this.group = group;
        this.role = role;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "chatId=" + chatId +
                ", username='" + username + '\'' +
                ", group='" + group + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
