package com.nvtdevmaster.lab08.service;

import com.nvtdevmaster.lab08.config.WebConfig;
import com.nvtdevmaster.lab08.entity.Author;
import com.nvtdevmaster.lab08.entity.Book;
import com.nvtdevmaster.lab08.entity.BookAuthor;
import com.nvtdevmaster.lab08.repository.AuthorRepository;
import com.nvtdevmaster.lab08.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepo;
    private final AuthorRepository authorRepo;

    public List<Book> findAll() {
        return bookRepo.findAll();
    }

    public Book findById(Long id) {
        return bookRepo.findById(id).orElse(null);
    }

    public void save(Book book,
                     List<Long> authorIds,
                     Long editorId,
                     MultipartFile imageFile) {

        // Nếu là update → load lại
        Book target;
        if (book.getId() != null) {
            target = bookRepo.findById(book.getId()).orElse(new Book());
        } else {
            target = new Book();
        }

        // Copy field cơ bản
        target.setCode(book.getCode());
        target.setName(book.getName());
        target.setQuantity(book.getQuantity());
        target.setPrice(book.getPrice());

        // ===========================================
        // UPLOAD ẢNH – FIX 100% KHÔNG TẠO THƯ MỤC RÁC
        // ===========================================
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // lấy đúng tên file
                String original = imageFile.getOriginalFilename();

                String fileName = original.substring(original.lastIndexOf("\\") + 1);
                fileName = fileName.substring(fileName.lastIndexOf("/") + 1);

                // COPY FILE VÀO C:\lab8_intellji\
                Path uploadDir = Paths.get(WebConfig.UPLOAD_DIR);
                Files.createDirectories(uploadDir);

                Path dest = uploadDir.resolve(fileName);

                imageFile.transferTo(dest.toFile());

                target.setImage(fileName);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // ===========================================
        // TÁC GIẢ (cho phép null)
        // ===========================================
        target.getBookAuthors().clear();

        if (authorIds != null) {
            for (Long aId : authorIds) {
                Author author = authorRepo.findById(aId).orElse(null);
                if (author == null) continue;

                BookAuthor ba = new BookAuthor();
                ba.setBook(target);
                ba.setAuthor(author);
                ba.setIsEditor(editorId != null && editorId.equals(aId));

                target.getBookAuthors().add(ba);
            }
        }

        // LƯU DB
        bookRepo.save(target);
    }

    public void delete(Long id) {
        bookRepo.deleteById(id);
    }
}
