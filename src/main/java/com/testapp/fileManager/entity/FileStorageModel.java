package com.testapp.fileManager.entity;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
public class FileStorageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private int fileId;

    @Column(name = "file_name", length = 100, nullable = false)
    private String fileName;

    @Column(name = "file_type", length = 50, nullable = false)
    private String fileType;

    @Column(name = "file_size", nullable = false)
    private long fileSize;

    @Column(name = "upload_date", nullable = false)
    private LocalDateTime uploadDate;

    @Column(name = "last_update_date", nullable = false)
    private LocalDateTime updateDate;

    @Column(name = "file_data", length = 16000000, nullable = false)
    byte[] fileData;

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    @Override
    public String toString() {
        return "FileStorage{" +
                "fileId=" + fileId +
                ", uploadDate=" + uploadDate +
                ", updateDate=" + updateDate +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize=" + fileSize +
                '}';
    }
}
