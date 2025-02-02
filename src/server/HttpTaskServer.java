package server;

import com.sun.net.httpserver.HttpServer;
import controllers.InMemoryTaskManager;
import handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    InMemoryTaskManager taskManager = new InMemoryTaskManager();
    HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);


    public HttpTaskServer() throws IOException {
    }

    public void start() {
        main();
    }

    public void stop() {
        server.stop(1);
    }

    public void main() {
        server.createContext("/tasks", new TasksHandler(taskManager));
        server.createContext("/subtasks", new SubtasksHandler(taskManager));
        server.createContext("/epics", new EpicsHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager));
        server.start();
    }

    public InMemoryTaskManager getTaskManager() {
        return taskManager;
    }
}