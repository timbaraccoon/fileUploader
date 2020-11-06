package com.testapp.fileManager;

import com.testapp.fileManager.dao.OnlyFileNames;
import com.testapp.fileManager.rest.FileManagerRestController;
import com.testapp.fileManager.rest.customexceptions.UnsupportedFileFormatException;
import com.testapp.fileManager.rest.responses.FileInfoResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FileUploaderApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private FileManagerRestController controller;

	@Autowired
	private WebApplicationContext webApplicationContext;

	FileInfoResponse response1;
	FileInfoResponse response2;
	FileInfoResponse response3;

	@Test
	void testController() {
		assertNotNull(controller);
	}

	@Test
	void initializeDataBase() {
		MockMultipartFile file1 = new MockMultipartFile("HelloWorld.txt", "HelloWorld.txt", "text/plain", "HelloWorld.txt".getBytes());
		MockMultipartFile file2 = new MockMultipartFile("Archetype and Symbol.pdf", "Archetype and Symbol.pdf", "text/plain", "Archetype and Symbol.pdf".getBytes());
		MockMultipartFile file3 = new MockMultipartFile("TestWord.docx", "TestWord.docx", "text/plain", "TestWord.docx".getBytes());

		response1 = controller.saveFile(file1);
		response2 = controller.saveFile(file2);
		response3 = controller.saveFile(file3);
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
	void saveWrongTooBigFileTest() {
		MockMultipartFile badFile = new MockMultipartFile("Screenshot1.jpg", "Screenshot1.jpg", "text/plain", "Screenshot1.jpg".getBytes());

		UnsupportedFileFormatException thrown = assertThrows(
				UnsupportedFileFormatException.class,
				() -> controller.saveFile(badFile),
				"Expected save bad file to throw, but it didn't"
		);

		assertTrue(thrown.getMessage().contains("Unsupported type of File"));
	}

	@Test
	void getFileNamesListTest() {
		List<OnlyFileNames> listNames = controller.getFileNamesList();

		assertNotNull(listNames);
		assertEquals(3, listNames.size());
	}

	@Test
	public void test() throws Exception {
		// Mock Request
		MockMultipartFile jsonFile = new MockMultipartFile("test.json", "", "application/json", "{\"key1\": \"value1\"}".getBytes());


		// Mock Response
//		FileInfoResponse response = new FileInfoResponse();
//		Mockito.when(newController.postV1(Mockito.any(Integer.class), Mockito.any(MultipartFile.class))).thenReturn(response);

		mockMvc.perform(MockMvcRequestBuilders.multipart("/fileUpload")
				.file("file", jsonFile.getBytes())
				.characterEncoding("UTF-8"))
				.andExpect(status().isOk());

	}


	@Test
	void exampleTest(@Autowired MockMvc mvc) throws Exception {
		mvc.perform(get("/"))
				.andExpect(status().isNotFound());
//				.andExpect(content().string("Hello World"));
	}




}
