import controllers.InMemoryTaskManager;
import controllers.TaskManager;
import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryManagerTest {
    TaskManager manager = new InMemoryTaskManager();

    @Test
    void historyManagerTest() {
        Task newTask = new Task("newTask", "itsNewTask");
        manager.addNewTask(newTask);
        manager.getTask(newTask.getTaskId());
        Task secondTask = new Task("secondTask", "");
        manager.addNewTask(secondTask);
        manager.getTask(secondTask.getTaskId());
        Task thirdTask = new Task("thirdTask", "");
        manager.addNewTask(thirdTask);
        manager.getTask(thirdTask.getTaskId());

        assertEquals(manager.getHistory().getFirst(), newTask, "not add in history");
        assertEquals(manager.getHistory().get(1), secondTask, "not add in history");

        manager.deleteById(secondTask.getTaskId());
        assertEquals(manager.getHistory().get(1), thirdTask, "not delete in");

        manager.deleteById(thirdTask.getTaskId());
        assertEquals(manager.getHistory().size(), 1, "not delete last");

        manager.deleteById(newTask.getTaskId());
        assertEquals(manager.getHistory().size(), 0, "not delete first");
    }
}
