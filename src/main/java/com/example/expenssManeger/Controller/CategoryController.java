package com.example.expenssManeger.Controller;

import com.example.expenssManeger.dto.CategoryDTO;
import com.example.expenssManeger.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<CategoryDTO> saveCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategory = categoryService.saveCategory(categoryDTO);
        return ResponseEntity.ok(savedCategory);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }


    @GetMapping
    public  ResponseEntity<List<CategoryDTO>> getCategories() {
        List<CategoryDTO> categories = categoryService.getCategoriesBForCurrentUser();
        return ResponseEntity.ok(categories);
    }


    @GetMapping("/{type}")
    public ResponseEntity<List<CategoryDTO>> getcategoryBytypeforCurrentUsser(@PathVariable String type)
    {
        List<CategoryDTO> list=  categoryService.getCategoriesByTypeForCurrentUser(type);

        return ResponseEntity.ok(list);
    }



    @PutMapping("/update/{Categoryid}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long Categoryid,@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategory = categoryService.updateCategory(Categoryid,categoryDTO);
        return ResponseEntity.ok(updatedCategory);
    }
}
