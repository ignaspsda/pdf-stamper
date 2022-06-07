package com.stamp.pdf.payload.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class FileRequest {
    private final String fileName;
    private final String fileBytes;

    @JsonCreator
    public FileRequest(@JsonProperty("fileName") String fileName, @JsonProperty("fileBytes") String fileBytes) {
        this.fileName = fileName;
        this.fileBytes = fileBytes;
    }

    @Override
    public String toString() {
        return "FileRequest{" +
                "fileName='" + fileName + '\'' +
                ", fileBytes='" + fileBytes + '\'' +
                '}';
    }
}
