package com.expense.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
public class LoginDto implements Serializable {
    private String username;
    private String password;
}
