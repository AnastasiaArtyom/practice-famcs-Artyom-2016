package com.company;

public interface AbstractOperation {
    String getName();

    ChatHistory perform(ChatHistory oldHistory, Logger logger);
}
