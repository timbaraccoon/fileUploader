package com.testapp.fileManager.service;

import com.testapp.fileManager.dao.OnlyFileNames;
import com.testapp.fileManager.entity.FileStorageModel;
import com.testapp.fileManager.rest.requests.FilterRequestParams;
import com.testapp.fileManager.rest.responses.FileInfoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileManagerService {

    FileInfoResponse saveFile(MultipartFile file);

    FileInfoResponse updateFile(int fileId, MultipartFile file);

    FileStorageModel getFileById(int id);

    void deleteFile(int fileId);

    List<OnlyFileNames> getFileNamesList();

    List<FileInfoResponse> getFileList(FilterRequestParams filterParams);

}
