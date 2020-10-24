package com.testapp.fileManager.service;

import com.testapp.fileManager.entity.FileModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileManagerService {

    String saveFile(MultipartFile file);

    String UpdateFile(int fileId, MultipartFile file);

    FileModel getFileById(int id);

    String DeleteFile(int fileId);

    List<String> getFileNamesList();

}
