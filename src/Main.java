import controllers.FileBackedTaskManager;
import model.EpicTask;
import model.SubTask;
import model.Task;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public static void main(String[] args) throws IOException {
    FileBackedTaskManager memory = new FileBackedTaskManager(File.createTempFile("file", ".txt"));
    Task task = new Task("new task", "a", Duration.ofDays(2), LocalDateTime.now());
    memory.addTask(task);
    SubTask firstSub = new SubTask("new sub", "its new sub task",
            Duration.ofMinutes(30), LocalDateTime.now());
    SubTask secondSub = new SubTask("new sub", "its new sub task");
    EpicTask firstEpicTask = new EpicTask("new epic task", "its new epic task");
    memory.addEpic(firstEpicTask);
    memory.addSubTask(secondSub, firstEpicTask);
    memory.addSubTask(firstSub, firstEpicTask);
    memory.updateTaskStatus(4);
    Task newTask = new Task("new task2", "");
    memory.addTask(newTask);
    System.out.println(memory.printAllTasks());
    System.out.println("---------");
    System.out.println(memory.getPrioritizedTasks());
}