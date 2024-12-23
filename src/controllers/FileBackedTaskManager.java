package controllers;

import enums.Status;
import model.EpicTask;
import model.SubTask;
import model.Task;

import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic");
            writer.newLine();

            for (Task task : getTasks()) {
                writer.write(toCSV(task));
                writer.newLine();
            }

            for (Task epic : getEpicTasks()) {
                writer.write(toCSV(epic));
                writer.newLine();
            }

            for (Task subtask : getSubTasks()) {
                writer.write(toCSV(subtask));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private String toCSV(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getTaskId()).append(",");
        sb.append(task.getType()).append(",");
        sb.append(task.getTaskName()).append(",");
        sb.append(task.getTaskStatus()).append(",");
        sb.append(task.getTaskDescription());
        if (task instanceof SubTask) {
            SubTask subTask = (SubTask) task;
            sb.append(",");
            sb.append(subTask.getEpicId());
        }
        return sb.toString();
    }

    public void addTask(Task task) {
        super.addNewTask(task);
        save();
    }

    public void addEpic(EpicTask epic) {
        super.addNewEpicTask(epic);
        save();
    }

    public void addSubTask(SubTask subtask, EpicTask epic) {
        super.addNewSubTask(subtask, epic.getTaskId());
        save();
    }

    public void updateTask(Task task) {
        super.updateTask(task.getTaskId(), task.getTaskName(), task.getTaskDescription());
        save();
    }

    public void deleteById(int id) {
        super.deleteById(id);
        save();
    }

    public void loadFromFile() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        bufferedReader.readLine();
        while (bufferedReader.ready()) {
            String str = bufferedReader.readLine();
            if (str.split(",")[1].equals("TASK")) {
                Task newTask = new Task(Integer.parseInt(str.split(",")[0]),
                        str.split(",")[2], str.split(",")[4],
                        Status.valueOf(str.split(",")[3]));
                addNewTask(newTask);
            } else if (str.split(",")[1].equals("EPIC")) {
                EpicTask newTask = new EpicTask(Integer.parseInt(str.split(",")[0]),
                        str.split(",")[2], str.split(",")[4],
                        Status.valueOf(str.split(",")[3]));
                addNewEpicTask(newTask);
            } else if (str.split(",")[1].equals("SUB")) {
                SubTask newTask = new SubTask(Integer.parseInt(str.split(",")[0]),
                        str.split(",")[2], str.split(",")[4],
                        Status.valueOf(str.split(",")[3]),
                        Integer.parseInt(str.split(",")[5]));
                addNewSubTask(newTask, Integer.parseInt(str.split(",")[5]));
            }
        }
        bufferedReader.close();
    }
}

