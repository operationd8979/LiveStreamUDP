/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.chipchip.livestreamudp.Server;

import com.chipchip.livestreamudp.Server.model.StreamGroup;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author operationd
 */
public class StreamingFrm extends javax.swing.JFrame {

    private final MainFrm mainFrm;
    private final StreamGroup group;
    private boolean running = false;
    /**
     * Creates new form StreamingFrm
     */
    public StreamingFrm(MainFrm mainFrm,StreamGroup group) {
        
        this.mainFrm = mainFrm;
        this.group = group;
        
        initComponents();
        this.lbName.setText(group.getName());
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        if(this.group.getCurrentImage()==null){
            JOptionPane.showMessageDialog(null, "No DATA");
            dispose();
        }
        
        this.running = true;
        
        Runnable streaming = () -> {
            while (running) {
                ImageIcon imageIcon = null;
                while(this.running){
                    imageIcon = new ImageIcon(new ImageIcon(this.group.getCurrentImage()).getImage().getScaledInstance(750, 468, Image.SCALE_DEFAULT));
//                    imageIcon = new ImageIcon(this.group.getCurrentImage());
                    this.lbStreaming.setIcon(imageIcon);
                }
            }
        };
        
        new Thread(streaming).start();
        
    }
    
    public void run(){
        ImageIcon imageIcon = null;
        while(this.running){
            System.out.println(this.group.getCurrentImage());
            imageIcon = new ImageIcon(this.group.getCurrentImage());
            this.lbStreaming.setIcon(imageIcon);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbStreaming = new javax.swing.JLabel();
        lbName = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        lbName.setText("NAME");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbStreaming, javax.swing.GroupLayout.PREFERRED_SIZE, 750, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(321, 321, 321)
                        .addComponent(lbName)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbStreaming, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(lbName)
                .addContainerGap(59, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        this.mainFrm.setVisible(true);
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        this.running = false;
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowOpened



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbName;
    private javax.swing.JLabel lbStreaming;
    // End of variables declaration//GEN-END:variables
}
