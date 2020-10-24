package com.testapp.fileManager.service;

import com.testapp.fileManager.dao.FileStorageRepository;
import com.testapp.fileManager.entity.FileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

        private final Path fileStorageLocation;

        @Autowired
        FileStorageRepository fileStorageRepository;

        @Autowired
        public FileStorageService(FileModel fileStorage) {
            this.fileStorageLocation = Paths.get(fileStorage.getUploadDir())
                    .toAbsolutePath().normalize();

            try {
                Files.createDirectories(this.fileStorageLocation);
            } catch (Exception ex) {
                throw new fileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
            }
        }

        public String storeFile(MultipartFile file, Integer userId, String docType) {
            // Normalize file name
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileName = "";
            try {
                // Check if the file's name contains invalid characters
                if(originalFileName.contains("..")) {
                    throw new DocumentStorageException("Sorry! Filename contains invalid path sequence " + originalFileName);
                }
                String fileExtension = "";
                try {
                    fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                } catch(Exception e) {
                    fileExtension = "";
                }
                fileName = userId + "_" + docType + fileExtension;
                // Copy file to the target location (Replacing existing file with the same name)
                Path targetLocation = this.fileStorageLocation.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                FileModel doc; // = fileStorageRepository.checkDocumentByUserId(userId, docType);
                
                if(doc != null) {
                    doc.setFileType(file.getContentType());
                    doc.setFileName(fileName);
                    fileStorageRepository.save(doc);

                } else {
                    FileModel newDoc = new FileModel();
                    newDoc.setFileId(userId);
                    newDoc.setFileType(file.getContentType());
                    newDoc.setFileName(fileName);
                    fileStorageRepository.save(newDoc);
                }

                return fileName;
            } catch (IOException ex) {
                throw new DocumentStorageException("Could not store file " + fileName + ". Please try again!", ex);
            }
        }


        public Resource loadFileAsResource(String fileName) throws Exception {
            try {
                Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
                Resource resource = new UrlResource(filePath.toUri());
                if(resource.exists()) {
                    return resource;
                } else {
                    throw new FileNotFoundException("File not found " + fileName);
                }
            } catch (MalformedURLException ex) {
                throw new FileNotFoundException("File not found " + fileName);
            }
        }
        public String getDocumentName(Integer userId, String docType) {

            return fileStorageRepository.getUploadDocumnetPath(userId, docType);

        }

}
