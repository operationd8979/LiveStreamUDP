/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chipchip.livestreamudp.Server.model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author operationd
 */
public class ResponseListGroupLive implements Serializable{
    public final List<StreamGroup> listG;
    public final long timeGet;
    
    public ResponseListGroupLive(List<StreamGroup> listG){
        this.listG = listG;
        this.timeGet = System.currentTimeMillis();
    }

}
