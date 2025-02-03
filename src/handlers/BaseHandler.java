package handlers;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import enums.Endpoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseHandler implements HttpHandler {

    protected final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).registerTypeAdapter(Duration.class, new DurationAdapter()).setPrettyPrinting().create();

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(404, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendHasInteractions(HttpExchange h) throws IOException {
        byte[] resp = "Задача пересекается с предыдущими".getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(406, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendFieldsAreEmpty(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(400, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendMethodNotAllowed(HttpExchange h) throws IOException {
        byte[] resp = "METHOD_NOT_ALLOWED".getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(400, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    public Gson getGson() {
        return gson;
    }


    protected Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
            switch (requestMethod) {
                case "GET" -> {
                    return Endpoint.GET_TASKS;
                }
                case "POST" -> {
                    return Endpoint.POST_ADD_TASK;
                }
            }
        } else if (pathParts.length == 3 && pathParts[1].equals("tasks")) {
            switch (requestMethod) {
                case "DELETE" -> {
                    return Endpoint.DELETE_TASK;
                }
                case "POST" -> {
                    return Endpoint.POST_UPDATE_TASK;
                }
                case "GET" -> {
                    return Endpoint.GET_TASK_BY_ID;
                }
            }
        } else if (pathParts.length == 2 && pathParts[1].equals("epics")) {
            switch (requestMethod) {
                case "GET" -> {
                    return Endpoint.GET_EPICS;
                }
                case "POST" -> {
                    return Endpoint.POST_ADD_EPIC;
                }
            }
        } else if (pathParts.length == 3 && pathParts[1].equals("epics")) {
            switch (requestMethod) {
                case "DELETE" -> {
                    return Endpoint.DELETE_EPIC;
                }
                case "GET" -> {
                    return Endpoint.GET_EPIC_BY_ID;
                }
            }
        } else if (pathParts.length == 4 && pathParts[1].equals("epics") && pathParts[3].equals("subtasks")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_EPIC_SUBTASKS;
            }
        } else if (pathParts.length == 2 && pathParts[1].equals("history")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_HISTORY;
            }
        } else if (pathParts.length == 2 && pathParts[1].equals("prioritized")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_PRIORITIZED;
            }
        } else if (pathParts.length == 2 && pathParts[1].equals("subtasks")) {
            switch (requestMethod) {
                case "GET" -> {
                    return Endpoint.GET_SUBTASKS;
                }
                case "POST" -> {
                    return Endpoint.POST_ADD_SUBTASK;
                }
            }
        } else if (pathParts.length == 3 && pathParts[1].equals("subtasks")) {
            switch (requestMethod) {
                case "DELETE" -> {
                    return Endpoint.DELETE_SUBTASK;
                }
                case "POST" -> {
                    return Endpoint.POST_UPDATE_SUBTASK;
                }
                case "GET" -> {
                    return Endpoint.GET_SUBTASK_BY_ID;
                }
            }
        }
        return Endpoint.UNKNOWN;
    }


    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                processGet(exchange, endpoint);
                break;
            case "POST":
                processPost(exchange, endpoint);
                break;
            case "DELETE":
                processDelete(exchange, endpoint);
                break;
            default:
                sendMethodNotAllowed(exchange);
        }
    }

    protected void processGet(HttpExchange exchange, Endpoint endpoint) throws IOException {
        sendMethodNotAllowed(exchange);
    }

    protected void processPost(HttpExchange exchange, Endpoint endpoint) throws IOException {
        sendMethodNotAllowed(exchange);
    }

    protected void processDelete(HttpExchange exchange, Endpoint endpoint) throws IOException {
        sendMethodNotAllowed(exchange);
    }

}
