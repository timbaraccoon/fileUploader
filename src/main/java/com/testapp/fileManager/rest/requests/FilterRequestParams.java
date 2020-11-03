package com.testapp.fileManager.rest.requests;

import java.time.LocalDateTime;
import java.util.HashSet;

public class FilterRequestParams {

    private String namePart;
    private LocalDateTime dataFrom;
    private LocalDateTime dataTo;
    private HashSet<String> fileTypes;

    public FilterRequestParams() {
    }

    public String getNamePart() {
        return namePart;
    }

    public void setNamePart(String namePart) {
        this.namePart = namePart;
    }

    public LocalDateTime getDataFrom() {
        return dataFrom;
    }

    public void setDataFrom(LocalDateTime dataFrom) {
        this.dataFrom = dataFrom;
    }

    public LocalDateTime getDataTo() {
        return dataTo;
    }

    public void setDataTo(LocalDateTime dataTo) {
        this.dataTo = dataTo;
    }

    public HashSet<String> getFileTypes() {
        return fileTypes;
    }

    public void setFileTypes(HashSet<String> fileTypes) {
        this.fileTypes = fileTypes;
    }

    @Override
    public String toString() {
        return "FilterRequestParams{" +
                "namePart='" + namePart + '\'' +
                ", dataFrom=" + dataFrom +
                ", dataTo=" + dataTo +
                ", fileTypes=" + fileTypes +
                '}';
    }
}
