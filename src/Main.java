import controllers.InMemoryTaskManager;
import controllers.TaskManager;
import model.EpicTask;
import model.SubTask;
import model.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new InMemoryTaskManager();
        Task firstTask = new Task("firstTask", "");
        taskManager.addNewTask(firstTask);
        Task secondTask = new Task("secondTask", "");
        taskManager.addNewTask(secondTask);
        EpicTask firstEpic = new EpicTask("firstEpic", "");
        taskManager.addNewEpicTask(firstEpic);
        SubTask firstSub = new SubTask("firstSub", "");
        taskManager.addNewSubTask(firstSub, firstEpic.getTaskId());
        SubTask secondSub = new SubTask("secondSub", "");
        taskManager.addNewSubTask(secondSub, firstEpic.getTaskId());
        SubTask thirdSub = new SubTask("thirdSub", "");
        taskManager.addNewSubTask(thirdSub, firstEpic.getTaskId());
        EpicTask secondEpic = new EpicTask("secondEpic", "");
        taskManager.addNewEpicTask(secondEpic);
        taskManager.getTask(1);
        taskManager.getSubTask(5);
        printAllTasks(taskManager);
        taskManager.getEpicTask(7);
        taskManager.getTask(1);
        printAllTasks(taskManager);
        taskManager.deleteById(1);
        printAllTasks(taskManager);
        taskManager.deleteById(3);
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
        System.out.println(manager.getHistory());
    }
}