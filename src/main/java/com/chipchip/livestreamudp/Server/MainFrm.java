/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chipchip.livestreamudp.Server;

import com.chipchip.livestreamudp.Server.model.Client;
import com.chipchip.livestreamudp.Server.model.ClientHandlerTCP;
import com.chipchip.livestreamudp.Server.model.Command;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Admin
 */
public class MainFrm extends javax.swing.JFrame {
    
    private static int PORT_COMMAND = 12345;
    private static int PORT_STREAMING = 12346;
    private ServerSocket commandSocket = null;
    private DatagramSocket streamingSocket = null;
    public static List<Client> clients = new ArrayList<>();
    private boolean running = false;
    
    private static String START_SERVER = "Start Server";
    private static String STOP_SERVER = "Stop Server";
    
    Thread tcpThread = null;
    
    public void initServer(){
        commandSocket = null;
        streamingSocket = null;
        clients = new ArrayList<>();
        running = false;
    }

    /**
     * Creates new form MainFrm
     */
    public MainFrm() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnAction = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLogs = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lbLineD = new javax.swing.JLabel();
        lbLineU = new javax.swing.JLabel();
        lbClients = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lbDownload = new javax.swing.JLabel();
        lbUpload = new javax.swing.JLabel();
        pnLiveStream = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnAction.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        btnAction.setText("Start Server");
        btnAction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActionActionPerformed(evt);
            }
        });

        txtLogs.setEditable(false);
        txtLogs.setColumns(20);
        txtLogs.setRows(5);
        jScrollPane1.setViewportView(txtLogs);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Server Logs:");

        jLabel2.setText("Download:");

        jLabel3.setText("Upload:");

        jLabel4.setForeground(new java.awt.Color(200, 0, 0));
        jLabel4.setText("mb/s");

        jLabel5.setForeground(new java.awt.Color(200, 0, 0));
        jLabel5.setText("mb/s");

        jLabel6.setForeground(new java.awt.Color(0, 200, 0));
        jLabel6.setText("lines");

        jLabel7.setForeground(new java.awt.Color(0, 200, 0));
        jLabel7.setText("lines");

        lbLineD.setForeground(new java.awt.Color(0, 200, 0));
        lbLineD.setText("0");

        lbLineU.setForeground(new java.awt.Color(0, 200, 0));
        lbLineU.setText("0");

        lbClients.setForeground(new java.awt.Color(0, 0, 200));
        lbClients.setText("0");

        jLabel11.setForeground(new java.awt.Color(0, 0, 200));
        jLabel11.setText("clients");

        lbDownload.setForeground(new java.awt.Color(200, 0, 0));
        lbDownload.setText("0");

        lbUpload.setForeground(new java.awt.Color(200, 0, 0));
        lbUpload.setText("0");

        javax.swing.GroupLayout pnLiveStreamLayout = new javax.swing.GroupLayout(pnLiveStream);
        pnLiveStream.setLayout(pnLiveStreamLayout);
        pnLiveStreamLayout.setHorizontalGroup(
            pnLiveStreamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnLiveStreamLayout.setVerticalGroup(
            pnLiveStreamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 335, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(29, 29, 29)
                .addComponent(lbClients)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 309, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(63, 63, 63)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbDownload)
                    .addComponent(lbUpload))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbLineU)
                    .addComponent(lbLineD))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(21, 21, 21))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(269, 269, 269)
                        .addComponent(btnAction, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(62, 62, 62)
                        .addComponent(pnLiveStream, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(lbClients)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lbLineD)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lbLineU)
                                    .addComponent(jLabel5)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(lbDownload)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(lbUpload))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel7)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel6)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3)))
                        .addGap(30, 30, 30)
                        .addComponent(pnLiveStream, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(btnAction, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnActionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActionActionPerformed
        // TODO add your handling code here:
        if(btnAction.getText().equals(START_SERVER)){
            enableServer();
        }
        else{
            closeServer();
        }
    }//GEN-LAST:event_btnActionActionPerformed

    //FUNTION
    private void enableServer() {
        initServer();
        this.btnAction.setText(STOP_SERVER);
        try{
            this.running = true;
            commandSocket = new ServerSocket(PORT_COMMAND);
            tcpThread = new Thread(this::watchDog);
            tcpThread.setName("WatchDog_Thread");
            tcpThread.start();
            streamingSocket = new DatagramSocket(PORT_STREAMING);
            log("[*]Server listening....");
        }catch(IOException ex){
            log("[#]Error:Server can't start!");
            closeServer();
        }
    }
    
    public void watchDog() {
        log("..."+Thread.currentThread().getName()+" waiting for commands!");
        while(true){
            try{
                Socket clientSocket = commandSocket.accept();
                InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                if ((bytesRead = inputStream.read(buffer)) != -1) {
                    String requestData = new String(buffer, 0, bytesRead);
                    if(requestData.startsWith(Command.CONNECT)){
                        int port = Integer.parseInt(requestData.trim().strip().split("@")[1]);
                        outputStream.write((Command.OK+"@"+addClient(clientSocket,port)).getBytes());
                    }
                    else{
                        String command = requestData.trim().strip().split("@")[0];
                        String idClient = requestData.trim().strip().split("@")[1];
                        if(filterRequest(clientSocket,idClient)){
                            new Thread(new ClientHandlerTCP(clientSocket,command,idClient,this)).start();
                            continue;
                        }
                    } 
                }
                clientSocket.close();
            }catch(SocketException ex) {
                log("[#]Error:" + ex.getMessage() + " from [WatchDog]");
                log("..."+Thread.currentThread().getName()+" [Closed]");
                Thread.currentThread().stop();
            }catch(IOException ex){
                log("[#]Error:" + ex.getMessage() + " from [WatchDog]");
            }
        }
    }
    
    private boolean filterRequest(Socket clientSocket,String idClient){
        return clients.stream()
                .anyMatch((client) -> (client.getAddr().equals(clientSocket.getInetAddress())
                        &&client.getId().equals(idClient)));
    }
    
    private void closeServer() {
        this.running = false;
        clients = null;
        this.btnAction.setText(START_SERVER);
        try{
            if(commandSocket!=null){
                commandSocket.close();
                commandSocket = null;
            }
            if(streamingSocket!=null){
                streamingSocket.close();
                streamingSocket = null;
            }
            log("[*]Server stopped!");
        }catch(IOException ex){
            log(ex.getMessage());
        }
        
    }
    
    public String addClient(Socket clientSocket,int portUDPClient){
        String id = UUID.randomUUID().toString();
        clients.add(new Client(id, clientSocket.getInetAddress(), portUDPClient));
        this.lbClients.setText(Integer.toString(clients.size()));
        log("["+clientSocket.getInetAddress()+"]["+portUDPClient+"] connected!");
        return id;
    }
    
    public void log(String log){
        this.txtLogs.append(log+"\n");
    }
    
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAction;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbClients;
    private javax.swing.JLabel lbDownload;
    private javax.swing.JLabel lbLineD;
    private javax.swing.JLabel lbLineU;
    private javax.swing.JLabel lbUpload;
    private javax.swing.JPanel pnLiveStream;
    private javax.swing.JTextArea txtLogs;
    // End of variables declaration//GEN-END:variables
}
