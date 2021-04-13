package com.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserCRUD{

    @Autowired
    private DataSource dataSource;

    @GetMapping("/")
    public ArrayList<User> allUsers(HttpServletResponse response){
        try (Connection connection = dataSource.getConnection()){
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM chamis");

            ArrayList<User> L = new ArrayList<User>();
            while (rs.next()) {
                User u = new User();
                u.login = rs.getString("login");
                u.age = rs.getInt("age");
                L.add(u);
            }
            return L;
        } catch(Exception e){
            response.setStatus(500);
        try{
            response.getOutputStream().print(e.getMessage());
        } catch (Exception e2) {
            System.err.println(e2.getMessage()); 
        }
        System.err.println(e.getMessage());
        return null;
        }
    }

    @GetMapping("/{userId}")
    public User read(@PathVariable(value="userId") String id, HttpServletResponse response){
        try (Connection connection = dataSource.getConnection()){
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM chamis WHERE login='"+ id +"'");

            User u = new User();
            if(!rs.next()){
                System.err.println("Erreur HTTP 404"); 
            }else{
                rs.first();
                u.login = rs.getString("login");
                u.age = rs.getInt("age");
            }
            return u;
        } catch(Exception e){
            response.setStatus(500);
        try{
            response.getOutputStream().print(e.getMessage());
        } catch (Exception e2) {
            System.err.println(e2.getMessage()); 
        }
        System.err.println(e.getMessage());
        return null;
        }
    }

    @GetMapping("/{userId}")
    public User create(@PathVariable(value="userId") String id, @RequestBody User u, HttpServletResponse response){
        try (Connection connection = dataSource.getConnection()){
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM chamis WHERE login='"+ id +"'");
            if(rs.next()){
                System.err.println("Erreur HTTP 403");
                
            }else{
                //Jcrois que c'est bon faut faire le test
                // Je sors du partage pour tester sur un terminal
                User s = new User();
                s.login = rs.getString("login");
                s.age = rs.getInt("age");
                if(id == u.getLogin()){
                    int create = stmt.executeUpdate("Insert into chamis values('"+ u.getLogin() +"','"+u.getAge() +"')"); 
                }else
                    System.err.println("Erreur HTTP 412");
            }
            return u;
        } catch(Exception e){
            response.setStatus(500);
        try{
            response.getOutputStream().print(e.getMessage());
        } catch (Exception e2) {
            System.err.println(e2.getMessage()); 
        }
        System.err.println(e.getMessage());
        return null;
        } 
    }


 }
