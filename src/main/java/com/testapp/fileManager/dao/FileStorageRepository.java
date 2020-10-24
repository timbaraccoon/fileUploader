package com.testapp.fileManager.dao;

import com.testapp.fileManager.entity.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileStorageRepository extends JpaRepository<FileModel, Integer> {

}