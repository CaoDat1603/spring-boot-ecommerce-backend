package com.dat.ecommerce.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.security.access.AccessDeniedException;

import java.net.PortUnreachableException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailAlreadyExists(
            EmailAlreadyExistsException ex,
            HttpServletRequest request
    ) {

        ApiErrorResponse error = createErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(SkuAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleSkuAlreadyExists(
            SkuAlreadyExistsException ex,
            HttpServletRequest request
    ) {

        ApiErrorResponse error = createErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleCartItemNotFound(
            CartItemNotFoundException ex,
            HttpServletRequest request
    ) {

        ApiErrorResponse error = createErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ApiErrorResponse> handleInsufficientStockException(
            InsufficientStockException ex,
            HttpServletRequest request
    ) {

        ApiErrorResponse error = createErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUserNotFound(
            UserNotFoundException ex,
            HttpServletRequest request
    ) {

        ApiErrorResponse error = createErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleProductNotFound(
            ProductNotFoundException ex,
            HttpServletRequest request
    ) {

        ApiErrorResponse error = createErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(PaymentAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handlePaymentAlreadyExistsException(
            PaymentAlreadyExistsException ex,
            HttpServletRequest request
    ) {

        ApiErrorResponse error = createErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {

        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage(),
                        (existing, replacement) -> existing
                ));

        ApiErrorResponse response = createErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                request.getRequestURI()
        );

        response.setErrors(errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleOrderNotFound(
            OrderNotFoundException ex,
            HttpServletRequest request
    ) {

        ApiErrorResponse error = createErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleCartNotFound(
            CartNotFoundException ex,
            HttpServletRequest request
    ) {

        ApiErrorResponse error = createErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handlePaymentNotFound(
            PaymentNotFoundException ex,
            HttpServletRequest request
    ) {

        ApiErrorResponse error = createErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(EmptyCartException.class)
    public ResponseEntity<ApiErrorResponse> handleEmptyCart(
            EmptyCartException ex,
            HttpServletRequest request
    ) {

        ApiErrorResponse error = createErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(createErrorResponse(
                        HttpStatus.FORBIDDEN,
                        "You do not have permission to access this resource",
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> handlerIllegalStateException(
            IllegalStateException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(IdempotencyInProgressException.class)
    public ResponseEntity<ApiErrorResponse> handleIdempotencyInProgress(
            IdempotencyInProgressException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(createErrorResponse(
                        HttpStatus.CONFLICT,
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(IdempotencyRequestFailedException.class)
    public ResponseEntity<ApiErrorResponse> handleIdempotencyRequestFailed(
            IdempotencyRequestFailedException ex,
            HttpServletRequest request
    ) {

        HttpStatus status = HttpStatus.valueOf(ex.getStatus());

        return ResponseEntity
                .status(status)
                .body(createErrorResponse(
                        status,
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }


    private ApiErrorResponse createErrorResponse(
            HttpStatus status,
            String message,
            String path
    ) {
        return new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        );
    }

}