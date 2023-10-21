package com.chipchip.livestreamudp.Client.Model;

import com.chipchip.livestreamudp.Client.ClientFrm;
import org.ice4j.TransportAddress;
import org.ice4j.ice.*;
import org.ice4j.socket.IceSocketWrapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class StateListener implements PropertyChangeListener {

    InetAddress hostname;
    int port;
    public BufferedImage bufferedImage = null;
    public final boolean host;

    public StateListener(Boolean host){
        this.host = host;
    }

    public StateListener(Boolean host, BufferedImage bufferedImage){
        this.host = host;
        this.bufferedImage = bufferedImage;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getSource() instanceof Agent){
            Agent agent = (Agent) evt.getSource();
            if(agent.getState().equals(IceProcessingState.TERMINATED)) {
                // Your agent is connected. Terminated means ready to communicate
                for (IceMediaStream stream: agent.getStreams()) {
                    if (stream.getName().contains("audio")) {
                        Component rtpComponent = stream.getComponent(org.ice4j.ice.Component.RTP);
                        CandidatePair rtpPair = rtpComponent.getSelectedPair();
                        // We use IceSocketWrapper, but you can just use the UDP socket
                        // The advantage is that you can change the protocol from UDP to TCP easily
                        // Currently only UDP exists so you might not need to use the wrapper.
                        IceSocketWrapper wrapper  = rtpPair.getIceSocketWrapper();
                        DatagramSocket socket = wrapper.getUDPSocket();
                        // Get information about remote address for packet settings
                        TransportAddress ta = rtpPair.getRemoteCandidate().getTransportAddress();
                        hostname = ta.getAddress();
                        port = ta.getPort();

                        System.out.println("ChipChip "+hostname.toString()+" "+port);

                        DatagramSocket UDP = wrapper.getUDPSocket();

                        if(!host){
                            DatagramPacket recivePacket = new DatagramPacket(new byte[10000],10000);
                            while (true){
                                try{
                                    UDP.receive(recivePacket);
                                    bufferedImage = ImageIO.read(new ByteArrayInputStream(recivePacket.getData()));
                                }catch (IOException ex){
                                    ex.printStackTrace();
                                }
                            }
                        }
                        else{
                            DatagramPacket sendPacket = new DatagramPacket(new byte[10000],10000);
                            sendPacket.setAddress(hostname);
                            sendPacket.setPort(port);
                            while(bufferedImage==null){
                                //wait
                            }
                            try{
                                while(true){
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
                                    byte[] imageData = byteArrayOutputStream.toByteArray();
                                    sendPacket = new DatagramPacket(imageData,imageData.length,hostname,port);
                                    UDP.send(sendPacket);
                                }
                            }catch(Exception ex){
                                ex.printStackTrace();
                            }
                        }

                    }
                }
            }
        }
    }
}