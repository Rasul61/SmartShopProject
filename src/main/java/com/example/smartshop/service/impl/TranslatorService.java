package com.example.smartshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class TranslatorService {

    private final MessageSource messageSource;

    public String translate(String input, String language) {
        return messageSource.getMessage(input, new Object[] {},input, Locale.of(language));
    }

    public String translate(String input, String language, Object... args) {
        return messageSource.getMessage(input, args, input, Locale.of(language));
    }
}



