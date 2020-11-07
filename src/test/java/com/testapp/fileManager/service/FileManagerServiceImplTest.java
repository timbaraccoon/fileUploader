package com.testapp.fileManager.service;

import com.testapp.fileManager.dao.FileStorageRepository;
import com.testapp.fileManager.entity.FileStorageModel;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class FileManagerServiceImplTest {

    @Mock
    private FileStorageRepository repository;

    FileManagerServiceImpl service;

    private SupportedFileExtensionsSet extensionsSet;

    public FileManagerServiceImplTest() {
        MockitoAnnotations.initMocks(this);
        this.service = new FileManagerServiceImpl(repository, extensionsSet);
    }



    private final FileStorageModel testModel = new FileStorageModel(
            1,
            "test.txt",
            "txt",
            300,
            LocalDateTime.now(),
            LocalDateTime.now(),
            new byte[300]
    );

//    public FileManagerServiceImplTest() {
//        MockitoAnnotations.initMocks(this);
//        this.repository = new FileStorageRepository();
//    }

    @Test
    void saveFile() {

    }

    @Test
    void getFileByIdSuccessTest() {
        given(repository.findById(1)).willReturn(
                java.util.Optional.of(testModel));

        FileStorageModel model = service.getFileStorageById(1);
        assertEquals(testModel, model);
    }

//    @Test
//    void getFileByIdExceptionTest()  {
//        given(repository.findById(1)).willThrow(CustomFileNotFoundException.class);
//        int id = 1;
//
//        CustomFileNotFoundException thrown = assertThrows(
//                CustomFileNotFoundException.class,
//                () -> repository.findById(id),
//                "Expected save bad file to throw, but it didn't"
//        );
//
//        assertTrue(thrown.getMessage().contains("Can't find File with id: " + id));
//    }

    @Test
    void updateFile() {
    }

    @Test
    void getFileList() {
    }

    @Test
    void getArchive() {
    }

    @Test
    void getFileNamesList() {
    }

    @Test
    void deleteFile() {
    }
}