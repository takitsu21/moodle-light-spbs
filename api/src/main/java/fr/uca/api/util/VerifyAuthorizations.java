package fr.uca.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerifyAuthorizations {

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
            System.out.println("Here verify autho");
            CloseableHttpResponse resp = executePost(
                    "http://localhost:8081/api/auth/verify",
                    null,
                    headers);
            System.out.println("Resp:"+resp.toString());
            String jsonString = EntityUtils.toString(resp.getEntity());
            System.out.println("verify: "+jsonString);
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
            }
            ret.put("success", success && successRoles);
            ret.put("username", jsonObject.getAsJsonObject("user").get("username").getAsString());
        } catch (IOException e) {
            ret.put("success", false);
            ret.put("error", e.toString());
        }
        return ret;
    }

    public static boolean isSuccess(Map<String, Object> resp) {
        return (Boolean)resp.get("success");
    }
}
