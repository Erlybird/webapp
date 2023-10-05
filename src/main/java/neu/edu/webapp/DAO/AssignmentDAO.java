package neu.edu.webapp.DAO;

import neu.edu.webapp.Model.Account;
import neu.edu.webapp.Model.Assignment;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class AssignmentDAO extends DAO {
    @Autowired
    Assignment assignment;
    @Autowired
    Account account;

    //save it to DB
    public boolean saveAssignmentToDB(Assignment assignment) {
        try {
            begin();
            getSession().save(assignment);
            commit();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            close();
        }
        return true;
    }


    //retrive from DB using User id
    public List<Assignment> retriveAllAssignmentsUsingAccID(Account account) {

        List<Assignment> result = null;
        try {
            begin();
            Query query = getSession().createQuery("from Assignment where accountId=:accId");
            query.setParameter("accId", account);

            result = query.getResultList();
            commit();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        } finally {
            close();
        }
        return result;
    }


    //retrive from DB using assignmentid
    public Assignment retriveAssignmentById(String id, Account account) {
        Assignment result = null;
        try {
            begin();
            Query query = getSession().createQuery("from Assignment where id=:assignId and accountId=:userId ");
            query.setParameter("assignId", id);
            query.setParameter("userId", account);

            result = (Assignment) query.uniqueResult();
            commit();

        } catch (Exception e) {
            System.out.println("Exception in DAO:" + e.getMessage());
        } finally {
            close();
        }
        return result;
    }


    //delete an assignment in DB
    public boolean deleteAssignmentById(String id, Account account) {
        try {
            begin();
            Query query = getSession().createQuery("delete from Assignment where id=:assignmentId and accountId=:accId");
            query.setParameter("assignmentId", id);
            query.setParameter("accId", account);
            int rowCount = query.executeUpdate();
            System.out.println("Rows affected: " + rowCount);
            commit();

            if (rowCount == 0) {
                System.out.println("Assignment not found for ID:");
                return false;
            }

        } catch (Exception e) {
            rollback();
            System.out.println("Exception: " + e.getMessage());
            return false;

        } finally {
            close();

        }
        return true;
    }


    //update the assignment by ID
    public boolean updateAssignmentById(String id, Assignment newAssignment, Account account) {
        Assignment existingAssignment = null;
        Date newUpdatedTimeStamp = new Date();
        try {
            begin();
            Query query = getSession().createQuery("from Assignment where accountId=:userId and id=:assignId ");
            query.setParameter("assignId", id);
            query.setParameter("userId", account);

            existingAssignment = (Assignment) query.uniqueResult();

            if (existingAssignment != null) {
                if (newAssignment.getName() != null) {
                    existingAssignment.setName(newAssignment.getName());
                }
                if (newAssignment.getPoints() >= 1 && newAssignment.getPoints() <= 100) {
                    existingAssignment.setPoints(newAssignment.getPoints());
                }
                if (newAssignment.getNum_of_attemps() >= 1 && newAssignment.getNum_of_attemps() <= 100) {
                    existingAssignment.setNum_of_attemps(newAssignment.getNum_of_attemps());
                }
                if (newAssignment.getDeadline() != null) {
                    existingAssignment.setDeadline(newAssignment.getDeadline());
                }
                existingAssignment.setAssignment_updated(newUpdatedTimeStamp);

                getSession().saveOrUpdate(existingAssignment);
                commit();
            } else {
                return false;
            }

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return false;
        } finally {
            close();
        }
        return true;

    }
}






