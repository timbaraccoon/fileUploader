package com.testapp.fileManager.dao;

import com.testapp.fileManager.entity.FileStorageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileStorageRepository extends JpaRepository<FileStorageModel, Integer> {

    List<OnlyFileNames> findNamesByFileNameNotNull();


}