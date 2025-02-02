package httpTests;

import com.google.gson.Gson;
import controllers.InMemoryTaskManager;
import handlers.BaseHandler;
import model.EpicTask;
import model.SubTask;
import server.HttpTaskServer;
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

public class HttpTaskManagerSubTasksTest {
    BaseHandler handler = new BaseHandler();
    HttpTaskServer taskServer = new HttpTaskServer();

    Gson gson = handler.getGson();

    InMemoryTaskManager manager = taskServer.getTaskManager();

    public HttpTaskManagerSubTasksTest() throws IOException {
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
    public void testAddSub() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("et", "");
        manager.addNewEpicTask(epicTask);
        SubTask task = new SubTask("Test 2", "Testing task 2", epicTask.getTaskId());

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());

        List<SubTask> tasksFromManager = manager.getSubTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        Assertions.assertEquals(1, tasksFromManager.size(), "Неверное кол-во задач");
        Assertions.assertEquals("Test 2", tasksFromManager.getFirst().getTaskName(), "Некорректное имя задачи");
    }

    @Test
    public void testDeleteSub() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("et", "");

        manager.addNewEpicTask(epicTask);

        SubTask task = new SubTask("Test 2", "Testing task 2", epicTask.getTaskId());

        manager.addNewSubTask(task);

        HttpClient client = HttpClient.newHttpClient();

        URI urlDelete = URI.create("http://localhost:8080/subtasks/" + task.getTaskId());

        HttpRequest requestDelete = HttpRequest.newBuilder()
                .uri(urlDelete)
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        HttpResponse<String> deleteResponse = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, deleteResponse.statusCode(), "Ошибка при удалении задачи");

        Assertions.assertEquals(0, manager.getSubTasks().size(), "Неверное кол-во задач после удаления");
    }
}
