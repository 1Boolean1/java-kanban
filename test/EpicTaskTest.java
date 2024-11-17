import model.EpicTask;
import model.SubTask;
import model.Task;
import controllers.InMemoryTaskManager;
import controllers.TaskManager;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EpicTaskTest {
    TaskManager manager = new InMemoryTaskManager();

    @Test
    void addNewEpic(){
        EpicTask epicTask = new EpicTask("Test addNewEpic", "Test addNewEpic description");
        SubTask subTask = new SubTask("new sub task", "new sub");
        final int taskId = manager.addNewEpicTask(epicTask);
        final int subId = manager.addNewSubTask(subTask, epicTask.getTaskId());

        final Task savedTask = manager.getEpicTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(epicTask, savedTask, "Задачи не совпадают.");

        final ArrayList<Task> epicTasks = manager.getEpicTasks();

        assertNotNull(epicTasks, "Задачи не возвращаются.");
        assertEquals(1, epicTasks.size(), "Неверное количество задач.");
        assertEquals(epicTask, epicTasks.get(0), "Задачи не совпадают.");
        assertEquals(2, subId, "Неверный id у подзадачи");
        assertEquals(subTask, epicTask.getSubTasks().get(subId), "Подзадач не совпадают");
    }
}
