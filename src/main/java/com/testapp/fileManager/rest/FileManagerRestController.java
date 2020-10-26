package com.testapp.fileManager.rest;

import com.testapp.fileManager.dao.OnlyFileNames;
import com.testapp.fileManager.entity.FileModel;
import com.testapp.fileManager.rest.responses.FileInfoResponse;
import com.testapp.fileManager.service.FileManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FileManagerRestController {

    private final FileManagerService fileManagerService;

    @Autowired
    public FileManagerRestController(FileManagerService fileManagerService) {
        this.fileManagerService = fileManagerService;
    }

    @PostMapping("/files")
    public FileInfoResponse saveFile(@RequestBody MultipartFile file) {

        return fileManagerService.saveFile(file);
    }

    @GetMapping("/files/{fileId}")
    public ResponseEntity<Resource> getFileById(@PathVariable int fileId) {
        // Load file from database
        FileModel model = fileManagerService.getFileById(fileId);

        if (model == null) {
            throw new RuntimeException("File id is not found: " + fileId);
            // поменять на   CustomNotFoundException(""File id is not found: " + fileId)
        }

        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String mimeType = fileNameMap.getContentTypeFor(model.getFileName());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + model.getFileName() + "\"")
                .contentType(MediaType.asMediaType(MimeType.valueOf(mimeType)))
                .body(new ByteArrayResource(model.getFileData()));
    }


    @GetMapping("/files/list-of-names")
    public List<OnlyFileNames> getCustomers() {
        return fileManagerService.getFileNamesList();
    }


    @PutMapping("/files/{fileId}")
    public FileInfoResponse updateFile(@PathVariable int fileId,
                                       @RequestBody MultipartFile file) {

        return fileManagerService.updateFile(fileId, file);
    }

    @DeleteMapping("/files/{fileId}")
    public String deleteCustomer(@PathVariable int fileId) {
        FileModel model = fileManagerService.getFileById(fileId);

        if (model == null) {
            throw new RuntimeException("File id is not found: " + fileId);
            // поменять на   CustomNotFoundException(""File id is not found: " + fileId)
        }
        fileManagerService.deleteFile(fileId);

        return "File with id: " + fileId + " - successful deleted.";
    }




}
