package handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import controllers.TaskManager;
import enums.Endpoint;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class TasksHandler extends BaseHandler {

    private final TaskManager taskManager;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    protected void processGet(HttpExchange exchange, Endpoint endpoint) throws IOException {
        if (endpoint.equals(Endpoint.GET_TASKS)) {
            handleGetTasks(exchange);
        } else if (endpoint.equals(Endpoint.GET_TASK_BY_ID)) {
            handleGetTaskById(exchange);
        }
    }

    @Override
    protected void processPost(HttpExchange exchange, Endpoint endpoint) throws IOException {
        if (endpoint.equals(Endpoint.POST_ADD_TASK)) {
            handleAddTask(exchange);
        } else if (endpoint.equals(Endpoint.POST_UPDATE_TASK)) {
            handleUpdateTask(exchange);
        }
    }

    @Override
    protected void processDelete(HttpExchange exchange, Endpoint endpoint) throws IOException {
        if (endpoint.equals(Endpoint.DELETE_TASK)) {
            handleDeleteTask(exchange);
        }
    }

    private void handleAddTask(HttpExchange exchange) throws IOException {
        Optional<Task> taskOpt = parseTask(exchange.getRequestBody());

        if (taskOpt.isPresent()) {
            int id = taskManager.addNewTask(taskOpt.get());
            if (id != 0) {
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


    private void handleGetTasks(HttpExchange exchange) throws IOException {
        if (taskManager.getTasks().isEmpty()) {
            sendNotFound(exchange, "Список задач пуст");
        } else {
            sendText(exchange, gson.toJson(taskManager.getTasks()));
        }
    }

    private void handleGetTaskById(HttpExchange exchange) throws IOException {
        int id = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
        if (taskManager.getTask(id) == null) {
            sendNotFound(exchange, "Задачи с таким id нет");
        } else {
            sendText(exchange, gson.toJson(taskManager.getTask(id)));
        }
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        int id = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
        if (taskManager.getTask(id) == null) {
            sendNotFound(exchange, "Задачи с таким id нет");
        } else {
            taskManager.deleteById(id);
            sendText(exchange, "Задача успешно удалена!");
        }
    }


    private void handleUpdateTask(HttpExchange exchange) throws IOException {
        int id = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
        if (taskManager.getTask(id) == null) {
            sendNotFound(exchange, "Задачи с таким id нет");
        } else {
            Optional<Task> taskOpt = parseTask(exchange.getRequestBody());
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

    private Optional<Task> parseTask(InputStream bodyInputStream) throws IOException {
        String body = new String(bodyInputStream.readAllBytes(), StandardCharsets.UTF_8);
        try {
            Task task = gson.fromJson(body, Task.class);
            return task != null ? Optional.of(task) : Optional.empty();
        } catch (JsonSyntaxException ex) {
            return Optional.empty();
        }
    }
}