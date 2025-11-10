package com.nvtdevmaster.lession04.controller;

import com.nvtdevmaster.lession04.dto.UsersDTO;
import com.nvtdevmaster.lession04.entity.Users;
import com.nvtdevmaster.lession04.service.UsersService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;

    @GetMapping
    public List<Users> getAllUsers() {
        return usersService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        return usersService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> addUser(@Valid @RequestBody UsersDTO user) {
        boolean isCreated = usersService.create(user);
        return isCreated ?
                ResponseEntity.ok("User created successfully") :
                ResponseEntity.badRequest().body("Failed to create user");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @Valid @RequestBody UsersDTO userDTO) {
        boolean isUpdated = usersService.update(id, userDTO);
        return isUpdated ?
                ResponseEntity.ok("User updated successfully") :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        boolean isDeleted = usersService.delete(id);
        return isDeleted ?
                ResponseEntity.ok("User deleted successfully") :
                ResponseEntity.notFound().build();
    }
}
