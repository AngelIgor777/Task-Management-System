package org.task.task_management_system.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        Map<String, Object> response =
                getExceptionResponse(HttpStatus.FORBIDDEN, "Доступ запрещен", "message", "У вас нет прав на выполнение данного действия");
        response.put("path", request.getRequestURI());
        return ResponseEntity.status(403).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        Map<String, Object> response = getExceptionResponse(HttpStatus.BAD_REQUEST, "Неверный запрос", "path", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, Object> response = getExceptionResponse(HttpStatus.BAD_REQUEST, "Некорректные данные", "message", "Некорректные данные в запросе");
        response.put("details", ex.getBindingResult().getFieldErrors());
        response.put("path", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex, HttpServletRequest request) {
        Map<String, Object> response = getExceptionResponse(HttpStatus.BAD_REQUEST, "Отсутствует обязательный параметр", "message", "Отсутствует обязательный параметр: " + ex.getParameterName());
        response.put("path", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<Map<String, Object>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        Map<String, Object> response = getExceptionResponse(HttpStatus.METHOD_NOT_ALLOWED, "Метод не поддерживается", "message", "Метод не поддерживается для этого ресурса");
        response.put("path", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex, WebRequest request) {
        Map<String, Object> body = getExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "path", request.getDescription(false).replace("uri=", ""));
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        Map<String, Object> body = getExceptionResponse(httpStatus, ex.getMessage(), "path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, httpStatus);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        String errorMessage = "Ошибка при обработке запроса: нарушено ограничение целостности данных.";

        if (ex.getMessage().contains("users_email_key")) {
            errorMessage = "Пользователь с таким email уже существует.";
        }
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", errorMessage);
        response.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private static Map<String, Object> getExceptionResponse(HttpStatus internalServerError, String Internal_Server_Error, String path, String request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", internalServerError.value());
        body.put("error", Internal_Server_Error);
        body.put(path, request);
        return body;
    }



}
