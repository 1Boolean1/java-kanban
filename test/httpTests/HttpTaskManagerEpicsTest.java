package httpTests;

import com.google.gson.Gson;
import controllers.Managers;
import controllers.TaskManager;
import handlers.BaseHandler;
import model.EpicTask;
import server.HttpTaskServer;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerEpicsTest {

    TaskManager manager = Managers.getDefault();
    BaseHandler handler = new BaseHandler();
    HttpTaskServer taskServer = new HttpTaskServer(manager);

    Gson gson = handler.getGson();

    public HttpTaskManagerEpicsTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteTasks();
        manager.deleteSubTasks();
        manager.deleteEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        EpicTask task = new EpicTask("Test 2", "Testing task 2");

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = manager.getEpicTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        Assertions.assertEquals(1, tasksFromManager.size(), "Неверное кол-во задач");
        Assertions.assertEquals("Test 2", tasksFromManager.getFirst().getTaskName(), "Некорректное имя задачи");
    }

    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        EpicTask task = new EpicTask("Test 2", "Testing task 2");

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = manager.getEpicTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        Assertions.assertEquals(1, tasksFromManager.size(), "Неверное кол-во задач");

        int taskId = tasksFromManager.getFirst().getTaskId();
        URI urlDelete = URI.create("http://localhost:8080/epics/" + taskId);

        HttpRequest requestDelete = HttpRequest.newBuilder()
                .uri(urlDelete)
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        HttpResponse<String> deleteResponse = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, deleteResponse.statusCode(), "Ошибка при удалении задачи");

        tasksFromManager = manager.getEpicTasks();

        Assertions.assertEquals(0, tasksFromManager.size(), "Неверное кол-во задач после удаления");
    }
}