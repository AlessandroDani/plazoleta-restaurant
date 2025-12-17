package com.pragma.powerup.infrastructure.out.http.feign;

import com.pragma.powerup.infrastructure.exception.*;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        HttpStatus status = HttpStatus.valueOf(response.status());

        switch (status) {
            case BAD_REQUEST:
                return new InvalidDataException();
            case UNAUTHORIZED:
                return new UserAuthenticationException();
            case FORBIDDEN:
                return new ActionForbiddenException();
            case NOT_FOUND:
                return new ResourceNotFoundException();
            case INTERNAL_SERVER_ERROR:
                return new ExternalServiceFailureException();
            case SERVICE_UNAVAILABLE:
                return new ExternalServiceUnavailableException();
            default:
                return new Exception();
        }
    }
}
