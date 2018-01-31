//package proj1;

import java.io.Console;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;
import java.util.*;
// ArrayList.add(int index, E elemen)
// It can currently read up to 5 packets.
public class client {
    // we need to store the data how an array of byte arrays of the packets we get on server side
    //
    public static void main (String args[]){
        try{

            //create a scanner for user input
            Scanner scan = new Scanner(System.in);
            //get the server address
//          String addr = ipAddress(scan);
            String addr = "127.0.0.1";
            //get the port to use
//             int port = Integer.parseInt(portSelection(scan));
            int port = 9999;
            //create a datagram channel to transfer data over
            DatagramChannel sc = DatagramChannel.open();
            //create a console to utilize
            Console cons = System.console();
            //store the filename the user requests and save it as m
            String m = getFileName(cons);
            //create a buffer to store the byte data from the filename
            ByteBuffer buff = ByteBuffer.allocate(1026);
            buff.putChar('F');
            byte[] fileByte = m.getBytes();
            buff.put(fileByte);
            buff.flip();
            //set the socket address to the server
            InetSocketAddress serverAddr = new InetSocketAddress(addr, port);
            //send the buffer over the socket
            sc.send(buff, serverAddr);
            // receive packets form the buffer
            // if the client gets a packet that contains the number of packets it will receive for the file transfer do this v

            ByteBuffer packetBuffer = ByteBuffer.allocate(1028);
            sc.receive(packetBuffer);
            packetBuffer.flip();
            int packetNumber = packetBuffer.getInt();
            System.out.println(packetNumber);


            if(packetNumber == -6){
                System.out.println("The file does not exist.");
                sc.close();
            }
            else if(packetNumber > 0){

//               byte[] byteReceivedArray = new byte[5];
//               byteReceivedArray[0]= a;
                //recipt generator and data allocation
                System.out.println(packetNumber);

                for(int i =0; i <= packetNumber; i++){
                    //ArrayList.add(int index, E elemen)
                }
            }
            else{
                System.out.println("Invalid Packet Number Response");
            }
            //packetBuffer.clear();
            ByteBuffer ackBuffer = ByteBuffer.allocate(1028);
            ackBuffer.putChar('A');
            ackBuffer.flip();
            //int ackTest = ackBuffer.getInt();
            //System.out.println("Acknowledgment test: " + ackTest);
            //ackBuffer.rewind();
            sc.send(ackBuffer, serverAddr);

            //FileOutputStream fos = new FileOutputStream("test1.txt");


            for(int i = 1; i <= packetNumber; i++){
                System.out.println("For Loop Packet NUMBER" + i);
                ByteBuffer secondBuffer = ByteBuffer.allocate(1028);
                sc.receive(secondBuffer);
                secondBuffer.flip();
                int b = secondBuffer.getInt();
                byte[] secondByte = new byte[secondBuffer.remaining()];
                secondBuffer.get(secondByte);
                //fos.write(secondByte);
                String theSecond = new String(secondByte);
                System.out.println(theSecond + "?");
            }
            //fos.close();

            ///------------------------------------------- 1

            // prepare to receive data from the server by allocating a buffer
//             System.out.println("START OF RECEIVING!");

//             ByteBuffer buffer = ByteBuffer.allocate(1028);
//             //receive data from server and store it in buffer
//             sc.receive(buffer);
//             //flip the buffer to restrict the buffer to the content
//             System.out.println("Buffer position: " + buffer.position() + " Buffer limit: " + buffer.limit());
//             buffer.flip();
//             System.out.println("Buffer position after flip(): " + buffer.position() + " Buffer limit after flip(): " + buffer.limit());
//             //create a byte array to store the content from the byte buffer
//             int a = buffer.getInt();
//           System.out.println();
//             System.out.println("The getInt returned FFFFFFFFFFFFFFFF "+ a);
//           System.out.println();
//             System.out.println("Buffer position after getInt(): " + buffer.position() + " Buffer limit after getInt(): " + buffer.limit());
//             byte[] msgBuffer = new byte[buffer.remaining()];
//             //get the data from the buffer to msgBuffer

//             System.out.println("Length of the msgBuffer array: "+ msgBuffer.length);

//             //buffer.rewind();
//             //System.out.println("Buffer position after rewind: " + buffer.position() + " Buffer limit after rewind: " + buffer.limit());
//             buffer.get(msgBuffer);
//             System.out.println("Buffer position after get(byte[] arr): " + buffer.position() + " Buffer limit after get(byte[] arr): " + buffer.limit());
//             //ourput the data from msgBuffer
//             String msgg = new String(msgBuffer);
//             //byte[] bytesReceived = new byte[buffer.rexmaining()];
//             //buffer.get(bytesReceived);
//             //String message = new String(bytesReceived);
//             System.out.println(msgg);
//             System.out.println("First packet done!!!!!!!!!!!!!!!!!");

//             //---------------------------------------------------------- 2
//             System.out.println("Second packet START!!!!!!!!!!!!!");
//             ByteBuffer secondBuffer = ByteBuffer.allocate(1028);
//             sc.receive(secondBuffer);
//             secondBuffer.flip();
//             int b = secondBuffer.getInt();
//           System.out.println();
//             System.out.println("The getInt returned FFFFFFFFFFFFFFFF "+ b);
//           System.out.println();
//             byte[] secondByte = new byte[secondBuffer.remaining()];
//             secondBuffer.get(secondByte);
//             String theSecond = new String(secondByte);
//             System.out.println(theSecond + "?");

//             //---------------------------------------------------------- 3
//             System.out.println("Third packet START!!!!!!!!!!!!!");
//             ByteBuffer thirdBuffer = ByteBuffer.allocate(1028);
//             sc.receive(thirdBuffer);
//             thirdBuffer.flip();
//             int c = thirdBuffer.getInt();
//           System.out.println();
//             System.out.println("The getInt returned FFFFFFFFFFFFFFFF "+ c);
//           System.out.println();
//             byte[] thirdByte = new byte[thirdBuffer.remaining()];
//             thirdBuffer.get(thirdByte);
//             String theThird = new String(thirdByte);
//             System.out.println(theThird + "?");

//             //---------------------------------------------------------- 4
//              System.out.println("four packet START!!!!!!!!!!!!!");
//             ByteBuffer fourBuffer = ByteBuffer.allocate(1028);
//             sc.receive(fourBuffer);
//             fourBuffer.flip();
//             int d = fourBuffer.getInt();
//           System.out.println();
//             System.out.println("The getInt returned FFFFFFFFFFFFFFFF "+ d);
//           System.out.println();
//             byte[] fourByte = new byte[fourBuffer.remaining()];
//             fourBuffer.get(fourByte);
//             String theFour = new String(fourByte);
//             System.out.println(theFour + "?");

//             //---------------------------------------------------------- 5
//              System.out.println("five packet START!!!!!!!!!!!!!");
//             ByteBuffer fiveBuffer = ByteBuffer.allocate(1028);
//             sc.receive(fiveBuffer);
//             fiveBuffer.flip();
//             int e = fiveBuffer.getInt();
//           System.out.println();
//             System.out.println("The getInt returned FFFFFFFFFFFFFFFF "+ e);
//           System.out.println();
//             byte[] fiveByte = new byte[fiveBuffer.remaining()];
//             fiveBuffer.get(fiveByte);
//             String theFive = new String(fiveByte);
//             System.out.println(theFive + "?");

            sc.close();
        }catch(IOException e){
            //System.out.println("error " + e);
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
}

