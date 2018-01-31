//package proj1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Scanner;
import java.util.ArrayList;

//It can send an infinite amount of packets! and DNE
public class server {

    public static ArrayList<Integer> returnedArray = new ArrayList<Integer>();
    public static int minValGlobal = 0;
    public static int startByteGlobal = 0;
    public static String fileNameGlobal;
    public static File myFileGlobal;
    public static int packetNumGlobal = 1;
    public static int packetAmountGlobal;
    public static FileInputStream fisGlobal;
    public static int counterMinGlobal = 0;
    public static int counterMaxGlobal = 0;
    public static void main(String args[]) {
        try {
            // scanner for reading in data
            Scanner scan = new Scanner(System.in);
            // port to use provided by server
            //int port = Integer.parseInt(getPort(scan));
            int port = 9999;
            // opens a channnel to communicate through
            DatagramChannel c = DatagramChannel.open();
            // think of as a set of channels - along with an associated operation, int eh set for reading or writing? meant help check multiple channels at a time.
            Selector s = Selector.open();
            // is there data available at this instance? if then return if not go past the line // block forever or not but the selector lets us block for a certain amount of time
            c.configureBlocking(false);
            // ??????
            c.register(s, SelectionKey.OP_READ);
            // lsiten on a port
            c.bind(new InetSocketAddress(port));
            // get the info as long as there is a thing open to read from
            while (true) {
                //check if there is data to read
                int n = s.select(5000);

                if (n == 0) {
                    // didnt get any packets
                    System.out.println("Got a timeout");
                } else { // got something
                    // ????????
                    Iterator i = s.selectedKeys().iterator();
                    // while the iterator had something in it?
                    while (i.hasNext()) {
                        // ????????
                        SelectionKey k = (SelectionKey) i.next();
                        // New channel to communicate with
                        DatagramChannel myc = (DatagramChannel) k.channel();
                        // allocate space for the data to be held in the buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1028);
                        //takes the info received form the buffer and returns the data
                        SocketAddress clientaddr = c.receive(buffer);
                        //flip the buffer to restrict the buffer to the content
                        buffer.flip();
                        // the bytes of the message
                        char index = buffer.getChar();
                        byte[] msgBuffer = new byte[buffer.remaining()];
                        // get the data from the buffer to the msgbuffer
                        buffer.get(msgBuffer);
                        // convert byte buffer into a string just a new way to do it old can still work old way is better tho// only good for fresh buffers ^
                        String message = new String(msgBuffer);
                        // print the message to the console        vcbvbc
                        System.out.println("Filename: " + message);
                        // fresh buffer to return info to the client with
                        if(index == 'F'){
                            // file exists
                            if(fileExists(message)){
                                myFileGlobal = new File(message);
                                try {
                                    fisGlobal = new FileInputStream(myFileGlobal);
                                } catch (Exception e) {
                                    System.out.println("Exception: " + e);
                                }
                                ByteBuffer sendPacketNumber = ByteBuffer.allocate(1028);
                                packetAmountGlobal = fileSize(myFileGlobal.length());
                                sendPacketNumber.putInt(packetAmountGlobal);
                                sendPacketNumber.flip();
                                System.out.println("Packets needed to be sent: " + packetAmountGlobal);
                                myc.send(sendPacketNumber, clientaddr);
                            }
                            // file doesnt exist
                            else{
                                ByteBuffer noFile = ByteBuffer.allocate(1028);
                                String nope = "File non existant";
                                noFile.putInt(-6);
                                byte[] noData = nope.getBytes();
                                noFile.put(noData);
                                noFile.flip();
                                myc.send(noFile, clientaddr);
                            }
                        }
                        if(index == 'A'){
                            System.out.println("Acknowledged!!!!!!!!");
                            if(packetAmountGlobal > 0){
                                sendFile(myc, clientaddr);
                            }
                        }
                        if(index == 'B'){
                            ByteBuffer clientSlideAck = ByteBuffer.allocate(1028);
                            myc.receive(clientSlideAck);
                            clientSlideAck.flip();
                            int clientGot = clientSlideAck.getInt();
                            counterMinGlobal = clientGot;
                            sendFile(myc, clientaddr);
                        }
                        buffer.rewind();
                        //reset values
                        minValGlobal = 0;
                        startByteGlobal = 0;
                        packetNumGlobal = 1;

                        i.remove();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Got an error: " + e);
        }
    }

    public static String getPort(Scanner scan) {
        System.out.println("Please enter a port to connect to");
        String info = scan.next();
        return info;
    }

    public static Boolean fileExists(String fileName){
        File myFile = new File(fileName);
        if (myFile.exists() && !myFile.isDirectory()) {
            return true;
        }
        return false;
    }

    public static int fileSize(long fileSize){
        if (myFileGlobal.exists() && !myFileGlobal.isDirectory()) {
            System.out.println("Size of file: " + fileSize);
            double count = fileSize/1024.0;
            int ceiledValue = (int) Math.ceil(count);
            return ceiledValue;
        }
        else
            System.out.println("The file does not exist!");
        return 0;
    }

    public static int checkMin(){
        while(returnedArray.contains(minValGlobal)){
            minValGlobal++;
        }
        return minValGlobal;
    }

    public static void sendFile(DatagramChannel myC, SocketAddress cAddr){
// 			if (counterMinGlobal == counterMaxGlobal){
// 				for(int i = 5; i > 0; i--){
// 				if (counterMaxGlobal + i < packetAmountGlobal){
// 					counterMaxGlobal = counterMaxGlobal + i;
// 					break;
// 				}
// 			}
// 			}

        while (startByteGlobal < myFileGlobal.length() ){
            if(startByteGlobal + 1024 < myFileGlobal.length()){
                System.out.println("inside if" + startByteGlobal + " " + myFileGlobal.length());
                try {
                    ByteBuffer buf = ByteBuffer.allocate(1028);
                    System.out.println("The index is at: " + buf.position() + "The limit is at: " + buf.limit());
                    buf.putInt(packetNumGlobal);
                    System.out.println("The index is at: " + buf.position() + "The limit is at: " + buf.limit());
                    byte[] bArray = new  byte[buf.remaining()];
                    fisGlobal.read(bArray, 0, 1024);
                    buf.put(bArray);
                    System.out.println("PUTTTTTUTUTUTUTUTUTUT");
                    buf.flip();
                    System.out.println("if After Flip");
                    myC.send(buf, cAddr);
                    packetNumGlobal++;
                    startByteGlobal += 1024;

                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println("Exception: " + e);
                }
            }
            else{
                try {
                    ByteBuffer buf = ByteBuffer.allocate(1028);
                    buf.putInt(packetNumGlobal);
                    byte[] bArray = new byte[buf.remaining()];//1024
                    fisGlobal.read(bArray, 0, 1024);
                    System.out.println("solved");
                    buf.put(bArray);
                    fisGlobal.close();
                    buf.flip();
                    System.out.println("else after Flip");
                    myC.send(buf, cAddr);
                    packetNumGlobal++;
                    startByteGlobal += (int) myFileGlobal.length() - startByteGlobal;

                } catch (Exception e) {
                    System.out.println("Exception: " + e);
                }
            }
        }
    }
}
