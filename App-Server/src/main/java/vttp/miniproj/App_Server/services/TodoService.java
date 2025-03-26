package vttp.miniproj.App_Server.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp.miniproj.App_Server.Repository.TodoRepository;
import vttp.miniproj.App_Server.models.Todo;

@Service
public class TodoService {
    
    @Autowired
    private TodoRepository todoRepo;

    public List<Todo> getTodoList(String userId) {
        return todoRepo.findByUserId(userId);
    }

    public void addTodo(Todo todo) {
        String uuid = UUID.randomUUID().toString();
        todo.setTodoId(uuid);
        todoRepo.addTodo(todo);
    }

    public void deleteTodo(String todoId) {
        todoRepo.deleteTodo(todoId);
    }
}
