package cz.shmoula.textio.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cz.shmoula.textio.domain.Book;


@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
	
}
