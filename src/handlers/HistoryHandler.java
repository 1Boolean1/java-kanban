package handlers;

import com.sun.net.httpserver.HttpExchange;
import controllers.TaskManager;
import enums.Endpoint;

import java.io.IOException;

public class HistoryHandler extends BaseHandler {

    private final TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }


    protected void processGet(HttpExchange exchange, Endpoint endpoint) throws IOException {
        if (endpoint.equals(Endpoint.GET_HISTORY)) {
            handleGetHistory(exchange);
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
