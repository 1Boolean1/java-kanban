package handlers;

import com.sun.net.httpserver.HttpExchange;
import controllers.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHandler {

    private final TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }


    @Override
    protected void processGet(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getPath().split("/").length == 2) {
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
