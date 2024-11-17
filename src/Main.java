import controllers.EpicTask;
import controllers.SubTask;
import controllers.Task;
import model.InMemoryTaskManager;
import model.Managers;
import model.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new InMemoryTaskManager();
        Task firstTask = new Task("new task", "its new task");
        taskManager.addNewTask(firstTask);
        Task secondTask = new Task("second task", "its second task");
        taskManager.addNewTask(secondTask);
        EpicTask firstEpicTask = new EpicTask("new epic task", "its new epic task");
        taskManager.addNewEpicTask(firstEpicTask);
        SubTask firstSub = new SubTask("new sub", "its new sub task");
        taskManager.addNewSubTask(firstSub, firstEpicTask.getTaskId());
        SubTask secondSub = new SubTask("second sub", "its second sub task");
        EpicTask secondEpic = new EpicTask("second epic", "its second epic");
        taskManager.addNewEpicTask(secondEpic);
        SubTask thirdSub = new SubTask("thirdSub", "its thirdSub");
        taskManager.updateTaskStatus(1);
        taskManager.updateTaskStatus(1);
        Task task = taskManager.getTask(2);
        Task task2 = taskManager.getTask(1);
        taskManager.updateTaskStatus(2);
        taskManager.updateTaskStatus(4);
        taskManager.updateTaskStatus(7);
        taskManager.updateTaskStatus(7);
        taskManager.deleteById(1);
        taskManager.deleteById(7);
        printAllTasks(taskManager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        System.out.println(manager.getTasks());
        System.out.println("Эпики:");
        for (Task epic : manager.getEpicTasks()) {
            System.out.println(epic);

            for (Task task : manager.getSubTasksById(epic.getTaskId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubTasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : Managers.getDefaultHistory().getHistory()) {
            System.out.println(task);
        }
    }
}