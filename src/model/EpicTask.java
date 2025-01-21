package model;

import enums.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class EpicTask extends Task {

    Duration duration = null;
    LocalDateTime startTime = null;

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public ArrayList<SubTask> getSubTasksArray() {
        return new ArrayList<>(subTasks.values());
    }

    public EpicTask(String epicTaskName, String epicTaskDescription) {
        super(epicTaskName, epicTaskDescription);
    }

    public EpicTask(int epicTaskId, String epicTaskName, String epicTaskDescription, Status epicTaskStatus) {
        super(epicTaskId, epicTaskName, epicTaskDescription, epicTaskStatus);
    }

    public String getType() {
        return "EPIC";
    }

    public void addSubTask(SubTask subTask, Integer id) {
        subTasks.put(id, subTask);
        if (getTaskStatus().equals(Status.DONE)) {
            setTaskStatus(Status.IN_PROGRESS);
        }
        if (subTask.getDuration() != null) {
            recalculateEpicTimes();
        }
    }

    private void recalculateEpicTimes() {
        this.startTime = getStartTime();
        this.duration = getDuration();
    }


    @Override
    public String toString() {
        if (getDuration() != null) {
            return "EpicTask{" +
                    "EpicTaskName='" + getTaskName() + '\'' +
                    ", EpicTaskDescription='" + getTaskDescription() + '\'' +
                    ", EpicTaskID=" + getTaskId() +
                    ", EpicTaskStatus=" + getTaskStatus() +
                    ", EpicTaskDuration=" + getDuration().toMinutes() +
                    ", EpicTaskStartTime=" + getStartTime() +
                    '}';
        } else {
            return "EpicTask{" +
                    "EpicTaskName='" + getTaskName() + '\'' +
                    ", EpicTaskDescription='" + getTaskDescription() + '\'' +
                    ", EpicTaskID=" + getTaskId() +
                    ", EpicTaskStatus=" + getTaskStatus() +
                    '}';
        }
    }

    public void changeEpicTaskStatus() {
        if (getSubTasks().isEmpty()) {
            setTaskStatus(Status.NEW);
        } else {
            long numOfSubTasksWithDone = getSubTasks().values().stream()
                    .filter(subTask -> subTask.getTaskStatus().equals(Status.DONE))
                    .count();
            long numOfSubTasksWithNew = getSubTasks().values().stream()
                    .filter(subTask -> subTask.getTaskStatus().equals(Status.NEW))
                    .count();
            if (numOfSubTasksWithDone == getSubTasksArray().size()) {
                setTaskStatus(Status.DONE);
            } else if (numOfSubTasksWithNew == getSubTasksArray().size()) {
                setTaskStatus(Status.NEW);
            } else {
                setTaskStatus(Status.IN_PROGRESS);
            }
        }
    }

    @Override
    public Duration getDuration() {
        return subTasks.values().stream()
                .filter(subTask -> subTask.getDuration() != null)
                .map(SubTask::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
    }


    @Override
    public LocalDateTime getStartTime() {
        if (!subTasks.isEmpty()) {
            return subTasks.values().stream()
                    .map(SubTask::getStartTime)
                    .filter(Objects::nonNull)
                    .min(LocalDateTime::compareTo)
                    .orElse(null);
        } else {
            return null;
        }
    }


    @Override
    public LocalDateTime getEndTime() {
        if (getStartTime() != null && getDuration() != null) {
            return getStartTime().plus(getDuration());
        } else {
            return null;
        }
    }
}
