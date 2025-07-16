package com.example.expenssManeger.service;
import com.example.expenssManeger.Repository.CategoryRepo;
import com.example.expenssManeger.Repository.IncomeRepo;
import com.example.expenssManeger.dto.ExpenseDto;
import com.example.expenssManeger.dto.IncomeDTO;
import com.example.expenssManeger.entity.CategoryEntity;
import com.example.expenssManeger.entity.ExpenseEntity;
import com.example.expenssManeger.entity.IncomeEntity;
import com.example.expenssManeger.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private  final IncomeRepo incomeRepo;
    private  final CategoryRepo categoryRepo;
    private  final  ProfileService profileService;





    //filter income

    public  List<IncomeDTO> filterexpenses(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {
        ProfileEntity profile = profileService.getCurrentprofile();
        List<IncomeEntity> expenses=incomeRepo.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(),startDate,endDate,keyword,sort);
        return expenses.stream().map(this::toDto).toList();
    }


    //latest 5 ivncome
    public List<IncomeDTO> getLatestincome() {
        ProfileEntity profile = profileService.getCurrentprofile();
        List<IncomeEntity> expenses = incomeRepo.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return expenses.stream().map(this::toDto).toList();
    }


    //get total income by current user
    public BigDecimal getTotalincome() {
        ProfileEntity profile = profileService.getCurrentprofile();
        BigDecimal total= incomeRepo.findTotalExpenseByProfileId(profile.getId());
        return  total != null ? total : BigDecimal.ZERO;

    }



    //delete income by id fro current user

    public  void deleteExpenseById(Long incomeId) {
        ProfileEntity profile = profileService.getCurrentprofile();
        IncomeEntity income = incomeRepo.findById(incomeId)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + incomeId));
        if(!income.getProfile().getId().equals(profile.getId()))
        {
            throw  new RuntimeException("Unauthorized to delete the expense");
        }
        incomeRepo.delete(income);
    }





    //income current month

    public List<IncomeDTO> getCurrentMonthExpenses() {
        ProfileEntity profile = profileService.getCurrentprofile();
        List<IncomeEntity> expenses = incomeRepo.findByProfileIdAndDateBetween(
                profile.getId(),
                java.time.LocalDate.now().withDayOfMonth(1),
                java.time.LocalDate.now().withDayOfMonth(java.time.LocalDate.now().lengthOfMonth())
        );
        return expenses.stream().map(this::toDto).toList();
    }







    public IncomeDTO addExpense(IncomeDTO dto)
    {
        ProfileEntity profile=  profileService.getCurrentprofile();
        CategoryEntity category=categoryRepo.findById(dto.getCategoryId()).orElseThrow(()->
                new RuntimeException("Category not found with id: " + dto.getCategoryId()));

        IncomeEntity newincome = toEntity(dto, profile, category);
        IncomeEntity savedExpense = incomeRepo.save(newincome);
        return toDto(savedExpense);
    }


    //helper
    private  IncomeEntity toEntity(IncomeDTO dto, ProfileEntity profile, CategoryEntity category) {
        return IncomeEntity.builder()
                .id(dto.getId())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .name(dto.getName())
                .date(dto.getDate())
                .profile(profile)
                .category(category)
                .build();
    }

    private IncomeDTO toDto(IncomeEntity entity) {
        return IncomeDTO.builder()
                .id(entity.getId())
                .icon(entity.getIcon())
                .name(entity.getName())
                .amount(entity.getAmount())
                .date(entity.getDate())
                .categoryId(entity.getCategory().getId())
                .categoryName(entity.getCategory().getName())
                .createdAt(String.valueOf(entity.getCreatedAt()))
                .updatedAt(String.valueOf(entity.getUpdatedAt()))
                .build();
    }

}
