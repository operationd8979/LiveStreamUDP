package com.chipchip.livestreamudp;

import com.chipchip.livestreamudp.Client.ClientFrm;

public class Run {
    public static void main(String[] args){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                new ClientFrm().setVisible(true);
            }
        };
        new Thread(runnable).start();
    }
}
