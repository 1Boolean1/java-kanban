package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.InMemoryTaskManager;
import enums.Endpoint;

import java.io.IOException;
import java.util.Objects;

public class PrioritizedHandler extends BaseHandler implements HttpHandler {

    private final InMemoryTaskManager taskManager;

    public PrioritizedHandler(InMemoryTaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        if (Objects.requireNonNull(endpoint) == Endpoint.GET_PRIORITIZED) {
            handleGetPrioritized(exchange);
        } else {
            sendNotFound(exchange, "Такого эндпоинта не существует");
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
