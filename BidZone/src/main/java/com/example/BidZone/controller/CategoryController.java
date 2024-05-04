package com.example.BidZone.controller;


import com.example.BidZone.dto.CategoryDTO;
import com.example.BidZone.entity.Category;
import com.example.BidZone.repostry.CategoryRepository;
import com.example.BidZone.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/auctionappBidZone")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/categories")
    public List<CategoryDTO> findAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping("/addcategories")
    public ResponseEntity<Category> addCategory(@RequestBody CategoryDTO categoryDTO) {
        if (categoryDTO.getName() == null || categoryDTO.getName().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Category category = new Category(categoryDTO.getName());
        categoryRepository.save(category);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }


}
