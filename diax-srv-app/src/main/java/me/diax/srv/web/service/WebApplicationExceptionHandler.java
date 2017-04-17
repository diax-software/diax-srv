package me.diax.srv.web.service;

import me.diax.srv.stubs.service.ServiceException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionHandler implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException ex) {
        ServiceException exception = new ServiceException();
        exception.setCode(ex.getResponse().getStatus());
        exception.setMessage(ex.getMessage());

        return Response.status(exception.getCode())
                .entity(exception)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}

