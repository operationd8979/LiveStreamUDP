package com.chipchip.livestreamudp.Client;

import com.chipchip.livestreamudp.Client.Model.StateListener;
import com.chipchip.livestreamudp.Server.model.Command;
import com.chipchip.livestreamudp.Server.model.GroupLive;
import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.ice.*;
import org.ice4j.ice.harvest.StunCandidateHarvester;
import org.ice4j.ice.sdp.CandidateAttribute;
import org.ice4j.ice.sdp.IceSdpUtils;
import org.opentelecoms.javax.sdp.NistSdpFactory;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.sdp.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Admin
 */
public class LiveStreamPTPFrm extends javax.swing.JFrame {

    private boolean running = false;
    private ClientFrm clientFrm;

    private String HostWatchID = null;

    public BufferedImage bm = null;
    private ImageIcon img =  null;

    //punch hole
    private Agent agent = null;
    private StateListener stateListener = null;
    private final String[] hostnames = new String[] {"jitsi.org","numb.viagenie.ca","stun.ekiga.net"};


    static DatagramPacket recivePacket = null;
    ByteArrayInputStream byteArrayInputStream = null;


    static byte[] imageData = new byte[65507];

    private Thread WatchLiveThread  = null;

    private boolean puchHoleEnable = false;



    public LiveStreamPTPFrm(ClientFrm clientFrm, String HostWatchID) {
        this.clientFrm = clientFrm;
        this.HostWatchID = HostWatchID;
        initComponents();
        this.txtChat.setEditable(false);
        this.running = true;
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //thread watch live
        this.WatchLiveThread = new Thread(this::watchLiveStream);
        this.WatchLiveThread.start();

        //thread chat in live
        new Thread(this::onMessage).start();

    }

    public void watchLiveStream() {
        this.recivePacket = new DatagramPacket(imageData, imageData.length);
        System.out.println("running watch");
        while (this.running&&!this.puchHoleEnable) {
            System.out.println("Watching...........");
            try{
                this.clientFrm.getUDPSocket().receive(recivePacket);
                this.byteArrayInputStream = new ByteArrayInputStream(recivePacket.getData());
                this.bm = ImageIO.read(byteArrayInputStream);
            }catch(IOException ex){
                    ex.printStackTrace();
            }
            this.img = new ImageIcon(new ImageIcon(bm).getImage().getScaledInstance(417, 438, Image.SCALE_DEFAULT));
            //417x438
            this.jpStreaming.setIcon(img);
        }
        this.WatchLiveThread.interrupt();
    }

    private void onMessage(){
        if(this.clientFrm.ChatSocket!=null){
            Socket socket = this.clientFrm.ChatSocket;
            try{
                InputStream inputStream = socket.getInputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while(this.running){
                    if ((bytesRead = inputStream.read(buffer)) != -1){
                        String contentChat = new String(buffer, 0, bytesRead);
                        if(contentChat.startsWith("SERVER")){
                            System.out.println("get "+contentChat.substring(6,9));
                            handleRequestServer(contentChat);
                            System.out.println("done "+contentChat.substring(6,9));
                        }
                        else{
                            this.txtChat.append(contentChat+"\n");
                        }
                    }
                }
                Thread.currentThread().interrupt();
            }catch (IOException ex){

            }
        }
    }

    private void handleRequestServer(String requestServer) throws IOException{
        String SENDSDP = "SENDSDP";
        String SDPDES = "SDPDES";
        String UP = "UP";
        String DOWN = "DOWN";
        String[] arrayString = requestServer.split("@");
        String command = arrayString[1];
        OutputStream outputStream = this.clientFrm.ChatSocket.getOutputStream();
        if(command.equals(SENDSDP)){
            //send local SDP
            this.agent = new Agent();
            for(String hostname: this.hostnames){
                try {
                    // InetAddress qualifies a url to an IP Address, if you have an error here, make sure the url is reachable and correct
                    TransportAddress ta = new TransportAddress(InetAddress.getByName(hostname), 3478, Transport.UDP);
                    // Currently Ice4J only supports UDP and will throw an Error otherwise
                    this.agent.addCandidateHarvester(new StunCandidateHarvester(ta));
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
            IceMediaStream stream = agent.createMediaStream("chip");
            int port = 1234; // Choose any port
            agent.createComponent(stream, port, port, port+100);
            try{
                String toSend = this.createSDPDescription(agent);
                outputStream.write(toSend.getBytes());
            }catch(Throwable throwable){
                throwable.printStackTrace();
            }
        }
        if(command.equals(SDPDES)){
            //get remote SDP
            String remoteSDP = arrayString[2];
            try {
                this.parseSDP(this.agent, remoteSDP); // This will add the remote information to the agent.
                outputStream.write("OK".getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(command.equals(UP)){
            //start punch hole upload
            stateListener = new StateListener(true,bm);
            agent.addStateChangeListener(stateListener);
            this.puchHoleEnable = true;
            agent.startConnectivityEstablishment();
            this.handleOpenPTP(true);
        }
        if(command.equals(DOWN)){
            //start punch hole download
            stateListener = new StateListener(false,bm);
            agent.addStateChangeListener(stateListener);
            this.puchHoleEnable = true;
            agent.startConnectivityEstablishment();
            this.handleOpenPTP(false);
        }

    }

    private void handleOpenPTP(boolean host){
        this.WatchLiveThread.interrupt();
        while(!this.WatchLiveThread.isInterrupted()){
        }
        this.WatchLiveThread = null;
        System.out.println("STOPED thread");
        if(host){
            this.WatchLiveThread = new Thread(this::handleUPPTP);
        }
        else{
            this.WatchLiveThread = new Thread(this::handleDownPTP);
        }
        this.WatchLiveThread.start();
    }

    private void handleUPPTP(){
        this.recivePacket = new DatagramPacket(imageData, imageData.length);
        System.out.println("running up");
        while (this.running) {
            try{
                this.clientFrm.getUDPSocket().receive(recivePacket);
                this.byteArrayInputStream = new ByteArrayInputStream(recivePacket.getData());
                this.bm = ImageIO.read(byteArrayInputStream);
                this.stateListener.bufferedImage = this.bm;
            }catch(IOException ex){
//                    ex.printStackTrace();
            }
            this.img = new ImageIcon(new ImageIcon(this.bm).getImage().getScaledInstance(417, 438, Image.SCALE_DEFAULT));
            //417x438
            this.jpStreaming.setIcon(img);
//            System.out.println("aaaaa "+this.stateListener.bufferedImage);
        }
        this.WatchLiveThread.interrupt();
    }

    private void handleDownPTP(){
        System.out.println("running down");
        while (this.running){
//            System.out.println("aaaaa "+this.stateListener.bufferedImage);
            this.bm = this.stateListener.bufferedImage;
            if(this.bm != null){
                this.img = new ImageIcon(new ImageIcon(this.bm).getImage().getScaledInstance(417, 438, Image.SCALE_DEFAULT));
                this.jpStreaming.setIcon(img);
            }
            //417x438
        }
        this.WatchLiveThread.interrupt();
    }

    public String createSDPDescription(Agent agent)   throws Throwable
    {
        SdpFactory factory = new NistSdpFactory();
        SessionDescription sdess = factory.createSessionDescription();

        IceSdpUtils.initSessionDescription(sdess, agent);

        return sdess.toString();
    }

    public void parseSDP(Agent localAgent, String sdp)
            throws Exception
    {
        SdpFactory factory = new NistSdpFactory();
        SessionDescription sdess = factory.createSessionDescription(sdp);

        for(IceMediaStream stream : localAgent.getStreams())
        {
            stream.setRemotePassword(sdess.getAttribute("ice-pwd"));
            stream.setRemoteUfrag(sdess.getAttribute("ice-ufrag"));
        }

        Connection globalConn = sdess.getConnection();
        String globalConnAddr = null;
        if(globalConn != null)
            globalConnAddr = globalConn.getAddress();

        Vector<MediaDescription> mdescs = sdess.getMediaDescriptions(true);

        for (MediaDescription desc : mdescs)
        {
            String streamName = desc.getMedia().getMediaType();

            IceMediaStream stream = localAgent.getStream(streamName);

            if(stream == null)
                continue;

            Vector<Attribute> attributes = desc.getAttributes(true);
            for (Attribute attribute : attributes)
            {
                if (attribute.getName().equals(CandidateAttribute.NAME))
                    parseCandidate(attribute, stream);
            }

            //set default candidates
            Connection streamConn = desc.getConnection();
            String streamConnAddr = null;
            if(streamConn != null)
                streamConnAddr = streamConn.getAddress();
            else
                streamConnAddr = globalConnAddr;

            int port = desc.getMedia().getMediaPort();

            TransportAddress defaultRtpAddress =
                    new TransportAddress(streamConnAddr, port, Transport.UDP);

            int rtcpPort = port + 1;
            String rtcpAttributeValue = desc.getAttribute("rtcp");

            if (rtcpAttributeValue != null)
                rtcpPort = Integer.parseInt(rtcpAttributeValue);

            TransportAddress defaultRtcpAddress =
                    new TransportAddress(streamConnAddr, rtcpPort, Transport.UDP);

            Component rtpComponent = stream.getComponent(Component.RTP);
            Component rtcpComponent = stream.getComponent(Component.RTCP);

            Candidate<?> defaultRtpCandidate
                    = rtpComponent.findRemoteCandidate(defaultRtpAddress);
            rtpComponent.setDefaultRemoteCandidate(defaultRtpCandidate);

            if(rtcpComponent != null)
            {
                Candidate<?> defaultRtcpCandidate
                        = rtcpComponent.findRemoteCandidate(defaultRtcpAddress);
                rtcpComponent.setDefaultRemoteCandidate(defaultRtcpCandidate);
            }
        }
    }

    private RemoteCandidate parseCandidate(Attribute attribute,
                                                  IceMediaStream stream)
    {
        String value = null;

        try{
            value = attribute.getValue();
        }catch (Throwable t){}//can't happen

        StringTokenizer tokenizer = new StringTokenizer(value);

        //XXX add exception handling.
        String foundation = tokenizer.nextToken();
        int componentID = Integer.parseInt( tokenizer.nextToken() );
        Transport transport = Transport.parse(tokenizer.nextToken());
        long priority = Long.parseLong(tokenizer.nextToken());
        String address = tokenizer.nextToken();
        int port = Integer.parseInt(tokenizer.nextToken());

        TransportAddress transAddr
                = new TransportAddress(address, port, transport);

        tokenizer.nextToken(); //skip the "typ" String
        CandidateType type = CandidateType.parse(tokenizer.nextToken());

        Component component = stream.getComponent(componentID);

        if(component == null)
            return null;

        // check if there's a related address property

        RemoteCandidate relatedCandidate = null;
        if (tokenizer.countTokens() >= 4)
        {
            tokenizer.nextToken(); // skip the raddr element
            String relatedAddr = tokenizer.nextToken();
            tokenizer.nextToken(); // skip the rport element
            int relatedPort = Integer.parseInt(tokenizer.nextToken());

            TransportAddress raddr = new TransportAddress(
                    relatedAddr, relatedPort, Transport.UDP);

            relatedCandidate = component.findRemoteCandidate(raddr);
        }

        RemoteCandidate cand = new RemoteCandidate(transAddr, component, type,
                foundation, priority, relatedCandidate);

        component.addRemoteCandidate(cand);

        return cand;
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
        this.clientFrm.sendGetRequest(Command.OFF_WATCH,HostWatchID);
        if( this.clientFrm.ChatSocket!=null){
            try{
                this.clientFrm.ChatSocket.close();
                this.clientFrm.ChatSocket = null;
            }catch(Exception ex){
                ex.printStackTrace();
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
