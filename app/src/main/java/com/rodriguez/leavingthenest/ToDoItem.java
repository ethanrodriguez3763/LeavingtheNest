package com.rodriguez.leavingthenest;

/**
 * Class is the underlying model for the list of To Do items in
 * ToDoListActivity
 */
public class ToDoItem {
    private int id;
    private String title;
    private String description;
    private int importance;

    public ToDoItem() {}

    public ToDoItem(String title, String description, int importance) {
        this.title = title;
        this.description = description;
        this.importance = importance;
    }

    public ToDoItem(int id, String title, String description, int importance) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.importance = importance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }
}
