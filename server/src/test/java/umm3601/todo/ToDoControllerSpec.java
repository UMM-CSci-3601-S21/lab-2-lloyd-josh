package umm3601.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.javalin.core.validation.Validator;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import umm3601.Server;

/**
 * Tests the logic of the ToDoController
 *
 * @throws IOException
 */
public class ToDoControllerSpec {

  private Context ctx = mock(Context.class);

  private ToDoController todoController;
  private static ToDoDatabase db;

  @BeforeEach
  public void setUp() throws IOException {
    ctx.clearCookieStore();

    db = new ToDoDatabase(Server.USER_DATA_FILE);
    todoController = new ToDoController(db);
  }

  @Test
  public void GET_to_request_all_todos() throws IOException {
    // Call the method on the mock controller
    todoController.getToDos(ctx);

    // Confirm that `json` was called with all the todos.
    ArgumentCaptor<ToDo[]> argument = ArgumentCaptor.forClass(ToDo[].class);
    verify(ctx).json(argument.capture());
    assertEquals(db.size(), argument.getValue().length);
  }

  @Test
  public void GET_to_request_todo_with_existent_id() throws IOException {
    when(ctx.pathParam("id", String.class)).thenReturn(new Validator<String>("588935f5c668650dc77df581", "", "id"));
    todoController.getToDo(ctx);
    verify(ctx).status(201);
  }

  @Test
  public void GET_to_request_todo_with_nonexistent_id() throws IOException {
    when(ctx.pathParam("id", String.class)).thenReturn(new Validator<String>("nonexistent", "", "id"));
    Assertions.assertThrows(NotFoundResponse.class, () -> {
      todoController.getToDo(ctx);
    });
  }

  @Test
  public void GET_to_request_todos_with_illegal_limit() {
    // testing entering a limit with a string, which is illegal
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("limit", Arrays.asList(new String[] { "limit" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);
    // This should now throw a `BadRequestResponse` exception because
    // our request has a limit that can't be parsed to a number.
    Assertions.assertThrows(BadRequestResponse.class, () -> {
      todoController.getToDos(ctx);
    });
  }


  // test for checking how many todos contain the string "in occaecat"
  // correct answer should be 3.
  @Test
  public void listToDosWithContainsFilter() throws IOException {
    ToDoDatabase database = new ToDoDatabase("/todos.json");
    Map<String, List<String>> queryParams = new HashMap<>();

    queryParams.put("contains", Arrays.asList(new String[] { "in occaecat" }));
    ToDo[] allTodos = database.listToDos(queryParams);
    assertEquals(3, allTodos.length, "Incorrect number of todos with 'In sint'");

  }
}
