package fr.uca.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerifyAuthorizations {


    public static String codeRunnerHost =
            System.getenv("CODE_RUNNER_HOST") != null ? System.getenv("CODE_RUNNER") :
            "http://localhost:8082/";

    public static String authHost =
            System.getenv("AUTH_HOST") != null ?
                    System.getenv("AUTH_HOST") :
                    "http://localhost:8081/";

    public static String apiHost =
            System.getenv("API_HOST") != null ? System.getenv("API_HOST") :
                    "http://localhost:8080/";

    private final static CloseableHttpClient httpClient = HttpClients.createDefault();

    public static CloseableHttpResponse executePost(String url,
                                                    Object entity,
                                                    Map<String, String> headers) throws IOException {
        HttpPost request = new HttpPost(url);
        request.addHeader("content-type", "application/json");
        if (headers != null) {
            request.addHeader("authorization", headers.get("authorization"));
        }
        ObjectMapper ObjMapper = new ObjectMapper();
        request.setEntity(new StringEntity(ObjMapper.writeValueAsString(entity)));
        return httpClient.execute(request);
    }


    public static boolean verifyRoles(JsonArray rolesArray, String... roles) {
        if (roles.length > 0) {
            List<String> lRoles = Arrays.asList(roles);
            for (JsonElement jsonElement : rolesArray) {
                if (!lRoles.contains(jsonElement.getAsJsonObject().get("name").getAsString())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Map<String, Object> verify(Map<String, String> headers, String... roles) {
        Map<String, Object> ret = new HashMap<>();
        try {
            CloseableHttpResponse resp = executePost(
                    VerifyAuthorizations.authHost+ "api/auth/verify",
                    null,
                    headers);
            String jsonString = EntityUtils.toString(resp.getEntity());
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();

            Gson gson = builder.create();
            JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);

            boolean success = jsonObject.get("success").getAsBoolean();
            boolean successRoles = true;
            if (success) {
                JsonArray rolesArray = jsonObject.getAsJsonObject("user")
                        .get("roles").getAsJsonArray();
                successRoles = verifyRoles(rolesArray, roles);
                if (!successRoles) {
                    ret.put("message", "User does not have the roles " + Arrays.toString(roles));
                }
                ret.put("username", jsonObject.getAsJsonObject("user").get("username").getAsString());
            }
            ret.put("success", success && successRoles);
        } catch (IOException e) {
            ret.put("success", false);
            ret.put("error", e.toString());
            ret.put("username", "default");
        }
        return ret;
    }

    public static boolean isSuccess(Map<String, Object> resp) {
        return (Boolean)resp.get("success");
    }
}
