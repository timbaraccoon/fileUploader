package com.testapp.fileManager.service;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SupportedFileExtensionsSet {
    private final Set<String> extensions;

    public SupportedFileExtensionsSet() {
        extensions = new HashSet<>();

        extensions.add("txt");
        extensions.add("doc");
        extensions.add("docx");
        extensions.add("pdf");
    }

    public Set<String> getExtensions() {
        return extensions;
    }

    @Override
    public String toString() {
        return "SupportedFileExtensions{" +
                "extensions=" + extensions +
                '}';
    }
}
