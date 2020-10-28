package com.testapp.fileManager.service;

import com.testapp.fileManager.dao.FileStorageRepository;
import com.testapp.fileManager.dao.OnlyFileNames;
import com.testapp.fileManager.entity.FileStorageModel;
import com.testapp.fileManager.rest.requests.FilterRequestParams;
import com.testapp.fileManager.rest.responses.FileInfoResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileManagerServiceImpl implements FileManagerService {

    private final FileStorageRepository fileStorageRepository;

    @Autowired
    public FileManagerServiceImpl(FileStorageRepository fileStorageRepository) {
        this.fileStorageRepository = fileStorageRepository;
    }

    @Override
    public FileInfoResponse saveFile(MultipartFile inputFile) {
        FileStorageModel model;

        try {
            // fileData = inputFile.getInputStream().readAllBytes();

            model = new FileStorageModel();

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
        FileStorageModel model = getFileModelById(fileId);

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
    public FileStorageModel getFileById(int fileId) {
        FileStorageModel model = getFileModelById(fileId);
        return model;
    }

    @Override
    public List<FileInfoResponse> getFileList(FilterRequestParams filterParams) {

        List<FileInfoResponse> responseList = new ArrayList<>();
        List<FileStorageModel> storageModels = fileStorageRepository.findAll();

        for (FileStorageModel model : storageModels) {
            responseList.add(createNewFileInfoResponse(model));
        }

        if (filterParams == null) {
            return responseList;
        } else {
            Stream<FileInfoResponse> stream = responseList.stream();

            if (filterParams.getNamePart() != null) {
                stream = stream.filter(s -> s.getFileName().toLowerCase()
                                .contains(filterParams.getNamePart()));
            }

            if (filterParams.getDataFrom() != null) {
                stream = stream.filter(s -> s.getUpdateDate().isAfter(filterParams.getDataFrom()));
            }

            if (filterParams.getDataTo() != null) {
                stream = stream.filter(s -> s.getUpdateDate().isBefore(filterParams.getDataTo()));
            }
            if (filterParams.getFileTypes() != null) {
                stream = stream.filter(s -> {
                    for (String fileType : filterParams.getFileTypes()) {
                        if (fileType.contains(s.getFileType())) {
                            return true;
                        }
                    }
                    return false;
                }); // ugly but hope it will work
            }

            return stream.collect(Collectors.toList());
        }
    }

    @Override
    public List<OnlyFileNames> getFileNamesList() {
        return fileStorageRepository.findNamesByFileNameNotNull();
    }



    private FileInfoResponse createNewFileInfoResponse(FileStorageModel model) {
        return new FileInfoResponse(
                model.getFileId(),
                model.getFileName(),
                model.getFileType(),
                model.getFileSize(),
                model.getUploadDate(),
                model.getUpdateDate()
        );
    }

    private FileStorageModel getFileModelById(int id) {
        Optional<FileStorageModel> result = fileStorageRepository.findById(id);
        FileStorageModel fileModel;

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


}
