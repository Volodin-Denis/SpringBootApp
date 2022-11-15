package com.example.ProjectTwoBoot.services;


import com.example.ProjectTwoBoot.models.Book;
import com.example.ProjectTwoBoot.models.Person;
import com.example.ProjectTwoBoot.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@Transactional(readOnly = true)
public class BookService {

    private final BooksRepository booksRepository;

    @Autowired
    public BookService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(boolean sortedByName){
        if(sortedByName)return booksRepository.findAll(Sort.by("name"));
        else return booksRepository.findAll();

    }

    public Book findOne(int id){
        return booksRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Book book){
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook){
        updatedBook.setId(id);
        booksRepository.save(updatedBook);
    }
    @Transactional
    public void delete(int id){
        booksRepository.deleteById(id);
    }

    public List<Book> findAllByOwner(Person owner) {
        return booksRepository.findAllByOwner(owner);
    }

    @Transactional
    public void assign(int bookId, Person person) {
        Book updatedBook = booksRepository.findById(bookId).orElse(null);
        assert updatedBook != null;
        updatedBook.setOwner(person);
        updatedBook.setTakenAt(new Date());
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void unassign(int bookId) {
        Book updatedBook = booksRepository.findById(bookId).orElse(null);
        assert updatedBook != null;
        updatedBook.setOwner(null);
        updatedBook.setTakenAt(null);
        booksRepository.save(updatedBook);
    }

    public List<Book> search(String query){
        return booksRepository.findAllByNameStartingWith(query);
    }

    public List<Book> findOnPage(int page, int books_per_page, boolean sortedByName) {
        List<Book> allBooks;
        if(sortedByName) allBooks = booksRepository.findAll(Sort.by("name"));
        else allBooks = booksRepository.findAll();
        List<Book> booksOnPage = new ArrayList<>();
        for(int i = page*books_per_page;i<page*books_per_page+books_per_page&&i<allBooks.size();i++){
            booksOnPage.add(allBooks.get(i));
        }
        return booksOnPage;
    }

//    public void updateHolder(int bookId, Optional<Integer> personId){
//        Book book = booksRepository.findById(bookId).orElse(null);
//        assert book != null;
//        if(personId.isPresent()){
//            book.setPersonId(personId);
//        }else {
//            book.setPersonId(Optional.empty());
//        }
//        booksRepository.save(book);
//    }
}
