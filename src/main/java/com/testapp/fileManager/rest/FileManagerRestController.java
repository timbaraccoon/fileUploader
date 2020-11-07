package com.testapp.fileManager.rest;

import com.testapp.fileManager.dao.OnlyFileNames;
import com.testapp.fileManager.entity.FileStorageModel;
import com.testapp.fileManager.rest.customexceptions.CustomFileNotFoundException;
import com.testapp.fileManager.rest.requests.FilterRequestParams;
import com.testapp.fileManager.rest.responses.FileInfoResponse;
import com.testapp.fileManager.service.FileManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;

@Api(tags = {"In memory File Downloader"})
//@SwaggerDefinition(tags = {
//        @Tag(name = "In memory File Downloader", description = "API for download storage and upload different types of " +
//                "files in memory, also return list of filenames and downloads group of files in archive")
//})
@RestController
@RequestMapping("/api")
public class FileManagerRestController {

    private final FileManagerService fileManagerService;

    @Autowired
    public FileManagerRestController(FileManagerService fileManagerService) {
        this.fileManagerService = fileManagerService;
    }

    @ApiOperation(value = "Save file to database",
            notes = "Upload file in database, return file info with download link",
            response = FileInfoResponse.class)
    @PostMapping("/files")
    public FileInfoResponse saveFile(@RequestBody MultipartFile file) {

        return fileManagerService.saveFile(file);
    }

    @ApiOperation(value = "Download file",
            notes = "Download file from database with file id")
    @GetMapping("/files/{fileId}")
    public ResponseEntity<Resource> downloadFileById(@PathVariable int fileId) {
        // Load file from database
        FileStorageModel model = fileManagerService.getFileStorageById(fileId);

        if (model == null) {
            throw new CustomFileNotFoundException("File id is not found: " + fileId);
        }

        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String mimeType = fileNameMap.getContentTypeFor(model.getFileName());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + model.getFileName() + "\"")
                .contentType(MediaType.asMediaType(MimeType.valueOf(mimeType)))
                .body(new ByteArrayResource(model.getFileData()));
    }

    @GetMapping("/files/info/{fileId}")
    public FileInfoResponse getFileInfoById(@PathVariable int fileId) {
        FileInfoResponse response = fileManagerService.getFileInfoById(fileId);

        if (response == null) {
            throw new CustomFileNotFoundException("File id is not found: " + fileId);
        }
        return response;
    }

    @ApiOperation(value = "Get List of Files",
            notes = "Return list of files info with download links, also filter result list by " +
                    "different parameters, or without filters return all",
            response = FileInfoResponse.class)
    @GetMapping("/files/list")
    public List<FileInfoResponse> getFileList(@RequestBody FilterRequestParams filterParams) {
        return fileManagerService.getFileInfoList(filterParams);
    }

    @ApiOperation(value = "Download Archive of Files",
            notes = "Take List of file ID, return archive of selected files")
    @GetMapping("/files/archive")

    public void getArchive(@RequestBody List<Integer> fileIds, HttpServletResponse response) {
        try {
            fileManagerService.getArchive(fileIds, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"archive\"");
    }

    @ApiOperation(value = "Get List of File Names")
    @GetMapping("/files/list-of-names")
    public List<OnlyFileNames> getFileNamesList() {
        return fileManagerService.getFileNamesList();
    }

    @ApiOperation(value = "Update File",
            notes = "Update selected by ID file, return updated file info model",
            response = FileInfoResponse.class)
    @PutMapping("/files/{fileId}")
    public FileInfoResponse updateFile(@PathVariable int fileId,
                                       @RequestBody MultipartFile file) {

        return fileManagerService.updateFile(fileId, file);
    }

    @ApiOperation(value = "Delete file by ID")
    @DeleteMapping("/files/{fileId}")
    public String deleteFile(@PathVariable int fileId) {
        FileStorageModel model = fileManagerService.getFileStorageById(fileId);

        if (model == null) {
            throw new CustomFileNotFoundException("File id is not found: " + fileId);
        }
        fileManagerService.deleteFile(fileId);

        return "File with id: " + fileId + " - successful deleted.";
    }

}
