package com.testapp.fileManager.service;

import com.testapp.fileManager.dao.FileStorageRepository;
import com.testapp.fileManager.entity.FileModel;
import com.testapp.fileManager.rest.responses.FileInfoResponse;
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

            model.setFileName(inputFile.getOriginalFilename());
            model.setFileSize(inputFile.getSize());
            model.setFileType(inputFile.getContentType());
            model.setFileData(inputFile.getBytes());

            LocalDateTime currTime = LocalDateTime.now();

            model.setUploadDate(currTime);
            model.setUpdateDate(currTime);

            fileStorageRepository.save(model);

        } catch (IOException e) {
            e.printStackTrace();
            throw new FileStorageException("Can't store this file, something goes wrong", ex);
        }

        // return "File: " + inputFile.getName() + " successfully saved. Go celebrate Man!";
        return createNewInfoResponseFromModel(model);
    }

    @Override
    public FileInfoResponse UpdateFile(int fileId, MultipartFile file) {
        FileModel model = getFileModelById(fileId);

        // update logic

        return createNewInfoResponseFromModel(model);
    }

    @Override
    public MultipartFile getFileById(int fileId) {
        FileModel model = getFileModelById(fileId);

        return file;
    }

    private FileInfoResponse createNewInfoResponseFromModel(FileModel model) {
        return new FileInfoResponse(
                model.getFileId(),
                model.getFileName(),
                model.getFileName(),
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
    public void DeleteFile(int fileId) {
        fileStorageRepository.deleteById(fileId);
        // return "File with id: " + fileId + " - successfully deleted.";
    }

    @Override
    public List<String> getFileNamesList() {
        return null;
    }
}
