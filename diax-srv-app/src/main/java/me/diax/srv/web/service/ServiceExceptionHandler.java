package me.diax.srv.web.service;

import me.diax.srv.stubs.service.ServiceException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ServiceExceptionHandler implements ExceptionMapper<ServiceException> {

    @Override
    public Response toResponse(ServiceException ex) {
        return Response.status(ex.getCode())
                .entity(ex)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}
