package team_f.jsonconnector.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import team_f.jsonconnector.entities.JSONObjectEntity;
import java.io.IOException;
import java.io.Reader;

public class ReadHelper {
    public static <T extends JSONObjectEntity> JSONObjectEntity readJSONObject(Reader reader, Class<T> jsonObjectEntityClass) {
        if(reader != null && jsonObjectEntityClass != null) {
            ObjectMapper mapper = new ObjectMapper();

            try {
                return mapper.readValue(reader, jsonObjectEntityClass);
            } catch (IOException e) {
                return null;
            }
        }

        return null;
    }

    public static <T extends JSONObjectEntity> JSONObjectEntity readJSONObject(String content, Class<T> jsonObjectEntityClass) {
        if(content != null && jsonObjectEntityClass != null) {
            ObjectMapper mapper = new ObjectMapper();

            try {
                return mapper.readValue(content, jsonObjectEntityClass);
            } catch (IOException e) {
            }
        }

        return null;
    }
}