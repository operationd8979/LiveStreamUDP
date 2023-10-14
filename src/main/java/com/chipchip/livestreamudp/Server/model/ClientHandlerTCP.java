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
    private Socket clinetSocket;

    public ClientHandlerTCP(Socket clinetSocket,String commnad,String idClient,MainFrm mainFrm) {
        this.idClient = idClient;
        this.mainFrm = mainFrm;
        this.commnad = commnad;
        this.clinetSocket = clinetSocket;
    }
    
    public String informationClient(){
        return "["+this.idClient+"]";
    }
    
    private boolean equalToClient(Client client){
        return this.idClient.equals(client.getId());
    }

    @Override
    public void run() {
        try {
            // Xử lý dữ liệu từ khách hàng (ở đây là một ví dụ đơn giản)   
            OutputStream outputStream = this.clinetSocket.getOutputStream();
            switch(this.commnad){
                case Command.EXIST:
                    MainFrm.clients = MainFrm.clients.stream()
                            .filter(c->!this.equalToClient(c)).collect(Collectors.toList());
                    outputStream.write(Command.OK.getBytes());
                    mainFrm.log(this.informationClient()+ "disconnected!");
                    break;
                default:
                    mainFrm.log(this.informationClient()+ "call unknow command!");
                    outputStream.write(Command.UNKNOW_COMMAND.getBytes());     
            }
            this.clinetSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
