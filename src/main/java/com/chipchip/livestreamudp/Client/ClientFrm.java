/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chipchip.livestreamudp.Client;

import com.chipchip.livestreamudp.Server.model.Command;
import com.chipchip.livestreamudp.Server.model.ResponseListGroupLive;
import com.chipchip.livestreamudp.Server.model.StreamGroup;
import com.chipchip.livestreamudp.Server.model.GroupLive;
import java.awt.Button;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Admin
 */
public class ClientFrm extends javax.swing.JFrame {
    
    public static final String IPServer = "localhost";
    public static final int portTCPServer = 12345;
    public static final int portUDPServer = 12346;

    private static String CONNECT = "Connect";
    private static String CONNECTING = "Connecting";
    private static String DISCONECT = "Disconnect";
    private static String ONLINE = "online";
    private static String OFFLINE = "offline";
    
    
    private boolean running = false;
    private String idClinet = "FFFFFFF";
    private DatagramSocket UDPSocket = null;
    private int portUDP = -1;
    private ResponseListGroupLive responseListGroupLive = null;
    
    /**
     * Creates new form ClientFrm
     */
    public ClientFrm() {
        initComponents();
        this.pnLiveStream.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        initFrm();
    }
    
    public final void initFrm(){
        try{
            if(UDPSocket!=null){
                UDPSocket.close();
                UDPSocket = null;
            }
            this.portUDP = -1;
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        this.running = false;
        this.idClinet = "FFFFFFF";
        this.txtID.setText(idClinet);
        this.txtName.setEditable(!running);
        this.btnLive.setEnabled(running);
        this.lbState.setText(OFFLINE);
        this.btnAction.setText(CONNECT);
        this.txtID.setText(idClinet); 
        this.txtTimeGetLives.setText("");
        removeAllButton();
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
        txtName = new javax.swing.JTextField();
        lbState = new javax.swing.JLabel();
        pnLiveStream = new javax.swing.JPanel();
        btnLive = new javax.swing.JButton();
        txtID = new javax.swing.JTextField();
        txtTimeGetLives = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        btnAction.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnAction.setText("Connect");
        btnAction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActionActionPerformed(evt);
            }
        });

        txtName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lbState.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lbState.setForeground(new java.awt.Color(200, 0, 0));
        lbState.setText("OFFLINE");

        javax.swing.GroupLayout pnLiveStreamLayout = new javax.swing.GroupLayout(pnLiveStream);
        pnLiveStream.setLayout(pnLiveStreamLayout);
        pnLiveStreamLayout.setHorizontalGroup(
            pnLiveStreamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 787, Short.MAX_VALUE)
        );
        pnLiveStreamLayout.setVerticalGroup(
            pnLiveStreamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 308, Short.MAX_VALUE)
        );

        btnLive.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnLive.setText("LiveStream");
        btnLive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLiveActionPerformed(evt);
            }
        });

        txtID.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(303, 303, 303)
                        .addComponent(btnLive)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtTimeGetLives, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(lbState)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAction))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnLiveStream, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAction)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbState)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnLiveStream, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLive)
                    .addComponent(txtTimeGetLives, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnActionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActionActionPerformed
        // TODO add your handling code here:
        if(this.btnAction.getText().equals(CONNECT)){
            if(!txtName.getText().equals("")&&txtName.getText()!=null){
                connect();
                refreshLiveList();
            }
        }
        else{
            disconnect();
        }
    }//GEN-LAST:event_btnActionActionPerformed

    private void btnLiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLiveActionPerformed
        // TODO add your handling code here:
        if(this.sendGetRequest(Command.LIVE,"").equals(Command.OK)){
            this.setVisible(false);
            new LiveStreamFrm(this).setVisible(true);
        }
    }//GEN-LAST:event_btnLiveActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        if(running){
            disconnect();
        }
    }//GEN-LAST:event_formWindowClosing

    public String sendGetRequest(String content,String payload) {
        Socket socket = null;
        try{
            socket = new Socket(IPServer, portTCPServer);
            OutputStream outputStream = socket.getOutputStream();
            if(!content.equals(Command.CONNECT)){
                if(idClinet==null){
                    return "idNULL";
                }
                content+="@"+idClinet;
            }
            if(content.startsWith(Command.LIVE)||content.startsWith(Command.OFF_STREAM)){
                String name = this.txtName.getText();
                if(name.equals("")){
                    return "nameNull";
                }
                content+="@"+name;
            }
            if(!payload.equals("")){
                content+="@"+payload;
            }
            outputStream.write(content.getBytes());
            //////////////////////////////////
            System.out.println("Đã gửi xong: "+ content);
            //////////////////////////////////
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                socket.close();
                return new String(buffer, 0, bytesRead);
            }
            return "";
        }catch(IOException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
            if(socket!=null){
                try{
                    socket.close();
                }catch(Exception e){}
            }
            return "";
        }
    }
    
    public ResponseListGroupLive sendPostRequest(String content) {
        Socket socket = null;
        try{
            socket = new Socket(IPServer, portTCPServer);
            OutputStream outputStream = socket.getOutputStream();
            if(!content.equals(Command.CONNECT)){
                if(idClinet==null){
                    return null;
                }
                content+="@"+idClinet;
            }
            outputStream.write(content.getBytes());
            //////////////////////////////////
            System.out.println("Đã gửi xong: "+ content);
            //////////////////////////////////
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            try{
                ResponseListGroupLive response = (ResponseListGroupLive) objectInputStream.readObject();
                return response;
            }catch(ClassNotFoundException ex){
                JOptionPane.showMessageDialog(null, ex.getMessage()+ "from sendPostRequest");
                return null;
            }catch(EOFException ex){
                return null;
            }
        }catch(IOException ex){
            JOptionPane.showMessageDialog(null, ex.getClass()+ " from sendPostRequest");
            if(socket!=null){
                try{
                    socket.close();
                }catch(Exception e){}
            }
            return null;
        }
    }
    
    
    public DatagramSocket openSocketUDP() {
        int i = 15000;
        DatagramSocket socket;
        while(true) {
            if(i>20000)
                break;
            try{
                socket = new DatagramSocket(i);
                return socket;
            }catch(Exception ex){
                i++;
            }
        }
        return null;
    }
    
    public void connect(){
        this.btnAction.setText(CONNECTING);
        this.running = true;
        UDPSocket = openSocketUDP();
        if(UDPSocket==null){
            disconnect();
            return;
        }
        String responseTCP = sendGetRequest(Command.CONNECT+"@"+UDPSocket.getLocalPort(),"");
        if(!responseTCP.startsWith("OK")){
            disconnect();
            return;
        }
        this.idClinet = responseTCP.trim().split("@")[1];
        this.txtName.setEditable(!running);
        this.btnLive.setEnabled(running);
        this.lbState.setText(ONLINE);
        this.btnAction.setText(DISCONECT);
        portUDP = UDPSocket.getLocalPort();
        this.txtID.setText(idClinet);
    }
    
    private void disconnect() {
        this.running = false;
        try{
            String response = sendGetRequest(Command.EXIST,"");
            if(!response.equals("OK")){
                //Server error response disconnect!
                //////////////////////////
//                System.out.println("Error: "+response);
                //////////////////////////
            }
            if(UDPSocket!=null){
                UDPSocket.close();
                UDPSocket = null;
            }
            this.portUDP = -1;
            initFrm();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    public void refreshLiveList() {
        this.responseListGroupLive = this.sendPostRequest(Command.GET_LIST_STREAM);
        if(this.responseListGroupLive!=null){
            refreshLiveStreamPanel();
        }
    }
    
    private void removeAllButton () {
        for (Component component : pnLiveStream.getComponents()) {
            if (component instanceof JPanel && component.getName() != null) {
                pnLiveStream.remove(component);
                pnLiveStream.revalidate();
                pnLiveStream.repaint();
            }
        }
    }
    
    private void refreshLiveStreamPanel(){
        List<GroupLive> listLive = this.responseListGroupLive.listLive;
        Long timeGet = this.responseListGroupLive.timeGet;
        this.txtTimeGetLives.setText(Long.toString(timeGet));
        for(GroupLive live : listLive){
            boolean alreadyAdded = false;
            String IDLive = live.getHost().getId();
            for (Component component : this.pnLiveStream.getComponents()) {
                if (component instanceof JPanel && component.getName() != null && component.getName().equals(IDLive)) {
                    alreadyAdded = true;
                    break;
                }
            }
            if(!alreadyAdded){
                Button button = new Button();
                button.setLabel(live.getName());
                ClientFrm clientFrmRef = this;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) { 
                        String response = sendGetRequest(Command.WATCH_LIVE,live.getHost().getId());
                        if(response.equals(Command.OK)){
                            clientFrmRef.setVisible(false);
                            new LiveStreamFrm(clientFrmRef,live.getHost().getId()).setVisible(true);
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Error: không link được live stream.");
                        }
                    }
                });
                BufferedImage bw = null;
                try{
                    ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(live.getImageData());
                    bw = ImageIO.read(arrayInputStream);
                }catch(IOException ex){
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
                ImageIcon originalIcon = new ImageIcon(bw);
                Image scaledImage = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                System.out.println(scaledIcon);
                JLabel liveGroup = new JLabel(scaledIcon);
                JPanel labelPanel = new JPanel();
                labelPanel.add(liveGroup);
                labelPanel.add(button);
                labelPanel.setName(IDLive);
                this.pnLiveStream.add(labelPanel);
            }
        }
        this.pnLiveStream.revalidate();
        this.pnLiveStream.repaint();
    }
    
    
    public DatagramSocket getUDPSocket(){
        return this.UDPSocket;
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
            java.util.logging.Logger.getLogger(ClientFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientFrm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAction;
    private javax.swing.JButton btnLive;
    private javax.swing.JLabel lbState;
    private javax.swing.JPanel pnLiveStream;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtTimeGetLives;
    // End of variables declaration//GEN-END:variables
}
