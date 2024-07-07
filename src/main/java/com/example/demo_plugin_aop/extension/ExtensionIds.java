package com.example.demo_plugin_aop.extension;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExtensionIds {
    public static final String DEMO_PRE_PROCESS = "demo-pre-process";
    public static final String DEMO_POST_PROCESS = "demo-post-process";
    public static final String DEMO_OVERRIDE_PROCESS = "demo-override-process";
}
