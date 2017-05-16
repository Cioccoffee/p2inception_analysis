/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2inception_analysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Date;

/**
 *
 * @author Lucie
 */
public class DataRecuperation {
    
    //attributs = data to collect
    String Subject_Name ;
    Date date ;
    int pulse;
    double temp1;
    double temp2;
    float avgAcc;
    float avgGyr;
    float maxAcc;
    float maxGyr;
    
    // Prepared Statement
    
    // connexion avec arduino
 //package fr.insalyon.p2i2.javaarduino;

/*import fr.insalyon.p2i2.javaarduino.util.Console;
    
import fr.insalyon.p2i2.javaarduino.usb.ArduinoUsbChannel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import jssc.SerialPortException;
*/

    
    public static void main( String[] args )
    {
        final Console console = new Console();
        
        console.log( "DEBUT du programme TestArduino !.." );
        
        String port = null;
        
        do {
        
            console.log( "RECHERCHE d'un port disponible..." );
            port = ArduinoUsbChannel.getOneComPort();
            
            if (port == null) {
                console.log( "Aucun port disponible!" );
                console.log( "Nouvel essai dans 5s" );
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    // Ignorer l'Exception
                }
            }

        } while (port == null);
        
        port = "COM10";
        
        console.println("Connection au Port " + port);
        try {

            final ArduinoUsbChannel vcpChannel = new ArduinoUsbChannel(port);

            Thread readingThread = new Thread(new Runnable() {

                public void run() {
                    
                    BufferedReader vcpInput = new BufferedReader(new InputStreamReader(vcpChannel.getReader()));
                    String line ;
                    try {
                    while((line = vcpInput.readLine())!= null){
                        this.read(line);
                        
                        DataInsertion data_insert = new DataInsertion();
                        data_insert.addMesure(Subject_Name, date, pulse, temp1, temp2, maxAcc, maxGyr, avgAcc, avgGyr);
//ajouter dans BD
                    }
                    
                    
                    
                    
                } catch(IOException ex){
                    ex.printStackTrace(System.err);
                }
            });
            
            readingThread.start();
            
            vcpChannel.open();
            
            boolean exit = false;
            
            while (!exit) {
            
                String line = console.readLine("Envoyer une ligne (ou 'fin') > ");
            
                if (line.length() == 0) {
                    continue;
                }
                
                if ("fin".equals(line)) {
                    exit = true;
                    continue;
                }
                
                vcpChannel.getWriter().write(line.getBytes("UTF-8"));
                vcpChannel.getWriter().write('\n');
            
            }
            
            vcpChannel.close();
            
            readingThread.interrupt();
            try {
                readingThread.join(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace(System.err);
            }

        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        } catch (SerialPortException ex) {
            ex.printStackTrace(System.err);
        }
        
    }
    


    
    //recuperation des données
public void read(String line){
    
     
    if(line!=null){
        String[] data = line.split(":");
        temp1 = Double.parseDouble(data[0]);
        temp2 = Double.parseDouble(data[1]);
        maxAcc = Float.parseFloat(data[2]);
        maxGyr = Float.parseFloat(data[3]);
        avgAcc = Float.parseFloat(data[4]);
        avgGyr = Float.parseFloat(data[5]);
        pulse = Integer.parseInt(data[6]);
        date = new Date(); //faire un format
        Subject_Name = "";
        
    }
    
    
    }
    
}
    
    //établissment connexion avec remote DB
    //enregistrement direct des tuples
    //SI DB ne peut pas être atteinte, création fichier output et écriture dedans
    //ajout du nom du fichier non envoyé dans une liste attribut qu'on ouvre à chaque XXX
    
}
