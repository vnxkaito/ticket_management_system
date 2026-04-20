package com.ga.tms.service;

import com.ga.tms.exceptions.InformationExistException;
import com.ga.tms.exceptions.InformationNotFoundException;
import com.ga.tms.model.TicketCategory;
import com.ga.tms.repository.TicketCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketCategoryService {

    private final TicketCategoryRepository ticketCategoryRepository;

    @Autowired
    public TicketCategoryService(TicketCategoryRepository ticketCategoryRepository) {
        this.ticketCategoryRepository = ticketCategoryRepository;
    }

    public TicketCategory createCategory(TicketCategory category) {
        if (ticketCategoryRepository.existsByName(category.getName())) {
            throw new InformationExistException("Category with name " + category.getName() + " already exists.");
        }
        return ticketCategoryRepository.save(category);
    }

    public List<TicketCategory> getAllCategories() {
        return ticketCategoryRepository.findAll();
    }

    public TicketCategory getCategoryById(Long id) {
        return ticketCategoryRepository.findById(id)
                .orElseThrow(() -> new InformationNotFoundException("No category found with id " + id));
    }

    public TicketCategory getCategoryByName(String name) {
        return ticketCategoryRepository.findByName(name)
                .orElseThrow(() -> new InformationNotFoundException("Category '" + name + "' doesn't exist"));
    }

    public TicketCategory updateCategory(Long id, TicketCategory categoryDetails) {
        TicketCategory category = getCategoryById(id);
        category.setName(categoryDetails.getName());
        category.setDefaultPriority(categoryDetails.getDefaultPriority());
        return ticketCategoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        TicketCategory category = getCategoryById(id);
        ticketCategoryRepository.delete(category);
    }
}
