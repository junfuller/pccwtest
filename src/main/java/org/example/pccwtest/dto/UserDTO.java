package org.example.pccwtest.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NotBlank(message = "{username.notblank}")
    String username;

    @Email(message = "{email.invalid}")
    @NotBlank(message = "{email.notblank}")
    String email;

    @NotBlank(message = "{password.notblank}")
    @Size(min = 8, message = "{password.size}")
    String password;
}
