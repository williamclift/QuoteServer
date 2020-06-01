/** -----------------------------------------------------------------------
	QuoteClient.java

	@author William Clift
			Operating Systems
			25 March 2020

	Compile and Run Instructions:

	-------------------------------------------------------------------
		By Assignment
	-------------------------------------------------------------------
			
			Compile:	javac QuoteClient.java
		
			Run:		java Quote Client [numberOfQuotes]

					Prints quotes of a random type

	-------------------------------------------------------------------
		Categories Option -- (Extra Credit Portion)
	-------------------------------------------------------------------

			Compile:	javac QuoteClient.java

			Run: 		java Quote Client [numberOfQuotes_0] [typeOfQuote_0] [number_1] [type_1] ... [number_n] [type_n]

					//Type Choices: "Happy", "Reflective", "Nerdy_Computer", "Nerdy_Math", "By_William" 

	-------------------------------------------------------------------
	Close Server (Kill Server Command):

			Run:		java QuoteClient x

    ------------------------------------------------------------------- **/

	import java.net.*;
    import java.io.*;

	public class QuoteClient{
	    public static void main(String[] args){
	   		try{	
	    		//	Make Connection to Server Socket
	    		Socket sock = new Socket("localhost", 6013);
	    		InputStream in = sock.getInputStream();
	    		OutputStream out = sock.getOutputStream();

	    		PrintWriter pout = new PrintWriter(out, true);
	    		BufferedReader bin = new BufferedReader(new InputStreamReader(in));

	    		if(args.length == 0){
	    			System.out.println("Invalid Arguments.");
	    		}else if(args[0].equals("x")){			// Indicates the server should shutdown
	    			pout.println(args[0]);
	    		}else{
	    			// Send Request to Server
	    			pout.println("c");
		    		pout.println(args.length); 		// Sends the Size of args
		    		for(String arg : args)
		    			pout.println(arg);

		    		pout.println("x");				// Signal that the list of arguments is over

		    		// Read from Socket	
		    		String line;
		    		boolean done = false;
		    		System.out.println();			// Add space before the quotes
		    		while((line = bin.readLine()) != null && !done){
		    			if(line.equals("x")){		// When the quotes are returned, terminate connection
		    				done = true;
		    			} else{
		    				System.out.println(line);
		    			}
	    			}
	    		}

	    		//	Close Socket Connection	
	    		pout.close();
	    		sock.close();
	    		in.close();

	    	}
	    	catch(IOException ioe){
	    		System.err.println(ioe);
	    	}

	   	}
	    	
	}	   	