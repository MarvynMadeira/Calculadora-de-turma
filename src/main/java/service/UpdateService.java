package service;

import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class UpdateService {
    private static final String CURRENT_VERSION = "1.0.0"; //Alterar manualmente

    private final static String VERSION_URL = "https://raw.githubusercontent.com/MarvynMadeira/Calculadora-de-turma/master/version.json";

    public Optional<UpdateInfo> checkForUpdates() {
        if(VERSION_URL.equals("https://raw.githubusercontent.com/MarvynMadeira/Calculadora-de-turma/master/version.json")) {
            return Optional.empty();
        }

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(VERSION_URL))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());

            String latestVersion = json.getString("version");
            String downloadUrl = json.getString("url");

            if (!latestVersion.equals(CURRENT_VERSION)) {
                return Optional.of(new UpdateInfo(latestVersion, downloadUrl));
            }
        } catch (Exception e) {
            System.err.println("Falha ao verificar atualizações:" + e.getMessage());
        }
        return Optional.empty();
    }
    public record UpdateInfo(String latestVersion, String downloadUrl) {}
}