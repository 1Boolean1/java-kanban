import controllers.InMemoryTaskManager;
import controllers.TaskManager;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class IntervalTest {

    @Test
    void crossTimeTest() {
        TaskManager manager = new InMemoryTaskManager();
        Task firstTask = new Task("firstTask", "",
                Duration.ofDays(2), LocalDateTime.of(2025, 1, 21, 15, 41));
        manager.addNewTask(firstTask);
        Task secondTask = new Task("secondTask", "",
                Duration.ofDays(2), LocalDateTime.of(2025, 1, 20, 15, 41));
        manager.addNewTask(secondTask);
        Task thirdTask = new Task("thirdTask", "",
                Duration.ofDays(2), LocalDateTime.of(2024, 1, 20, 15, 41));
        manager.addNewTask(thirdTask);
        Task taskWithoutTime = new Task("withoutTime", "");
        manager.addNewTask(taskWithoutTime);

        assertNull(manager.getTask(secondTask.getTaskId()), "Interval error, its crossind tasks");
        assertEquals(manager.getTask(thirdTask.getTaskId()), thirdTask, "Interval error, it isnt crossing tasks");
        assertEquals(manager.getTask(taskWithoutTime.getTaskId()), taskWithoutTime, "Error with adding");
    }
}
