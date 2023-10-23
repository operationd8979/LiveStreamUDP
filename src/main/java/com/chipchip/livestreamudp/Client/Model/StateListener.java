package com.chipchip.livestreamudp.Client.Model;

import com.chipchip.livestreamudp.Client.ClientFrm;
import com.chipchip.livestreamudp.Client.LiveStreamPTPFrm;
import org.ice4j.TransportAddress;
import org.ice4j.ice.*;
import org.ice4j.socket.IceSocketWrapper;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class StateListener implements PropertyChangeListener {

    public InetAddress hostname;
    public int port;
    public BufferedImage bufferedImage = null;
    public final boolean host;
    public int length = -1;
    public boolean running = true;



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
                    if (stream.getName().contains("chip")) {
                        Component rtpComponent = stream.getComponent(org.ice4j.ice.Component.RTP);
                        CandidatePair rtpPair = rtpComponent.getSelectedPair();
                        // We use IceSocketWrapper, but you can just use the UDP socket
                        // The advantage is that you can change the protocol from UDP to TCP easily
                        // Currently only UDP exists so you might not need to use the wrapper.
                        IceSocketWrapper wrapper  = rtpPair.getIceSocketWrapper();
                        // Get information about remote address for packet settings
                        TransportAddress ta = rtpPair.getRemoteCandidate().getTransportAddress();

                        hostname = ta.getAddress();
                        port = ta.getPort();
                        DatagramSocket UDP = wrapper.getUDPSocket();

                        System.out.println("ChipChip host:"+host+" "+hostname.toString()+" "+port);


                        if(!host){
                            DatagramPacket recivePacket = new DatagramPacket(new byte[1500],1500);
                            while (running){
                                int packetSize = 1400; // Kích thước gói tin tùy chỉnh, phải khớp với phần gửi
                                byte[] receivedData = new byte[60000]; // Đủ lớn để chứa toàn bộ hình ảnh
                                byte[] receivedSizePacket = new byte[1400];
                                DatagramPacket totalPacketsPacket = new DatagramPacket(receivedSizePacket, receivedSizePacket.length);
                                try{
                                    UDP.receive(totalPacketsPacket);
                                }catch (IOException ex){
                                    ex.printStackTrace();
                                }
                                int totalPackets = Integer.parseInt(new String(totalPacketsPacket.getData()).trim());
                                if(totalPackets>4)
                                    System.out.println(totalPackets);
                                try{
                                    for (int packetNumber = 0; packetNumber < totalPackets; packetNumber++) {
                                        DatagramPacket receivePacket = new DatagramPacket(new byte[packetSize], packetSize);
                                        UDP.receive(receivePacket);
                                        int offset = packetNumber * packetSize;
                                        int length = receivePacket.getLength();
                                        byte[] packetData = Arrays.copyOf(receivePacket.getData(), length);

                                        System.arraycopy(packetData, 0, receivedData, offset, length);
//                                        System.out.println("Received packet " + (packetNumber + 1) + " of " + totalPackets);
                                    }
                                    //
                                    //bufferedImage = ImageIO.read(new ByteArrayInputStream(recivePacket.getData()));
                                    this.bufferedImage = ImageIO.read(new ByteArrayInputStream(receivedData));
                                }catch (IIOException ex){

                                }catch (IOException ex){
                                    ex.printStackTrace();
                                }catch (Exception ex){
                                    ex.printStackTrace();
                                }
                            }
                        }
                        else{
                            DatagramPacket sendPacket = new DatagramPacket(new byte[1500],1500);
                            sendPacket.setAddress(hostname);
                            sendPacket.setPort(port);
                            while(running){
                                try{
                                    int packetSize = 1400; // Kích thước gói tin tùy chỉnh, có thể điều chỉnh
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    //
                                    ImageIO.write(bufferedImage, "jpeg", byteArrayOutputStream);

                                    byte[] imageData = byteArrayOutputStream.toByteArray();
                                    if(length == imageData.length){
                                        continue;
                                    }
                                    int totalPackets = (int)Math.ceil((double)imageData.length / packetSize);
                                    if(totalPackets>40)
                                        continue;
                                    this.length = imageData.length;


                                    byte[] totalByte = Integer.toString(totalPackets).getBytes();
                                    UDP.send(new DatagramPacket(totalByte,totalByte.length,hostname,port));
                                    System.out.println(imageData.length);

                                    for (int packetNumber = 0; packetNumber < totalPackets; packetNumber++) {
                                        int offset = packetNumber * packetSize;
                                        int length = Math.min(packetSize, imageData.length - offset);
                                        byte[] packetData = Arrays.copyOfRange(imageData, offset, offset + length);
                                        sendPacket = new DatagramPacket(packetData, packetData.length, hostname, port);
                                        UDP.send(sendPacket);
//                                        System.out.println("Sent packet " + (packetNumber + 1) + " of " + totalPackets);
                                        //System.out.println(sendPacket.getLength());
                                    }
                                }catch(IOException ex){
                                    ex.printStackTrace();
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}