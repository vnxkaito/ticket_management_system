package com.ga.tms.controller;

import com.ga.tms.model.TicketCategory;
import com.ga.tms.service.TicketCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class TicketCategoryController {

    private final TicketCategoryService ticketCategoryService;

    @Autowired
    public TicketCategoryController(TicketCategoryService ticketCategoryService) {
        this.ticketCategoryService = ticketCategoryService;
    }

    @PostMapping
    public ResponseEntity<TicketCategory> createCategory(@RequestBody TicketCategory category) {
        return ResponseEntity.ok(ticketCategoryService.createCategory(category));
    }

    @GetMapping
    public ResponseEntity<List<TicketCategory>> getAllCategories() {
        return ResponseEntity.ok(ticketCategoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketCategory> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketCategoryService.getCategoryById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketCategory> updateCategory(@PathVariable Long id, @RequestBody TicketCategory category) {
        return ResponseEntity.ok(ticketCategoryService.updateCategory(id, category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        ticketCategoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully.");
    }
}
