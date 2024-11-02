import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class Task {
    private String taskName;
    Status status;

    public Task(String taskName, Status status) {
        this.taskName = taskName;
        this.status = status;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static void printAllTasks(HashMap<Integer, Epic> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст!");
        } else {
            for (Integer key : tasks.keySet()) {
                tasks.get(key).print(key);
                if (!tasks.get(key).getSubTasks().isEmpty()) {
                    for (Integer subTaskKey : tasks.get(key).getSubTasks().keySet()) {
                        tasks.get(key).getSubTasks().get(subTaskKey).printSubTask(subTaskKey);
                    }
                }
            }
        }
    }

    public static void addNewTask(HashMap<Integer, Epic> tasks, String taskName) {
        if (tasks.isEmpty()) {
            Epic task = new Epic(taskName, Status.NEW);
            tasks.put(tasks.size() + 1, task);
            System.out.println("Ваша задача успешно добавлена!");
        } else {
            for (Integer key : tasks.keySet()) {
                if (tasks.get(key).getTaskName().equals(taskName)) {
                    System.out.println("Задача с таким названием уже существует, придумайте новое!");
                } else {
                    Epic task = new Epic(taskName, Status.NEW);
                    tasks.put(tasks.size() + 1, task);
                    System.out.println("Ваша задача успешно добавлена!");
                }
            }
        }
    }

    public static void printTasksByStatus(HashMap<Integer, Epic> tasks, Scanner scanner) {
        System.out.println("""
                Выберите статус:
                1. NEW
                2. IN_PROGRESS
                3. DONE""");
        int numStatus = scanner.nextInt();
        switch (numStatus) {
            case 1:
                for (Integer key : tasks.keySet()) {
                    if (tasks.get(key).getStatus().equals(Status.NEW))
                        tasks.get(key).print(key);
                    if (!tasks.get(key).getSubTasks().isEmpty()) {
                        for (Integer subTaskKey : tasks.get(key).getSubTasks().keySet()) {
                            tasks.get(key).getSubTasks()
                                    .get(subTaskKey).printSubTask(subTaskKey);
                        }
                    }
                }
                break;
            case 2:
                for (Integer key : tasks.keySet()) {
                    if (tasks.get(key).getStatus().equals(Status.IN_PROGRESS))
                        tasks.get(key).print(key);
                    if (!tasks.get(key).getSubTasks().isEmpty()) {
                        for (Integer subTaskKey : tasks.get(key).getSubTasks().keySet()) {
                            tasks.get(key).getSubTasks()
                                    .get(subTaskKey).printSubTask(subTaskKey);
                        }
                    }
                }
                break;
            case 3:
                for (Integer key : tasks.keySet()) {
                    if (tasks.get(key).getStatus().equals(Status.DONE))
                        tasks.get(key).print(key);
                    if (!tasks.get(key).getSubTasks().isEmpty()) {
                        for (Integer subTaskKey : tasks.get(key).getSubTasks().keySet()) {
                            tasks.get(key).getSubTasks()
                                    .get(subTaskKey).printSubTask(subTaskKey);
                        }
                    }
                }
                break;
            default:
                System.out.println("Введена неизвестная команда");
        }
    }

    public void printTasksById(HashMap<Integer, Epic> tasks, int numTask) {
        tasks.get(numTask).print(numTask);
        if (!tasks.get(numTask).getSubTasks().isEmpty()) {
            for (Integer subTaskKey : tasks.get(numTask).getSubTasks().keySet()) {
                tasks.get(numTask).getSubTasks().get(subTaskKey).printSubTask(subTaskKey);
            }
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return Objects.equals(taskName, task.taskName) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, status);
    }

    public void print(int num) {
        System.out.println("Задача №" + num + ": " + taskName + "\nСтатус: " + status);
    }

    public Status getStatus() {
        return status;
    }
}
