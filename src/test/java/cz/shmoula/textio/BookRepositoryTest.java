package cz.shmoula.textio;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cz.shmoula.textio.service.BookRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("test-service-context.xml")
public class BookRepositoryTest {

	@Autowired
	private BookRepository bookRepository;

	@Test
	public void testOfNothingJustCountBooks() {
		
		assertEquals(2, bookRepository.count());
	}
}