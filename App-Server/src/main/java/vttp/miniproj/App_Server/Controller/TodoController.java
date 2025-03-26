package vttp.miniproj.App_Server.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import vttp.miniproj.App_Server.models.Todo;
import vttp.miniproj.App_Server.services.TodoService;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    
    @Autowired
    private TodoService todoSvc;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getTodo(@PathVariable String userId) {

        List<Todo> todoList = todoSvc.getTodoList(userId);
        
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
      
        for(Todo todo : todoList) {
            arrayBuilder.add(Json.createObjectBuilder()
                        .add("todoId", todo.getTodoId())
                        .add("userId", todo.getUserId())
                        .add("task", todo.getTask())
            );

        }

        JsonArray todoArray = arrayBuilder.build();

        JsonObjectBuilder responseBuilder = Json.createObjectBuilder()
                                            .add("status", "success")
                                            .add("message", "Todos fetched")
                                            .add("data", todoArray);

        JsonObject response = responseBuilder.build();

        return ResponseEntity.ok(response);
        
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> addTodo(@PathVariable String userId, @RequestBody Todo todo) {
        String todoId = UUID.randomUUID().toString();
        todo.setTodoId(todoId);
        todo.setUserId(userId);
        todoSvc.addTodo(todo);
        JsonObject response = Json.createObjectBuilder()
                            .add("message", "Todo added successfully").build();
        
        return ResponseEntity.ok(response);

    }

    @DeleteMapping("{todoId}")
    public ResponseEntity<?> deleteTodo(@PathVariable String todoId){
        todoSvc.deleteTodo(todoId);
        JsonObject response = Json.createObjectBuilder()
                            .add("message", "Todo deleted successfully").build();

        return ResponseEntity.ok(response);
    }

}
