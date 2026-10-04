package dev.timjelenz.openlocationapi.controllers;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import dev.timjelenz.openlocationapi.exceptions.clients.APIResourceNotFoundException;
import dev.timjelenz.openlocationapi.exceptions.clients.ServerSideException;
import dev.timjelenz.openlocationapi.exceptions.service.IdentUpdateException;
import dev.timjelenz.openlocationapi.exceptions.service.location.LocationNotFoundException;
import dev.timjelenz.openlocationapi.exceptions.service.user.InvalidCredentialsException;
import dev.timjelenz.openlocationapi.exceptions.service.user.UserAlreadyExistsException;
import dev.timjelenz.openlocationapi.exceptions.service.user.UserNotFoundException;
import dev.timjelenz.openlocationapi.exceptions.service.weathersnapshot.WeatherSnapshotNotFoundException;
import jakarta.validation.ConstraintViolationException;

/**
 * Maps application exceptions to RFC 7807 problem responses.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    /**
     * Handles missing locations.
     *
     * @param exception the thrown exception
     * @return a 404 problem response
     */
    @ExceptionHandler(LocationNotFoundException.class)
    public ProblemDetail handleLocationNotFound(
        final LocationNotFoundException exception
    ) {
        return problem(HttpStatus.NOT_FOUND, "Location not found");
    }

    /**
     * Handles missing users.
     *
     * @param exception the thrown exception
     * @return a 404 problem response
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFound(
        final UserNotFoundException exception
    ) {
        return problem(HttpStatus.NOT_FOUND, "User not found");
    }

    /**
     * Handles missing snapshots.
     *
     * @param exception the thrown exception
     * @return a 404 problem response
     */
    @ExceptionHandler(WeatherSnapshotNotFoundException.class)
    public ProblemDetail handleSnapshotNotFound(
        final WeatherSnapshotNotFoundException exception
    ) {
        return problem(HttpStatus.NOT_FOUND, "Weather snapshot not found");
    }

    /**
     * Handles duplicate users and other unique constraint violations.
     *
     * @param exception the thrown exception
     * @return a 409 problem response
     */
    @ExceptionHandler({
        UserAlreadyExistsException.class,
        DataIntegrityViolationException.class
    })
    public ProblemDetail handleConflict(final RuntimeException exception) {
        return problem(
            HttpStatus.CONFLICT,
            "Request conflicts with existing data"
        );
    }

    /**
     * Handles invalid credentials.
     *
     * @param exception the thrown exception
     * @return a 401 problem response
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail handleInvalidCredentials(
        final InvalidCredentialsException exception
    ) {
        return problem(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }

    /**
     * Handles rejected identity updates.
     *
     * @param exception the thrown exception
     * @return a 409 problem response
     */
    @ExceptionHandler(IdentUpdateException.class)
    public ProblemDetail handleIdentUpdate(
        final IdentUpdateException exception
    ) {
        return problem(
            HttpStatus.CONFLICT,
            "Identity can not be updated to the same value"
        );
    }

    /**
     * Handles resources missing in an upstream API.
     *
     * @param exception the thrown exception
     * @return a 502 problem response
     */
    @ExceptionHandler(APIResourceNotFoundException.class)
    public ProblemDetail handleUpstreamResourceNotFound(
        final APIResourceNotFoundException exception
    ) {
        return problem(
            HttpStatus.BAD_GATEWAY,
            "Upstream resource not found"
        );
    }

    /**
     * Handles errors of an upstream API.
     *
     * @param exception the thrown exception
     * @return a 502 problem response
     */
    @ExceptionHandler(ServerSideException.class)
    public ProblemDetail handleUpstreamError(
        final ServerSideException exception
    ) {
        return problem(HttpStatus.BAD_GATEWAY, "Upstream service error");
    }

    /**
     * Handles invalid request bodies.
     *
     * @param exception the thrown exception
     * @return a 400 problem response including field errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(
        final MethodArgumentNotValidException exception
    ) {
        final ProblemDetail problem = problem(
            HttpStatus.BAD_REQUEST,
            "Validation failed"
        );
        final Map<String, String> errors = new LinkedHashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error ->
            errors.putIfAbsent(error.getField(), error.getDefaultMessage())
        );
        problem.setProperty("errors", errors);

        return problem;
    }

    /**
     * Handles invalid request parameters.
     *
     * @param exception the thrown exception
     * @return a 400 problem response
     */
    @ExceptionHandler({
        HandlerMethodValidationException.class,
        ConstraintViolationException.class,
        MethodArgumentTypeMismatchException.class,
        MissingServletRequestParameterException.class,
        HttpMessageNotReadableException.class,
        IllegalArgumentException.class
    })
    public ProblemDetail handleBadRequest(final Exception exception) {
        final String detail = exception.getMessage() != null
            ? exception.getMessage()
            : "Invalid request";

        return problem(HttpStatus.BAD_REQUEST, detail);
    }

    private static ProblemDetail problem(
        final HttpStatus status,
        final String detail
    ) {
        final ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            status,
            detail
        );
        problem.setTitle(status.getReasonPhrase());

        return problem;
    }
}
