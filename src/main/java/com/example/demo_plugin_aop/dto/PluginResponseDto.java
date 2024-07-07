package com.example.demo_plugin_aop.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PluginResponseDto {

    private String pluginResponse;

    private boolean success;
}
