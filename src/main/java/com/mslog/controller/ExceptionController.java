package com.mslog.controller;


import com.mslog.exception.InvalidRequest;
import com.mslog.exception.MslogException;
import com.mslog.exception.PostNotFound;
import com.mslog.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();
        for (FieldError fieldError : e.getFieldErrors()) {
            errorResponse.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return errorResponse;
    }

    @ResponseBody
    @ExceptionHandler(MslogException.class)
    public ResponseEntity<ErrorResponse> mslogException(MslogException e) {
        int statusCode = e.getStatusCode();

        ErrorResponse errorResponse = ErrorResponse.builder().code(String.valueOf(statusCode)).message(e.getMessage()).validation(e.getValidation()).build();

        ResponseEntity<ErrorResponse> errorentity = ResponseEntity.status(statusCode).body(errorResponse);

        return errorentity;
    }


}
