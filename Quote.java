    




    import java.net.*;
    import java.io.*;
    import java.util.Scanner;
    import java.util.Random;

    public class Quote{
    	private File f;
    	private Random r;
    	private int numberOfLines = 0;
    	private int numberOfQuotes = 0;
    	private int quoteCount = 0;
    	private int[] quotesGiven;

    	public Quote(File f, int numberOfQuotes){
    		this.f = f;
    		this.numberOfQuotes = numberOfQuotes;
    		this.quotesGiven = new int[numberOfQuotes];
            this.r = new Random();
    		countLines();
    	}

        public Quote(){

        }

        /**
        *
        * @return int rand 
        */
        public int getRandomLine(File f, int numberOfQuotes){
            int rand;
            boolean found = false;
            Random r = new Random();
            int numberOfLines = countLines(f);

            while(!found){
                rand = r.getNextInt(numberOfLines);
                found = true;

                for(int i = 0; i < quoteCount; i++){       // Checks to see if this quote was already given
                    if(rand == quotesGiven[i])
                        found = false;                      // If there is a match, try again
                }
            }

            return rand;
        }

    	/**
    	*
    	* @return int rand 
    	*/
    	public int getRandomLine(){
    		int rand;
    		boolean found = false;

    		while(!found){
	    		rand = r.getNextInt(numberOfLines);
				found = true;

	    		for(int i = 0; i < quoteCount; i++){		// Checks to see if this quote was already given
	    			if(rand == quotesGiven[i])
	    				found = false;						// If there is a match, try again
	    		}
	    	}

    		return rand;
    	}

    	/**
    	*
    	* @return quote 
    	*/
    	public String getQuote(int n){
    		String quote = "error";
    		
    		Scanner in = new Scanner(this.f);

    		for(int i = 0; i < n - 1; i++){
    			in.nextLine();
    		}

    		quote = in.nextLine();

    		quoteCount++;
    		quotesGiven[quoteCount] = n;

    		return quote;
    	}

    	/**
    	*	Counts the number of lines in the file. 
    	*/
    	private void countLines(){
    		int count = 0;
    		Scanner in = new Scanner(this.f);

    		for(int i = 0; in.hasNextLine(); i++)
    			count++;

    		this.numberOfLines = count;
    	}

        /**
        *   Counts the number of lines in the file. 
        */
        private int countLines(File f){
            int count = 0;
            Scanner in = new Scanner(this.f);

            for(int i = 0; in.hasNextLine(); i++)
                count++;

            return count;
        }

    }