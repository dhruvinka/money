package com.example.expenssManeger.Controller;

import com.example.expenssManeger.dto.ExpenseDto;
import com.example.expenssManeger.dto.IncomeDTO;
import com.example.expenssManeger.service.ExpenseService;
import com.example.expenssManeger.service.IncomeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/income")
@CrossOrigin(origins = "*")
public class IncomeController {


    private  final IncomeService incomeService;

    @PostMapping("/addincome")
    public ResponseEntity<IncomeDTO> addExpense(@RequestBody  IncomeDTO Dto) {
        IncomeDTO savedExpense = incomeService.addExpense(Dto);
        return ResponseEntity.ok(savedExpense);
    }

    @GetMapping
    public  ResponseEntity<List<IncomeDTO>> getExpenss()
    {
        List<IncomeDTO> expenseDtos=incomeService.getCurrentMonthExpenses();
        return ResponseEntity.ok(expenseDtos);
    }


    @DeleteMapping("/{incomeId}")
    public  ResponseEntity<Void> delete(@PathVariable Long incomeId) {
        incomeService.deleteExpenseById(incomeId);
        return ResponseEntity.noContent().build();
    }


}
