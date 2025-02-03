package handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import controllers.TaskManager;
import enums.Endpoint;
import model.EpicTask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class EpicsHandler extends BaseHandler {

    private final TaskManager taskManager;

    public EpicsHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    protected void processGet(HttpExchange exchange, Endpoint endpoint) throws IOException {
        if (endpoint.equals(Endpoint.GET_EPICS)) {
            handleGetEpics(exchange);
        } else if (endpoint.equals(Endpoint.GET_EPIC_BY_ID)) {
            handleGetEpicById(exchange);
        } else if (endpoint.equals(Endpoint.GET_EPIC_SUBTASKS)) {
            handleGetEpicSubtasks(exchange);
        }
    }

    @Override
    protected void processPost(HttpExchange exchange, Endpoint endpoint) throws IOException {
        if (endpoint.equals(Endpoint.POST_ADD_EPIC)) {
            handleAddEpic(exchange);
        }
    }

    @Override
    protected void processDelete(HttpExchange exchange, Endpoint endpoint) throws IOException {
        if (endpoint.equals(Endpoint.DELETE_EPIC)) {
            handleDeleteEpic(exchange);
        }
    }

    private void handleAddEpic(HttpExchange exchange) throws IOException {
        Optional<EpicTask> taskOpt = parseEpicTask(exchange.getRequestBody());

        if (taskOpt.isPresent()) {
            int id = taskManager.addNewEpicTask(taskOpt.get());
            if (id != 0) {
                String response = gson.toJson(taskManager.getEpicTask(id));
                sendText(exchange, response);
            } else {
                sendHasInteractions(exchange);
            }
        } else {
            String response = "Поля задачи не могут быть пустыми";
            sendFieldsAreEmpty(exchange, response);
        }
    }


    private void handleGetEpics(HttpExchange exchange) throws IOException {
        if (taskManager.getEpicTasks().isEmpty()) {
            sendNotFound(exchange, "Список задач пуст");
        } else {
            sendText(exchange, gson.toJson(taskManager.getEpicTasks()));
        }
    }

    private void handleGetEpicSubtasks(HttpExchange exchange) throws IOException {
        int id = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
        if (taskManager.getEpicTask(id).getSubTasks().isEmpty()) {
            sendNotFound(exchange, "Список задач пуст");
        } else {
            sendText(exchange, gson.toJson(taskManager.getEpicTask(id).getSubTasks()));
        }
    }

    private void handleGetEpicById(HttpExchange exchange) throws IOException {
        int id = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
        if (taskManager.getEpicTask(id) == null) {
            sendNotFound(exchange, "Задачи с таким id нет");
        } else {
            sendText(exchange, gson.toJson(taskManager.getEpicTask(id)));
        }
    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        int id = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
        if (taskManager.getEpicTask(id) == null) {
            sendNotFound(exchange, "Задачи с таким id нет");
        } else {
            taskManager.deleteById(id);
            sendText(exchange, "Задача успешно удалена!");
        }
    }

    private Optional<EpicTask> parseEpicTask(InputStream bodyInputStream) throws IOException {
        String body = new String(bodyInputStream.readAllBytes(), StandardCharsets.UTF_8);
        try {
            EpicTask task = gson.fromJson(body, EpicTask.class);
            return task != null ? Optional.of(task) : Optional.empty();
        } catch (JsonSyntaxException ex) {
            return Optional.empty();
        }
    }
}
