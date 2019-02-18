import java.io.* ;
import java.net.* ;
import java.util.* ;

public class HttpRequest implements Runnable 
{
    final static String CRLF = "\r\n";
	Socket socket;

	// Constructor
	public HttpRequest(Socket socket) throws Exception 
	{
		this.socket = socket;
    }
    
    public void run() {
        try {
            processRequest();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void processRequest() throws Exception {
        // Get a reference to the socket's input and output streams.
        InputStream inputStream = socket.getInputStream();
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

        // Set up input stream filters.
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String requestLine = bufferedReader.readLine();

        System.out.println("HTTP Request Line: " + requestLine);

        // Get and display header lines
        String headerLine = null;
        while((headerLine = bufferedReader.readLine()).length() != 0) {
            System.out.println(headerLine);
        }
        outputStream.close();
        bufferedReader.close();
        socket.close();
    }
}