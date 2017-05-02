package me.diax.srv.web.service;

import me.diax.srv.stubs.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionHandler implements ExceptionMapper<WebApplicationException> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(WebApplicationException ex) {
        if (ex instanceof InternalServerErrorException) {
            logger.error("Internal Server error for request: " + uriInfo.getPath(), ex.getCause());
        }

        ServiceException exception = new ServiceException();
        exception.setCode(ex.getResponse().getStatus());
        exception.setMessage(ex.getMessage());

        return Response.status(exception.getCode())
                .entity(exception)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}

