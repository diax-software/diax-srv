package me.diax.srv.client;

import me.diax.srv.common.DiaxJsonContext;
import me.diax.srv.stubs.service.ServiceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

class DiaxServiceClient {

    final DiaxJsonContext context;
    final String endpoint;


    DiaxServiceClient(String endpoint) {
        this.endpoint = endpoint;
        try {
            context = DiaxJsonContext.getInstance();
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpURLConnection getConnection(String path, String method) throws IOException {
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(Objects.equals("POST", method) || Objects.equals("PUT", method));
        connection.setRequestMethod(method);
        return connection;
    }

    /**
     * Executes a HTTP POST request to the given URL
     *
     * @param path the path to execute a request
     * @param body the request body
     * @return the response
     * @throws ServiceException if the something went wrong
     */
    String doPost(String path, String body, String contentType) throws ServiceException {
        try {
            HttpURLConnection connection = getConnection(path, "POST");
            connection.setRequestProperty("Content-Type", contentType);
            OutputStream stream = connection.getOutputStream();
            stream.write(body.getBytes());
            stream.flush();
            return getResponse(connection);
        } catch (IOException e) {
            throw new ServiceException(e, 0);
        }
    }

    /**
     * Executes a HTTP PUT request to the given URL
     *
     * @param path the path to execute a request
     * @param body the request body
     * @return the response
     * @throws ServiceException if the semething failed
     */
    String doPut(String path, String body, String contentType) throws ServiceException {
        try {
            HttpURLConnection connection = getConnection(path, "PUT");
            connection.setRequestProperty("Content-Type", contentType);
            OutputStream stream = connection.getOutputStream();
            stream.write(body.getBytes());
            stream.flush();
            return getResponse(connection);
        } catch (IOException e) {
            throw new ServiceException(e, 0);
        }
    }

    private String getResponse(HttpURLConnection connection) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String output;
            StringBuilder sb = new StringBuilder();
            while ((output = reader.readLine()) != null) {
                sb.append(output);
            }
            return sb.toString();
        } catch (IOException e) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                String output;
                StringBuilder sb = new StringBuilder();
                while ((output = reader.readLine()) != null) {
                    sb.append(output);
                }
                return sb.toString();
            }
        }
    }

    /**
     * Executes a HTTP GET request to the given URL
     *
     * @param path the path to execute a request
     * @return the response
     * @throws ServiceException if the something went wrong
     */
    String doGet(String path, String accept) throws ServiceException {
        try {
            HttpURLConnection connection = getConnection(path, "GET");
            connection.setRequestProperty("Accept", accept);
            return getResponse(connection);
        } catch (IOException e) {
            throw new ServiceException(e, 0);
        }
    }
}
