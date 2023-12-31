/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chipchip.livestreamudp.Server.model;

import com.chipchip.livestreamudp.Server.MainFrm;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;

/**
 *
 * @author Admin
 */
public class ClientHandlerTCP implements Runnable {
    
    private MainFrm mainFrm;
    private String idClient;
    private String commnad;
    private Socket clientSocket;
    private String payLoad;
    private String payLoad2;

    public ClientHandlerTCP(Socket clientSocket,String commnad,String idClient,String payLoad,String payLoad2,MainFrm mainFrm) {
        this.idClient = idClient;
        this.mainFrm = mainFrm;
        this.commnad = commnad;
        this.clientSocket = clientSocket;
        this.payLoad = payLoad;
        this.payLoad2 = payLoad2;
    }
    
    public String informationClient(){
        return "["+this.idClient.substring(0, 10)+"...]";
    }
    
    private boolean equalToClient(Client client){
        return this.idClient.equals(client.getId());
    }

    @Override
    public void run() {
        try {
            // Xử lý dữ liệu từ khách hàng (ở đây là một ví dụ đơn giản)   
            OutputStream outputStream = this.clientSocket.getOutputStream();
            switch(this.commnad){
                case Command.EXIST:
                    this.mainFrm.logoutClient(this.idClient);
                    outputStream.write(Command.OK.getBytes());
                    outputStream.flush();
                    break;
                case Command.LIVE:
                    this.mainFrm.addLiveGroup(this.idClient, this.payLoad, this.clientSocket);
                    outputStream.write(Command.OK.getBytes());
                    outputStream.flush();
                    break;
                case Command.OFF_STREAM:
                    this.mainFrm.removeLiveGroup(this.idClient,this.payLoad);
                    outputStream.write(Command.OK.getBytes());
                    outputStream.flush();
                    break;
                case Command.GET_LIST_STREAM:
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                    List<GroupLive> listLive = this.mainFrm.getListStream(); 
                    if(!listLive.isEmpty()){
                        ResponseListGroupLive responseLive = new ResponseListGroupLive(listLive);
                        objectOutputStream.writeObject(responseLive);
                        objectOutputStream.flush();
                    }
                    break;
                case Command.WATCH_LIVE:
                    if(this.mainFrm.linkStream(this.idClient, this.payLoad, clientSocket)==1){
                        outputStream.write(Command.OK.getBytes());
                        outputStream.flush();
                    }  
                    break;
                case Command.OFF_WATCH:
                    if(this.mainFrm.unLinkStream(this.idClient, this.payLoad)==1){
                        outputStream.write(Command.OK.getBytes());
                        outputStream.flush();
                    }  
                    break;
                case Command.OFF_WATCH_P2PUP:
                    if(this.mainFrm.unLinkP2PStream(this.idClient, this.payLoad, true)==1){
                        outputStream.write(Command.OK.getBytes());
                        outputStream.flush();
                    }
                    break;
                case Command.OFF_WATCH_P2PDOWN:
                    if(this.mainFrm.unLinkP2PStream(this.idClient, this.payLoad, false)==1){
                        outputStream.write(Command.OK.getBytes());
                        outputStream.flush();
                    }
                    break;
                case Command.CHAT:
                    this.mainFrm.onSendChat(this.payLoad,this.payLoad2);
                    break;
                default:
                    outputStream.write(Command.UNKNOW_COMMAND.getBytes());     
            }
            if(!(this.commnad.equals(Command.LIVE)||this.commnad.equals(Command.WATCH_LIVE))){
                this.clientSocket.close();
            }  
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
