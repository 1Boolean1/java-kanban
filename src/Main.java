import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        HashMap<Integer, Epic> tasks = new HashMap<>();
        menu(tasks);
    }

    public static void menu(HashMap<Integer, Epic> tasks) {
        Scanner scanner = new Scanner(System.in);
        boolean cycle = true;
        int numTask;
        while (cycle) {
            System.out.println("""
                    Введите номер команды:
                    1. Ввод новой задачи
                    2. Добавление подзадачи
                    3. Изменить задачу
                    4. Изменить статус задачи
                    5. Вывод всех задач
                    6. Вывод задачи по идентификатору
                    7. Вывод задач по статусу
                    8. Удалить выбранную задачу
                    9. Очистить список задач
                    0. Выход""");
            int menuChoice = scanner.nextInt();
            switch (menuChoice) {
                case 1: //DONE
                    System.out.println("Введите название задачи: ");
                    String taskName = scanner.next();
                    Task.addNewTask(tasks, taskName);
                    break;
                case 2: //DONE
                    System.out.println("Введите номер задачи," +
                            " в которую вы бы хотели добавить подзадачу");
                    numTask = scanner.nextInt();
                    if (tasks.containsKey(numTask)) {
                        System.out.println("Введите название подзадачи: ");
                        String subTaskName = scanner.next();
                        tasks.get(numTask).addSubTasks(subTaskName);
                        break;
                    } else {
                        System.out.println("Задачи с таким номером не существует!");
                    }
                case 3: //DONE
                    System.out.println("Введите номер задачи, которую вы бы хотели изменить");
                    numTask = scanner.nextInt();
                    if (tasks.containsKey(numTask)) {
                        System.out.println("Введите новое имя задачи");
                        String newName = scanner.next();
                        tasks.get(numTask).setTaskName(newName);
                        break;
                    } else {
                        System.out.println("Задачи с таким номером не существует!");
                        break;
                    }
                case 4: //DONE
                    System.out.println("Выберите номер задачи, " +
                            "статус которой вы бы хотели изменить:");
                    numTask = scanner.nextInt();
                    if (tasks.containsKey(numTask)) {
                        tasks.get(numTask).changeStatus(tasks, numTask, scanner);
                        break;
                    } else {
                        System.out.println("Задачи с таким номером не существует!");
                        break;
                    }
                case 5: //DONE
                    Task.printAllTasks(tasks);
                    break;
                case 6: //DONE
                    System.out.println("Введите номер задачи: ");
                    numTask = scanner.nextInt();
                    if (tasks.containsKey(numTask)) {
                        tasks.get(numTask).printTasksById(tasks, numTask);
                        break;
                    } else {
                        System.out.println("Задачи с таким номером не существует!");
                        break;
                    }
                case 7: //DONE
                    Task.printTasksByStatus(tasks, scanner);
                    break;
                case 8:
                    System.out.println("Введите номер задачи: ");
                    numTask = scanner.nextInt();
                    if (tasks.containsKey(numTask)) {
                        tasks.remove(numTask);
                        break;
                    } else {
                        System.out.println("Задачи с таким номером не существует!");
                        break;
                    }
                case 9: //DONE
                    if (tasks.isEmpty()) {
                        System.out.println("Невозможно очистить список, список задач пуст");
                    } else {
                        tasks.clear();
                        System.out.println("Список задач успешно очищен!");
                    }
                    break;
                case 0: //DONE
                    System.out.println("Вы вышли из программы...");
                    cycle = false;
                    break;
                default:
                    System.out.println("Введена неизвестная команда, попробуйте снова");
            }
        }
    }
}