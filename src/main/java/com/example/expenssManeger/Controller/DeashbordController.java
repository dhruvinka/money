package com.example.expenssManeger.Controller;

import com.example.expenssManeger.service.DeashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DeashbordController {

    private  final DeashboardService deashboardService;

    @GetMapping
    public ResponseEntity<Map<String ,Object>> getDashboardData() {
        Map<String, Object> dashboardData = deashboardService.getDeashbordData();
        return ResponseEntity.ok(dashboardData);
    }

}
