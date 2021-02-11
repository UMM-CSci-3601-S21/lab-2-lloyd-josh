package umm3601.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

/**
 * Tests umm3601.user.Database listUser functionality
 */
public class FullToDoListFromDB {

  @Test
  public void totalToDoCount() throws IOException {
    ToDoDatabase db = new ToDoDatabase("/todos.json");
    ToDo[] allToDos = db.listToDos(new HashMap<>());
    assertEquals(300, allToDos.length, "Incorrect total number of users");
  }

  @Test
  public void firstToDoInFullList() throws IOException {
    ToDoDatabase db = new ToDoDatabase("/todos.json");
    ToDo[] allToDos = db.listToDos(new HashMap<>());
    ToDo firstToDo = allToDos[0];
    assertEquals("Blanche", firstToDo.owner, "Incorrect owner");
    assertEquals(false, firstToDo.status, "Incorrect status");
    assertEquals("In sunt ex non tempor cillum commodo amet incididunt anim qui commodo quis. Cillum non labore ex sint esse.", firstToDo.body, "Incorrect body");
    assertEquals("software design", firstToDo.category, "Incorrect category");
  }

}
