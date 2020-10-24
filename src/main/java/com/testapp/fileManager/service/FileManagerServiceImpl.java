package com.testapp.fileManager.service;

import com.testapp.fileManager.dao.FileStorageRepository;
import com.testapp.fileManager.entity.FileData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class FileManagerServiceImpl implements FileManagerService {

    private final FileStorageRepository repository;

    @Autowired
    public FileManagerServiceImpl(FileStorageRepository repository) {
        this.repository = repository;
    }

    @Override
    public String saveFile(MultipartFile file, Integer fileId, String docType) {

        byte[] dataByte;

        try {
            dataByte = file.getInputStream().readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String UpdateFile(int fileId, MultipartFile file, String docType) {
        return null;
    }

    @Override
    public FileData getFileById(int id) {
        Optional<FileData> result = repository.findById(id);
        FileData fileData = null;

        if (result.isPresent()) {
            fileData = result.get();
        } else {
            throw new RuntimeException("Can't find Employee with id: " + id);
        }
        return fileData;

    }

    @Override
    public String DeleteFile(int fileId) {
        return null;
    }

    @Override
    public List<String> getFileNamesList() {
        return null;
    }
}
