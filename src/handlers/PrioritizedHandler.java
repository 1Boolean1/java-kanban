package handlers;

import com.sun.net.httpserver.HttpExchange;
import controllers.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHandler {

    private final TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    protected void processGet(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getPath().split("/").length == 2) {
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
