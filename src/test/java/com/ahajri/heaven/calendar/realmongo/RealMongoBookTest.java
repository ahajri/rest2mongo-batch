package com.ahajri.heaven.calendar.realmongo;

import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ahajri.heaven.calendar.collection.BookCollection;
import static org.junit.Assert.*;

import org.junit.FixMethodOrder;
import org.junit.Ignore;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RealMongoBookTest extends ATest {

	private static final String BASE_URL = "/api/v1/hcalendar/books";

	private static BookCollection persisted;
	private static BookCollection updated;

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

	@Test
	@Ignore
	@Override
	public void testCFindAll() {

	}

	@Ignore
	@Test
	@Override
	public void testDFindByCriteria() {

	}

	@Ignore
	@Test
	@Override
	public void testEDeleteByCriteria() {

	}

}
