package com.lambdaschool.bookstore.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.bookstore.BookstoreApplication;
import com.lambdaschool.bookstore.exceptions.ResourceNotFoundException;
import com.lambdaschool.bookstore.models.*;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;


//**********
// Note security is handled at the controller, hence we do not need to worry about security here!
//**********
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookstoreApplication.class, properties = {"command.line.runner.enabled=true"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BookServiceImplTestWithDB {

	@Autowired
	private BookService bookService;

	@Before
	public void setUp()
	throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown()
	throws Exception {
	}

	@Test
	public void a_findAll() {
		List<Book> books = bookService.findAll();
		for (Book book : books) {
			System.out.println("ID: " + book.getBookid() + " TITLE: " + book.getTitle());
		}
		assertEquals(5,
				bookService.findAll()
						.size()
		);
	}

	@Test
	public void b_findBookById() {

		assertEquals("Flatterland",
				bookService.findBookById(26)
						.getTitle()
		);
	}

	@Test(expected = ResourceNotFoundException.class)
	public void c_notFindBookById() {
		assertEquals("Flatterland",
				bookService.findBookById(1L)
						.getTitle()
		);
	}

	@Test
	public void d_delete() {
		bookService.delete(29);
		assertEquals(4, bookService.findAll().size());
	}

	@Test
	public void e_save() {
		Section s1 = new Section("Fiction");
		s1.setSectionid(21);

		Author a6 = new Author("Ian", "Stewart");
		a6.setAuthorid(15);

		String b1Title = "Testbook";
		Book   b1      = new Book(b1Title, "9780738206752", 2001, s1);
		b1.getWrotes()
				.add(new Wrote(a6, new Book()));

		Book savedBook = bookService.save(b1);
		assertNotNull(savedBook);
		assertEquals(b1Title, savedBook.getTitle());
	}

	@Test
	public void f_update()
	throws Exception {
		Section s1 = new Section("Fiction");
		s1.setSectionid(21);

		Author a6 = new Author("Ian", "Stewart");
		a6.setAuthorid(15);

		String b1Title = "TestbookAgain";
		Book   b1      = new Book(b1Title, "9780738206752", 2001, s1);
		b1.getWrotes()
				.add(new Wrote(a6, new Book()));
		b1.setBookid(31);

		Book updatedBook = bookService.update(b1, 31);
		assertNotNull(updatedBook);
		assertEquals(b1Title, updatedBook.getTitle());
	}

	@Test
	public void z_deleteAll() {
		bookService.deleteAll();
		assertEquals(0, bookService.findAll().size());
	}

}
