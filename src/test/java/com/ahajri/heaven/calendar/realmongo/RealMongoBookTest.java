package com.ahajri.heaven.calendar.realmongo;

import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ahajri.heaven.calendar.collection.BookCollection;
import com.ahajri.heaven.calendar.constants.enums.OperatorEnum;
import com.ahajri.heaven.calendar.queries.QueryParam;
import com.google.gson.Gson;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.apache.commons.codec.language.bm.Lang;
import org.junit.FixMethodOrder;
import org.junit.Ignore;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RealMongoBookTest extends ATest {

	private static final String BASE_URL = "/api/v1/hcalendar/books";

	private static BookCollection persisted;
	private static BookCollection updated;
	
	private final static Gson gson = new Gson();

	@Test
	@Override
	public void testACreate() {
		BookCollection book = new BookCollection();
		book.setAuthor("Janine Fontaine");
		book.setName("Médecine des trois orps");
		book.setDescription("Energy Healing");
		persisted = restTemplate.postForObject(BASE_URL + "/create", book, BookCollection.class);
		assertNotEquals(null, persisted.getBookId());

	}

	@Test
	@Override
	public void testBUpdate() {
		persisted.setName("Médecine des trois corps");
		ResponseEntity<Void> response=  restTemplate.postForEntity(BASE_URL+"/update", persisted, Void.class);
		assertSame(HttpStatus.OK, response.getStatusCode());
	}

	@SuppressWarnings("unchecked")
	@Test
	@Override
	public void testCFindAll() {
		ResponseEntity<List> response= restTemplate.getForEntity(BASE_URL+"/all", List.class);
		List<BookCollection> books = response.getBody();
		assertSame(1, books.size());
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	@Override
	public void testDFindByCriteria() {
		QueryParam urlVariables = new QueryParam("name", OperatorEnum.EQ, "Médecine des trois corps");
		String encrypted = Base64.getEncoder().encodeToString(gson.toJson(Arrays.asList(urlVariables)).getBytes());
		ResponseEntity<List> response = restTemplate.getForEntity(BASE_URL+"/find/criteria?query="+encrypted, List.class);
		List<BookCollection> books = response.getBody();
		assertSame(1, books.size());
	}

	@Test
	@Override
	public void testEDeleteAllByCriteria() {
		QueryParam urlVariables = new QueryParam("name", OperatorEnum.EQ, "Médecine des trois corps");
		String encrypted = Base64.getEncoder().encodeToString(gson.toJson(Arrays.asList(urlVariables)).getBytes());
		ResponseEntity<Long> response  = restTemplate.getForEntity(BASE_URL+"/delete/criteria?query="+encrypted,Long.class);
		assertEquals(response.getStatusCodeValue(),200);
		assertEquals(response.getBody(), new Long(1));
				

	}

}
