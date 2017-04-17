package me.diax.srv.client;

import me.diax.srv.stubs.service.ServiceException;
import org.eclipse.persistence.exceptions.DescriptorException;
import org.reflections.Reflections;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.Set;

class DiaxServiceClient {

    static final String TYPE = "application/json";

    final String endpoint;

    private final Unmarshaller unmarshaller;
    private final Marshaller marshaller;

    DiaxServiceClient(String endpoint) {
        try {
            this.endpoint = endpoint.endsWith("/") ? endpoint : endpoint + "/";

            Reflections reflections = new Reflections("me.diax.srv.stubs");
            Set<Class<?>> types = reflections.getTypesAnnotatedWith(XmlRootElement.class);
            JAXBContext context = JAXBContext.newInstance(types.toArray(new Class[types.size()]));

            unmarshaller = context.createUnmarshaller();
            unmarshaller.setProperty("eclipselink.media-type", TYPE);
            unmarshaller.setProperty("eclipselink.json.include-root", false);

            marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty("eclipselink.media-type", TYPE);
            marshaller.setProperty("eclipselink.json.include-root", false);
        } catch (JAXBException e) {
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

    <T> T unmarshall(String obj, Class<T> type) throws ServiceException {
        try (StringReader reader = new StringReader(obj)) {
            try {
                return unmarshaller.unmarshal(new StreamSource(reader), type).getValue();
            } catch (DescriptorException e) {
                reader.reset();
                throw unmarshaller.unmarshal(new StreamSource(reader), ServiceException.class).getValue();
            }
        } catch (IOException | JAXBException e) {
            throw new ServiceException(e, 0);
        }
    }

    String marshall(Object obj) throws ServiceException {
        try (StringWriter writer = new StringWriter()) {
            marshaller.marshal(obj, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new ServiceException(e, 0);
        }
    }
}
