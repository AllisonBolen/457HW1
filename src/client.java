//package proj1;

import java.io.Console;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Scanner;

// ArrayList.add(int index, E elemen)
// It can currently read up to 5 packets.
public class client {
    // we need to store the data how an array of byte arrays of the packets we get on server side
    //
    public static void main(String args[]) {
        try {

            //create a scanner for user input
            Scanner scan = new Scanner(System.in);
            //get the server address
            Selector s = Selector.open();
            // String addr = ipAddress(scan);

            String addr = "127.0.0.1";
            //get the port to use
            //int port = Integer.parseInt(portSelection(scan));

            int port = 9999;

            DatagramChannel sc = DatagramChannel.open();
            //Selector s = Selector.open();
            // is there data available at this instance? if then return if not go past the line // block forever or not but the selector lets us block for a certain amount of time
            sc.configureBlocking(false);
            // ??????
            sc.register(s, SelectionKey.OP_READ);
            Console cons = System.console();
            //store the filename the user requests and save it as m
            String m = getFileName(cons);
            int wantedPacket = 0;
            int packetNumber = 0;
            //set the socket address to the server
            InetSocketAddress serverAddr = new InetSocketAddress(addr, port);
            //This is the while for Sending the File name w/ timeout resend
            while (true) {
                ByteBuffer buff = ByteBuffer.allocate(1026);
                buff.putChar('F');
                byte[] fileByte = m.getBytes();
                buff.put(fileByte);
                buff.flip();
                //send the buffer over the socket
                sc.send(buff, serverAddr);
                int n = s.select(5000);
                if (n == 0) {
                    // didnt get any packets
                    System.out.println("Got a timeout");
                } else { //else we got a reponse from the server.
                    Iterator i = s.selectedKeys().iterator();
                    while (i.hasNext()) {
                        SelectionKey k = (SelectionKey) i.next();
                        i.remove();
                    }
                    //The packetnumber the server sends back.
                    ByteBuffer getA = ByteBuffer.allocate(1028);
                    sc.receive(getA);
                    packetNumber = getA.getInt();
                    if (packetNumber == -6) {
                        System.out.println("The file does not exist.");
                        sc.close();
                        return;//maybe system.exit.
                    }
                    //File exists, break out of this loop to go to next loop
                    //to send acknowledgemnt.
                    else if (packetNumber > 0) {
                        break;

                    }
                }
            }
            //This is for sending the acknowledgment that client received the packet number w/ resend
            while (true) {
                ByteBuffer ackBuffer = ByteBuffer.allocate(1028);
                //Need to implement for server: if server gets A again, resend all data.
                ackBuffer.putChar('A');
                ackBuffer.flip();
                sc.send(ackBuffer, serverAddr);
                int n = s.select(5000);
                if (n == 0) {
                    // didnt get any packets
                    System.out.println("Got a timeout");
                } else { //else we got data from server.
                    break;
                }
            }
            //Set blocking true so that we hang at receives. and send back acknowledgements
            sc.configureBlocking(true);
            FileOutputStream fos = new FileOutputStream("dl-" + m);
            while (wantedPacket != packetNumber) {
                //our recive buffer created every loop because unless its the last packet we keep going
                ByteBuffer receivedPackets = ByteBuffer.allocate(1028);
                sc.receive(receivedPackets);
                receivedPackets.flip();
                //the packet number fo rhte packet jsut recived form teh server
                int packetNum = receivedPackets.getInt();
                // first if for case 1: the packet we got from teh server is teh packet we want form teh server ie: hte lowest in our window
                if (packetNum == wantedPacket) {
                    byte[] data = new byte[receivedPackets.remaining()];
                    receivedPackets.get(data);
                    // data has our bytes
                    // write to file
                    wantedPacket++;
                    fos.write(data);
                    // send ack for the packet we just got and wrote to file
                    ByteBuffer sendingPacket = ByteBuffer.allocate(1024);
                    sendingPacket.putChar('B');
                    sendingPacket.putInt(packetNum);
                    sendingPacket.flip();
                    sc.send(sendingPacket, serverAddr);
                    // check our window
                    checkWindow(wantedPacket);
                }
                if (packetNum < wantedPacket || packetNum > (wantedPacket + 5)) {
                    // if the packet is not in the window we are looking for
                    // send the packet to the server taht we got this already if its less than the wanted or more then the window +5
                    ByteBuffer sendingPacket = ByteBuffer.allocate(1024);
                    sendingPacket.putChar('B');
                    sendingPacket.putInt(packetNum);
                    sendingPacket.flip();
                    sc.send(sendingPacket, serverAddr);
                }
                if (packetNum > wantedPacket && packetNum < (wantedPacket + 5)) {
                    //if the packet is in our window but not what we need right now
                    save data to the array in the proper spot
                    // send out the ack for the array
                    ByteBuffer sendingPacket = ByteBuffer.allocate(1024);
                    sendingPacket.putChar('B');
                    sendingPacket.putInt(packetNum);
                    sendingPacket.flip();
                    sc.send(sendingPacket, serverAddr);
                }
                if (packetNum == packetNumber) {
                    save the data
                    //send an ack to the server
                    ByteBuffer sendingPacket = ByteBuffer.allocate(1024);
                    sendingPacket.putChar('B');
                    sendingPacket.putInt(packetNum);
                    sendingPacket.flip();
                    sc.send(sendingPacket, serverAddr);
                    sc.close();
                    System.exit(0);
                }

            }


        } catch (IOException e) {
            System.out.println("error " + e);
        }
    }

    public static String portSelection(Scanner scan) {
        System.out.println("Enter a port to connect to:");
        String info = scan.next();
        return info;
    }

    public static String ipAddress(Scanner scan) {
        System.out.println("Enter an IP address to connect to:");
        String info = scan.next();
        return info;
    }

    public static String getFileName(Console cons) {
        String info = cons.readLine("Enter a file name: ");
        return info;
    }

    public static int checkWindow(int wantedPacket) {
        put stuff here
    }

    public static void shiftArray(Params) {
        put stuff here
    }
}

