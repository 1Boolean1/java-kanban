import controllers.*;
import model.EpicTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HistoryManagerTest {

    TaskManager memoryManager = Managers.getDefault();

    @Test
    public void checkHistoryManager() {
        Task newTask = new Task("new task", "its new task");
        memoryManager.addNewTask(newTask);
        Assertions.assertEquals(newTask, memoryManager.getHistory().getFirst(),
                "Неправильная работа с историей задач");
        EpicTask secondTask = new EpicTask("new Epic", "its new epic");
        memoryManager.addNewEpicTask(secondTask);
        Assertions.assertEquals(secondTask, memoryManager.getHistory().get(1), "Неправильная работа с историей задач");
        Task task = memoryManager.getTask(1);
        Assertions.assertEquals(secondTask, memoryManager.getHistory().get(0), "Неправильная работа с историей задач");
        memoryManager.getTask(1).setTaskName("newName");
        Assertions.assertEquals(newTask, memoryManager.getHistory().get(1), "Неправильная работа с историей задач, при изменении задачи");
        memoryManager.deleteById(1);
        Assertions.assertEquals(secondTask, memoryManager.getHistory().get(0), "Неправильная работа с историей задач при удалении");

    }
}
