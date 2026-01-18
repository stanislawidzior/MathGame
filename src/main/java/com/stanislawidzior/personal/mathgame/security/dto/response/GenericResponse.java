package com.stanislawidzior.personal.mathgame.security.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GenericResponse<T> {
    private String status;
    private String message;
    private T data;

    public static <T> GenericResponse<T> success(String message, T data) {
        return new GenericResponse<>("success", message, data);
    }

    public static <T> GenericResponse<T> fail(String message, T data) {
        return new GenericResponse<>("fail", message, data);
    }

    public static <T> GenericResponse<T> error(String message, T data) {
        return new GenericResponse<>("error", message, data);
    }
}
