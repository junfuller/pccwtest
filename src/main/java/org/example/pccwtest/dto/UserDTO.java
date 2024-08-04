package org.example.pccwtest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Data Transfer Object for user registration and details.
 * This class is used to capture and validate user details.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    /**
     * The username of the user.
     * This field is mandatory and cannot be blank.
     * Error message if blank: "{username.notblank}"
     */
    @NotBlank(message = "{username.notblank}")
    String username;

    /**
     * The email address of the user.
     * This field is mandatory and cannot be blank.
     * The email must be in a valid format.
     * Error message if blank: "{email.notblank}"
     * Error message if invalid: "{email.invalid}"
     */
    @Email(message = "{email.invalid}")
    @NotBlank(message = "{email.notblank}")
    String email;

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
