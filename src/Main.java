import controllers.FileBackedTaskManager;
import model.EpicTask;
import model.SubTask;
import model.Task;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        FileBackedTaskManager memory = new FileBackedTaskManager(File.createTempFile("file", "txt"));
        Task task = new Task("new task", "a");
        memory.addTask(task);
        EpicTask firstEpicTask = new EpicTask("new epic task", "its new epic task");
        memory.addEpic(firstEpicTask);
        SubTask firstSub = new SubTask("new sub", "its new sub task");
        memory.addSubTask(firstSub, firstEpicTask);
        System.out.println(memory.printAllTasks());
        memory.deleteTasks();
        memory.deleteEpics();
        memory.deleteSubTasks();
        System.out.println(memory.printAllTasks());
        memory.loadFromFile();
        System.out.println(memory.printAllTasks());
    }
}