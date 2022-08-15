package com.sparta.sorisam.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SuccessResponse<M> {
    private final int code;
    private final boolean success;

    private String message;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final M result;


    @Builder
    public SuccessResponse(int code, boolean success, String message, M result) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.result = result;
    }

}
