package com.example.welcome;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    // GET: http://localhost:8080/api/hello
    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("message", "Hello from Spring Boot!");
    }

    // GET: http://localhost:8080/api/users/123
    @GetMapping("/users/{id}")
    public Map<String, Object> getUser(@PathVariable int id) {
        return Map.of("id", id, "name", "User " + id);
    }

    // POST: http://localhost:8080/api/echo
    // Body JSON: {"name":"Thinh","age":20}
    @PostMapping("/echo")
    public ResponseEntity<Map<String, Object>> echo(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(Map.of(
                "you_sent", body,
                "note", "POST received successfully"
        ));
    }
}