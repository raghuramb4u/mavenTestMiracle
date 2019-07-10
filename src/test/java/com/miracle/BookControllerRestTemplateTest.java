package com.miracle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miracle.core.repository.BookRepository;
import com.miracle.entity.Book;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // for restTemplate
@ActiveProfiles("test")
public class BookControllerRestTemplateTest {

	private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private BookRepository mockRepository;
	
	@Mock
	Object object;
	
	@Mock
	Map<String, String> update;

	@Before
	public void init() {
		Book book = new Book(1L, "Book Name", "Miracle Books", new BigDecimal("9.99"));
		Book book1 = new Book("Book Name", "Miracle Books", new BigDecimal("9.99"));
		book1.toString();
		when(mockRepository.findById(1L)).thenReturn(Optional.of(book));
		when(update.get(any(String.class))).thenReturn("author");
	}

	@Test
	public void find_bookId_OK() throws JSONException {

		String expected = "{id:1,name:\"Book Name\",author:\"Miracle Books\",price:9.99}";

		ResponseEntity<String> response = restTemplate.getForEntity("/books/1", String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON_UTF8, response.getHeaders().getContentType());

		JSONAssert.assertEquals(expected, response.getBody(), false);

		verify(mockRepository, times(1)).findById(1L);

	}

	@Test
	public void find_allBook_OK() throws Exception {

		List<Book> books = Arrays.asList(new Book(1L, "Book A", "Ah Pig", new BigDecimal("1.99")),
				new Book(2L, "Book B", "Ah Dog", new BigDecimal("2.99")));

		when(mockRepository.findAll()).thenReturn(books);

		String expected = om.writeValueAsString(books);

		ResponseEntity<String> response = restTemplate.getForEntity("/books", String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);

		verify(mockRepository, times(1)).findAll();
	}

	@Test
	public void find_bookIdNotFound_404() throws Exception {

		String expected = "{status:404,error:\"Not Found\",message:\"Book id not found : 5\",path:\"/books/5\"}";

		ResponseEntity<String> response = restTemplate.getForEntity("/books/5", String.class);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);

	}

	@Test
	public void save_book_OK() throws Exception {

		Book newBook = new Book(1L, "Spring Boot Guide", "Miracle Books", new BigDecimal("2.99"));
		when(mockRepository.save(any(Book.class))).thenReturn(newBook);

		String expected = om.writeValueAsString(newBook);

		ResponseEntity<String> response = restTemplate.postForEntity("/books", newBook, String.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);

		verify(mockRepository, times(1)).save(any(Book.class));

	}

	@Test
	public void save_random_book_OK() throws Exception {
		long range = 1234567L;
		Random r = new Random();
		long number = (long) (r.nextDouble() * range);
		Book updateBook = new Book(number, "Spring Boot Guide", "Miracle Books", new BigDecimal("2.99"));

		when(mockRepository.save(any(Book.class))).thenReturn(updateBook);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(om.writeValueAsString(updateBook), headers);

		ResponseEntity<String> response = restTemplate.exchange("/books/" + number, HttpMethod.PUT, entity,
				String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(om.writeValueAsString(updateBook), response.getBody(), false);

		verify(mockRepository, times(1)).findById(number);
		verify(mockRepository, times(1)).save(any(Book.class));

	}

	@Test
	public void update_book_OK() throws Exception {

		Book updateBook = new Book(1L, "ABC", "Miracle Books", new BigDecimal("19.99"));
		when(mockRepository.save(any(Book.class))).thenReturn(updateBook);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(om.writeValueAsString(updateBook), headers);

		ResponseEntity<String> response = restTemplate.exchange("/books/1", HttpMethod.PUT, entity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(om.writeValueAsString(updateBook), response.getBody(), false);

		verify(mockRepository, times(1)).findById(1L);
		verify(mockRepository, times(1)).save(any(Book.class));

	}

	@Test
	public void patch_bookAuthor_OK() {

		when(mockRepository.save(any(Book.class))).thenReturn(new Book());
		String patchInJson = "{\"author\":\"ultraman\"}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(patchInJson, headers);

		ResponseEntity<String> response = restTemplate.exchange("/books/1", HttpMethod.PUT, entity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		verify(mockRepository, times(1)).findById(1L);
		verify(mockRepository, times(1)).save(any(Book.class));

	}

	@Test
	public void delete_book_OK() {

		doNothing().when(mockRepository).deleteById(1L);

		HttpEntity<String> entity = new HttpEntity<>(null, new HttpHeaders());
		ResponseEntity<String> response = restTemplate.exchange("/books/1", HttpMethod.DELETE, entity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		verify(mockRepository, times(1)).deleteById(1L);
	}

	@Test
	public   void printJSON() {
		String result;		
		try {
			result = om.writerWithDefaultPrettyPrinter().writeValueAsString(object);
			assertNotNull(result);
			System.out.println(result);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

}
