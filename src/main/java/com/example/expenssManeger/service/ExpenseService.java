package com.example.expenssManeger.service;

import com.example.expenssManeger.Repository.CategoryRepo;
import com.example.expenssManeger.Repository.ExpenseRepo;
import com.example.expenssManeger.dto.ExpenseDto;
import com.example.expenssManeger.entity.CategoryEntity;
import com.example.expenssManeger.entity.ExpenseEntity;
import com.example.expenssManeger.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {


    private  final ExpenseRepo expenseRepo;
    private  final CategoryRepo categoryRepo;
    private  final ProfileService profileService;


    //notification

    public  List<ExpenseDto> getexpenesForUserOnDate(Long profileId, LocalDate date) {
        List<ExpenseEntity> expenses = expenseRepo.findByProfileIdAndDate(profileId, date);
        return expenses.stream().map(this::toDto).toList();
    }


    //filter expenss

    public  List<ExpenseDto> filterexpenses(LocalDate startDate,LocalDate endDate, String keyword, Sort sort) {
        ProfileEntity profile = profileService.getCurrentprofile();
        List<ExpenseEntity> expenses=expenseRepo.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(),startDate,endDate,keyword,sort);
        return expenses.stream().map(this::toDto).toList();
    }






    //latest 5 expenss
    public List<ExpenseDto> getLatestExpenses() {
        ProfileEntity profile = profileService.getCurrentprofile();
        List<ExpenseEntity> expenses = expenseRepo.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return expenses.stream().map(this::toDto).toList();
    }


//    //get total expenss for current year
//    public double getTotalExpensesForCurrentYear() {
//        ProfileEntity profile = profileService.getCurrentprofile();
//        return expenseRepo.(profile.getId(), java.time.LocalDate.now().getYear());
//    }



    //get total expenss by current user
    public BigDecimal getTotalExpenses() {
        ProfileEntity profile = profileService.getCurrentprofile();
        BigDecimal total= expenseRepo.findTotalExpenseByProfileId(profile.getId());
             return  total != null ? total : BigDecimal.ZERO;

    }




    //delete expenss by id fro current user

    public  void deleteExpenseById(Long expenseId) {
        ProfileEntity profile = profileService.getCurrentprofile();
        ExpenseEntity expense = expenseRepo.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + expenseId));
        if(!expense.getProfile().getId().equals(profile.getId()))
        {
         throw  new RuntimeException("Unauthorized to delete the expense");
        }
        expenseRepo.delete(expense);
    }






    //expense current month

    public List<ExpenseDto> getCurrentMonthExpenses() {
        ProfileEntity profile = profileService.getCurrentprofile();
        List<ExpenseEntity> expenses = expenseRepo.findByProfileIdAndDateBetween(
                profile.getId(),
                java.time.LocalDate.now().withDayOfMonth(1),
                java.time.LocalDate.now().withDayOfMonth(java.time.LocalDate.now().lengthOfMonth())
        );
        return expenses.stream().map(this::toDto).toList();
    }









    //adding expense
    public  ExpenseDto addExpense(ExpenseDto dto)
    {
      ProfileEntity profile=  profileService.getCurrentprofile();
       CategoryEntity category=categoryRepo.findById(dto.getCategoryId()).orElseThrow(()->
                new RuntimeException("Category not found with id: " + dto.getCategoryId()));

        ExpenseEntity expenseEntity = toEntity(dto, profile, category);
        ExpenseEntity savedExpense = expenseRepo.save(expenseEntity);
        return toDto(savedExpense);
    }





    //helper
    private  ExpenseEntity toEntity(ExpenseDto dto, ProfileEntity profile, CategoryEntity category) {
        return ExpenseEntity.builder()
                .id(dto.getId())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .name(dto.getName())
                .profile(profile)
                .category(category)
                .build();
    }

    private ExpenseDto toDto(ExpenseEntity entity) {
        return ExpenseDto.builder()
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
