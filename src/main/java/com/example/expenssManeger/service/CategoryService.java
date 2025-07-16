package com.example.expenssManeger.service;

import com.example.expenssManeger.Repository.CategoryRepo;
import com.example.expenssManeger.dto.CategoryDTO;
import com.example.expenssManeger.entity.CategoryEntity;
import com.example.expenssManeger.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final ProfileService profileService;
    private final CategoryRepo categoryRepo;

    // Save category
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        ProfileEntity profile = profileService.getCurrentprofile();

        if (categoryRepo.existsByNameAndProfile_Id(categoryDTO.getName(), profile.getId())) {
            throw new IllegalArgumentException("Category with this name already exists for this profile.");
        }

        CategoryEntity newCategory = toEntity(categoryDTO, profile);
        newCategory = categoryRepo.save(newCategory);
        return toDTO(newCategory);
    }

    // Get categories for current profile
    public List<CategoryDTO> getAllCategories() {
        ProfileEntity profile = profileService.getCurrentprofile();
        List<CategoryEntity> categories = categoryRepo.findByProfile_Id(profile.getId());
        return categories.stream().map(this::toDTO).collect(Collectors.toList());
    }

    //get category for current user
    public  List<CategoryDTO> getCategoriesBForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentprofile();
        List<CategoryEntity> categories = categoryRepo.findByProfile_Id(profile.getId());
        return categories.stream().map(this::toDTO).collect(Collectors.toList());
    }



    public  List<CategoryDTO> getCategoriesByTypeForCurrentUser(String type) {
        ProfileEntity profile = profileService.getCurrentprofile();
        List<CategoryEntity> categories = categoryRepo.findByTypeAndProfile_Id( type, profile.getId());
        return categories.stream().map(this::toDTO).collect(Collectors.toList());
    }

    //update category
    public CategoryDTO updateCategory(Long Categoryid,CategoryDTO categoryDTO) {
        ProfileEntity profile = profileService.getCurrentprofile();
        CategoryEntity existingCategory = categoryRepo.findByIdAndProfile_Id( Categoryid,profile.getId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found for this profile."));

        existingCategory.setName(categoryDTO.getName());
        existingCategory.setIcon(categoryDTO.getIcon());
        existingCategory.setType(categoryDTO.getType());

        CategoryEntity updatedCategory = categoryRepo.save(existingCategory);
        return toDTO(updatedCategory);
    }




    private CategoryEntity toEntity(CategoryDTO categoryDTO, ProfileEntity profile) {
        return CategoryEntity.builder()
                .name(categoryDTO.getName())
                .icon(categoryDTO.getIcon())
                .profile(profile)
                .type(categoryDTO.getType())
                .build();
    }

    private CategoryDTO toDTO(CategoryEntity categoryEntity) {
        return CategoryDTO.builder()
                .id(categoryEntity.getId())
                .name(categoryEntity.getName())
                .icon(categoryEntity.getIcon())
                .type(categoryEntity.getType())
                .profileId(categoryEntity.getProfile().getId())
                .createdAt(categoryEntity.getCreatedAt().toLocalDateTime())
                .updatedAt(categoryEntity.getUpdatedAt().toLocalDateTime())
                .build();
    }
}
