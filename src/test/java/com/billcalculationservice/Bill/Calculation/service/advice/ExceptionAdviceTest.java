package com.billcalculationservice.Bill.Calculation.service.advice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExceptionAdviceTest {

    private ExceptionAdvice exceptionAdvice;

    @BeforeEach
    void setUp() {
        exceptionAdvice = new ExceptionAdvice();
    }

    @Test
    void testHandleTypeMismatch() {
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getMessage()).thenReturn("Type mismatch test message");

        ResponseEntity<String> response = exceptionAdvice.handleTypeMismatch(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Invalid input: Type mismatch test message"));
    }

    @Test
    void testHandleValidationExceptions() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError = new FieldError("objectName", "fieldName", "error message");
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));

        ResponseEntity<Map<String, String>> response = exceptionAdvice.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("error message", response.getBody().get("fieldName"));
    }

    @Test
    void testHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Illegal argument test");

        ResponseEntity<String> response = exceptionAdvice.handleIllegalArgumentException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Illegal argument test", response.getBody());
    }

    @Test
    void testHandleRuntimeException() {
        RuntimeException ex = new RuntimeException("Runtime error test");

        ResponseEntity<Map<String, Object>> response = exceptionAdvice.handleRuntimeException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("An unexpected error occurred", response.getBody().get("message"));
        assertEquals("Runtime error test", response.getBody().get("details"));
    }

    @Test
    void testHandleGeneralException() {
        Exception ex = new Exception("General error test");

        ResponseEntity<Map<String, Object>> response = exceptionAdvice.handleGeneralException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("A system error occurred", response.getBody().get("message"));
        assertEquals("General error test", response.getBody().get("details"));
    }

    @Test
    void testHandleValidationExceptionsWithMultipleErrors() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        List<FieldError> fieldErrors = Arrays.asList(
                new FieldError("object1", "field1", "error1"),
                new FieldError("object2", "field2", "error2")
        );

        when(ex.getBindingResult()).thenReturn(bindingResult);
        doReturn(fieldErrors).when(bindingResult).getAllErrors();

        ResponseEntity<Map<String, String>> response = exceptionAdvice.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("error1", response.getBody().get("field1"));
        assertEquals("error2", response.getBody().get("field2"));
    }
}
