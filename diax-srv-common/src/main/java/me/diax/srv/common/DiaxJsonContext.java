package me.diax.srv.common;

import me.diax.srv.stubs.service.ServiceException;
import org.eclipse.persistence.exceptions.DescriptorException;
import org.reflections.Reflections;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Set;

/**
 * Represents the {@code DiaxJsonContext}.<br>
 * It loads models from the stubs package, and provides a simple interface to serialize them.
 */
public class DiaxJsonContext {

    private static final String PACKAGE = "me.diax.srv.stubs";
    private static final String MEDIA_TYPE = "application/json";
    private static DiaxJsonContext context;

    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;

    /**
     * Creates a new context based on the given package
     *
     * @param packageName the package where to look for models
     */
    private DiaxJsonContext(String packageName) {
        try {
            Reflections reflections = new Reflections(packageName);
            Set<Class<?>> types = reflections.getTypesAnnotatedWith(XmlRootElement.class);
            JAXBContext context = JAXBContext.newInstance(types.toArray(new Class[types.size()]));

            unmarshaller = context.createUnmarshaller();
            unmarshaller.setProperty("eclipselink.media-type", MEDIA_TYPE);
            unmarshaller.setProperty("eclipselink.json.include-root", false);

            marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty("eclipselink.media-type", MEDIA_TYPE);
            marshaller.setProperty("eclipselink.json.include-root", false);
        } catch (JAXBException e) {
            throw new RuntimeException("failed to initialize context", e);
        }
    }

    /**
     * Gets the current instance of the {@code DiaxJsonContext}
     *
     * @return current context
     */
    public static DiaxJsonContext getInstance() throws ServiceException {
        if (context == null) {
            try {
                context = new DiaxJsonContext(PACKAGE);
            } catch (RuntimeException e) {
                throw new ServiceException(e, 0);
            }
        }
        return context;
    }

    /**
     * Deserializes an object
     *
     * @param obj  the object
     * @param type the datatype
     * @param <T>  the type
     * @return the object
     * @throws ServiceException if deserializing failed
     */
    public <T> T deserialize(String obj, Class<T> type) throws ServiceException {
        try (StringReader reader = new StringReader(obj)) {
            try {
                return unmarshaller.unmarshal(new StreamSource(reader), type).getValue();
            } catch (DescriptorException e) {
                reader.reset();
                throw unmarshaller.unmarshal(new StreamSource(reader), ServiceException.class).getValue();
            }
        } catch (Exception e) {
            throw new ServiceException(e, 0);
        }
    }

    /**
     * Serializes an object
     *
     * @param obj the object
     * @return the serialized string
     * @throws ServiceException if serializing failed
     */
    public String serialize(Object obj) throws ServiceException {
        try (StringWriter writer = new StringWriter()) {
            marshaller.marshal(obj, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new ServiceException(e, 0);
        }
    }

    /**
     * Gets the MIME type of this context
     *
     * @return the media type
     */
    public String getMediaType() {
        return MEDIA_TYPE;
    }

}
