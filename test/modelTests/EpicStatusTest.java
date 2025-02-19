package modelTests;

import controllers.InMemoryTaskManager;
import controllers.TaskManager;
import enums.Status;
import model.EpicTask;
import model.SubTask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicStatusTest {
    TaskManager manager = new InMemoryTaskManager();

    @Test
    void allSubIsNew() {
        EpicTask epicTask = new EpicTask("epicTask", "newEpic");
        SubTask subTask1 = new SubTask("firstSub", "itsFirstSub", epicTask.getTaskId());
        SubTask subTask2 = new SubTask("secondSub", "itsSecondSub", epicTask.getTaskId());

        manager.addNewEpicTask(epicTask);
        manager.addNewSubTask(subTask1);
        manager.addNewSubTask(subTask2);

        assertEquals(manager.getEpicTask(epicTask.getTaskId()).getTaskStatus(), Status.NEW,
                "Error with new subs");
    }

    @Test
    void allSubIsDone() {
        EpicTask epicTask = new EpicTask("epicTask", "newEpic");
        manager.addNewEpicTask(epicTask);
        SubTask subTask1 = new SubTask("firstSub", "itsFirstSub", epicTask.getTaskId());
        SubTask subTask2 = new SubTask("secondSub", "itsSecondSub", epicTask.getTaskId());

        manager.addNewSubTask(subTask1);
        manager.addNewSubTask(subTask2);
        manager.updateTaskStatus(subTask1.getTaskId());
        manager.updateTaskStatus(subTask1.getTaskId());
        manager.updateTaskStatus(subTask2.getTaskId());
        manager.updateTaskStatus(subTask2.getTaskId());

        assertEquals(manager.getEpicTask(epicTask.getTaskId()).getTaskStatus(), Status.DONE,
                "Error with done subs");
    }

    @Test
    void subWithDoneAndNew() {
        EpicTask epicTask = new EpicTask("epicTask", "newEpic");
        manager.addNewEpicTask(epicTask);
        SubTask subTask1 = new SubTask("firstSub", "itsFirstSub", epicTask.getTaskId());
        SubTask subTask2 = new SubTask("secondSub", "itsSecondSub", epicTask.getTaskId());

        manager.addNewSubTask(subTask1);
        manager.addNewSubTask(subTask2);
        manager.updateTaskStatus(subTask1.getTaskId());
        manager.updateTaskStatus(subTask1.getTaskId());

        assertEquals(manager.getEpicTask(epicTask.getTaskId()).getTaskStatus(), Status.IN_PROGRESS,
                "Error with done and new subs");
    }

    @Test
    void allSubsIsIn_progress() {
        EpicTask epicTask = new EpicTask("epicTask", "newEpic");
        manager.addNewEpicTask(epicTask);
        SubTask subTask1 = new SubTask("firstSub", "itsFirstSub", epicTask.getTaskId());
        SubTask subTask2 = new SubTask("secondSub", "itsSecondSub", epicTask.getTaskId());

        manager.addNewSubTask(subTask1);
        manager.addNewSubTask(subTask2);
        manager.updateTaskStatus(subTask1.getTaskId());
        manager.updateTaskStatus(subTask2.getTaskId());

        assertEquals(manager.getEpicTask(epicTask.getTaskId()).getTaskStatus(), Status.IN_PROGRESS,
                "Error with in_progress subs");
    }
}
