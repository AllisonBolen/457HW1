//package proj1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Scanner;
import java.nio.file.Files;
import java.util.ArrayList;

//It can send an infinite amount of packets! and DNE
public class server {

    public static ArrayList<Integer> returnedArray = new ArrayList<Integer>();
    public static int minVal = 0;
    public static int startByte = 0;
    public static String fileName;
    public static File myFile;
    public static int packetNum = 1;

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
                        byte[] msgBuffer = new byte[buffer.remaining()];
                        // get the data from the buffer to the msgbuffer
                        buffer.get(msgBuffer);
                        // convert byte buffer into a string just a new way to do it old can still work old way is better tho// only good for fresh buffers ^
                        String message = new String(msgBuffer);
                        myFile = new File(message);
                        // print the message to the console
                        System.out.println("Filename: " + message);
                        // fresh buffer to return info to the client with


                        if(fileExists(message)){
                            int packetAmount = fileSize(message);
                            System.out.println("Packets needed to be sent: " + packetAmount);
                            if(packetAmount > 0){
                                sendFile(myc, clientaddr);
                            }
                        }
                        else{
                            ByteBuffer noPacket = ByteBuffer.allocate(1024);
                            noPacket.putInt(5);
                            byte[] noPacketByte = new byte[noPacket.remaining()];
                            String noPacketString = "Does Not Exist";
                            noPacketByte = noPacketString.getBytes();
                            noPacket.put(noPacketByte);
                            noPacket.flip();
                            myc.send(noPacket, clientaddr);
                        }
                        // from the file present we send that byte array to teh buffer

                        //buf2.put(filePresent(message));
                        //flip the buffer to restrict the buffer to the content, flip to read.

                        // get the test data from the buffer buff2

                        // get the data from the buffer to the msgbuffer

                        // teh data in test data

                        // changes the shrunken space to the allocated space

                        buffer.rewind();
                        //sed it to the client

                        //myc.send(buffer, clientaddr);
                        // me here ^
                        // remove the iterator

                        //reset values
                        minVal = 0;
                        startByte = 0;
                        packetNum = 1;
                        i.remove();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Got an error: " + e);
        }
    }

//	public static void sendOnePacket(String fileName, DatagramChannel firstMyc, SocketAddress firstAddr){
//
//		ByteBuffer firstBuffer = ByteBuffer.allocate(1028);
//		firstBuffer.putInt(1).put(filePresent(fileName));
//		firstBuffer.flip();
//		byte[] testData = new byte[firstBuffer.remaining()];
//		firstBuffer.get(testData);
//		String testData2 = new String(testData);
//		// print it to console
//    System.out.println(testData2);
//		firstBuffer.rewind();
//		//myc.send(buf2, clientaddr);
//		try {
//			firstMyc.send(firstBuffer, firstAddr);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

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

    public static int fileSize(String fileName){
        myFile = new File(fileName);

        if (myFile.exists() && !myFile.isDirectory()) {
            int size = (int)myFile.length();
            System.out.println("Size of file: " + size);
            double count = size/1024.0;
            int ceiledValue = (int) Math.ceil(count);
            return ceiledValue;
        }
        else
            System.out.println("The file does not exist!");
        return 0;
    }

    public static int checkMin(){
        while(returnedArray.contains(minVal)){
            minVal++;
        }
        return minVal;
    }

    public static void sendFile(DatagramChannel myC, SocketAddress cAddr){
        while (startByte < myFile.length()){
            //System.out.println("inside while: " + startByte + " " + myFile.length());
            if(startByte + 1024 < myFile.length()){
                System.out.println("inside if" + startByte + " " + myFile.length());
                try {
                    //System.out.println("Inside try");
                    ByteBuffer buf = ByteBuffer.allocate(1028);
                    FileInputStream fis = new FileInputStream(myFile);
                    //System.out.println("FISSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
                    //String s = new String(bArray);
                    //System.out.println(s);
                    //System.out.println("REAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD");
                    System.out.println("The index is at: " + buf.position() + "The limit is at: " + buf.limit());
                    buf.putInt(packetNum);
                    System.out.println("The index is at: " + buf.position() + "The limit is at: " + buf.limit());
                    byte[] bArray = new  byte[buf.remaining()];
                    //fis.read(bArray, startByte, startByte + 1024);
                    fis.skip(startByte);
                    fis.read(bArray, 0, 1024);
                    buf.put(bArray);
                    fis.close();
                    System.out.println("PUTTTTTUTUTUTUTUTUTUT");
                    buf.flip();
                    System.out.println("if After Flip");
                    myC.send(buf, cAddr);
                    packetNum++;
                    startByte += 1024;
                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println("Exception: " + e);
                }
            }
            else{
                try {
                    //System.out.println("The index is at: " + buf.position() + "The limit is at: " + buf.limit());
                    ByteBuffer buf = ByteBuffer.allocate(1028);
                    FileInputStream fis = new FileInputStream(myFile);
                    buf.putInt(packetNum);
                    byte[] bArray = new byte[buf.remaining()];//1024
                    //System.out.println("file Length:"  + myFile.length());
                    //System.out.println("array: " + bArray.length);
                    //System.out.println("startByte: " + startByte);
                    //System.out.println("things: "+ (bArray.length-startByte) + ", length: " + myFile.length());
                    fis.skip(startByte);
                    fis.read(bArray);
                    //fis.read(bArray, startByte, (int) bArray.length;// 1024 - 1620
                    System.out.println("solved");
                    buf.put(bArray);
                    fis.close();
                    buf.flip();
                    System.out.println("else after Flip");
                    myC.send(buf, cAddr);
                    packetNum++;
                    startByte += (int)myFile.length() - startByte;
                } catch (Exception e) {
                    System.out.println("Exception: " + e);
                }
            }
        }
    }



// 	public void slidingWindow(){
// 		for(minVal, minVal + 4, minVal++){
//			if(!returnedArray.contain(packetNum))
// 				sendFile(myC, clientAddr);
// 		}
//
// 	}
}
