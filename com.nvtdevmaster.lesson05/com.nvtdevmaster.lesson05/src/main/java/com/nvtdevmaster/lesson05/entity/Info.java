package com.nvtdevmaster.lesson05.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Info {
    private String name;
    private String nickname;
    private String email;
    private String website;
}
