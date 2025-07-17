package com.example.expenssManeger.Controller;

import com.example.expenssManeger.dto.ExpenseDto;
import com.example.expenssManeger.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expense")
@CrossOrigin(origins = "*")
public class ExpenseController {


    private  final ExpenseService expenseService;


    @PostMapping
    public ResponseEntity<ExpenseDto> addExpense(@RequestBody  ExpenseDto expenseDto) {
        ExpenseDto savedExpense = expenseService.addExpense(expenseDto);
        return ResponseEntity.ok(savedExpense);
    }



    @GetMapping
    public  ResponseEntity<List<ExpenseDto>> getExpenss()
    {
        List<ExpenseDto> expenseDtos=expenseService.getCurrentMonthExpenses();
        return ResponseEntity.ok(expenseDtos);
    }



    @DeleteMapping("/{expenseId}")
    public  ResponseEntity<Void> delete(@PathVariable Long expenseId) {
        expenseService.deleteExpenseById(expenseId);
        return ResponseEntity.noContent().build();
    }

}
