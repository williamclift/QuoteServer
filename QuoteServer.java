/** ----------------------------------- 
	QuoteServer.java

	@author William Clift
			Operating Systems
			25 March 2020

	Compile and Run Instructions:
			
		Compile:	javac QuoteServer.java QuoteClient.java
			
		Run:		 java QuoteServer quotes.txt

    ----------------------------------- **/

    import java.net.*;
    import java.io.*;
    import java.util.Scanner;
    import java.util.Random;
    import java.util.ArrayList;

    public class QuoteServer{

        private File f;
        private ArrayList<String> quotesFromFile = new ArrayList<String>();
        private ArrayList<String> typeKey = new ArrayList<String>();
        private Random r;

    	public static void main(String[] args) throws FileNotFoundException{
    		try{
                File f = new File(args[0]);
    			ServerSocket sock = new ServerSocket(6013);
                QuoteServer q = new QuoteServer(f);

    			//	Listen for Connections
                boolean requestFinish = false;
    			while(true && !requestFinish){
    				Socket client = sock.accept();
    				//Quote q = new Quote(f);

    				PrintWriter pout = new PrintWriter(client.getOutputStream(), true);
                    InputStream in = client.getInputStream();
                    BufferedReader bin = new BufferedReader(new InputStreamReader(in));

                    //  Read from Client

                    String check = bin.readLine();

                    if(check.equals("x")){
                        requestFinish = true;
                    }else{

                        int sizeOfArgs = Integer.parseInt(bin.readLine());

                        if(sizeOfArgs == 1){
                            String line;
                            boolean done = false;
                            while(!done && (line = bin.readLine()) != null){
                                if(line.equals("x")){
                                    done = true;
                                }else{
                                    String[] quotes = q.getQuotes(Integer.parseInt(line));
                                    for(int i = 0; i<quotes.length; i++){
                                        pout.println(quotes[i]);
                                    }
                                }
                                pout.println();
                            }
                        }else if(sizeOfArgs % 2 == 0){
                            String line;
                            boolean done = false;
                            while(!done && (line = bin.readLine()) != null){
                                if(line.equals("x")){
                                    done = true;
                                }else{
                                    char type = getType(bin.readLine());
                                    if(type != 'N'){
                                        String[] quotes = q.getQuotes(Integer.parseInt(line), type);
                                        for(int i = 0; i< quotes.length; i++){
                                            pout.println(quotes[i]);
                                        }
                                    }else{
                                        pout.println("* \"Invalid Type\" - The QuoteServer");
                                    }
                                }
                                pout.println();
                            }
                        }else{
                            pout.println("Invalid Arguments.");
                        }

                        pout.println("x");      // End Message
                    }

                    //  Close Socket
                    pout.close();
                    in.close();
                    client.close();
    			}

    		}
    		catch(IOException ioe){
    			System.err.println(ioe);
    		}
    	}

        /**
        *
        * @return t -- the character key to the string representation of quote type
        */
        public static char getType(String type){
            char t = 'N';

            switch(type){
                case "Happy":           t = 'h'; break;
                case "Reflective":      t = 'r'; break; 
                case "Nerdy_Computer":  t = 'c'; break;
                case "Nerdy_Math":      t = 'm'; break;
                case "By_William":      t = 'w'; break;
                default:                t = 'N';
            }
                
            return t;
        }

        /**
        *   Constructor for the QuoteServer Class.
        *   Reads the file and creates an arraylist of keys to where each type starts
        * @param f the File of quotes considered for analysis
        */
        public QuoteServer(File f) throws FileNotFoundException{
            this.f = f;
            this.r = new Random();

            Scanner scan = new Scanner(f);
            String line;
            int i=0;    
            while(scan.hasNextLine()){
                line = scan.nextLine();

                // Stores the type and key as successive chars "[type][key]"
                // [key] is the line where the type starts
                if(typeKey.size()==0){
                    typeKey.add(line.charAt(0) + String.valueOf(i));
                }else if(typeKey.get(typeKey.size()-1).charAt(0)!=line.charAt(0)){
                    typeKey.add(line.charAt(0) + String.valueOf(i));                    
                }

                this.quotesFromFile.add(line);
                i++;
            }
        }


        /**
        *   Gets the number of quotes of a particular type and returns as a String array
        * @param number the number of quotes requested
        * @param type the type of quotes
        * @return quotes
        */
        public String[] getQuotes(int number, char type){
            String[] quotes = new String[number];
            ArrayList<String> quotesOfType = new ArrayList<String>();

            int key = 0;
            boolean found = false;
            for(int i = 0; i<typeKey.size() && !found; i++){
                String tk = typeKey.get(i);
                if(type == tk.charAt(0)){
                    key = Integer.parseInt(String.valueOf(tk.substring(1, tk.length())));
                    found = true;
                }
            }

            String line = quotesFromFile.get(key);
            for(int i = key+1; line.charAt(0) == type && i < quotesFromFile.size(); i++){
                quotesOfType.add(line);
                line = quotesFromFile.get(i);
            }
            
            quotes = collectQuotes(number, quotesOfType);                              

            return quotes;
        }

        /**
        * Gets the number of quote of any type and returns as a String array
        * @param number the number of quotes of any type
        * @return a string array of quotes
        */
        public String[] getQuotes(int number){
            return collectQuotes(number, this.quotesFromFile);
        }

        /**
        *   Gets the number of quotes and returns as a String array
        * @param number the number of quotes requested
        * @param list the arraylist of potential quotes
        * @return quotes
        */
        public String[] collectQuotes(int number, ArrayList<String> list){
            String[] quotes = new String[number];
            ArrayList<String> quotesOfType = list;

            // Pick a random index of the arraylist and select that quote
            // Removes the quote and returns it as an element of quotes[]
            for(int i = 0; i < quotes.length; i++){
                if(0 < quotesOfType.size()){
                    String quote = quotesOfType.remove(r.nextInt(quotesOfType.size()));
                    quotes[i] = quote.substring(1, quote.length());  
                }
                else{
                    quotes[i] = "* \"No remaining quotes\"- The QuoteServer";           
                }
            }                                   


            return quotes;
        }
        
    }