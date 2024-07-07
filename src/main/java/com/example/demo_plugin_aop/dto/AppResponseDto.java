package com.example.demo_plugin_aop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppResponseDto {

    private String response;

    public AppResponseDto(String prefix, AppRequestDto requestDto) {
        this.response = prefix + "-" + requestDto.getName() + "-" + requestDto.getSurname();
    }
}
