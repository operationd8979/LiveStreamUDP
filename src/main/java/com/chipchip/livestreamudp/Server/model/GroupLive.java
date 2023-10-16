/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chipchip.livestreamudp.Server.model;

import java.io.Serializable;

/**
 *
 * @author Admin
 */
public class GroupLive implements Serializable{
    
    private final Client host;
    private final String name;
    private final int size;
    private byte[] imageData;
//    private BufferedImage currentImage = null;
    
    public GroupLive(Client host,String name,int size,byte[] imageData){
        this.host = host;
        this.name = name;
        this.size = size;
        this.imageData = imageData;
    }
    
    public Client getHost() {
        return this.host;
    }
    
    public int getSize(){
        return this.size;
    }
    
    
    public String getName() {
        return this.name;
    }
    
    public byte[] getImageData() {
        return this.imageData;
    }
    
//    public BufferedImage getCurrentImage(){
//        return this.currentImage;
//    }
    
    @Override
    public String toString() {
        return this.host+" "+this.name;
    }
}