package com.testapp.fileManager.rest.responses;

import java.time.LocalDateTime;

public class FileInfoResponse {

    private final int fileId;
    private final String fileName;
    private final String fileType;
    private final long fileSize;
    private final LocalDateTime uploadDate;
    private final LocalDateTime updateDate;
    private final String downloadLink;


    public FileInfoResponse(int fileId, String fileName, String fileType, long fileSize,
                            LocalDateTime uploadDate, LocalDateTime updateDate) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.uploadDate = uploadDate;
        this.updateDate = updateDate;
        this.downloadLink = "http://localhost:8080/api/files/" + fileId; // get request link for download

    }

    public int getFileId() {
        return fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    @Override
    public String toString() {
        return "FileInfoResponse{" +
                "fileId=" + fileId +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize=" + fileSize +
                ", uploadDate=" + uploadDate +
                ", updateDate=" + updateDate +
                ", downloadLink='" + downloadLink + '\'' +
                '}';
    }
}
