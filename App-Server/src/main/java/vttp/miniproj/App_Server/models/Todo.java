package vttp.miniproj.App_Server.models;

public class Todo {
    
    private String todoId;
    private String userId;
    private String task;
    
    public String getTodoId() {
        return todoId;
    }
    public void setTodoId(String todoId) {
        this.todoId = todoId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getTask() {
        return task;
    }
    public void setTask(String task) {
        this.task = task;
    }
    @Override
    public String toString() {
        return "Todo [todoId=" + todoId + ", userId=" + userId + ", task=" + task + "]";
    }
    
}
