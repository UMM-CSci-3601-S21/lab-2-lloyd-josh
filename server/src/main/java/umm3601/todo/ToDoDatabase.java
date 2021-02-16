package umm3601.todo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import io.javalin.http.BadRequestResponse;

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

    // limit number of todos, if contained in the query
    if (queryParams.containsKey("limit")) {
      String todoParam = queryParams.get("limit").get(0);
      try {
        int targetLimit = Integer.parseInt(todoParam);
        filteredToDos = limitToDos(filteredToDos, targetLimit);
      } catch (NumberFormatException e) {
        throw new BadRequestResponse("Specified limit '" + todoParam + "' can't be parsed to an integer");
      }
    }

    // code to filter by a string in the body of the todo
    if (queryParams.containsKey("contains")){
      String targetString = queryParams.get("contains").get(0);
      filteredToDos = filterToDosByBody(filteredToDos, targetString);

      }

      if (queryParams.containsKey("status")) {
        String statusParam = queryParams.get("status").get(0);
        if( "complete".equals(statusParam)) {
          filteredToDos = filterToDosByStatus(filteredToDos, true);
        }
        else {
          filteredToDos = filterToDosByStatus(filteredToDos, false);
        }
      }

      if (queryParams.containsKey("owner")) {
        String targetString = queryParams.get("owner").get(0);
        filteredToDos = filterToDosByOwner(filteredToDos, targetString);
      }

      if (queryParams.containsKey("category")) {
        String targetString = queryParams.get("category").get(0);
        filteredToDos = filterToDosByCategory(filteredToDos, targetString);
      }
    return filteredToDos;
  }

 // Method displaying only a set limit of todos

  public ToDo[] limitToDos(ToDo[] todos, Integer targetLimitToDos) {
    return Arrays.stream(todos).limit(targetLimitToDos).toArray(ToDo[]::new);
  }
// Method displaying only todos with a set string inside body
  public ToDo[] filterToDosByBody(ToDo[] todos, String targetString){
    return Arrays.stream(todos).filter(x -> x.body.contains(targetString)).toArray(ToDo[]::new);
  }
  // Method displaying todos by status, either complete or incomplete
  public ToDo[] filterToDosByStatus(ToDo[] todos, boolean targetStatus){
    return Arrays.stream(todos).filter(x-> x.status == targetStatus).toArray(ToDo[]::new);
  }
  public ToDo[] filterToDosByOwner(ToDo[] todos, String targetOwner) {
    return Arrays.stream(todos).filter(x -> x.owner.equals(targetOwner)).toArray(ToDo[]::new);
  }
  public ToDo[] filterToDosByCategory(ToDo[] todos, String targetCategory) {
    return Arrays.stream(todos).filter(x -> x.category.equals(targetCategory)).toArray(ToDo[]::new);
  }
}
