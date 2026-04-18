package com.smartcampus;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Logger;

@Provider
public class ApiLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger logger = Logger.getLogger(ApiLoggingFilter.class.getName());

    // Logs every incoming request
    @Override
    public void filter(ContainerRequestContext requestContext) {
        logger.info("REQUEST: " + requestContext.getMethod()
                + " " + requestContext.getUriInfo().getRequestUri());
    }

    // Logs every outgoing response
    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) {
        logger.info("RESPONSE: " + responseContext.getStatus());
    }
}