package model;

import java.io.Serializable;

public class Task implements Serializable {
    private String title;
    private String description;
    private User assignedUser;

    public Task(String title) {
        this.title = title;
        this.description = "";
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public User getAssignedUser() { return assignedUser; }
    public void setAssignedUser(User assignedUser) { this.assignedUser = assignedUser; }
}
