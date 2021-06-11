package web.task.track.dto;

import lombok.Data;
import java.util.Set;

@Data
public class RegistrationDto {

    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Set<String> roles;

}
