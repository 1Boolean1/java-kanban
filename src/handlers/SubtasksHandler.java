package handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.InMemoryTaskManager;
import enums.Endpoint;
import enums.Status;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class SubtasksHandler extends BaseHandler implements HttpHandler {

    private final InMemoryTaskManager taskManager;

    public SubtasksHandler(InMemoryTaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        switch (endpoint) {
            case GET_SUBTASKS: {
                handleGetSubTasks(exchange);
                break;
            }
            case GET_SUBTASK_BY_ID: {
                handleGetSubTaskById(exchange);
                break;
            }
            case DELETE_SUBTASK: {
                handleDeleteSubTask(exchange);
                break;
            }
            case POST_UPDATE_SUBTASK: {
                handleUpdateSubTask(exchange);
                break;
            }
            case POST_ADD_SUBTASK: {
                handleAddSubTask(exchange);
                break;
            }
            default:
                sendNotFound(exchange, "Такого эндпоинта не существует");
        }
    }

    private void handleAddSubTask(HttpExchange exchange) throws IOException {
        Optional<SubTask> taskOpt = parseSubTask(exchange.getRequestBody());

        if (taskOpt.isPresent()) {
            SubTask subTask = taskOpt.get();
            subTask.setTaskStatus(Status.NEW);
            List<Task> epicTasks = taskManager.getEpicTasks().stream().filter(epicTask -> epicTask.getTaskId() == subTask.getEpicId()).toList();
            if (!epicTasks.isEmpty()) {
                int id = taskManager.addNewSubTask(subTask);
                if (id != 0) {
                    String response = gson.toJson(taskManager.getSubTask(id));
                    sendText(exchange, response);
                } else {
                    sendHasInteractions(exchange);
                }
            } else {
                sendNotFound(exchange, "EpicTask с таким id нет");
            }
        } else {
            String response = "Поля задачи не могут быть пустыми";
            sendFieldsAreEmpty(exchange, response);
        }
    }


    private void handleGetSubTasks(HttpExchange exchange) throws IOException {
        if (taskManager.getSubTasks().isEmpty()) {
            sendNotFound(exchange, "Список задач пуст");
        } else {
            sendText(exchange, gson.toJson(taskManager.getSubTasks()));
        }
    }

    private void handleGetSubTaskById(HttpExchange exchange) throws IOException {
        int id = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
        if (taskManager.getSubTask(id) == null) {
            sendNotFound(exchange, "Задачи с таким id нет");
        } else {
            sendText(exchange, gson.toJson(taskManager.getSubTask(id)));
        }
    }

    private void handleDeleteSubTask(HttpExchange exchange) throws IOException {
        int id = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
        if (taskManager.getSubTask(id) == null) {
            sendNotFound(exchange, "Задачи с таким id нет");
        } else {
            taskManager.deleteById(id);
            sendText(exchange, "Задача успешно удалена!");
        }
    }


    private void handleUpdateSubTask(HttpExchange exchange) throws IOException {
        int id = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
        if (taskManager.getTask(id) == null) {
            sendNotFound(exchange, "Задачи с таким id нет");
        } else {
            Optional<SubTask> taskOpt = parseSubTask(exchange.getRequestBody());
            if (taskOpt.isPresent()) {
                Task updatedTask = taskOpt.get();
                boolean updated;
                if (updatedTask.getDuration() != null) {
                    updated = taskManager.updateTask(id, updatedTask.getTaskName(), updatedTask.getTaskDescription(), updatedTask.getDuration(), updatedTask.getStartTime());
                } else {
                    updated = taskManager.updateTask(id, updatedTask.getTaskName(), updatedTask.getTaskDescription());
                }

                if (updated) {
                    String response = gson.toJson(taskManager.getTask(id));
                    sendText(exchange, response);
                } else {
                    sendHasInteractions(exchange);
                }
            } else {
                String response = "Поля задачи не могут быть пустыми";
                sendFieldsAreEmpty(exchange, response);
            }
        }
    }

    private Optional<SubTask> parseSubTask(InputStream bodyInputStream) throws IOException {
        String body = new String(bodyInputStream.readAllBytes(), StandardCharsets.UTF_8);
        try {
            SubTask task = gson.fromJson(body, SubTask.class);
            return task != null ? Optional.of(task) : Optional.empty();
        } catch (JsonSyntaxException ex) {
            return Optional.empty();
        }
    }
}