package com.example.smartshop.exception;

import com.example.smartshop.service.impl.TranslatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Optional;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
    private final TranslatorService translatorService;



    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class      // Когда не прошла @Valid
    })
    public ResponseEntity<ErrorResponseDto> handleSystemBadRequest(Exception ex, WebRequest req) {
        String path = ((ServletWebRequest) req).getRequest().getRequestURL().toString();
        String lang = Optional.ofNullable(((ServletWebRequest) req).getRequest().getHeader(HttpHeaders.ACCEPT_LANGUAGE))
                .orElse("en");

        String errorCode = "DEMO_PROJECT_EXCEPTION_0005";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.builder()
                        .timestamp(LocalDateTime.now())
                        .code(errorCode)
                        .path(path)
                        .error(translatorService.translate("400", lang))
                        .details(translatorService.translate(errorCode, lang))
                        .build());
    }


    @ExceptionHandler(GenericException.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(GenericException ex, WebRequest req) {
        String path = ((ServletWebRequest) req).getRequest().getRequestURL().toString();

        String lang = Optional.ofNullable(((ServletWebRequest) req)
                        .getRequest()
                        .getHeader(HttpHeaders.ACCEPT_LANGUAGE))
                .orElse("en");

        return ResponseEntity.status(ex.getStatus())
                .body(ErrorResponseDto.builder()
                        .timestamp(LocalDateTime.now())
                        .code(ex.getErrorCode().getCode())
                        .path(path)
                        .error(translatorService.translate(String.valueOf(ex.getStatus()), lang))
                        .details(translatorService.translate(
                                ex.getErrorCode().getCode(),
                                lang,
                                ex.arguments
                        ))
                        .build());
    }

    //    @ExceptionHandler({
//            MethodArgumentTypeMismatchException.class, // буквы в ID
//            HttpMessageNotReadableException.class,     // сломанный JSON
//            MissingServletRequestParameterException.class // забыли параметр
//    })
//    public ResponseEntity<ErrorResponseDto> handleBadRequestSystem(Exception ex, WebRequest req) {
//        String path = ((ServletWebRequest) req).getRequest().getRequestURL().toString();
//        String lang = Optional.ofNullable(((ServletWebRequest) req).getRequest().getHeader(HttpHeaders.ACCEPT_LANGUAGE))
//                .orElse("en");
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(ErrorResponseDto.builder()
//                        .timestamp(LocalDateTime.now())
//                        .code("BAD_REQUEST_ERROR") // общий код для системных ошибок
//                        .path(path)
//                        .error(translatorService.translate("400", lang))
//                        .details("Invalid request format or parameter") // или свой текст
//                        .build());
//    }
}
