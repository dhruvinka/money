package com.example.expenssManeger.Controller;

import com.example.expenssManeger.dto.ExpenseDto;
import com.example.expenssManeger.dto.FilterDTO;
import com.example.expenssManeger.dto.IncomeDTO;
import com.example.expenssManeger.service.ExpenseService;
import com.example.expenssManeger.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/filter")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FilterController {

    private final ExpenseService expenseService;
    private  final IncomeService incomeService;


    @PostMapping()
    public ResponseEntity<?> filterTransactions(@RequestBody FilterDTO filterDTO) {
        LocalDate startDate = filterDTO.getStartDate();
        LocalDate endDate = filterDTO.getEndDate();
        if (startDate == null) startDate = LocalDate.of(2000, 1, 1);
        if (endDate == null) endDate = LocalDate.now().plusYears(100);

        String keyword = filterDTO.getKeyword() != null ? filterDTO.getKeyword() : "";
        String sortField = filterDTO.getSortField() != null ? filterDTO.getSortField() : "date";
        Sort.Direction direction = "desc".equalsIgnoreCase(filterDTO.getSortOrder()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortField);

        if ("expense".equalsIgnoreCase(filterDTO.getType())) {
            List<ExpenseDto> expenseDTOS = expenseService.filterexpenses(startDate, endDate, keyword, sort);
            return ResponseEntity.ok(expenseDTOS);
        } else if ("income".equalsIgnoreCase(filterDTO.getType())) {
            List<IncomeDTO> incomeDTOS = incomeService.filterexpenses(startDate, endDate, keyword, sort);
            return ResponseEntity.ok(incomeDTOS);
        } else {
            return ResponseEntity.badRequest().body("Invalid type specified. Use 'expense' or 'income'.");
        }
    }



}
