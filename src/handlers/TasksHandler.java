package handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.InMemoryTaskManager;
import enums.Endpoint;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class TasksHandler extends BaseHandler implements HttpHandler {

    private final InMemoryTaskManager taskManager;

    public TasksHandler(InMemoryTaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        switch (endpoint) {
            case GET_TASKS: {
                handleGetTasks(exchange);
                break;
            }
            case GET_TASK_BY_ID: {
                handleGetTaskById(exchange);
                break;
            }
            case DELETE_TASK: {
                handleDeleteTask(exchange);
                break;
            }
            case POST_UPDATE_TASK: {
                handleUpdateTask(exchange);
                break;
            }
            case POST_ADD_TASK: {
                handleAddTask(exchange);
                break;
            }
            default:
                sendNotFound(exchange, "Такого эндпоинта не существует");
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
                boolean updated = false;
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



