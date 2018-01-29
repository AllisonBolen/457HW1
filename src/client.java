import java.io.Console;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;

public class client {
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
            ByteBuffer buff = ByteBuffer.wrap(m.getBytes());
            //send the buffer over the socket
            sc.send(buff, new InetSocketAddress(addr,port));



            //prepare to receive data from the server by allocating a buffer
            ByteBuffer buffer = ByteBuffer.allocate(1028);
            //receive data from server and store it in buffer
            sc.receive(buffer);
            //flip the buffer to restrict the buffer to the content
            buffer.flip();
            //create a byte array to store the content from the byte buffer
            byte[] msgBuffer = new byte[buffer.remaining()];
            //get the data from the buffer to msgBuffer
            buffer.get(msgBuffer);
            //ourput the data from msgBuffer
            String msgg = new String(msgBuffer);
            //byte[] bytesReceived = new byte[buffer.remaining()];
            //buffer.get(bytesReceived);
            //String message = new String(bytesReceived);
            System.out.println(msgg);

            sc.close();
        }catch(IOException e){
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
}

