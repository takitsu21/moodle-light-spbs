package fr.uca.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import fr.uca.api.models.ERole;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;

public class VerifyAuthorizations {

    private final static CloseableHttpClient httpClient = HttpClients.createDefault();

    private static CloseableHttpResponse executePost(String url,
                                                     Object entity,
                                                     Map<String, String> headers) throws IOException {
        HttpPost request = new HttpPost(url);
        request.addHeader("content-type", "application/json");
        request.addHeader("authorization", headers.get("authorization"));
        ObjectMapper ObjMapper = new ObjectMapper();
        request.setEntity(new StringEntity(ObjMapper.writeValueAsString(entity)));
        return httpClient.execute(request);
    }


    public static boolean verify(Map<String, String> headers, Object entity, String... roles) throws IOException {
        CloseableHttpResponse resp = executePost(
                "http://localhost:8081/api/auth/verify",
                entity,
                headers);
        String jsonString = EntityUtils.toString(resp.getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        boolean success = jsonObject.get("success").getAsBoolean();
        if (success) {
            System.out.println(jsonObject);
            JsonArray rolesArray = jsonObject.getAsJsonObject("user").get("roles").getAsJsonArray();
            System.out.println(rolesArray);
            if (roles.length > 0) {
                List<String> lRoles = Arrays.asList(roles);
                for (JsonElement jsonElement : rolesArray) {
                    if (!lRoles.contains(jsonElement.getAsJsonObject().get("name").getAsString())) {
                        System.out.println("ROLE NOT FOUND " + jsonElement.getAsJsonObject().get("name").getAsString());
                        return false;
                    }
                }
            }
        }
        return success;
    }
}
