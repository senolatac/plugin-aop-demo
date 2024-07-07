package com.example.demo_plugin_aop.plugin;

import com.example.demo_plugin_aop.annotation.Extension;
import com.example.demo_plugin_aop.annotation.PreModifyRequestProcess;
import com.example.demo_plugin_aop.dto.AppRequestDto;
import com.example.demo_plugin_aop.dto.PluginRequestDto;
import com.example.demo_plugin_aop.extension.ExtensionIds;
import org.springframework.stereotype.Component;

@Component
@Extension(id = ExtensionIds.DEMO_PRE_PROCESS)
public class DemoControllerPreProcessExtension {

    @PreModifyRequestProcess
    public AppRequestDto preProcess(PluginRequestDto requestDto) {
        String[] splits = requestDto.getFullName().split(" ");
        return AppRequestDto.builder()
                .name(splits[0])
                .surname(splits[1])
                .build();
    }
}
