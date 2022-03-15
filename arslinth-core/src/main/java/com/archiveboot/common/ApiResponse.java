package com.archiveboot.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Arslinth
 * @ClassName ApiResponse
 * @Description 返回体
 * @Date 2022/2/10
 */
@Data
public class ApiResponse {


    private Integer code;


    private String message;


    private Map<String, Object> data = new HashMap<String, Object>();

    private ApiResponse() {
    }


    public ApiResponse message(String message) {
        this.setMessage(message);
        return this;
    }

    public static ApiResponse code(Integer code) {
        ApiResponse result = new ApiResponse();
        result.setCode(code);
        return result;
    }

    public ApiResponse data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public ApiResponse data(Map<String, Object> map) {
        this.setData(map);
        return this;
    }
}
