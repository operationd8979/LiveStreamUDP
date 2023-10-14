/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chipchip.livestreamudp.Server.model;

import com.chipchip.livestreamudp.Server.MainFrm;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.stream.Collectors;

/**
 *
 * @author Admin
 */
public class ClientHandlerTCP implements Runnable {
    
    private MainFrm mainFrm;
    private String idClient;
    private String commnad;
    private Socket clientSocket;
    private String nameClient;

    public ClientHandlerTCP(Socket clientSocket,String commnad,String idClient,String nameClient,MainFrm mainFrm) {
        this.idClient = idClient;
        this.mainFrm = mainFrm;
        this.commnad = commnad;
        this.clientSocket = clientSocket;
        this.nameClient = nameClient;
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
                    break;
                case Command.LIVE:
                    this.mainFrm.addLiveGroup(this.idClient, this.nameClient);
                    outputStream.write(Command.OK.getBytes());
                    break;
                case Command.OFFSTREAM:
                    this.mainFrm.removeLiveGroup(this.idClient,this.nameClient);
                    outputStream.write(Command.OK.getBytes());
                    break;
                default:
                    outputStream.write(Command.UNKNOW_COMMAND.getBytes());     
            }
            this.clientSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
