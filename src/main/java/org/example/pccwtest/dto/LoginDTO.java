package org.example.pccwtest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Data Transfer Object for user login.
 * This class is used to capture and validate user login details.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    /**
     * The username of the user.
     * This field is mandatory and cannot be blank.
     * Error message if blank: "{username.notblank}"
     */
    @NotBlank(message = "{username.notblank}")
    String username;

    /**
     * The password of the user.
     * This field is mandatory and cannot be blank.
     * It must have a minimum length of 8 characters.
     * Error message if blank: "{password.notblank}"
     * Error message if size requirement not met: "{password.size}"
     */
    @NotBlank(message = "{password.notblank}")
    @Size(min = 8, message = "{password.size}")
    String password;
}
