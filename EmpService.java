package com.EmpManagement.demo.Service;

import com.EmpManagement.demo.Model.Department;
import com.EmpManagement.demo.Model.Emp;
import com.EmpManagement.demo.Repo.DepRepo;
import com.EmpManagement.demo.Repo.EmpRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpService {

    private final EmpRepo empRepo;
    private final DepRepo depRepo;

    // Get All Employees
    public List<Emp> getAllUsers() {
        return empRepo.findAll();
    }

    // Get Employee By Id
    public Emp getUserById(Integer id) {

        return empRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with id : " + id));
    }

    // Get All Departments
    public List<Department> getAllDepartments() {
        return depRepo.findAll();
    }

    // Get Department By Id
    public Department getDepartmentById(Integer id) {

        return depRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Department not found with id : " + id));
    }

    // Add Employee
    public Emp addUser(Emp emp) {

        // Check Department
        if (emp.getDepartment() == null) {
            throw new RuntimeException("Department is required");
        }
        Department department = depRepo.findById(
                        emp.getDepartment().getDepartmentId())
                .orElseThrow(() ->
                        new RuntimeException("Department not found"));

        emp.setDepartment(department);
        return empRepo.save(emp);
    }


    // Update Employee
    public Emp updateUser(Integer id, Emp emp) {

        Emp existingEmp = empRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        existingEmp.setEmpName(emp.getEmpName());
        existingEmp.setEmpEmail(emp.getEmpEmail());
        existingEmp.setEmpPhone(emp.getEmpPhone());
        existingEmp.setEmpPhone2(emp.getEmpPhone2());
        existingEmp.setGender(emp.getGender());
        existingEmp.setDateOfJoining(emp.getDateOfJoining());
        existingEmp.setEmpAddress(emp.getEmpAddress());

        if (emp.getDepartment() != null) {

            Department department = depRepo.findById(
                            emp.getDepartment().getDepartmentId())
                    .orElseThrow(() ->
                            new RuntimeException("Department not found"));

            existingEmp.setDepartment(department);
        }
        return empRepo.save(existingEmp);
    }

    // Delete Employee
    public void deleteUser(Integer id) {

        Emp emp = empRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));
        empRepo.delete(emp);
    }

}