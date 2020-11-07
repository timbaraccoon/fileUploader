package com.testapp.fileManager;

import com.testapp.fileManager.dao.OnlyFileNames;
import com.testapp.fileManager.entity.FileStorageModel;
import com.testapp.fileManager.rest.FileManagerRestController;
import com.testapp.fileManager.rest.customexceptions.FileStorageException;
import com.testapp.fileManager.rest.customexceptions.UnsupportedFileFormatException;
import com.testapp.fileManager.rest.responses.FileInfoResponse;
import com.testapp.fileManager.service.FileManagerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FileUploaderApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private FileManagerService service;

	@Autowired
	private FileManagerRestController controller;

	private MockMultipartFile file1;
	private MockMultipartFile file2;
	private MockMultipartFile file3;

	private FileInfoResponse response1;
	private FileInfoResponse response2;
	private FileInfoResponse response3;

	@BeforeAll
	static void initializeDataBaseTest() {

	}

	@Test
	void testController() {
		assertNotNull(controller);
	}



	@Test
	void saveResponsesTest() throws IOException {
		file1 = new MockMultipartFile("HelloWorld.TXT", "HelloWorld.TXT", "text/plain", "HelloWorld.TXT".getBytes());
		file2 = new MockMultipartFile("Archetype and Symbol.pdf", "Archetype and Symbol.pdf", "text/plain", "Archetype and Symbol.pdf".getBytes());
		file3 = new MockMultipartFile("TestWord.docx", "TestWord.docx", "text/plain", "TestWord.docx".getBytes());

		response1 = controller.saveFile(file1);
		response2 = controller.saveFile(file2);
		response3 = controller.saveFile(file3);

		assertEquals("HelloWorld.TXT", response1.getFileName());
		assertEquals("Archetype and Symbol.pdf", response2.getFileName());
		assertEquals("TestWord.docx", response3.getFileName());

		assertEquals("txt", response1.getFileType());
		assertEquals("pdf", response2.getFileType());
		assertEquals("docx", response3.getFileType());

		assertEquals(file1.getBytes().length, response1.getFileSize());
		assertEquals(file2.getBytes().length, response2.getFileSize());
		assertEquals(file3.getBytes().length, response3.getFileSize());

		assertEquals(file1.getBytes().length, response1.getFileSize());
		assertEquals(file2.getBytes().length, response2.getFileSize());
		assertEquals(file3.getBytes().length, response3.getFileSize());


	}

	@Test
	void saveWrongFileFormatTest() {
		MockMultipartFile badFile = new MockMultipartFile("Screenshot1.jpg", "Screenshot1.jpg", "text/plain", "Screenshot1.jpg".getBytes());

		UnsupportedFileFormatException thrown = assertThrows(
				UnsupportedFileFormatException.class,
				() -> controller.saveFile(badFile),
				"Expected save bad file to throw, but it didn't"
		);

		assertTrue(thrown.getMessage().contains("Unsupported type of File"));
	}

	@Test
	void updateShouldSuccessTest() {
		FileInfoResponse beforeUpdate = service.getFileInfoById(1);

		LocalDateTime create = beforeUpdate.getUploadDate();
		assertEquals(create, beforeUpdate.getUpdateDate());

		int fileId = beforeUpdate.getFileId();
		MockMultipartFile updateFile = new MockMultipartFile("HelloWorld.TXT", "HelloWorld.TXT", "text/plain", "HelloWorld.TXT".getBytes());
		FileInfoResponse afterUpdate = controller.updateFile(fileId, updateFile);

		assertNotEquals(beforeUpdate.getUpdateDate(), afterUpdate.getUpdateDate());
	}

	@Test
	void updateFailedAndThrowExceptionTest() {
		FileInfoResponse beforeUpdate = service.getFileInfoById(1);
		int fileId = beforeUpdate.getFileId();

		MockMultipartFile badFileForUpdate = new MockMultipartFile("TestWord.docx", "TestWord.docx", "text/plain", "TestWord.docx".getBytes());

		FileStorageException thrown = assertThrows(
				FileStorageException.class,
				() -> controller.updateFile(fileId, badFileForUpdate),
				"Expected update bad file to throw, but it didn't"
		);

		assertTrue(thrown.getMessage().contains("Can't update, it's another File"));
	}


	@Test
	void getFileNamesListTest() {
		List<OnlyFileNames> listNames = controller.getFileNamesList();

		assertNotNull(listNames);
		assertEquals(3, listNames.size());
	}

	@Test
	void getFileById() {
		MockMultipartFile fileInput = new MockMultipartFile("HelloWorld.TXT", "HelloWorld.TXT", "text/plain", "HelloWorld.TXT".getBytes());

		controller.downloadFileById(1).getStatusCode();

	}

//	@Test
//	public void testReturn200() throws Exception {
//		given(controller.getFileById(any()));
//		mockMvc.perform(get("/api/files")
//				.andExpect(status().isOk())
//				.andExpect(content()
//						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
//	}

//	@Test
//	public void test() throws Exception {
//		// Mock Request
//		MockMultipartFile jsonFile = new MockMultipartFile("test.json", "", "application/json", "{\"key1\": \"value1\"}".getBytes());
//
//
//		// Mock Response
////		FileInfoResponse response = new FileInfoResponse();
////		Mockito.when(newController.postV1(Mockito.any(Integer.class), Mockito.any(MultipartFile.class))).thenReturn(response);
//
//		mockMvc.perform(MockMvcRequestBuilders.multipart("/fileUpload")
//				.file("file", jsonFile.getBytes())
//				.characterEncoding("UTF-8"))
//				.andExpect(status().isOk());
//
//	}


	@Test
	void exampleTest(@Autowired MockMvc mvc) throws Exception {
		mvc.perform(get("/"))
				.andExpect(status().isNotFound());
	}




}
