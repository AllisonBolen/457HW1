import java.io.Console;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;

public class client {
    public static void main (String args[]){
        try{
            Scanner scan = new Scanner(System.in);
            String addr = ipAddress(scan);
            int port = Integer.parseInt(portSelection(scan));
            DatagramChannel sc = DatagramChannel.open();
            Console cons = System.console();
            String m = cons.readLine("enter your message: ");
            ByteBuffer buff = ByteBuffer.wrap(m.getBytes());
            sc.send(buff, new InetSocketAddress(addr,port));

            //my code is here get info back from server
            ByteBuffer buffer = ByteBuffer.wrap(m.getBytes());
            sc.receive(buffer);
            String message = new String(buffer.array()); // convert byte buffer into a string just a new way to do it old can still work old way is better tho// only good for fresh buffers ^
            System.out.println(message);

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

