DROP TABLE IF EXISTS file_storage;

CREATE TABLE file_storage (
  file_id INT AUTO_INCREMENT  PRIMARY KEY,
  upload_date DATETIME NOT NULL,
  last_update_date DATETIME NOT NULL,
  file_name VARCHAR(100) NOT NULL,
  file_type VARCHAR(20) NOT NULL,
  file_size INT NOT NULL,
  file_data VARBINARY
);
