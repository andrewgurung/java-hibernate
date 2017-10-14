package hibernateexample;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import org.hibernate.Criteria;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;

import hibernateexample.HibernateUtil;

public class HibernateExample {
    private static SessionFactory factory;
    private static Scanner in = new Scanner(System.in);

	public static void main(String[] args) {
		factory = HibernateUtil.getSessionFactory();
		HibernateExample HE = new HibernateExample();
	      String more = "yes";
	      Integer empID=0;

	      /* Add few employee records in database */
	      while(more.charAt(0) == 'y' || more.charAt(0) =='Y')
	      {
	          empID = HE.addEmployee();
	          System.out.println("More employees? (y/n)");
	          more = in.nextLine();
	      }

	      /* Update employee's records */
	      //HE.updateEmployee(65, 95000);

	      /* Delete an employee from the database */
	     // HE.deleteEmployee(67);

	      HE.listEmployees(); 
	      HibernateUtil.shutdown();
	}
	
	/* Method to CREATE an employee in the database */
	   public Integer addEmployee(){
	       
	        System.out.println("Enter first name: ");
	        String fname = in.nextLine();
	        System.out.println("Enter last name: ");
	        String lname = in.nextLine();
	        System.out.println("Enter cell: ");
	        String cell = in.nextLine();
	        System.out.println("Enter home phone: ");
	        String hPhone = in.nextLine();
	        System.out.println("Enter salary: ");
	        int salary = in.nextInt();
	        in.nextLine();
	        HashSet hs = new HashSet();
	        hs.add(new Phone(cell));
	        hs.add(new Phone(hPhone));
	     
	      Session session = factory.openSession();
	      Transaction tx = null;
	      Integer employeeID = null;
	      try{
	         tx = session.beginTransaction();
	         Employee employee = new Employee(fname, lname, salary);
	         employee.setPhones(hs);
	         employeeID = (Integer) session.save(employee);
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	      return employeeID;
	   }
	   /* Method to print all the employees */
	   public void listEmployees( ){
	      Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	  
	        List employees = session.createQuery("FROM Employee").list();
	         for (Iterator iterator1 = 
	                           employees.iterator(); iterator1.hasNext();){
	            Employee employee = (Employee) iterator1.next(); 
	            System.out.print("First Name: " + employee.getFirstName()); 
	            System.out.print("  Last Name: " + employee.getLastName()); 
	            System.out.println("  Salary: " + employee.getSalary()); 
	            Set phoneNums = employee.getPhones();
	            for (Iterator iterator2 = 
	                         phoneNums.iterator(); iterator2.hasNext();){
	                  Phone phoneNum = (Phone) iterator2.next(); 
	                  System.out.println("Phone: " + phoneNum.getPhoneNumber()); 
	            }
	         }
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	   }
	   /* Method to UPDATE salary for an employee */
	   public void updateEmployee(Integer EmployeeID, int salary ){
	      Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Employee employee = 
	                    (Employee)session.get(Employee.class, EmployeeID); 
	         employee.setSalary( salary );
		 session.update(employee); 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	   }
	   /* Method to DELETE an employee from the records */
	   public void deleteEmployee(Integer EmployeeID){
	      Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Employee employee = 
	                   (Employee)session.get(Employee.class, EmployeeID); 
	         session.delete(employee); 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	   }

}
