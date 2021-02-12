package umm3601.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * Tests umm3601.todo.Database getToDo functionality
 */
public class GetToDoByIDFromDB {

  @Test
  public void getStokesClayton() throws IOException {
    ToDoDatabase db = new ToDoDatabase("/todos.json");
    ToDo todo = db.getToDo("58895985a22c04e761776d54");
    assertEquals("Blanche", todo.owner, "Incorrect name");
  }

  @Test
  public void getBoltonMonroe() throws IOException {
    ToDoDatabase db = new ToDoDatabase("/todos.json");
    ToDo todo = db.getToDo("58895985c1849992336c219b");
    assertEquals("Fry", todo.owner, "Incorrect name");
  }
}
