package com.testapp.fileManager.service;

import com.testapp.fileManager.dao.FileStorageRepository;
import com.testapp.fileManager.entity.FileModel;
import com.testapp.fileManager.rest.responses.FileInfoResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FileManagerServiceImpl implements FileManagerService {

    private final FileStorageRepository fileStorageRepository;

    @Autowired
    public FileManagerServiceImpl(FileStorageRepository fileStorageRepository) {
        this.fileStorageRepository = fileStorageRepository;
    }

    @Override
    public FileInfoResponse saveFile(MultipartFile inputFile) {
        FileModel model;

        try {
            // fileData = inputFile.getInputStream().readAllBytes();

            model = new FileModel();

            model.setFileName(FilenameUtils.getName(inputFile.getOriginalFilename()));
            model.setFileSize(inputFile.getSize());
            model.setFileType(FilenameUtils.getExtension(inputFile.getOriginalFilename()));
            model.setFileData(inputFile.getBytes());

            LocalDateTime currTime = LocalDateTime.now();

            model.setUploadDate(currTime);
            model.setUpdateDate(currTime);

            fileStorageRepository.save(model);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't store this file, something goes wrong", e);
        }
        // return "File: " + inputFile.getName() + " successfully saved. Go celebrate Man!";
        return createNewFileInfoResponse(model);
    }

    @Override
    public FileInfoResponse updateFile(int fileId, MultipartFile inputFile) {
        FileModel model = getFileModelById(fileId);

        try {
            model.setFileData(inputFile.getBytes());
            model.setFileSize(inputFile.getSize());
            model.setUpdateDate(LocalDateTime.now());

        } catch (IOException e) {
            e.printStackTrace();
        }
        fileStorageRepository.save(model);

        return createNewFileInfoResponse(model);
    }

    @Override
    public FileModel getFileById(int fileId) {
        FileModel model = getFileModelById(fileId);
        return model;
    }

    private FileInfoResponse createNewFileInfoResponse(FileModel model) {
        return new FileInfoResponse(
                model.getFileId(),
                model.getFileName(),
                model.getFileType(),
                model.getFileSize(),
                model.getUploadDate(),
                model.getUpdateDate()
        );
    }

    private FileModel getFileModelById(int id) {
        Optional<FileModel> result = fileStorageRepository.findById(id);
        FileModel fileModel;

        if (result.isPresent()) {
            fileModel = result.get();
        } else {
            throw new RuntimeException("Can't find File with id: " + id);
        }
        return fileModel;

    }

    @Override
    public void deleteFile(int fileId) {
        fileStorageRepository.deleteById(fileId);
        // return "File with id: " + fileId + " - successfully deleted.";
    }
/*
    @Override
    public List<String> getFileNamesList() {
        return fileStorageRepository.findFileNames();
    } */
}
