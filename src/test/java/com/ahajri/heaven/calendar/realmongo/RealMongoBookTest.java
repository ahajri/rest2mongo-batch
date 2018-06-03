package com.ahajri.heaven.calendar.realmongo;

import org.junit.Test;

import com.ahajri.heaven.calendar.collection.BookCollection;
import static org.junit.Assert.*;

import org.junit.Ignore;

public class RealMongoBookTest extends ATest {

	private static final String BASE_URL = "/api/v1/hcalendar/books";

	private static BookCollection persisted;

	@Test
	@Override
	public void testACreateTest() {
		BookCollection book = new BookCollection();
		book.setAuthor("Janine Fontaine");
		book.setName("Médecine des trois orps");
		book.setDescription("Energy Healing");
		persisted = restTemplate.postForObject(BASE_URL + "/create", book, BookCollection.class);
		assertNotEquals(null, book.getBookId());

	}

	@Test
	@Override
	public void testBUpdateTest() {
		persisted.setName("Médecine des trois corps");
		persisted = restTemplate.postForObject(BASE_URL+"/update", persisted, BookCollection.class);
		assertSame(persisted.getName(), "Médecine des trois corps");
	}

	@Test
	@Ignore
	@Override
	public void testCFindAllTest() {

	}

	@Ignore
	@Test
	@Override
	public void testDFindByCriteriaTest() {

	}

	@Ignore
	@Test
	@Override
	public void testEDeleteByCriteriaTest() {

	}

}
