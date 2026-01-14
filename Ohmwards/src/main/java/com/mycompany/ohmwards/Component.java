/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ohmwards;
import java.util.Vector;
import javax.swing.JButton;
/**
 *
 * @author Cameron
 */
public abstract class Component {
    private ConnectionType type;
    private Vector<Double> position;
    private JButton compBtn;
    private double voltage;
    private double current;
    private double resistance;
    private double power;
    
    public Component(ConnectionType type, Vector<Double> position, JButton btn)
    {
        this.type = type;
        this.position = position;
        compBtn = btn;
    }
    
    // Getters + Setters
     
    // Calculate values
    
    public void displayPopup(){
        // Display the info and stuff in a popup menu on click
    }
}
