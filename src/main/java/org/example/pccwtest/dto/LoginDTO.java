package org.example.pccwtest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    @NotBlank(message = "{username.notblank}")
    String username;

    @NotBlank(message = "{password.notblank}")
    @Size(min = 8, message = "{password.size}")
    String password;
}
