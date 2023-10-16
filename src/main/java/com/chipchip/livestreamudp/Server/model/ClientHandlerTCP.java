/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chipchip.livestreamudp.Server.model;

import com.chipchip.livestreamudp.Server.MainFrm;
import com.google.gson.Gson;
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
                case Command.OFF_STREAM:
                    this.mainFrm.removeLiveGroup(this.idClient,this.nameClient);
                    outputStream.write(Command.OK.getBytes());
                    break;
                case Command.GET_LIST_STREAM:
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                    List<GroupLive> listLive = new ArrayList<>();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    for(StreamGroup sg: MainFrm.streamers){
                        if(sg.getCurrentImage()!=null){
                            ImageIO.write(sg.getCurrentImage(), "PNG", byteArrayOutputStream);
                            listLive.add(new GroupLive(sg.getHost(), sg.getName(), sg.getViewers().size(),byteArrayOutputStream.toByteArray()));
                        }
                    }
                    if(listLive.size()>0){
                        ResponseListGroupLive responseLive = new ResponseListGroupLive(listLive);
                        objectOutputStream.writeObject(responseLive);
                    }
//                    Graphics2D
//                    ResponseListGroupLive responseLive = new ResponseListGroupLive(null);
//                    Gson gson = new Gson();
//                    String json = gson.toJson(responseLive);
//                    outputStream.write(json.getBytes());
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
