/*
 * Copyright 2023 OPPO.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oppo.cloud.gpt.util;

import com.oppo.cloud.gpt.util.ChatGPTClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPTClientTest {

    private ChatGPTClient chatGPTClient;
    private static final String TEST_API_KEY = "sk-123";
    private static final String TEST_PROXY = "http://10.9.27.41:9997/";
    private static final String TEST_MODEL = "qwen2-7B-instruct";

    @BeforeEach
    void setUp() {
        List<String> apiKeys = Arrays.asList(TEST_API_KEY);
        chatGPTClient = new ChatGPTClient(apiKeys, TEST_PROXY, TEST_MODEL);
    }

    @Test
    void testCompletions() {
        String prompt = "You are a helpful assistant.";
        String text = "What is the capital of France?";

        String result = chatGPTClient.completions(prompt, text);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        // You might want to add more specific assertions based on the expected response
    }

    @Test
    void testCompletionsWithEmptyResponse() {
        String prompt = "You are a helpful assistant.";
        String text = "Generate an empty response.";

        String result = chatGPTClient.completions(prompt, text);

        assertEquals("", result);
    }
}