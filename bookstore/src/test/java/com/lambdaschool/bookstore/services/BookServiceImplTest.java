package com.lambdaschool.bookstore.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.bookstore.BookstoreApplication;
import com.lambdaschool.bookstore.exceptions.ResourceNotFoundException;
import com.lambdaschool.bookstore.models.*;
import com.lambdaschool.bookstore.repository.AuthorRepository;
import com.lambdaschool.bookstore.repository.BookRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookstoreApplication.class, properties = {"command.line.runner.enabled=false"})
//**********
// Note security is handled at the controller, hence we do not need to worry about security here!
//**********
public class BookServiceImplTest {

	private final List<Book> listBooks = new ArrayList<>();

	@Autowired
	private BookService bookService;

	@MockBean
	private BookRepository   bookRepo;
	@MockBean
	private SectionService   sectionService;
	@MockBean
	private AuthorRepository authorRepo;
	@MockBean
	private UserAuditing     userAuditing;


	@Before
	public void setUp()
	throws Exception {

		Role r1 = new Role("admin");
		Role r2 = new Role("user");
		Role r3 = new Role("data");
		r1.setRoleid(1);
		r2.setRoleid(2);
		r3.setRoleid(3);

		//		r1 = roleService.save(r1);
		//		r2 = roleService.save(r2);
		//		r3 = roleService.save(r3);

		// admin, data, user
		User u1 = new User("admin", "password", "admin@lambdaschool.local");
		u1.getRoles()
				.add(new UserRoles(u1, r1));
		u1.getRoles()
				.add(new UserRoles(u1, r2));
		u1.getRoles()
				.add(new UserRoles(u1, r3));
		u1.getUseremails()
				.add(new Useremail(u1, "admin@email.local"));
		u1.getUseremails()
				.add(new Useremail(u1, "admin@mymail.local"));
		u1.setUserid(11);


		//		userService.save(u1);

		// data, user
		User u2 = new User("cinnamon", "1234567", "cinnamon@lambdaschool.local");
		u2.getRoles()
				.add(new UserRoles(u2, r2));
		u2.getRoles()
				.add(new UserRoles(u2, r3));
		u2.getUseremails()
				.add(new Useremail(u2, "cinnamon@mymail.local"));
		u2.getUseremails()
				.add(new Useremail(u2, "hops@mymail.local"));
		u2.getUseremails()
				.add(new Useremail(u2, "bunny@email.local"));
		//		userService.save(u2);
		u2.setUserid(22);

		// user
		User u3 = new User("barnbarn", "ILuvM4th!", "barnbarn@lambdaschool.local");
		u3.getRoles()
				.add(new UserRoles(u3, r2));
		u3.getUseremails()
				.add(new Useremail(u3, "barnbarn@email.local"));
		//		userService.save(u3);
		u3.setUserid(33);

		User u4 = new User("puttat", "password", "puttat@school.lambda");
		u4.getRoles()
				.add(new UserRoles(u4, r2));
		//		userService.save(u4);
		u4.setUserid(44);

		User u5 = new User("misskitty", "password", "misskitty@school.lambda");
		u5.getRoles()
				.add(new UserRoles(u5, r2));
		//		userService.save(u5);
		u5.setUserid(55);

		/************
		 * Seed Books
		 ************/

		Author a1 = new Author("John", "Mitchell");
		Author a2 = new Author("Dan", "Brown");
		Author a3 = new Author("Jerry", "Poe");
		Author a4 = new Author("Wells", "Teague");
		Author a5 = new Author("George", "Gallinger");
		a1.setAuthorid(1);
		a2.setAuthorid(2);
		a3.setAuthorid(3);
		a4.setAuthorid(4);
		a5.setAuthorid(5);

		//		a1 = authorService.save(a1);
		//		a2 = authorService.save(a2);
		//		a3 = authorService.save(a3);
		//		a4 = authorService.save(a4);
		//		a5 = authorService.save(a5);
		//		a6 = authorService.save(a6);

		Section s2 = new Section("Technology");
		Section s3 = new Section("Travel");
		Section s4 = new Section("Business");
		Section s5 = new Section("Religion");
		s2.setSectionid(22);
		s3.setSectionid(33);
		s4.setSectionid(44);
		s5.setSectionid(55);


		Section s1 = new Section("Fiction");
		s1.setSectionid(11);

		Author a6 = new Author("Ian", "Stewart");
		a6.setAuthorid(6);

		Book b1 = new Book("Flatterland", "9780738206752", 2001, s1);
		b1.getWrotes()
				.add(new Wrote(a6, new Book()));
		//		b1 = bookService.save(b1);
		b1.setBookid(11);
		listBooks.add(b1);

		Book b2 = new Book("Digital Fortess", "9788489367012", 2007, s1);
		b2.getWrotes()
				.add(new Wrote(a2, new Book()));
		//		b2 = bookService.save(b2);
		b2.setBookid(12);

		listBooks.add(b2);
		Book b3 = new Book("The Da Vinci Code", "9780307474278", 2009, s1);
		b3.getWrotes()
				.add(new Wrote(a2, new Book()));
		//		b3 = bookService.save(b3);
		b3.setBookid(13);

		listBooks.add(b3);
		Book b4 = new Book("Essentials of Finance", "1314241651234", 0, s4);
		b4.getWrotes()
				.add(new Wrote(a3, new Book()));
		b4.getWrotes()
				.add(new Wrote(a5, new Book()));
		//		b4 = bookService.save(b4);
		b4.setBookid(14);

		listBooks.add(b4);
		Book b5 = new Book("Calling Texas Home", "1885171382134", 2000, s3);
		b5.getWrotes()
				.add(new Wrote(a4, new Book()));
		//		b5 = bookService.save(b5);
		b5.setBookid(15);
		listBooks.add(b5);
		//		listBooks.addAll(Arrays.asList(b1, b2, b3, b4, b5));

		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown()
	throws Exception {
	}

	@Test
	public void findAll() {
		Mockito.when(bookRepo.findAll())
				.thenReturn(listBooks);
		assertEquals(5,
				bookService.findAll()
						.size()
		);
	}

	@Test
	public void findBookById() {
		Mockito.when(bookRepo.findById(11L))
				.thenReturn(Optional.of(listBooks.get(0)));
		assertEquals("Flatterland",
				bookService.findBookById(11)
						.getTitle()
		);

	}

	@Test(expected = ResourceNotFoundException.class)
	public void notFindBookById() {
		Mockito.when(bookRepo.findById(4L))
				.thenReturn(Optional.empty());
		assertEquals("Flatterland",
				bookService.findBookById(4)
						.getTitle()
		);
	}

	@Test
	public void delete() {
		Mockito.when(bookRepo.findById(11L))
				.thenReturn(Optional.of(listBooks.get(0)));
		Mockito.doNothing()
				.when(bookRepo)
				.deleteById(11L);
		bookService.delete(11);
		assertEquals(5, listBooks.size());
	}

	@Test
	public void save() {
		Section s1 = new Section("Fiction");
		s1.setSectionid(11);

		Author a6 = new Author("Ian", "Stewart");
		a6.setAuthorid(6);

		String b1Title = "Testbook";
		Book   b1      = new Book(b1Title, "9780738206752", 2001, s1);
		b1.getWrotes()
				.add(new Wrote(a6, new Book()));
		b1.setBookid(0);

		Mockito.when(bookRepo.save(any(Book.class)))
				.thenReturn(b1);
		Mockito.when(authorRepo.findById(6L))
				.thenReturn(Optional.of(a6));
		Mockito.when(sectionService.findSectionById(11))
				.thenReturn(s1);

		Book savedBook = bookService.save(b1);

		assertNotNull(savedBook);
		assertEquals(b1Title, savedBook.getTitle());

	}

	@Test
	public void update()
	throws Exception {
		Section s1 = new Section("Fiction");
		s1.setSectionid(11);

		Author a6 = new Author("Ian", "Stewart");
		a6.setAuthorid(6);

		String b1Title = "Testbook";
		Book   b1      = new Book(b1Title, "9780738206752", 2001, s1);
		b1.getWrotes()
				.add(new Wrote(a6, new Book()));
		b1.setBookid(11);

		ObjectMapper objectMapper = new ObjectMapper();
		Book         copyB1       = objectMapper.readValue(objectMapper.writeValueAsString(b1), Book.class);

		Mockito.when(bookRepo.findById(11L))
				.thenReturn(Optional.of(copyB1));
		Mockito.when(authorRepo.findById(6L))
				.thenReturn(Optional.of(a6));
		Mockito.when(sectionService.findSectionById(11))
				.thenReturn(s1);
		Mockito.when(bookRepo.save(any(Book.class)))
				.thenReturn(b1);

		Book updatedBook = bookService.update(b1, 11);
		assertNotNull(updatedBook);
		assertEquals(b1Title, updatedBook.getTitle());
	}

	@Test
	public void deleteAll() {
		Mockito.doNothing()
				.when(bookRepo)
				.deleteAll();
		bookService.deleteAll();
		assertEquals(5, listBooks.size());
	}

}