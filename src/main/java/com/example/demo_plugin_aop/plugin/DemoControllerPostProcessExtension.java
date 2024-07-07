package com.example.demo_plugin_aop.plugin;

import com.example.demo_plugin_aop.annotation.Extension;
import com.example.demo_plugin_aop.annotation.PostProcess;
import com.example.demo_plugin_aop.dto.AppResponseDto;
import com.example.demo_plugin_aop.dto.PluginResponseDto;
import com.example.demo_plugin_aop.extension.ExtensionIds;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Extension(id = ExtensionIds.DEMO_POST_PROCESS)
public class DemoControllerPostProcessExtension {

    @PostProcess
    public ResponseEntity<PluginResponseDto> postProcess(ResponseEntity<AppResponseDto> responseEntity) {
        PluginResponseDto pluginResponseDto = PluginResponseDto.builder()
                .pluginResponse("plugin-post-result-" + responseEntity.getBody().getResponse())
                .success(true)
                .build();
        return ResponseEntity.ok(pluginResponseDto);
    }
}
