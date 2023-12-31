/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chipchip.livestreamudp.Client;

import com.chipchip.livestreamudp.Server.model.Command;
import com.chipchip.livestreamudp.Server.model.GroupLive;
import com.github.sarxos.webcam.Webcam;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Admin
 */
public class LiveStreamFrm extends javax.swing.JFrame {

    private boolean running = false;
    private ClientFrm clientFrm;

    private Webcam webcam = null;
    private String HostWatchID = null;

    private BufferedImage bm = null;
    private ImageIcon img =  null;

    static DatagramPacket sendPacket = null;
    ByteArrayOutputStream byteArrayOutputStream = null;

    static DatagramPacket recivePacket = null;
    ByteArrayInputStream byteArrayInputStream = null;

    static byte[] imageData = new byte[65507];


    public LiveStreamFrm(ClientFrm clientFrm) {
        this.clientFrm = clientFrm;
        initComponents();
        this.jLabel1.setText(this.clientFrm.getUserName());
        this.running = true;
        this.txtChat.setEditable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.webcam = Webcam.getDefault();

        webcam.open();

        bm = this.webcam.getImage();
        img =  new ImageIcon(bm);
        this.jpStreaming.setIcon(img);

        Runnable liveStream = () -> {
            while (running) {
                byteArrayOutputStream = new ByteArrayOutputStream();
                bm = this.webcam.getImage();
                img = new ImageIcon(new ImageIcon(bm).getImage().getScaledInstance(417, 438, Image.SCALE_DEFAULT));
                //417x438
                this.jpStreaming.setIcon(img);
                try {
                    ImageIO.write(bm, "jpeg", byteArrayOutputStream);
                    imageData = byteArrayOutputStream.toByteArray();
                    sendPacket = new DatagramPacket(imageData,imageData.length,InetAddress.getByName(ClientFrm.IPServer),ClientFrm.portUDPServer);
                    this.clientFrm.getUDPSocket().send(sendPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if(webcam != null)
                webcam.close();
            Thread.currentThread().interrupt();
        };
        new Thread(liveStream).start();
        Runnable onMessage = () -> {
            try{
                this.onMessage();
            }catch(IOException ex){
//                ex.printStackTrace();
            }
        };
        new Thread(onMessage).start();
    }

    public LiveStreamFrm(ClientFrm clientFrm, String HostWatchID) {
        this.clientFrm = clientFrm;
        this.HostWatchID = HostWatchID;
        initComponents();
        this.running = true;
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        recivePacket = new DatagramPacket(imageData, imageData.length);
        Runnable watchLive = () -> {
            System.out.println("runing watch");
            while (running) {
                try{
                    this.clientFrm.getUDPSocket().receive(recivePacket);
                    byteArrayInputStream = new ByteArrayInputStream(recivePacket.getData());
                    bm = ImageIO.read(byteArrayInputStream);
                }catch(IOException ex){
                    ex.printStackTrace();
                }
                img = new ImageIcon(new ImageIcon(bm).getImage().getScaledInstance(417, 438, Image.SCALE_DEFAULT));
                //417x438
                this.jpStreaming.setIcon(img);
            }
        };
        new Thread(watchLive).start();
        Runnable onMessage = () -> {
            try{
                this.onMessage();
            }catch(IOException ex){
//                ex.printStackTrace();
            }
        };
        new Thread(onMessage).start();
    }

    private void onMessage() throws IOException{
        if(this.clientFrm.ChatSocket!=null){
            Socket socket = this.clientFrm.ChatSocket;
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while(this.running){
                if ((bytesRead = inputStream.read(buffer)) != -1){
                    String contentChat = new String(buffer, 0, bytesRead);
                    this.txtChat.append(contentChat+"\n");
                }
            }
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        txtChat = new javax.swing.JTextArea();
        txtMessage = new javax.swing.JTextField();
        btnSendMessage = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jpStreaming = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        txtChat.setColumns(20);
        txtChat.setRows(5);
        jScrollPane1.setViewportView(txtChat);

        btnSendMessage.setText("Send");
        btnSendMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendMessageActionPerformed(evt);
            }
        });

        jLabel1.setText("Chat log:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(txtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 563, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnSendMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jpStreaming, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(23, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jpStreaming, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtMessage)
                                        .addComponent(btnSendMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                                .addContainerGap(42, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        this.clientFrm.setVisible(true);
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        this.running = false;
        if(this.webcam!=null)
            this.clientFrm.sendGetRequest(Command.OFF_STREAM,"");
        else{
            this.clientFrm.sendGetRequest(Command.OFF_WATCH,HostWatchID);
            if( this.clientFrm.ChatSocket!=null){
                try{
                    this.clientFrm.ChatSocket.close();
                    this.clientFrm.ChatSocket = null;
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }

        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    private void btnSendMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendMessageActionPerformed
        // TODO add your handling code here:
        if(txtMessage.getText().equals(""))
            return;
        try{
            String content = this.clientFrm.getUserName()+":"+this.txtMessage.getText();
            String idGroup = this.HostWatchID==null?this.clientFrm.idClinet:this.HostWatchID;
            this.clientFrm.sendGetRequest(Command.CHAT, idGroup+"@"+content);
            this.txtMessage.setText("");
        }catch(Exception ex){

        }

    }//GEN-LAST:event_btnSendMessageActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSendMessage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jpStreaming;
    private javax.swing.JTextArea txtChat;
    private javax.swing.JTextField txtMessage;
    // End of variables declaration//GEN-END:variables
}
