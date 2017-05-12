/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2inception_analysis;
import java.sql.*;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Query_DB{
    private Connection conn;
    private Statement stmt;
    private ResultSet res;
            
    /**
     * Methode pour se connecter Ã  la base ; prend en paramÃ¨tre le login et le mot de passe
     */
    public Query_DB(String nomLogin, String mdp) {
                    try { 
                        
                                //Enregistrement de la classe du driver par le driverManager
                                Class.forName("com.mysql.jdbc.Driver");
				System.out.println("Driver trouvÃ©...");
				 //CrÃ©ation d'une connexion sur la base de donnÃ©e
				conn = DriverManager.getConnection("jdbc:mysql://https://www.freemysqlhosting.net:3306/sql11172818", nomLogin, mdp);
                                System.out.println("Connexion Ã©tablie...");
		    }
                    catch(Exception e){
                                    System.out.println(e.getMessage());
                                    System.out.println("Error !");
                                    System.exit(0);
                            }
                
                     
                    
            }           
             
}
