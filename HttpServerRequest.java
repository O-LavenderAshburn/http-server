class HttpServerRequest
{
    private String file = null;
    private String host = null;
    private boolean done = false;
    private int line = 0;

    public boolean isDone() { return done; }
    public String getFile() { return file; }
    public String getHost() { return host; }
    
    public void process(String in)
    {	
      if(in.equals("")){
      	done =true;
      	return;
      }
      //Split the request
      String parts[] = in.split(" ");

      if (line == 0){
        if(parts[0].compareTo("GET")==0){
        
       	 if(parts[1].startsWith("/")){
       	  	
       	  
  
          file = parts[1].substring(1);
          
          if(file.endsWith("/")){
            file = file+"index.html";
          }
          if(file.equals("")){
            file = null;
          }
         }
          line++;
          return;
        }
      }else if (in.startsWith("Host: ")){
        host = parts[1];
        if(host.equals("")){
          host = null;
        }
        done = true;
        return;
      }
      
      line++;
    }
}
