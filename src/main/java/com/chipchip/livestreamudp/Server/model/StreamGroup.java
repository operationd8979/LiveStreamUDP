/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chipchip.livestreamudp.Server.model;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author operationd
 */
public class StreamGroup {
    
    private final Client host;
    private final String name;
    private List<Client> viewers;

    private Map<String,DatagramPacket> mapDatagramPacket;
    private BufferedImage currentImage = null;
    public Socket socketHost = null;
    
    public StreamGroup(Client host,String name, Socket socketHost){
        this.host = host;
        this.name = name;
        this.viewers = new ArrayList<>();
        this.mapDatagramPacket = new HashMap<>();
        this.socketHost = socketHost;
    }
    
    public Client getHost() {
        return this.host;
    }
    
    public List<Client> getViewers(){
        return this.viewers;
    }

    public Map<String,DatagramPacket> getMapDatagramPacket(){
        return this.mapDatagramPacket;
    }
    
    public void addViewer(Client client){
        this.viewers.add(client);
    }

    public DatagramPacket addDataPacket(String clientID,DatagramPacket datagramPacket){
        this.mapDatagramPacket.put(clientID,datagramPacket);
        return datagramPacket;
    }

    public String removeDataPacket(String clientID){
        this.mapDatagramPacket.remove(clientID);
        return clientID;
    }
    public String getName() {
        return this.name;
    }
    
    public BufferedImage getCurrentImage(){
        return this.currentImage;
    }
    
    public void setCurrentImage(BufferedImage bufferedImage){
        this.currentImage = bufferedImage;
    }
    
    @Override
    public String toString() {
        return this.host+" "+this.name;
    }
}
