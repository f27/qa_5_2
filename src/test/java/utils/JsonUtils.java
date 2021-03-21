package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonUtils {
    public static Map<String, String> mapFromJson (String json){
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = null;
        try {
            data = mapper.readValue(json,
                    new TypeReference<Map<String, String>>(){});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return data;
    }
}
