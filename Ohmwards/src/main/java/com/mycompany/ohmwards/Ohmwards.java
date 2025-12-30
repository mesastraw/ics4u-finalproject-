/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ohmwards;
import java.util.ArrayList;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cameron
 */
public class Ohmwards {
    public static MainMenu main;
    public static ArrayList<ArrayList<String>> accounts = new ArrayList<>();
    
    public static void main(String[] args) {
        // Read accounts.txt and set the accounts ArrayList
        try (BufferedReader reader = new BufferedReader(new FileReader("accounts.txt"))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                if (lineNumber % 2 == 1) {
                    ArrayList<String> acc = new ArrayList<>();
                    acc.add(line);
                    accounts.add(acc);
                } else { // Even line: add to last list
                    accounts.get(accounts.size() - 1).add(line);
                }
                lineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Start the Application
        System.out.println(accounts);
        main = new MainMenu();
        main.setVisible(true);
    }
    
    public static void addAccount(String username, String password){
        // Add the account to the ArrayList
        ArrayList<String> newAcc = new ArrayList<>();
        newAcc.add(username);
        newAcc.add(password);
        accounts.add(newAcc);
        
        // Write the new account to the text file
        try{  
            FileWriter fileWrite = new FileWriter("accounts.txt", true);
            BufferedWriter buffWrite = new BufferedWriter(fileWrite);  
            buffWrite.newLine();
            buffWrite.write(username);
            buffWrite.newLine();
            buffWrite.write(password);
            buffWrite.newLine();
            buffWrite.close();
        }
            
        catch (FileNotFoundException e){
            System.out.println("File not found");
        } 
        // This just has to be here in order for the file to read and write properly
        catch (IOException ex) {
            Logger.getLogger(Ohmwards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
