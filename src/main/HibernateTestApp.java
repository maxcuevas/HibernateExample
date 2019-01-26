package main;

import java.util.Iterator;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateTestApp {

    private static SessionFactory factory;

    public static void main(String[] args) {

        try {
            final Configuration configuration = new Configuration().addAnnotatedClass(Person.class);
            final Configuration configure = configuration.configure();

            factory = configure.buildSessionFactory();
//            factory = new AnnotationConfiguration().
//                    configure().
//                    addAnnotatedClass(Person.class).
//                    buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

        HibernateTestApp ME = new HibernateTestApp();

        /* Add few employee records in database */
        long empID1 = ME.addEmployee(1000);
        long empID2 = ME.addEmployee(8000);
        long empID3 = ME.addEmployee(10000);

        /* List down all the employees */
        ME.listEmployees();

        /* Update employee's records */
        ME.updateEmployee(empID1, 5000);

        /* Delete an employee from the database */
        ME.deleteEmployee(empID2);

        System.out.println("");
        System.out.println("");
        System.out.println("");
        /* List down new list of the employees */
        ME.listEmployees();
        factory.close();
    }

    /* Method to CREATE an employee in the database */
    public long addEmployee(int age) {
        Session session = factory.openSession();
        Transaction tx = null;
        long employeeID = 0;

        try {
            tx = session.beginTransaction();
            Person person = new Person();
            person.setAge(age);
            employeeID = (long) session.save(person);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return employeeID;
    }

    /* Method to  READ all the employees */
    public void listEmployees() {
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            List employees = session.createQuery("FROM Person").list();
            for (Iterator iterator = employees.iterator(); iterator.hasNext();) {
                Person employee = (Person) iterator.next();
                System.out.println("age: " + employee.getAge());
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    /* Method to UPDATE salary for an employee */
    public void updateEmployee(long id, int age) {
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Person person = (Person) session.get(Person.class, id);
            person.setAge(age);
            session.update(person);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    /* Method to DELETE an employee from the records */
    public void deleteEmployee(long id) {
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Person employee = (Person) session.get(Person.class, id);
            session.delete(employee);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
