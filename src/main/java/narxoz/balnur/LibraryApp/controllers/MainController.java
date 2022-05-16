package narxoz.balnur.LibraryApp.controllers;

import narxoz.balnur.LibraryApp.model.Books;
import narxoz.balnur.LibraryApp.model.BookCategory;
import narxoz.balnur.LibraryApp.model.Users;
import narxoz.balnur.LibraryApp.repository.BookRepository;
import narxoz.balnur.LibraryApp.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @GetMapping(value = "/")
    public String indexPage(Model model) {
        List<Books> booksList = bookRepository.findAll();
        List<BookCategory> categoryList = categoryRepository.findAll();
        model.addAttribute("books", booksList);
        model.addAttribute("categories", categoryList);
        model.addAttribute("user", getCurrentUser());
        return "index";
    }

    @GetMapping(value = "/addBook")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String AddPage(Model model) {
        List<BookCategory> categoryList = categoryRepository.findAll();
        model.addAttribute("categories", categoryList);
        return "addBook";
    }

    @PostMapping(value = "/addBook")
    public String AddBooks(
            @RequestParam(name = "book_name") String name,
            @RequestParam(name = "book_author") String author,
            @RequestParam(name = "book_price") int price,
            @RequestParam(name = "book_review") float review,
            @RequestParam(name = "book_description") String description,
            @RequestParam(name = "book_category_id") Long categoryId
    ) {

        BookCategory category = categoryRepository.findById(categoryId).orElse(null);

        if (category != null) {
            Books book = new Books();
            book.setName(name);
            book.setAuthor(author);
            book.setPrice(price);
            book.setReviews(review);
            book.setDescription(description);
            book.setCategories(category);

            bookRepository.save(book);
        }

        return "redirect:/addBook?success";
    }

    @GetMapping(value = "/details/{idd}")
    public String Details(Model model,
                          @PathVariable(name = "idd") Long id
    ) {
        List<BookCategory> categoryList = categoryRepository.findAll();
        model.addAttribute("categories", categoryList);
        Books book = bookRepository.findById(id).orElse(null);
        model.addAttribute("book", book);
        return "details";
    }


    @PostMapping(value = "/update")
    public String Update(
            @RequestParam(name = "book_id") Long id,
            @RequestParam(name = "book_name") String name,
            @RequestParam(name = "book_author") String author,
            @RequestParam(name = "book_price") int price,
            @RequestParam(name = "book_review") float review,
            @RequestParam(name = "book_description") String description,
            @RequestParam(name = "book_category_id") Long categoryId
    ) {
        Books book = bookRepository.findById(id).orElse(null);
        BookCategory category = categoryRepository.findById(categoryId).orElse(null);


        if (book != null && category != null) {
            book.setName(name);
            book.setAuthor(author);
            book.setPrice(price);
            book.setReviews(review);
            book.setDescription(description);
            book.setCategories(category);

            bookRepository.save(book);

        }
        return "redirect:/details/" + id;
    }

    @PostMapping(value = "/delete")
    public String Delete(
            @RequestParam(name = "book_id") Long id
    ) {
        Books book = bookRepository.findById(id).orElse(null);

        String redirect = "/";
        if (book != null) {
            bookRepository.delete(book);
            redirect = "/?delete";
        }
        return "redirect:" + redirect;
    }

    @GetMapping(value = "/login")
    public String Login(Model model) {
        model.addAttribute("user", getCurrentUser());
        return "login";
    }

    @GetMapping(value = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String Profile(Model model) {
        model.addAttribute("user", getCurrentUser());
        return "profile";
    }

    @GetMapping(value = "/403")
    public String accessDeniedPage(Model model) {
        return "403";
    }


    private Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Users currentUser = (Users) authentication.getPrincipal();
            return currentUser;
        }
        return null;
    }


}
