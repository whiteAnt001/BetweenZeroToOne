package me.bzo.bzo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RegisterReuqest {
    private String name;
    private String email;
    private String password;
}
