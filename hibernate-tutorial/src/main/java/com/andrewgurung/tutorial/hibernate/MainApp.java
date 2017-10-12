package com.andrewgurung.tutorial.hibernate;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
/**
 * @author imssbora
 */
public class MainApp {
  public static void main(String[] args) {
    Session session = HibernateUtil.getSessionFactory().openSession();
//    session.beginTransaction();

    // Check database version
//    String sql = "select version()";
//
//    String result = (String) session.createNativeQuery(sql).getSingleResult();
//    System.out.println(result);
//
//    session.getTransaction().commit();
//    session.close();
    
    Scanner in = new Scanner(System.in);
    String m = "";
    System.out.println("Enter a message: ");
    m = in.nextLine();
    
    Transaction tx = null;
    Short msgId = null;
    try{
        tx=session.beginTransaction();
        Message msg = new Message(m);
        msgId = (Short) session.save(msg);
        List messages = session.createQuery("FROM Message").list();
        for(Iterator iterator = messages.iterator(); iterator.hasNext();){
            Message message = (Message)iterator.next();
            System.out.println("message: "+message.getMessage());
        }
        tx.commit();
    }catch(HibernateException e){
        if(tx != null) tx.rollback();
        e.printStackTrace();
    }finally{
        session.close();
    }

    
    HibernateUtil.shutdown();
  }
}
