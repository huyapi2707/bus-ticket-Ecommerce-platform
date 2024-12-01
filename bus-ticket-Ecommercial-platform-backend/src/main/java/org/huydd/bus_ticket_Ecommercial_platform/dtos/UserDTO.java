package org.huydd.bus_ticket_Ecommercial_platform.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@Builder
public class UserDTO implements Serializable {
    private Long id;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String role;
    private String phone;
    private String avatar;

    @JsonIgnore
    private MultipartFile file;

}
