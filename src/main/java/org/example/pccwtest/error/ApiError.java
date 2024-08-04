package org.example.pccwtest.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * This class is used to encapsulate detailed error information
 * for API responses.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ApiError {

    /**
     * The timestamp when the error occurred.
     */
    LocalDateTime timestamp;

    /**
     * The HTTP status code of the error response.
     */
    int status;

    /**
     * A brief description of the error.
     */
    String error;

    /**
     * The path of the request that caused the error.
     */
    String path;

    /**
     * A map of validation errors, where the key is the field name
     * and the value is the associated validation error message.
     */
    Map<String, String> validationErrors;
}
