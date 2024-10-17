import java.net.*;
import java.io.*;
import java.util.*;

/**
 *
 *
 */
class HttpServerSession extends Thread{
  private Socket s;
  private BufferedReader reader;
  public HttpServerSession(Socket s){
    this.s = s;
  }
  /**
   * [println description]
   * @param  bos[buffered output stream]
   * @param  s   [String to print]
   * @return
   */
  private boolean println(BufferedOutputStream bos, String s ){
    String news = s +"\r\n";
    byte[] array = news.getBytes();
    try{

      bos.write(array,0,array.length);
      bos.flush();
    }catch(IOException e){
      return false;
    }
    return true;
  }


  /**
   * [run description]
   */
  public void run(){
    try{
      //create buffered output and Reader
      BufferedOutputStream bos = new BufferedOutputStream(s.getOutputStream());
      BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
      //read the r989equest
      HttpServerRequest serverRequest = new HttpServerRequest();
      String request= reader.readLine();
        //process the request line by line
        while(serverRequest.isDone() != true){//!(request == null || request.equals(""))){
        //print the line of a request
        System.out.println(request);
        //proces request
        serverRequest.process(request);
        request = reader.readLine();
      }
      //if that works
      System.out.println(serverRequest.isDone());
      System.out.println(serverRequest.getHost());
      System.out.println(serverRequest.getFile());
      // get the host and file
      String host = serverRequest.getHost();
      String file = serverRequest.getFile();

      if(host.equals(null)){
        host = "localhost:55555";
      }
      //get the file path
      String filePath = (host+"/"+file);
      System.out.println(filePath);
      //byte buffer
      byte[] buf = new byte[1024];

      String Response="HTTP/1.1 200 OK";

      try{
        FileInputStream inputStream = new FileInputStream(filePath);
        int rc = inputStream.read(buf);
      	println(bos,Response);
    	 println(bos,"");
        //while not the end of file
        while(rc != -1){
          //write
          bos.write(buf,0,rc);
          this.sleep(500);
          //get next line of bytes
          rc =inputStream.read(buf);

        }
        //flush output stream
	 bos.flush();

      }catch(FileNotFoundException e){

	Response = "HTTP/1.1 404 file not found";
	println(bos,Response);
	println(bos,"");

      }
      s.close();
    }catch(Exception e){
      System.err.println("Exception: " + e);
    }
  }
}



/**
 *
 *
 */
class HttpServer{

  public static void main(String agrs[]){
    //Denote the program has started
    System.out.println("web server starting");
    try{
      // craete server Socket
      ServerSocket ss = new ServerSocket(55555);
      //Listen for a connection
      while(true){
        //accept connection and create a thread
        Socket s = ss.accept();
        HttpServerSession sesh = new HttpServerSession(s);
        //start the thread
        sesh.start();
        System.out.println("accepeted");
      }
    }catch(Exception e){
      System.err.println(e);
    }
  }
}
