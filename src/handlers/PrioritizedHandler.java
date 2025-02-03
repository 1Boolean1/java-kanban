package handlers;

import com.sun.net.httpserver.HttpExchange;
import controllers.TaskManager;
import enums.Endpoint;

import java.io.IOException;

public class PrioritizedHandler extends BaseHandler {

    private final TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    protected void processGet(HttpExchange exchange, Endpoint endpoint) throws IOException {
        if (endpoint.equals(Endpoint.GET_PRIORITIZED)) {
            handleGetPrioritized(exchange);
        }
    }

    private void handleGetPrioritized(HttpExchange exchange) throws IOException {
        if (taskManager.getPrioritizedTasks().isEmpty()) {
            sendNotFound(exchange, "Список задач пуст");
        } else {
            sendText(exchange, gson.toJson(taskManager.getPrioritizedTasks()));
        }
    }
}
