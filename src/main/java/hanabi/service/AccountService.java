package hanabi.service;

import hanabi.dao.OrderDAO;
import hanabi.dao.SalaryDAO;
import hanabi.dao.StaffDAO;
import hanabi.model.Staff;
import hanabi.util.PasswordUtil;

import java.util.List;
import java.util.UUID;

import hanabi.model.Salary;
import hanabi.util.global;

public class AccountService {
    private final StaffDAO staffDAO = new StaffDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final SalaryDAO salaryDAO = new SalaryDAO();

    public Staff getStaffById(UUID staffId) {
        return staffDAO.findById(staffId);
    }

    public Staff getStaffByName(String staffName) {
        return staffDAO.findByStaffName(staffName).orElse(null);
    }

    public long getTotalOrders(UUID staffId) {
        return orderDAO.countByStaffId(staffId);
    }

    public int getStaffCount() {
        return staffDAO.findAll().size();
    }

    public List<Staff> getAllStaff() {
        return staffDAO.findAll();
    }

    public boolean addStaff(Staff staff, double salary) {
        staff.setStaffId(UUID.randomUUID());
        Salary sa = new Salary();
        sa.setStaffId(staff.getStaffId());
        sa.setBaseSalary(salary);
        sa.setCommissionRate(global.COMMISSION_RATE);
        try {
            staffDAO.save(staff);
            salaryDAO.save(sa);
            return true;
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public boolean terminateStaff(UUID staffId) {
        Staff staff = staffDAO.findById(staffId);
        if (staff != null) {
            staff.setStatus(false);
            staffDAO.update(staff, staff.getStaffId());
            return true;
        }
        return false;
    }

    public List<Object[]> getSalaryData() {
        return salaryDAO.findAllWithStaffAndTotals();
    }

    public Double getSalaryTotal(UUID staffId) {
        return salaryDAO.getTotalByStaffId(staffId);
    }

    public boolean changePassword(UUID staffId, String newPassword) {
        Staff staff = staffDAO.findById(staffId);
        if (staff != null) {
            String salt = PasswordUtil.generateSalt();
            staff.setPassword(salt + ":" + PasswordUtil.hash(newPassword, salt));
            staffDAO.update(staff, staff.getStaffId());
            return true;
        }
        return false;
    }
}