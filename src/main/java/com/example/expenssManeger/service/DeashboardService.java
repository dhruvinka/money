package com.example.expenssManeger.service;

import com.example.expenssManeger.dto.ExpenseDto;
import com.example.expenssManeger.dto.IncomeDTO;
import com.example.expenssManeger.dto.RecentTransationDTO;
import com.example.expenssManeger.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

@Service
@RequiredArgsConstructor
public class DeashboardService {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final ProfileService profileService;

    public Map<String, Object> getDeashbordData() {
        ProfileEntity profile = profileService.getCurrentprofile();
        Map<String, Object> returnValue = new LinkedHashMap<>();

        List<IncomeDTO> latestIncome = incomeService.getLatestincome();
        List<ExpenseDto> latestExpense = expenseService.getLatestExpenses();

        List<RecentTransationDTO> recentTransationDTOS = concat(
                latestIncome.stream().map(income ->
                        RecentTransationDTO.builder()
                                .id(income.getId())
                                .profileId(income.getId())
                                .icon(income.getIcon())
                                .name(income.getName())
                                .amount(income.getAmount())
                                .date(income.getDate())
                                .createdAt(LocalDateTime.parse(income.getCreatedAt()))
                                .updateAt(LocalDateTime.parse(income.getUpdatedAt()))
                                .type("INCOME")
                                .build()),
                latestExpense.stream().map(expense ->
                        RecentTransationDTO.builder()
                                .id(expense.getId())
                                .profileId(expense.getCategoryId())
                                .icon(expense.getIcon())
                                .name(expense.getName())
                                .amount(expense.getAmount())
                                .date(expense.getDate())
                                .createdAt(LocalDateTime.parse(expense.getCreatedAt()))
                                .updateAt(LocalDateTime.parse(expense.getUpdatedAt()))
                                .type("EXPENSE")
                                .build())
        ).sorted((a, b) -> {
            int cmp = b.getDate().compareTo(a.getDate());
            if (cmp == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null) {
                return b.getCreatedAt().compareTo(a.getCreatedAt());
            }
            return cmp;
        }).collect(Collectors.toList());


        returnValue.put("totalBalance",incomeService.getTotalincome().subtract(expenseService.getTotalExpenses()));
        returnValue.put("totalIncome",incomeService.getTotalincome());
        returnValue.put("totalExpense",expenseService.getTotalExpenses());
        returnValue.put("Recent5Income",latestIncome);
        returnValue.put("Recent5Expense",latestExpense);
        returnValue.put("recentTransactions", recentTransationDTOS);
        return returnValue;
    }

    }

