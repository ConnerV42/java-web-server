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

        System.out.println("HTTP Request Message:\n" + requestLine);

        // Get and display header lines
        String headerLine = null;
        while((headerLine = bufferedReader.readLine()).length() != 0) {
            System.out.println(headerLine);
        }

        StringTokenizer tokens = new StringTokenizer(requestLine);
        tokens.nextToken();
        String fileName = "." + tokens.nextToken();

        // Open the file requested from the http GET request
        FileInputStream fileInputStream = null;
        boolean fileExists = true;
        try {
            fileInputStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            fileExists = false;
        }

        // Response message
        String statusLine = null;
        String contentTypeLine = null;
        String entityBody = null;

        if(fileExists) {
            statusLine = "HTTP/1.0 200 OK" + CRLF;
            contentTypeLine = "Content-type: " + contentType(fileName) + CRLF;
        } else {
            statusLine = "HTTP/1.0 404 Not Found" + CRLF;
            contentTypeLine = "Content-type: text/html" + CRLF;
            entityBody = "<HTML>" + 
                "<HEAD><TITLE>Not Found</TITLE></HEAD>" +
                "<BODY>Not Found</BODY></HTML>";
        }

        outputStream.writeBytes(statusLine);
        outputStream.writeBytes(contentTypeLine);
        outputStream.writeBytes(CRLF);

        if (fileExists) {
            sendBytes(fileInputStream, outputStream);
            fileInputStream.close();
        } else {
            outputStream.writeBytes(entityBody);
        }



        outputStream.close();
        bufferedReader.close();
        socket.close();
    }

    private static void sendBytes(FileInputStream fileInputStream, OutputStream outputStream) throws Exception {
        // 1024 byte buffer holds bytes on their way to the socket
        byte[] buffer = new byte[1024];
        int bytes = 0;

        while((bytes = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytes);
        }
    }

    private static String contentType(String fileName) {
        if(fileName.endsWith(".htm") || fileName.endsWith(".html")) {
            return "text/html";
        }
        if(fileName.endsWith(".gif")) {
            return "image/gif";
        }
        if(fileName.endsWith(".jpeg") || fileName.endsWith(".jpg"))  {
            return "image/jpeg";
        }
        return "application/octet-stream";
    }
}