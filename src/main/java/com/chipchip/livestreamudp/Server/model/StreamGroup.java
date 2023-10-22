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

    private Map<String,Client> mapDatagramPacket;

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

    public Map<String,Client> getMapDatagramPacket(){
        return this.mapDatagramPacket;
    }
    
    public void addViewer(Client client){
        this.viewers.add(client);
    }

    public Client addDataPacket(String clientID,Client client){
        this.mapDatagramPacket.put(clientID,client);
        return client;
    }

    public String removeDataPacket(String clientID){
        if (this.mapDatagramPacket.containsKey(clientID)) {
            this.mapDatagramPacket.remove(clientID);
            return clientID;
        } else {
            return null;
        }
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
