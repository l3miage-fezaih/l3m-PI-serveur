package com.example;
import java.sql.Date;
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
@RequestMapping("api/defis")
public class DefiCRUD{

    @Autowired
    private DataSource dataSource;

    @GetMapping("/")
    public ArrayList<Defi> allDefis(HttpServletResponse response){
        try (Connection connection = dataSource.getConnection()){
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM defis");
            
            ArrayList<Defi> L = new ArrayList<Defi>();
            while (rs.next()) { 
                Defi defi = new Defi(); 
                defi.id = rs.getString("id");
                defi.titre = rs.getString("titre");
                defi.dateDeCreation = rs.getDate("dateDeCreation");
                defi.description = rs.getString("description");
                defi.login_Auteur = rs.getString("login_fk");
                L.add(defi);
            }
            return L;
        }catch (Exception e){
            response.setStatus(500);
            try{
                response.getOutputStream().print(e.getMessage());
            } catch(Exception e2) {
                System.err.println(e2.getMessage());
            }
            System.err.println(e.getMessage());
            return null;
        }
    }
}