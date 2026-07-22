package com.EmpManagement.demo.Controller;

import com.EmpManagement.demo.Model.Department;
import com.EmpManagement.demo.Model.Emp;
import com.EmpManagement.demo.Service.EmpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emp")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class EmpController {

    private final EmpService empService;

    // Get All Employees
    @GetMapping("/user")
    public ResponseEntity<?> getAllUsers() {

        try {
            List<Emp> employees = empService.getAllUsers();
            return ResponseEntity.ok(employees);

        } catch (Exception e) {

            log.error("Error fetching employees", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch employees : " + e.getMessage());
        }
    }

    // Get Employee By Id
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {

        try {

            Emp employee = empService.getUserById(id);
            if (employee == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Employee not found with id : " + id);
            }
            return ResponseEntity.ok(employee);

        } catch (Exception e) {

            log.error("Error fetching employee", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch employee : " + e.getMessage());
        }
    }

    // Add Employee
    @PostMapping("/user")
    public ResponseEntity<?> addEmployee(@RequestBody Emp emp) {

        try {

            Emp savedEmployee = empService.addUser(emp);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(savedEmployee);

        } catch (Exception e) {

            log.error("Error adding employee", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to add employee : " + e.getMessage());
        }
    }

    // Update Employee
    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateEmployee(
            @PathVariable Integer id,
            @RequestBody Emp emp) {

        try {

            Emp updatedEmployee = empService.updateUser(id, emp);
            if (updatedEmployee == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Employee not found with id : " + id);
            }

            return ResponseEntity.ok(updatedEmployee);

        } catch (Exception e) {

            log.error("Error updating employee", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update employee : " + e.getMessage());
        }
    }

    // Delete Employee
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Integer id) {

        try {

            empService.deleteUser(id);
            return ResponseEntity.ok("Employee deleted successfully.");

        } catch (Exception e) {

            log.error("Error deleting employee", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete employee : " + e.getMessage());
        }
    }

    // Get All Departments
    @GetMapping("/departments")
    public ResponseEntity<?> getAllDepartments() {

        try {

            List<Department> departments = empService.getAllDepartments();
            return ResponseEntity.ok(departments);

        } catch (Exception e) {

            log.error("Error fetching departments", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch departments : " + e.getMessage());
        }
    }

    // Get Department By Id
    @GetMapping("/departments/{id}")
    public ResponseEntity<?> getDepartmentById(@PathVariable Integer id) {

        try {

            Department department = empService.getDepartmentById(id);

            if (department == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Department not found with id : " + id);
            }
            return ResponseEntity.ok(department);

        } catch (Exception e) {

            log.error("Error fetching department", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch department : " + e.getMessage());
        }
    }

}