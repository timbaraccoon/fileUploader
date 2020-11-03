package com.testapp.fileManager.service;

import com.testapp.fileManager.dao.OnlyFileNames;
import com.testapp.fileManager.entity.FileStorageModel;
import com.testapp.fileManager.rest.requests.FilterRequestParams;
import com.testapp.fileManager.rest.responses.FileInfoResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipOutputStream;

public interface FileManagerService {

    FileInfoResponse saveFile(MultipartFile file);

    FileInfoResponse updateFile(int fileId, MultipartFile file);

    FileStorageModel getFileById(int id);

    void deleteFile(int fileId);

    List<OnlyFileNames> getFileNamesList();

    List<FileInfoResponse> getFileList(FilterRequestParams filterParams);

    ZipOutputStream getArchive(List<Integer> fileIds, HttpServletResponse response) throws IOException;
}
