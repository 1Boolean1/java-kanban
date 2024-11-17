import model.Task;
import controllers.InMemoryTaskManager;
import controllers.TaskManager;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TaskTest {
    TaskManager manager = new InMemoryTaskManager();

    @Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        final int taskId = manager.addNewTask(task);

        final Task savedTask = manager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final ArrayList<Task> tasks = manager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

}