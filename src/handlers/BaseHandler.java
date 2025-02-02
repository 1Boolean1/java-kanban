package handlers;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import enums.Endpoint;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseHandler {

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

    private static class DurationAdapter implements JsonSerializer<Duration>, JsonDeserializer<Duration> {

        @Override
        public JsonElement serialize(Duration duration, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(duration.toString()); // Преобразуем в строку (ISO-8601)
        }

        @Override
        public Duration deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            return Duration.parse(json.getAsString()); // Преобразуем обратно из строки
        }
    }

    private static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        @Override
        public JsonElement serialize(LocalDateTime dateTime, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(dateTime.format(formatter)); // Преобразуем в строку
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            return LocalDateTime.parse(json.getAsString(), formatter); // Преобразуем обратно
        }
    }
}
