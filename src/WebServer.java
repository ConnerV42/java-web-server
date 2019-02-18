import java.io.* ;
import java.net.* ;
import java.util.* ;

public final class WebServer
{
	public static void main(String argv[]) throws Exception
	{
        System.out.println("Conner's Web Server has started...");
		int port = 6789;
		
		ServerSocket listenSocket = new ServerSocket(port);
		while (true)
		{
			//Listen for a TCP connection request
			Socket connectionSocket = listenSocket.accept();
		
	 		//Construct an object to process the HTTP request message.
        		HttpRequest request = new HttpRequest(connectionSocket);
        		// Create a new thread to process the request.
        		Thread thread = new Thread(request);
        		// Start the thread.
        		thread.start();
		}
	}
}
