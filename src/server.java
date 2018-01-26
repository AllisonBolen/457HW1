import java.io.File;
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

public class server {
    public static void main(String args[]) {
        try {
            // opens a channel
            Scanner scan = new Scanner(System.in);


            int port = Integer.parseInt(getPort(scan));
            DatagramChannel c = DatagramChannel.open();
            Selector s = Selector.open(); // think of as a set of channels - along with an associated operation, int eh set for reading or writing? meant help check multiple channels at a time.
            c.configureBlocking(false); // is there data available at this instance? if then return if not go past the line
            // block forever or not but the selector lets us block for a certain amount of time
            c.register(s, SelectionKey.OP_READ);

            // lsiten on a port
            c.bind(new InetSocketAddress(port));
            while (true) {
                int n = s.select(5000);//check if htere is data to read
                if (n == 0) {
                    // didnt get any packets
                    System.out.println("Got a timeout");
                } else {
                    Iterator i = s.selectedKeys().iterator();
                    while (i.hasNext()) {
                        SelectionKey k = (SelectionKey) i.next();
                        DatagramChannel myc = (DatagramChannel) k.channel();

                        ByteBuffer buffer = ByteBuffer.allocate(4096);
                        //takes the info received form teh buffer and returns the data
                        SocketAddress clientaddr = c.receive(buffer);
                        String message = new String(buffer.array()); // convert byte buffer into a string just a new way to do it old can still work old way is better tho// only good for fresh buffers ^
                        System.out.println(message);

                        // me here
                        ByteBuffer buf = ByteBuffer.wrap(message.getBytes());
                        myc.send(buf, clientaddr);

                        // me here ^

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

    public static String filePresent(String fileName) {
        File myFile = new File(fileName);

        if (myFile.exists() && !myFile.isDirectory()) {

            String out = "";
            try {
                Scanner fromFile = new Scanner(new FileReader(fileName));
                StringBuilder builder = new StringBuilder();
                while (fromFile.hasNextLine()) {
                    builder.append(fromFile.nextLine());
                }
                fromFile.close();
                out = builder.toString();
            } catch (IOException e) {
                System.out.println("Got an IO Exception: " + e);
            }
            return out;
        }

        return "File not found: " + fileName;
    }
}
