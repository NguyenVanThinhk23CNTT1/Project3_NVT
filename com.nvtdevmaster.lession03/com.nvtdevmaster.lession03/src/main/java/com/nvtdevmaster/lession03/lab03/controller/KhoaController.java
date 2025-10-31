package com.nvtdevmaster.lession03.lab03.controller;

import com.nvtdevmaster.lession03.lab03.entity.Khoa;
import com.nvtdevmaster.lession03.lab03.service.KhoaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lab03/khoa")
public class KhoaController {
    private final KhoaService service;
    public KhoaController(KhoaService service){ this.service = service; }

    @GetMapping public List<Khoa> all(){ return service.findAll(); }

    @GetMapping("/{makh}")
    public ResponseEntity<Khoa> byId(@PathVariable String makh){
        Khoa k = service.findById(makh);
        return k==null ? ResponseEntity.notFound().build() : ResponseEntity.ok(k);
    }

    @PostMapping
    public ResponseEntity<Khoa> create(@RequestBody Khoa k){
        return ResponseEntity.ok(service.create(k));
    }

    @PutMapping("/{makh}")
    public ResponseEntity<Khoa> update(@PathVariable String makh, @RequestBody Khoa input){
        Khoa k = service.update(makh, input);
        return k==null ? ResponseEntity.notFound().build() : ResponseEntity.ok(k);
    }

    @DeleteMapping("/{makh}")
    public ResponseEntity<Void> delete(@PathVariable String makh){
        return service.delete(makh) ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
