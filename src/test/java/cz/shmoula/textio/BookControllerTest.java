package cz.shmoula.textio;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "test-service-context.xml", "test-servlet-context.xml" })
public class BookControllerTest {
	
	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
	public void testGetBookListShouldReturnTwo() throws Exception {
		mockMvc.perform(
				get("/books").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(2)));
	}
	
	@Test
	public void testGetExistingBook() throws Exception {
		mockMvc.perform(
				get("/book/1").accept(MediaType.TEXT_PLAIN))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.TEXT_PLAIN));
	}
	
	@Test
	public void testGetNonExistingBook() throws Exception {
		mockMvc.perform(
				get("/book/666").accept(MediaType.TEXT_PLAIN))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.TEXT_PLAIN));
	}
	
	@Test
	public void testCreateBook() throws Exception {
		String author = "Karel Jaromir Erben";
		String title = "Kytice";
		
		mockMvc.perform(
				post("/book").contentType(MediaType.TEXT_PLAIN).accept(MediaType.APPLICATION_JSON)
				.header(BookController.HEADER_AUTHOR, author)
				.header(BookController.HEADER_TITLE, title)
				.content("asd"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(3))
				.andExpect(jsonPath("$.author").value(author))
				.andExpect(jsonPath("$.title").value(title));
	}
}
