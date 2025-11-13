package com.nvtdevmaster.lesson06.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDTO {

    private Long id;

    private String username;
    private String password;
    private String fullName;
    private String address;
    private String phone;
    private String email;

    private LocalDate birthDay;
    private boolean active;
}
