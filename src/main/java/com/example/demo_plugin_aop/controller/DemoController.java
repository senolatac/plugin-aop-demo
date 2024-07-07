package com.example.demo_plugin_aop.controller;

import com.example.demo_plugin_aop.annotation.Extendable;
import com.example.demo_plugin_aop.dto.AppRequestDto;
import com.example.demo_plugin_aop.dto.AppResponseDto;
import com.example.demo_plugin_aop.extension.ExtensionIds;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("demo")
public class DemoController {

    @PostMapping("actual-process")
    public ResponseEntity<AppResponseDto> actualProcess(@Valid @RequestBody AppRequestDto requestDto) {
        log.info("actual-process request came with: {}", requestDto);

        return ResponseEntity.ok(new AppResponseDto("pre", requestDto));
    }

    @PostMapping("pre-process")
    @Extendable(id = ExtensionIds.DEMO_PRE_PROCESS)
    public ResponseEntity<AppResponseDto> preProcess(@Valid @RequestBody AppRequestDto requestDto) {
        log.info("pre-process request came with: {}", requestDto);

        return ResponseEntity.ok(new AppResponseDto("pre", requestDto));
    }

    @PostMapping("override-process")
    @Extendable(id = ExtensionIds.DEMO_OVERRIDE_PROCESS)
    public ResponseEntity<AppResponseDto> overrideProcess(@Valid @RequestBody AppRequestDto requestDto) {
        log.info("override-process request came with: {}", requestDto);

        return ResponseEntity.ok(new AppResponseDto("override", requestDto));
    }

    @PostMapping("post-process")
    @Extendable(id = ExtensionIds.DEMO_POST_PROCESS)
    public ResponseEntity<AppResponseDto> postProcess(@Valid @RequestBody AppRequestDto requestDto) {
        log.info("post-process request came with: {}", requestDto);

        return ResponseEntity.ok(new AppResponseDto("post", requestDto));
    }
}
