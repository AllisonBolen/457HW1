//package proj1;

import java.io.Console;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.Pattern;
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
            String addr = ipAddress(scan);
            //String addr = "127.0.0.1";
            //get the port to use
            int port = Integer.parseInt(portSelection(scan));
            //int port = 9999;
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
            //System.out.println(packetNumber);

            if(packetNumber == -6){
                System.out.println("The file does not exist.");
                sc.close();
            }
            else if(packetNumber > 0){
                ByteBuffer ackBuffer = ByteBuffer.allocate(1028);
                ackBuffer.putChar('A');
                ackBuffer.flip();
                //int ackTest = ackBuffer.getInt();
                //System.out.println("Acknowledgment test: " + ackTest);
                //ackBuffer.rewind();
                sc.send(ackBuffer, serverAddr);

                FileOutputStream fos = new FileOutputStream("dl"+m);
                int counterMin = 0;
                int counterMax = 0;//4
                int streak = 0;
                int serverMax = 0;
                while(streak != packetNumber){

                    if (counterMin == counterMax){
                        for(int i = 5; i > 0; i--){
                            //System.out.println(i);
                            if (counterMax + i <= packetNumber){
                                counterMax = counterMax + i;
                                break;
                            }
                        }
                    }

                    for(int i = counterMin; i < counterMax; i++){
                        //System.out.println("For Loop Packet NUMBER" + i);
                        ByteBuffer secondBuffer = ByteBuffer.allocate(1028);
                        sc.receive(secondBuffer);
                        // System.out.println("????????");
                        secondBuffer.flip();
                        int b = secondBuffer.getInt() -1;
                        if (b == i){
                            byte[] secondByte = new byte[secondBuffer.remaining()];
                            secondBuffer.get(secondByte);
                            streak++;
                            fos.write(secondByte);
                            String theSecond = new String(secondByte);
                            //System.out.println(theSecond + "?");
                        }



                        serverMax++;
                    }
                    //System.out.println("The Streak is @: " + streak);
                    ByteBuffer clientAtBuffer = ByteBuffer.allocate(1026);
                    clientAtBuffer.putChar('B');
                    clientAtBuffer.putInt(streak);
                    clientAtBuffer.putInt(serverMax);
                    clientAtBuffer.flip();
                    if(streak == packetNumber){
                        sc.close();
                        break;
                    }
                    counterMin = streak;
                    //stopper += streak;
                    sc.send(clientAtBuffer, serverAddr);
                }
                fos.close();

            }
            else{
                System.out.println("Invalid Packet Number Response");
            }

            sc.close();
        }catch(IOException e){
            System.out.println("error " + e);
        }
    }

    public static String portSelection(Scanner scan) {
        System.out.println("Enter a port to connect to:");
        String info = scan.next();

//        Pattern port = Pattern.compile("([01]?\\\\d\\\\d?|2[0-4]\\\\d|25[0-5])$");
//        Matcher match = port.matcher(info);
//        while(!match.matches()) {
//            System.out.println("Enter a valid port please: ");
//            info = scan.next();
//            match = port.matcher(info);
//        }
        return info;
    }

    public static String ipAddress(Scanner scan) {
        System.out.println("Enter an IP address to connect to:");
        String info = scan.next();
//        Pattern ip = Pattern.compile("([01]?\\\\d\\\\d?|2[0-4]\\\\d|25[0-5])$");
//        Matcher match = ip.matcher(info);
        return info;
    }

    public static String getFileName(Console cons) {
        String info = cons.readLine("Enter a file name: ");
        return info;
    }
}

