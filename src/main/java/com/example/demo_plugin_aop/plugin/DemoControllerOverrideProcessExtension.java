package com.example.demo_plugin_aop.plugin;

import com.example.demo_plugin_aop.annotation.Extension;
import com.example.demo_plugin_aop.annotation.OverrideProcess;
import com.example.demo_plugin_aop.dto.AppRequestDto;
import com.example.demo_plugin_aop.dto.PluginResponseDto;
import com.example.demo_plugin_aop.extension.ExtensionIds;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Extension(id = ExtensionIds.DEMO_OVERRIDE_PROCESS)
public class DemoControllerOverrideProcessExtension {

    @OverrideProcess
    public ResponseEntity<PluginResponseDto> overrideProcess(AppRequestDto requestDto) {
        PluginResponseDto pluginResponseDto = PluginResponseDto.builder()
                .pluginResponse("plugin-override-result-" + requestDto.getName() + "-" + requestDto.getSurname())
                .success(true)
                .build();
        return ResponseEntity.ok(pluginResponseDto);
    }
}
