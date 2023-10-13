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
    
    private Socket clientSocket;
    private MainFrm mainFrm;

    public ClientHandlerTCP(Socket socket,MainFrm mainFrm) {
        this.clientSocket = socket;
        this.mainFrm = mainFrm;
    }
    
    public String informationClient(){
        return "["+this.clientSocket.getInetAddress().toString()+"]["+this.clientSocket.getPort()+"]";
    }
    
    private boolean equalToClient(Client client){
        return this.clientSocket.getInetAddress()
                .equals(client.getAddr())&&this.clientSocket.getPort()==client.getPort();
    }

    @Override
    public void run() {
        try {
            // Xử lý dữ liệu từ khách hàng (ở đây là một ví dụ đơn giản)
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                String message = new String(buffer, 0, bytesRead);
                switch(message){
                    case Command.EXIST:
                        MainFrm.clients = MainFrm.clients.stream()
                                .filter(c->!this.equalToClient(c)).collect(Collectors.toList());
                        outputStream.write("OK".getBytes());
                        mainFrm.log(this.informationClient()+ "disconnected!");
                        break;
                    default:
                        mainFrm.log(this.informationClient()+ "call unknow command!");
                        outputStream.write(Command.UNKNOW_COMMAND.getBytes());
                        
                }
            }
            clientSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
