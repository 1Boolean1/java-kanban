import controllers.EpicTask;
import controllers.Task;
import model.HistoryManager;
import model.InMemoryHistoryManager;
import model.InMemoryTaskManager;
import model.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HistoryManagerTest {

    HistoryManager historyManager = new InMemoryHistoryManager();
    TaskManager memoryManager = new InMemoryTaskManager();

    @Test
    public void checkHistoryManager(){
        EpicTask newEpicTask = new EpicTask("new epic task", "its new epic task");
        memoryManager.addNewEpicTask(newEpicTask);
        Task epicTask = memoryManager.getEpicTask(newEpicTask.getTaskId());
        Assertions.assertEquals(epicTask, newEpicTask, "Таски не верны");
        Assertions.assertEquals(newEpicTask, historyManager.getHistory().get(0),
                "Неправильная работа с историей задач");
    }
}
