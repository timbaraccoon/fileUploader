package com.testapp.fileManager.dao;

import com.testapp.fileManager.entity.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface FileStorageRepository extends JpaRepository<FileModel, Integer> {

//    @Query("SELECT file_name FROM testdb")
//    List<String> findAllFileNames();

//    List<String> findFileNames();

}