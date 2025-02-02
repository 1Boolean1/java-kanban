package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.InMemoryTaskManager;
import enums.Endpoint;

import java.io.IOException;
import java.util.Objects;

public class HistoryHandler extends BaseHandler implements HttpHandler {

    private final InMemoryTaskManager taskManager;

    public HistoryHandler(InMemoryTaskManager taskManager) {
        this.taskManager = taskManager;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        if (Objects.requireNonNull(endpoint) == Endpoint.GET_HISTORY) {
            handleGetHistory(exchange);
        } else {
            sendNotFound(exchange, "Такого эндпоинта не существует");
        }
    }

    private void handleGetHistory(HttpExchange exchange) throws IOException {
        if (taskManager.getHistory().isEmpty()) {
            sendNotFound(exchange, "История пуста");
        } else {
            sendNotFound(exchange, gson.toJson(taskManager.getHistory()));
        }
    }
}
