package com.testapp.fileManager.service;

import com.testapp.fileManager.entity.FileData;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileManagerService {

    String saveFile(MultipartFile file, Integer fileId, String docType);

    String UpdateFile(int fileId, MultipartFile file, String docType);

    FileData getFileById(int id);

    String DeleteFile(int fileId);

    List<String> getFileNamesList();

}
