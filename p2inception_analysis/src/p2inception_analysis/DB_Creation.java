/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2inception_analysis;

import java.sql.DriverManager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import java.util.LinkedList;


/**
 *
 * @author Lucie
 */
public class DB_Creation {
    
    private Connection conn;
    private PreparedStatement createMesureStatement;
 
    public DB_Creation(String login, String password){
        try {

            //Enregistrement de la classe du driver par le driverManager
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver trouvé...");

            //Création d'une connexion sur la base de donnée
            this.conn = DriverManager.getConnection("jdbc:mysql://https://www.freemysqlhosting.net:3306/sql11172818", login, password);
            //this.conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/" + bd, compte, motDePasse);
            System.out.println("Connexion établie...");

            //Liste des instructions de création de table
            LinkedList<String> CreationInstructionsList = new LinkedList<String>();
            CreationInstructionsList.add("");
            CreationInstructionsList.add("");
            CreationInstructionsList.add("");
            // Prepared Statement
            boolean tableCreated = false;
            for(int i = 0; i < CreationInstructionsList.size(); i++){
                while(!tableCreated){
                this.createMesureStatement = this.conn.prepareStatement(CreationInstructionsList.get(i));
                tableCreated = this.createMesureStatement.execute();
                }
            }
            
            
            
            //this.insertMesureStatement = this.conn.prepareStatement("INSERT INTO Mesure (valeur,idCapteur,dateMesure) VALUES (?,?,?) ;");
            //this.selectMesuresStatement = this.conn.prepareStatement("SELECT valeur,idCapteur numInventaire,dateMesure FROM Mesure WHERE idCapteur = ? AND dateMesure >= ? AND dateMesure < ? ;");

        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        }
        
        
    }
    
    
}
