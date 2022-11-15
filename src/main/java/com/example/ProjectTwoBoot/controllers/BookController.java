package com.example.ProjectTwoBoot.controllers;


import com.example.ProjectTwoBoot.models.Book;
import com.example.ProjectTwoBoot.models.Person;
import com.example.ProjectTwoBoot.services.BookService;
import com.example.ProjectTwoBoot.services.PeopleService;
import com.example.ProjectTwoBoot.util.BookValidator;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final BookValidator bookValidator;
    private final PeopleService peopleService;

    public BookController(BookService bookService, BookValidator bookValidator, PeopleService peopleService) {
        this.bookService = bookService;
        this.bookValidator = bookValidator;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "page",required = false) Optional<Integer> page,
                        @RequestParam(value = "books_per_page",required = false) Optional<Integer> booksPerPage,
                        @RequestParam(value = "sorted_by_name",required = false) Optional<Boolean> sortedByName){
        if(!sortedByName.isPresent()){
            sortedByName = Optional.of(false);
        }
        if(!page.isPresent()){
            model.addAttribute("books", bookService.findAll(sortedByName.get()));
        } else {
            model.addAttribute("books", bookService.findOnPage(page.get(), booksPerPage.get(),sortedByName.get()));
        }
        return "books/index";
    }
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model,@ModelAttribute("person") Person person){
        Book book = bookService.findOne(id);
        model.addAttribute("book", book);
        model.addAttribute("people", peopleService.findAll());
        if(book.getOwner()!=null){model.addAttribute("person",book.getOwner());}
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book){
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult){
        bookValidator.validate(book,bindingResult);
        if(bindingResult.hasErrors()) return "books/new";
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("book", bookService.findOne(id));
        return"books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult , @PathVariable("id") int id){
        bookValidator.validate(book,bindingResult);
        if(bindingResult.hasErrors()) return "books/edit";
        bookService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id){
        bookService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/assign/{bookId}")
    public String assign(@ModelAttribute("person") Person person, @PathVariable int bookId){
        bookService.assign(bookId, person);
        return "redirect:/books/" + bookId;
    }

    @PatchMapping("/unassign/{bookId}")
    public String unassign(@PathVariable int bookId){
        bookService.unassign(bookId);
        return "redirect:/books/" + bookId;
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "query", required = false) String query, Model model){
        if(query==null){
            model.addAttribute("noQuery", true);
            return "/books/search";
        }
        List<Book> books = bookService.search(query);
        model.addAttribute("searchResult", books);
        model.addAttribute("noQuery", false);
        return "/books/search";
    }
}
