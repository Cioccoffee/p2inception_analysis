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
import java.io.*;


import fr.insalyon.p2i2.javaarduino.util.Console;
    
import fr.insalyon.p2i2.javaarduino.usb.ArduinoUsbChannel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java-simple-serial-connector-2.8.0.src.java.jssc.SerialPortException;
import java-simple-serial-connector-2.8.0.src.java.jssc.SerialNativeInterface;

/**
 *
 * @author Lucie
 */
public class DataRecuperation {
    
    //attributs = data to collect
    private String Subject_Name ;
    private Date date ;
    private int pulse;
    private double temp1;
    private double temp2;
    private float avgAcc;
    private float avgGyr;
    private float maxAcc;
    private float maxGyr;
    
    
    
    // connexion avec arduino
 //package fr.insalyon.p2i2.javaarduino;


    

    
    public static void main( String[] args )
    {
        final Console console = new Console();
        
        console.log( "DEBUT du programme TestArduino !.." );
        
        final DataRecuperation dataRecup = new DataRecuperation();
        
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
        
        port = "COM3";
        
        console.println("Connection au Port " + port);
        try {

            final ArduinoUsbChannel vcpChannel = new ArduinoUsbChannel(port);

            Thread readingThread = new Thread(new Runnable() {

                public void run() {
                    
                    BufferedReader vcpInput = new BufferedReader(new InputStreamReader(vcpChannel.getReader()));
                    String line ;
                    try {
                        PrintWriter writer = new PrintWriter( new OutputStreamWriter(new FileOutputStream("SavedData\\"+(new Date()).getTime()+".csv")));
                        while((line = vcpInput.readLine())!= null){
                            dataRecup.read(line);

                            DataInsertion data_insert = new DataInsertion();
                            data_insert.addMesure(dataRecup.date, dataRecup.pulse, dataRecup.temp1, dataRecup.temp2, dataRecup.maxAcc, dataRecup.maxGyr, dataRecup.avgAcc, dataRecup.avgGyr);
                            //doit gérer le reach DB & if not : stockage ds file whose name = current date.getTime() & name stocked in list of files of non added date
                            //ajouter dans BD
                            writer.println("data : " + dataRecup.date +" "+ dataRecup.pulse +" "+dataRecup.temp1+" "+dataRecup.temp2+" "+dataRecup.maxAcc+" "+dataRecup.maxGyr+" "+dataRecup.avgAcc+" "+dataRecup.avgGyr );

                        }
                    
                    
                    
                    
                    } catch(IOException ex){
                        ex.printStackTrace(System.err);
                    }
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
        }catch (SerialNativeInterface sni){
            sni.printStackTrace(System.err);
        }
       
        
    
    }
    


    
    //recuperation des données et set de la date
    public void read(String line){


        if(line!=null){
            String[] data = line.split(":");
            temp1 = Double.parseDouble(data[0])/10.0;
            temp2 = Double.parseDouble(data[1])/10.0;
            maxAcc = Float.parseFloat(data[2]);
            maxGyr = Float.parseFloat(data[3]);
            avgAcc = Float.parseFloat(data[4]);
            avgGyr = Float.parseFloat(data[5]);
            pulse = Integer.parseInt(data[6]);
            date = new Date(); //faire un format
            Subject_Name = "";

        }


    }
    
    public boolean putMesuresInDB(){
        DataInsertion data_insert =  new DataInsertion();
        data_insert.addMesure(date, pulse, temp1, temp2, maxAcc, maxGyr, avgAcc, avgGyr);
        //faire query temp for last date
    }

    
}
    
    //établissment connexion avec remote DB
    //enregistrement direct des tuples
    //SI DB ne peut pas être atteinte, création fichier output et écriture dedans
    //ajout du nom du fichier non envoyé dans une liste attribut qu'on ouvre à chaque XXX
    

