/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chipchip.livestreamudp.Server.model;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author operationd
 */
public class StreamGroup {
    
    private final Client host;
    private final String name;
    private List<Client> viewers;
    private BufferedImage currentImage = null;
    public OutputStream osHost = null;
    
    public StreamGroup(Client host,String name, OutputStream os){
        this.host = host;
        this.name = name;
        viewers = new ArrayList<>();
        osHost = os;
    }
    
    public Client getHost() {
        return this.host;
    }
    
    public List<Client> getViewers(){
        return this.viewers;
    }
    
    public void addViewer(Client client){
        this.viewers.add(client);
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
