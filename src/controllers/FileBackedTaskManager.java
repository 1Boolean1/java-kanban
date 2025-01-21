package controllers;

import enums.Status;
import exceptions.ManagerSaveException;
import model.EpicTask;
import model.SubTask;
import model.Task;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,duration,startTime,epic");
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
            throw new ManagerSaveException("Error saving to file: " + file.getName());
        }
    }

    private String toCSV(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getTaskId()).append(",");
        sb.append(task.getType()).append(",");
        sb.append(task.getTaskName()).append(",");
        sb.append(task.getTaskStatus()).append(",");
        sb.append(task.getTaskDescription()).append(",");
        sb.append(task.getDuration() != null ? task.getDuration().toMinutes() : "null").append(",");
        sb.append(task.getStartTime() != null ? task.getStartTime() : "null");

        if (task instanceof SubTask subTask) {
            sb.append(",").append(subTask.getEpicId());
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

    public void loadFromFile() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            bufferedReader.readLine();

            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                String[] fields = line.split(",");

                int id = Integer.parseInt(fields[0]);
                String type = fields[1];
                String name = fields[2];
                Status status = Status.valueOf(fields[3]);
                String description = fields[4];
                Duration duration = fields[5].equals("null") ? null : Duration.ofMinutes(Long.parseLong(fields[5]));
                LocalDateTime startTime = fields[6].equals("null") ? null : LocalDateTime.parse(fields[6]);

                switch (type) {
                    case "TASK" -> {
                        Task task = new Task(id, name, description, status, duration, startTime);
                        super.addNewTask(task);
                    }
                    case "EPIC" -> {
                        EpicTask epic = new EpicTask(id, name, description, status);
                        super.addNewEpicTask(epic);
                    }
                    case "SUB" -> {
                        int epicId = Integer.parseInt(fields[7]);
                        SubTask subTask = new SubTask(id, name, description, status, duration, startTime, epicId);
                        super.addNewSubTask(subTask, epicId);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + file.getName(), e);
        } catch (Exception e) {
            throw new ManagerSaveException("Error reading from file: " + file.getName());
        }
    }

}

