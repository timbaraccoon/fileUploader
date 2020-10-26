package com.testapp.fileManager.dao;

import com.testapp.fileManager.entity.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileStorageRepository extends JpaRepository<FileModel, Integer> {

//    @Query("SELECT FILE_NAME FROM FILE_MODEL")
//    List<String> findAllFileNames();

    List<OnlyFileNames> findNamesByFileNameNotNull();

//    List<String> findFileNames();

}