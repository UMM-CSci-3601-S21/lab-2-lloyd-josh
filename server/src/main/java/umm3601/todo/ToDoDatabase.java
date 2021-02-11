package umm3601.todo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

/**
 * A fake "database" of ToDos
 * <p>
 * Since we don't want to complicate this lab with a real database, we're going
 * to instead just read a bunch of todo data from a specified JSON file, and
 * then provide various database-like methods that allow the `ToDoController` to
 * "query" the "database".
 */
public class ToDoDatabase {

  private ToDo[] allToDos;

  public ToDoDatabase(String todoDataFile) throws IOException {
    Gson gson = new Gson();
    InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(todoDataFile));
    allToDos = gson.fromJson(reader, ToDo[].class);
  }

  public int size() {
    return allToDos.length;
  }

  /**
   * Get the single todo specified by the given ID. Return `null` if there is no
   * todo with that ID.
   *
   * @param id the ID of the desired todo
   * @return the todo with the given ID, or null if there is no todo with that ID
   */
  public ToDo getToDo(String id) {
    return Arrays.stream(allToDos).filter(x -> x._id.equals(id)).findFirst().orElse(null);
  }

  /**
   * Get an array of all the todos satisfying the queries in the params.
   *
   * @param queryParams map of key-value pairs for the query
   * @return an array of all the todos matching the given criteria
   */
  public ToDo[] listToDos(Map<String, List<String>> queryParams) {
    ToDo[] filteredToDos = allToDos;

    return filteredToDos;
  }
}
