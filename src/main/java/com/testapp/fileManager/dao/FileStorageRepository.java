package com.testapp.fileManager.dao;

import com.testapp.fileManager.entity.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileStorageRepository extends JpaRepository<FileData, Integer> {

}