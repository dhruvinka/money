// src/EmployeeManagement.js
import React, { useState, useEffect } from "react";

import {
  fetchDepartments,
  fetchEmployees,
  createEmployee,
  updateEmployee,
  deleteEmployee,
  fetchEmployeeById,
} from "../services/api";

const emptyForm = {
  empName: "",
  empEmail: "",
  empPhone: "",
  empPhone2: "",
  gender: "",
  dateOfJoining: "",
  empAddress: "",
  departmentId: "",
};

export default function EmployeeManagement() {
  const [employees, setEmployees] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);

  // Load data on mount
  useEffect(() => {
    loadDepartments();
    loadEmployees();
  }, []);

  const loadDepartments = async () => {
    try {
      const response = await fetchDepartments();
      setDepartments(response.data);
    } catch (err) {
      console.error("Failed to load departments", err);
    }
  };

  const loadEmployees = async () => {
    try {
      const response = await fetchEmployees();
      setEmployees(response.data);
    } catch (err) {
      console.error("Failed to load employees", err);
    }
  };

  const handleChange = (e) => {
    const { id, value } = e.target;
    setForm((prev) => ({ ...prev, [id]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Simple required-field check
    if (
      !form.empName ||
      !form.empEmail ||
      !form.empPhone ||
      !form.empPhone2 ||
      !form.gender ||
      !form.dateOfJoining ||
      !form.empAddress ||
      !form.departmentId
    ) {
      alert("Please fill in all fields.");
      return;
    }

    const payload = {
      empName: form.empName,
      empEmail: form.empEmail,
      empPhone: form.empPhone,
      empPhone2: form.empPhone2,
      gender: form.gender,
      dateOfJoining: form.dateOfJoining,
      empAddress: form.empAddress,
      department: { departmentId: parseInt(form.departmentId, 10) },
    };

    try {
      if (editingId) {
        await updateEmployee(editingId, payload);
      } else {
        await createEmployee(payload);
      }
      resetForm();
      loadEmployees(); // refresh list
    } catch (err) {
      console.error("Save failed", err);
    }
  };

  const editEmployee = async (id) => {
    try {
      const response = await fetchEmployeeById(id);
      const emp = response.data;
      setForm({
        empName: emp.empName,
        empEmail: emp.empEmail,
        empPhone: emp.empPhone,
        empPhone2: emp.empPhone2,
        gender: emp.gender,
        dateOfJoining: emp.dateOfJoining,
        empAddress: emp.empAddress,
        departmentId: emp.department ? emp.department.departmentId : "",
      });
      setEditingId(id);
      window.scrollTo(0, 0);
    } catch (err) {
      console.error("Failed to load employee", err);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Delete this employee?")) return;
    try {
      await deleteEmployee(id);
      loadEmployees();
    } catch (err) {
      console.error("Delete failed", err);
    }
  };

  const resetForm = () => {
    setForm(emptyForm);
    setEditingId(null);
  };

  // ---------- JSX (same as before) ----------
  return (
    <div style={{ maxWidth: "600px", margin: "30px auto", fontFamily: "sans-serif" }}>
      <h2>{editingId ? "Update Employee" : "Add Employee"}</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Name</label><br />
          <input id="empName" type="text" value={form.empName} onChange={handleChange} style={{ width: "100%" }} />
        </div>
        <br/>
        <div>
          <label>Email</label><br />
          <input id="empEmail" type="email" value={form.empEmail} onChange={handleChange} style={{ width: "100%" }} />
        </div>
        <br/>
        <div>
          <label>Phone</label><br />
          <input id="empPhone" type="text" maxLength="10" value={form.empPhone} onChange={handleChange} style={{ width: "100%" }} />
        </div>
        <br/>
        <div>
          <label>Alternate Phone</label><br />
          <input id="empPhone2" type="text" maxLength="10" value={form.empPhone2} onChange={handleChange} style={{ width: "100%" }} />
        </div>
        <br/>
        <div>
          <label>Gender</label><br />
          <select id="gender" value={form.gender} onChange={handleChange} style={{ width: "100%" }}>
            <option value="">-- Select --</option>
            <option value="Male">Male</option>
            <option value="Female">Female</option>
            <option value="Other">Other</option>
          </select>
        </div>
        <br/>
        <div>
          <label>Date of Joining</label><br />
          <input id="dateOfJoining" type="date" value={form.dateOfJoining} onChange={handleChange} style={{ width: "100%" }} />
        </div>
        <br/>
        <div>
          <label>Address</label><br />
          <input id="empAddress" type="text" value={form.empAddress} onChange={handleChange} style={{ width: "100%" }} />
        </div>
        <br/>
        <div>
          <label>Department</label><br />
          <select id="departmentId" value={form.departmentId} onChange={handleChange} style={{ width: "100%" }}>
            <option value="">-- Select --</option>
            {departments.map((d) => (
              <option key={d.departmentId} value={d.departmentId}>
                {d.departmentName}
              </option>
            ))}
          </select>
        </div>
        <button type="submit" style={{ marginTop: "15px", padding: "8px 16px" }}>
          {editingId ? "Update" : "Save"}
        </button>
        {editingId && (
          <button type="button" onClick={resetForm} style={{ marginLeft: "10px", padding: "8px 16px" }}>
            Cancel
          </button>
        )}
      </form>

      <hr />

      <h2>Employee List</h2>
      <table style={{ borderCollapse: "collapse", width: "100%" }}>
        <thead>
          <tr>
            <th style={{ border: "1px solid #ccc", padding: "8px" }}>ID</th>
            <th style={{ border: "1px solid #ccc", padding: "8px" }}>Name</th>
            <th style={{ border: "1px solid #ccc", padding: "8px" }}>Email</th>
            <th style={{ border: "1px solid #ccc", padding: "8px" }}>Phone</th>
            <th style={{ border: "1px solid #ccc", padding: "8px" }}>Gender</th>
            <th style={{ border: "1px solid #ccc", padding: "8px" }}>Joining Date</th>
            <th style={{ border: "1px solid #ccc", padding: "8px" }}>Department</th>
            <th style={{ border: "1px solid #ccc", padding: "8px" }}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {employees.map((emp) => (
            <tr key={emp.empId}>
              <td style={{ border: "1px solid #ccc", padding: "8px" }}>{emp.empId}</td>
              <td style={{ border: "1px solid #ccc", padding: "8px" }}>{emp.empName}</td>
              <td style={{ border: "1px solid #ccc", padding: "8px" }}>{emp.empEmail}</td>
              <td style={{ border: "1px solid #ccc", padding: "8px" }}>{emp.empPhone}</td>
              <td style={{ border: "1px solid #ccc", padding: "8px" }}>{emp.gender}</td>
              <td style={{ border: "1px solid #ccc", padding: "8px" }}>{emp.dateOfJoining}</td>
              <td style={{ border: "1px solid #ccc", padding: "8px" }}>
                {emp.department ? emp.department.departmentName : ""}
              </td>
              <td style={{ border: "1px solid #ccc", padding: "8px" }}>
                <button onClick={() => editEmployee(emp.empId)} style={{ marginRight: "5px" }}>
                  Edit
                </button>
                <button onClick={() => handleDelete(emp.empId)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}


