package com.testapp.fileManager;

import com.testapp.fileManager.rest.FileManagerRestController;
import com.testapp.fileManager.rest.customexceptions.FileStorageException;
import com.testapp.fileManager.rest.customexceptions.UnsupportedFileFormatException;
import com.testapp.fileManager.rest.responses.FileInfoResponse;
import com.testapp.fileManager.service.FileManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FileUploaderApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

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

	@Test
	void testController() {
		assertNotNull(controller);
	}


	@BeforeEach
	void saveSuccessTest() throws IOException {

		file1 = new MockMultipartFile(
				"file1",
				"HelloWorld.TXT",
				MediaType.TEXT_PLAIN_VALUE,
				"HelloWorld.TXT".getBytes()
		);

		file2 = new MockMultipartFile(
				"file2",
				"Archetype and Symbol.pdf",
				MediaType.TEXT_PLAIN_VALUE,
				"Archetype and Symbol.pdf".getBytes()
		);

		file3 = new MockMultipartFile(
				"file3",
				"TestWord.docx",
				MediaType.TEXT_PLAIN_VALUE,
				"TestWord.docx".getBytes()
		);

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
	void saveSuccessResponseTest() throws Exception {
		MockMultipartFile updateFile = new MockMultipartFile(
				"file",
				"HelloWorld.TXT",
				MediaType.TEXT_PLAIN_VALUE,
				"HelloWorld.TXT".getBytes()
		);

		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

		mockMvc.perform(multipart("/api/files").file(updateFile))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.fileName").value(equalTo("HelloWorld.TXT")))
				.andExpect(jsonPath("$.fileType").value(equalTo("txt")))
				.andExpect(jsonPath("$.fileSize").value(equalTo(14)));
	}

	@Test
	void saveWrongFileExceptionTest() {
		MockMultipartFile badFile = new MockMultipartFile(
				"badFile",
				"Screenshot1.jpg",
				MediaType.IMAGE_JPEG_VALUE,
				"Screenshot1.jpg".getBytes()
		);

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
	void updateResponseTest() throws Exception {
		MockMultipartFile updateFile = new MockMultipartFile(
				"file",
				"HelloWorld.TXT",
				MediaType.TEXT_PLAIN_VALUE,
				"HelloWorld.TXT".getBytes()
		);
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

		mockMvc.perform(multipart("/api/files/1").file(updateFile))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.fileName").value(equalTo("HelloWorld.TXT")))
				.andExpect(jsonPath("$.fileType").value(equalTo("txt")))
				.andExpect(jsonPath("$.fileSize").value(equalTo(14)));
	}


	@Test
	void getFileInfoByIdTest() throws Exception {
		mockMvc.perform(get("/api/files/info/1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.fileName").value(equalTo("HelloWorld.TXT")))
				.andExpect(jsonPath("$.fileType").value(equalTo("txt")))
				.andExpect(jsonPath("$.fileSize").value(equalTo(14)));

	}

	@Test
	void getFileInfoByIdExceptionTest() throws Exception {
		mockMvc.perform(get("/api/files/info/0"))
				.andExpect(status().is4xxClientError())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.status").value(equalTo(404)))
				.andExpect(jsonPath("$.message")
						.value(equalTo("Can't find File with id: 0")));
	}

	@Test
	void getFileInfoByIdWrongPathExceptionTest() throws Exception {
		mockMvc.perform(get("/api/files/info/99999999999999999999999999999"))
				.andExpect(status().is4xxClientError())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.status").value(equalTo(400)))
				.andExpect(jsonPath("$.message")
						.value(containsString("Failed to convert value of type")));
	}

	@Test
	void downloadFileByIdSuccessTest() throws Exception {
		MockMultipartFile readFile = new MockMultipartFile(
				"file",
				"HelloWorld.TXT",
				MediaType.TEXT_PLAIN_VALUE,
				"HelloWorld.TXT".getBytes()
		);

		byte[] goal = readFile.getBytes();

		mockMvc.perform(get("/api/files/1"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(content().contentType(MediaType.TEXT_PLAIN))
				.andExpect(content().bytes(goal));
	}

	@Test
	void downloadFileByIdNotFoundExceptionTest() throws Exception {
		mockMvc.perform(get("/api/files/0"))
				.andExpect(status().is4xxClientError())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.status").value(equalTo(404)))
				.andExpect(jsonPath("$.message")
						.value(equalTo("Can't find File with id: 0")));
	}

	@Test
	void downloadFileByIdWrongPathExceptionTest() throws Exception {
		mockMvc.perform(get("/api/files/aaa"))
				.andExpect(status().is4xxClientError())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.status").value(equalTo(400)))
				.andExpect(jsonPath("$.message")
						.value(containsString("Failed to convert value of type")));
	}

	@Test
	void mvcTest(@Autowired MockMvc mvc) throws Exception {
		mvc.perform(get("/"))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteFileByIdNotFoundExceptionTest() throws Exception {
		mockMvc.perform(delete("/api/files/100500"))
				.andExpect(status().is4xxClientError())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.status").value(equalTo(404)))
				.andExpect(jsonPath("$.message")
						.value(equalTo("Can't find File with id: 100500")));
	}

	@Test
	void deleteFileByIdWrongPathExceptionTest() throws Exception {
		mockMvc.perform(delete("/api/files/aaa"))
				.andExpect(status().is4xxClientError())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.status").value(equalTo(400)))
				.andExpect(jsonPath("$.message")
						.value(containsString("Failed to convert value of type")));
	}


	@Test
	void deleteFileByIdSuccessTest() throws Exception {
		mockMvc.perform(delete("/api/files/1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/plain;charset=UTF-8"))
				.andExpect(content().string("File with id: 1 - successful deleted."));
	}
}
