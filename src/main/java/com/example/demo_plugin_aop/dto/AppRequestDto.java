package com.example.demo_plugin_aop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class AppRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;
}
