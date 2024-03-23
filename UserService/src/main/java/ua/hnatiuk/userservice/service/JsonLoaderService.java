package ua.hnatiuk.userservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hnatiuk Volodymyr on 23.03.2024.
 */
@Service
@RequiredArgsConstructor
public class JsonLoaderService {

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;
    @Getter
    private final Map<String, Integer> marks = new HashMap<>();
    @Getter
    private final Map<String, Integer> models = new HashMap<>();

    @PostConstruct
    public void init() {
        try {
            loadMarks();
            loadModels();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void loadMarks() throws IOException {

        Resource resource = resourceLoader.getResource("classpath:static/json/marks.json");

        fillMap(resource, marks);
    }

    private void loadModels() throws IOException {

        Resource resource = resourceLoader.getResource("classpath:static/json/models.json");

        fillMap(resource, models);
    }

    private void fillMap(Resource resource, Map<String, Integer> map) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(resource.getInputStream());

        for (JsonNode entry : jsonNode) {
            String name = entry.get("name").asText();
            int value = entry.get("value").asInt();
            if (map.containsKey(name)) {
                if (map.get(name) > value) {
                    map.put(name, value);
                }
                else continue;
            }
            map.put(name, value);
        }

        resource.getInputStream().close();
    }

}

