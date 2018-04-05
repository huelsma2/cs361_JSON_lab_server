/**
 * @author Andrew Yehle, Michael Davis
 */


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class MainDirectory {
	
	static ArrayList<Employee> dir = new ArrayList<Employee>();
	static final String _CSSfile = "style.css";
	
    // a shared area where we get the POST data and then use it in the other handler
    static String sharedResponse = "";
    static boolean gotMessageFlag = false;

    public static void main(String[] args) throws Exception {

        // set up a simple HTTP server on our local host
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // create a context to get the request to display the results
        server.createContext("/displayresults/raw", new RawTextDisplayer());
        server.createContext("/displayresults", new TableDisplayer());
        server.createContext("/displayresults/style.css", new CSSDisplayer());

        // create a context to get the request for the POST
        server.createContext("/sendresults",new PostHandler());
        server.setExecutor(null); // creates a default executor

        // get it going
        System.out.println("Starting Server...");
        server.start();
    }
    static class CSSDisplayer implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
        	t.sendResponseHeaders(200, 0);
            OutputStream output = t.getResponseBody();
            FileInputStream fs = new FileInputStream(_CSSfile);
            final byte[] buffer = new byte[0x10000];
            int count = 0;
            while ((count = fs.read(buffer)) >= 0) {
                output.write(buffer, 0, count);
            }
            output.flush();
            output.close();
            fs.close();
        }
    }

    static class RawTextDisplayer implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {

            String response = "Begin of response\n";
			// set up the header
			String echo = "";
            System.out.println(response);
			
            response += "End of response\n";
            System.out.println(response);
            // write out the response
            //t.sendResponseHeaders(200, response.length());
            t.sendResponseHeaders(200, echo.length());
            OutputStream os = t.getResponseBody();
            //os.write(response.getBytes());
            os.write(echo.getBytes());
            os.close();
        }
    }
    
    static class TableDisplayer implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {

            String response = "Begin of response\n";
			// set up the header
			String echo = "";
            System.out.println(response);
			
            dir.sort(new LexCompare());
			echo = toTable();
            response += "End of response\n";
            System.out.println(response);
            // write out the response
            //t.sendResponseHeaders(200, response.length());
            t.sendResponseHeaders(200, echo.length());
            OutputStream os = t.getResponseBody();
            //os.write(response.getBytes());
            os.write(echo.getBytes());
            os.close();
        }
        
        private String toTable()
        {
        	String html = "<html>"
        			+ "<head>"
        			+ "<link rel=\"stylesheet\" href=\"displayresults/style.css\">"
        			+ "</head>"
        			+ "<body>"
        			+ "<h2> Company Directory </h2>"
        			+ "<table>"
        			+ "<tr><th>Title</th>"
        			+"<th>First Name</th>"
        			+ "<th>Last Name</th>"
        			+ "<th>Department</th>"
        			+ "<th>Phone</th>"
        			+ "<th>Gender</th></tr>";
        	for(Employee e : dir)
        	{
        		html+= "<tr>"
        				+ "<td>" + e.getTitle() + "</td>"
        				+ "<td>" + e.get_fname() + "</td>"
        				+ "<td>" + e.get_lname() + "</td>"
        				+ "<td>" + e.getDept() + "</td>"
        				+ "<td>" + e.getPhone() + "</td>"
        				+ "<td>" + e.getGender() + "</td>"
        				+ "</tr>";
        	}
        	html+="</table></body></html>";
        	return html;
        }
        
        public class LexCompare implements Comparator<Employee>
    	{

    		@Override
    		public int compare(Employee arg0, Employee arg1) {
    			int ret = arg0.get_lname().compareTo(arg1.get_lname());
    			if (ret==0) ret = arg0.get_fname().compareTo(arg1.get_fname());
    			return ret;
    		}

    		
    	}
    }
    

    static class PostHandler implements HttpHandler {
        public void handle(HttpExchange transmission) throws IOException {

            //  shared data that is used with other handlers
            sharedResponse = "";

            // set up a stream to read the body of the request
            InputStream inputStr = transmission.getRequestBody();

            // set up a stream to write out the body of the response
            OutputStream outputStream = transmission.getResponseBody();

            // string to hold the result of reading in the request
            StringBuilder sb = new StringBuilder();

            // read the characters from the request byte by byte and build up the sharedResponse
            int nextChar = inputStr.read();
            while (nextChar > -1) {
                sb=sb.append((char)nextChar);
                nextChar=inputStr.read();
            }

            // create our response String to use in other handler
            sharedResponse = sharedResponse+sb.toString();

            // respond to the POST with ROGER
            String postResponse = "ROGER JSON RECEIVED";

            System.out.println("response: " + sharedResponse);
            String echo = "";
			Gson g = new Gson();
            try {
				if (!sharedResponse.isEmpty()) {
					switch(sharedResponse.split(" ")[0])
					{
					case "ADD":
						{ArrayList<Employee> fromJson = g.fromJson(sharedResponse.split(" ")[1],
								new TypeToken<Collection<Employee>>() {
								}.getType());
						dir.addAll(fromJson);
						echo = "Employees added:\n";
						for(Employee e : fromJson)
							echo+= e.toString() + "\n";
						System.out.println(echo);
						break;}
					case "CLEAR":
						{dir.clear();
						echo += "Directory cleared.";
						System.out.println(echo);
						break;}
					case "PRINT":
						{dir.sort(new LexCompare());
						echo = "Directory: \n";
						for(Employee e: dir) echo += e.toString() + "\n";
						System.out.println(echo);
						break;}
					default:
						System.out.println("Invalid command received...");
					}
				}
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			}
            //runCommand(sharedResponse);
            //Desktop dt = Desktop.getDesktop();
            //dt.open(new File("raceresults.html"));

            // assume that stuff works all the time
            transmission.sendResponseHeaders(300, postResponse.length());

            // write it and return it
            outputStream.write(postResponse.getBytes());

            outputStream.close();
        }
        public class LexCompare implements Comparator<Employee>
    	{

    		@Override
    		public int compare(Employee arg0, Employee arg1) {
    			int ret = arg0.get_lname().compareTo(arg1.get_lname());
    			if (ret==0) ret = arg0.get_fname().compareTo(arg1.get_fname());
    			return ret;
    		}

    		
    	}
    }
    
	@SuppressWarnings("unchecked")
	public void add(String json){
		
		Object[] list = new Gson().fromJson(json, new TypeToken<Collection<Object>>(){}.getType());
		dir.addAll((ArrayList<Employee>) list[1]);
		
	}
	
	public void print(){
		
		if(dir.size()==0)
			{
			System.out.println("<empty directory>");
			return;
			}
		dir.sort(new LexCompare());
		for(int i = 0; i<dir.size(); i++){
			
			System.out.println(dir.get(i).toString());
			
		}
		
	}
	
	public void clear(){
		
		dir.clear();
		
	}
	
	private void runCommand(String cmd)
	{
		String[] command = cmd.split(" ");
		if(cmd.equals("ADD"))
			add(command[1]);
		if(cmd.equals("PRINT"))
			print();
		if(cmd.equals("CLEAR"))
			clear();
	}
	
	public class LexCompare implements Comparator<Employee>
	{

		@Override
		public int compare(Employee arg0, Employee arg1) {
			int ret = arg0.get_lname().compareTo(arg1.get_lname());
			if (ret==0) ret = arg0.get_fname().compareTo(arg1.get_fname());
			return ret;
		}

		
	}
	
}
