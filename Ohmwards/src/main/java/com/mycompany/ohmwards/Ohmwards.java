/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ohmwards;
import java.util.ArrayList;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
/**
 *
 * @author Cameron
 */
public class Ohmwards {
    public static MainMenu main;
    public static ArrayList<ArrayList<String>> accounts = new ArrayList<>();
    
    public static void main(String[] args) {
        // Accounts List
        ArrayList<String> account1 = new ArrayList<>();
        account1.add("totallyRoland");
        account1.add("vergilliusIsStronger");
        
        ArrayList<String> account2 = new ArrayList<>();
        account2.add("bongbong");
        account2.add("#strongestEmployee");
        
        ArrayList<String> account3 = new ArrayList<>();
        account3.add("heathcliff");
        account3.add("catherine");
        
        accounts.add(account1);
        accounts.add(account2);
        accounts.add(account3);
        
        // Start the Application
        main = new MainMenu();
        main.setVisible(true);
    }
}
