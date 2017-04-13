package me.diax.srv.client;

import org.reflections.Reflections;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Set;

class DiaxServiceClient {

    static final String TYPE = "application/json";

    final RestClient client;
    final String endpoint;

    private final Unmarshaller unmarshaller;
    private final Marshaller marshaller;

    DiaxServiceClient(String endpoint) {
        try {
            this.client = new RestClient();
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

    <T> T unmarshall(String obj, Class<T> type) throws JAXBException {
        try (StringReader reader = new StringReader(obj)) {
            StreamSource source = new StreamSource(reader);
            return unmarshaller.unmarshal(source, type).getValue();
        }
    }

    String marshall(Object obj) throws JAXBException, IOException {
        try (StringWriter writer = new StringWriter()) {
            marshaller.marshal(obj, writer);
            return writer.toString();
        }
    }
}
