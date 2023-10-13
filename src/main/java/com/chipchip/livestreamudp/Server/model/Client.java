/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chipchip.livestreamudp.Server.model;

import java.net.InetAddress;

/**
 *
 * @author Admin
 */
public class Client {
    
    private final long id;
    private final InetAddress addr;
    private final int port;
    
    public Client(long id, InetAddress addr, int port){
        this.id = id;
        this.addr = addr;
        this.port = port;
    }
    
    public long getId(){
        return this.id;
    }
    
    public InetAddress getAddr(){
        return this.addr;
    }
    
    public int getPort(){
        return this.port;
    }
    
    
}
