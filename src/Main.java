import controllers.EpicTask;
import controllers.SubTask;
import controllers.Task;
import model.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task firstTask = new Task("new task", "its new task");
        taskManager.addNewTask(firstTask);
        Task secondTask = new Task("second task", "its second task");
        taskManager.addNewTask(secondTask);
        EpicTask firstEpicTask = new EpicTask("new epic task", "its new epic task");
        taskManager.addNewEpicTask(firstEpicTask);
        SubTask firstSub = new SubTask("new sub", "its new sub task", firstEpicTask.getTaskId());
        SubTask secondSub = new SubTask("second sub", "its second sub task", firstEpicTask.getTaskId());
        taskManager.addNewSubTask(firstSub);
        taskManager.addNewSubTask(secondSub);
        EpicTask secondEpic = new EpicTask("second epic", "its second epic");
        taskManager.addNewEpicTask(secondEpic);
        SubTask thirdSub = new SubTask("thirdSub", "its thirdSub", secondEpic.getTaskId());
        taskManager.addNewSubTask(thirdSub);
        System.out.println(taskManager.printAllTasks());
        taskManager.updateTaskStatus(1);
        taskManager.updateTaskStatus(1);
        taskManager.updateTaskStatus(2);
        taskManager.updateTaskStatus(4);
        taskManager.updateTaskStatus(7);
        taskManager.updateTaskStatus(7);
        System.out.println(taskManager.printAllTasks());
        taskManager.deleteById(1);
        taskManager.deleteById(7);
        System.out.println(taskManager.printAllTasks());
    }
}