package hanabi.service;

import hanabi.dao.AverageDAO;
import hanabi.dao.OrderDAO;
import hanabi.dao.SalaryDAO;
import hanabi.dao.StaffDAO;
import hanabi.model.Staff;
import hanabi.util.HibernateUtil;

import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.Transaction;

import hanabi.model.Average;
import hanabi.model.Salary;

public class AccountService {
    private final StaffDAO staffDAO = new StaffDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final AverageDAO averageDAO = new AverageDAO();
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

    public Integer getPoints(UUID staffId) {
        return averageDAO.findById(staffId) != null
                ? averageDAO.findById(staffId).getAverageScore()
                : 0;
    }

    public int getStaffCount() {
        return staffDAO.findAll().size();
    }

    public List<Staff> getAllStaff() {
        return staffDAO.findAll();
    }

    public boolean addStaff(Staff staff, double salary) {
        // staff init
        staff.setStaffId(UUID.randomUUID());
        // staffDAO.save(staff);
        Salary sa = new Salary();
        sa.setStaff(staff);
        sa.setBaseSalary(salary);
        sa.setCommissionRate(0.0);
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.persist(staff);
            session.persist(sa);
            tx.commit();
            return true;
        } catch (RuntimeException e) {
            if (tx != null)
                tx.rollback();
            throw e;
        } finally {
            if (session != null)
                session.close();
        }
    }


    public boolean terminateStaff(UUID staffId) {
        Staff staff = staffDAO.findById(staffId);
        if (staff != null) {
            staff.setStatus(false);
            staffDAO.update(staff);
            return true;
        }
        return false;
    }

    public List<Object[]> getSalaryData() {
        return salaryDAO.findAllWithStaffAndTotals();
    }

    public boolean changePassword(UUID staffId, String newPassword) {
        Staff staff = staffDAO.findById(staffId);
        if (staff != null) {
            staff.setPassword(newPassword);
            staffDAO.update(staff);
            return true;
        }
        return false;
    }
}
