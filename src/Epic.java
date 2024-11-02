import java.util.HashMap;
import java.util.Scanner;

public class Epic extends Task {
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public Epic(String task_name, Status status) {
        super(task_name, status);
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public void addSubTasks(String subTaskName){
        SubTask newSubTask = new SubTask(subTaskName, Status.NEW);
        subTasks.put(subTasks.size() + 1, newSubTask);
    }

    public void changeStatus(HashMap<Integer, Epic> tasks, int numTask, Scanner scanner){
        if (!tasks.get(numTask).getSubTasks().isEmpty()) {
            System.out.println("Выберите номер подзадачи, " +
                    "статус которой вы бы хотели изменить:");
            int numSubTask = scanner.nextInt();
            if (tasks.get(numTask).getSubTasks().get(numSubTask).getStatus().equals(Status.NEW)) {
                tasks.get(numTask).getSubTasks().get(numSubTask).setStatus(Status.IN_PROGRESS);
                System.out.println("Вы изменили статус подзадачи на IN_PROGRESS");
            } else if (tasks.get(numTask).getSubTasks()
                    .get(numSubTask).getStatus().equals(Status.IN_PROGRESS)) {
                tasks.get(numTask).getSubTasks().get(numSubTask).setStatus(Status.DONE);
                System.out.println("Вы изменили статус подзадачи на DONE");
            } else {
                System.out.println("Изменить статус выполненной подзадачи невозможно!");
            }
            int numDONE = 0;
            for(Integer subTaskKey: tasks.get(numTask).getSubTasks().keySet()){
                if(tasks.get(numTask).getSubTasks()
                        .get(subTaskKey).getStatus().equals(Status.IN_PROGRESS)){
                    tasks.get(numTask).setStatus(Status.IN_PROGRESS);
                } else if (tasks.get(numTask).getSubTasks()
                        .get(subTaskKey).getStatus().equals(Status.DONE)) {
                    numDONE++;
                }
                if(numDONE == tasks.get(numTask).getSubTasks().size()){
                    tasks.get(numTask).setStatus(Status.DONE);
                    System.out.println("Поздравляю, вы выполнили задачу!");
                }
            }
        } else {
            if (tasks.get(numTask).getStatus().equals(Status.NEW)) {
                tasks.get(numTask).setStatus(Status.IN_PROGRESS);
                System.out.println("Вы изменили статус задачи на IN_PROGRESS");
            } else if (tasks.get(numTask).getStatus().equals(Status.IN_PROGRESS)) {
                tasks.get(numTask).setStatus(Status.DONE);
                System.out.println("Вы изменили статус задачи на DONE");
            } else {
                System.out.println("Изменить статус выполненной задачи невозможно!");
            }
        }
    }
}
