package vttp.miniproj.App_Server.Repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp.miniproj.App_Server.models.Todo;

@Repository
public class TodoRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_GET_TODOS="SELECT * FROM todos WHERE user_id = ?";
    private static final String SQL_ADD_TODO = "INSERT INTO todos (todo_id, user_id, content) VALUES(?, ?, ?)";
    private static final String SQL_DELETE_TODO="DELETE FROM todos WHERE todo_id = ?";

    public List<Todo> findByUserId(String userId) {

        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_TODOS, userId);
        List<Todo> todoList = new ArrayList<>();

        while(rs.next()) {
            Todo todo = new Todo();
            todo.setTodoId(rs.getString("todo_id"));
            todo.setUserId(rs.getString("user_id"));
            todo.setTask(rs.getString("task"));

            todoList.add(todo);
        }

        return todoList;
    }

    public void addTodo(Todo todo) {

        jdbcTemplate.update(SQL_ADD_TODO, todo.getTodoId(), todo.getUserId(), todo.getTask());
    }

    public void deleteTodo(String todoId) {

        jdbcTemplate.update(SQL_DELETE_TODO, todoId);
    }
    
}
