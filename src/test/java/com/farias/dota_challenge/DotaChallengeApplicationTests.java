package com.farias.dota_challenge;

import java.io.IOException;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.hamcrest.Matchers.*;

import com.fasterxml.jackson.core.JsonProcessingException;

@SpringBootTest
@AutoConfigureMockMvc
class DotaChallengeApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Value("classpath:data/combatlog_1.log.txt")
	private Resource file1;

	@Value("classpath:data/combatlog_2.log.txt")
	private Resource file2;
	
	@Test
	public void contextLoads() throws JsonProcessingException, Exception {
		assertDataLoaded(file1, "1");
		assertDataLoaded(file2, "2");
	
		assertHeroKills();
		assertItems();
		assertHeroSpells();
		assertHeroDamage();
	}

	private void assertHeroDamage() throws Exception {
		callService("/2/npc_dota_hero_mars/damage")
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(10)));
	}

	private void assertHeroSpells() throws Exception {
		callService("/2/npc_dota_hero_mars/spells")
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)));
	}
	
	private void assertHeroKills() throws Exception {
		callService("/2")
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(44)));
	}

	private void assertItems() throws Exception {
		callService("/2/npc_dota_hero_mars/items")
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(25)));
	}
	
	private void assertDataLoaded(Resource file, String expectedResult) throws Exception {
		MockMultipartFile multipartFile = createMockMultipartFile(file);
		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/match")
				.file(multipartFile)
				.contentType(MediaType.TEXT_PLAIN).content(multipartFile.getBytes()))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(MockMvcResultMatchers.content().string(expectedResult));
		
	}

	private ResultActions callService(String api) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get("/api/match" + api).contentType(MediaType.TEXT_PLAIN)
                .accept(MediaType.APPLICATION_JSON));
    }
	
	private MockMultipartFile createMockMultipartFile(Resource resource) throws IOException {
		String boundary = "j1w2e3r4t5y2u7i8o1";
		MockMultipartFile multipartFile = new MockMultipartFile("payload", resource.getFilename(), "text/plain",
				createFileContent(resource.getInputStream().readAllBytes(), boundary, "text/plain",
						resource.getFilename()));
		return multipartFile;
	}

	private byte[] createFileContent(byte[] data, String boundary, String contentType, String fileName) {
		String start = "--" + boundary + "\r\n Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName
				+ "\"\r\n" + "Content-type: " + contentType + "\r\n\r\n";
		;

		String end = "\r\n--" + boundary + "--";
		return ArrayUtils.addAll(start.getBytes(), ArrayUtils.addAll(data, end.getBytes()));
	}

}
