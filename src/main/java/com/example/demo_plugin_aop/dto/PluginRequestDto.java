package com.example.demo_plugin_aop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PluginRequestDto {

    @NotBlank
    private String fullName;
}
