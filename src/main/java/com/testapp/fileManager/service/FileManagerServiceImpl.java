package com.testapp.fileManager.service;

import com.testapp.fileManager.dao.FileStorageRepository;
import com.testapp.fileManager.dao.OnlyFileNames;
import com.testapp.fileManager.entity.FileStorageModel;
import com.testapp.fileManager.rest.customexceptions.CustomFileNotFoundException;
import com.testapp.fileManager.rest.customexceptions.FileStorageException;
import com.testapp.fileManager.rest.customexceptions.UnsupportedFileFormatException;
import com.testapp.fileManager.rest.requests.FilterRequestParams;
import com.testapp.fileManager.rest.responses.FileInfoResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileManagerServiceImpl implements FileManagerService {

    private final FileStorageRepository fileStorageRepository;
    private final SupportedFileExtensionsSet extensionsSet;

    @Autowired
    public FileManagerServiceImpl(FileStorageRepository fileStorageRepository, SupportedFileExtensionsSet extensionsKeeper) {
        this.fileStorageRepository = fileStorageRepository;
        this.extensionsSet = extensionsKeeper;
    }

    @Override
    public FileInfoResponse saveFile(MultipartFile inputFile) {
        FileStorageModel model;

        try {
            model = new FileStorageModel();
            initializeModelFields(inputFile, model);
            fileStorageRepository.save(model);

        } catch (IOException e) {
            e.printStackTrace();
            throw new FileStorageException("Can't store this file, something goes wrong", e);
        }
        return createNewFileInfoResponse(model);
    }

    private void initializeModelFields(MultipartFile inputFile, FileStorageModel model) throws IOException {
        String fileName = inputFile.getOriginalFilename();
        String fileExtension = FilenameUtils.getExtension(fileName).toLowerCase();

        if (!(extensionsSet.getExtensions().contains(fileExtension))) {
            throw new UnsupportedFileFormatException("Unsupported type of File");
        }

        LocalDateTime currTime = LocalDateTime.now();

        model.setFileName(fileName);
        model.setFileSize(inputFile.getSize());
        model.setFileType(fileExtension);
        model.setFileData(inputFile.getBytes());
        model.setUploadDate(currTime);
        model.setUpdateDate(currTime);
    }

    @Override
    public FileStorageModel getFileStorageById(int fileId) {
        return getFileModelById(fileId);
    }

    @Override
    public FileInfoResponse updateFile(int fileId, MultipartFile inputFile) {
        String inputFileName = inputFile.getOriginalFilename();
        String inputFileExtension = FilenameUtils.getExtension(inputFileName).toLowerCase();
        FileStorageModel model = getFileModelById(fileId);

        if ( !(inputFileName.equals(model.getFileName()))
            || !(inputFileExtension.equals(model.getFileType())) ) {

            throw new FileStorageException("Can't update, it's another File");
        }

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

    public FileInfoResponse getFileInfoById(int id) {
        FileStorageModel model = getFileModelById(id);

        return createNewFileInfoResponse(model);
    }

    @Override
    public List<FileInfoResponse> getFileInfoList(FilterRequestParams filterParams) {

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
                }); // ugly but work
            }

            return stream.collect(Collectors.toList());
        }
    }

    @Override
    public ZipOutputStream getArchive(List<Integer> fileIds, HttpServletResponse response) throws IOException {
        ZipOutputStream resultZip = new ZipOutputStream(response.getOutputStream());

        try (resultZip) {
            for (Integer fileId : fileIds) {
                FileStorageModel fileModel = getFileStorageById(fileId);

                ZipEntry zipEntry = new ZipEntry(fileModel.getFileName());
                zipEntry.setSize(fileModel.getFileSize());

                resultZip.putNextEntry(zipEntry);
                resultZip.write(fileModel.getFileData());
                resultZip.closeEntry();
            }
            resultZip.finish();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultZip;
    }

    @Override
    public List<OnlyFileNames> getFileNamesList() {
        return fileStorageRepository.findNamesByFileNameNotNull();
    }

    private FileStorageModel getFileModelById(int id) {
        Optional<FileStorageModel> result = fileStorageRepository.findById(id);
        FileStorageModel fileModel;

        if (result.isPresent()) {
            fileModel = result.get();
        } else {
            throw new CustomFileNotFoundException("Can't find File with id: " + id);
        }
        return fileModel;
    }

    @Override
    public void deleteFile(int fileId) {
        fileStorageRepository.deleteById(fileId);
    }

}
