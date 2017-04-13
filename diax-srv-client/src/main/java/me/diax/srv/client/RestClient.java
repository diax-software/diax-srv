package me.diax.srv.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

/**
 * @author Sven Olderaan (s.olderaan@i-real.nl)
 */
class RestClient {

    private HttpURLConnection getConnection(String path, String method) throws IOException {
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(Objects.equals("POST", method) || Objects.equals("PUT", method));
        connection.setRequestMethod(method);
        return connection;
    }

    /**
     * Executes a HTTP GET request to the given URL
     *
     * @param path the path to execute a request
     * @return the response
     * @throws IOException if the connection failed
     */
    String doGet(String path, String accept) throws IOException {
        HttpURLConnection connection = getConnection(path, "GET");
        connection.setRequestProperty("Accept", accept);
        return getResponse(connection);
    }

    /**
     * Executes a HTTP POST request to the given URL
     *
     * @param path the path to execute a request
     * @param body the request body
     * @return the response
     * @throws IOException if the connection failed
     */
    public String doPost(String path, String body, String contentType) throws IOException {
        HttpURLConnection connection = getConnection(path, "POST");
        connection.setRequestProperty("Content-Type", contentType);
        OutputStream stream = connection.getOutputStream();
        stream.write(body.getBytes());
        stream.flush();
        return getResponse(connection);
    }

    /**
     * Executes a HTTP PUT request to the given URL
     *
     * @param path the path to execute a request
     * @param body the request body
     * @return the response
     * @throws IOException if the connection failed
     */
    public String doPut(String path, String body, String contentType) throws IOException {
        HttpURLConnection connection = getConnection(path, "PUT");
        connection.setRequestProperty("Content-Type", contentType);
        OutputStream stream = connection.getOutputStream();
        stream.write(body.getBytes());
        stream.flush();
        return getResponse(connection);
    }

    private String getResponse(HttpURLConnection connection) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String output;
            StringBuilder sb = new StringBuilder();
            while ((output = reader.readLine()) != null) {
                sb.append(output);
            }
            return sb.toString();
        }
    }
}
