import java.io.*;

public class Main {
    // main method
    public static void main(String[] args) throws IOException {
        // if the first argument is not -h and the improper number of arguments is passed in,
        // print an error statement that says wrong # of arguments, run with -h for more info
        if (!args[0].equals("-h") && args.length != 2 && args.length != 3) {
            System.err.println("Wrong number of arguments. " +
                    "Run with '-h' for usage info.");
            System.exit(1);
            // otherwise if the first argument is -h
            // print information about how to run TRIEST-BASE and TRIEST-IMPR algorithms
        } else if (args[0].equals("-h")) {
            System.out.println("Usage: java Main [-bhi] samplesize inputfile");
            System.out.println("\t-b: run the -BASE version of TRIEST (default)");
            System.out.println("\t-h: print this help message and exit");
            System.out.println("\t-i: print the -IMPR version of TRIEST");
            System.out.println("Only one of '-b', '-h', and '-i' can be specified.");
            System.exit(0);
        }
        boolean impr = false; // by default, we run the -BASE version.
        // if the correct number of arguments is passed
        if (args.length == 3) {
            // if -b or -i is not specified as the first argument, print an error message
            // asking the user to run with -h for more user info
            if (args[0].length() != 2 || args[0].charAt(0) != '-') {
                System.err.println("Invalid argument '" + args[0] +
                        "'. Run with '-h' for usage info.");
                System.exit(1);
            }
            char flag = args[0].charAt(1); // create case
            switch (flag) {
                // if -b then we want to run the -BASE version
                case 'b':
                    impr = false;
                    break;
                // if -i then we want to run the -IMPR version
                case 'i':
                    impr = true;
                    break;
                // if neither -i nor -b, print error message asking user to run with -h
                default:
                    System.err.println("Invalid argument '" + args[0] +
                            "'. Run with '-h' for usage info.");
            }
        }
        // Read the sample size argument.
        int sampleSize = 0;
        // try to read sample size
        try {
            sampleSize = Integer.parseInt(args[args.length - 2]);
            // if sample size is negative, throw an exception and print error message
            if (sampleSize <= 0)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.err.println("The 'samplesize' argument must be a positive integer.");
            System.exit(1);
        }

            // Create a reader for the input file.
            BufferedReader br = null;
            // try to read the input file
            try {
                br = new BufferedReader(new FileReader(new File(args[args.length - 1])));
                // if input file cannot be found, throw an exception and print error message
            } catch (IOException e) {
                System.err.println("File '" + args[args.length - 1] +
                        "' not found or not readable.");
                System.exit(1);
            }


            // create an instance of DataStreamAlgo called algo, and set to either impr or base variation
            DataStreamAlgo algo;
            if (impr)
                algo = new TriestImpr(sampleSize);
            else
                algo = new TriestBase(sampleSize);

            // Iterate over the lines of the input file: read the edge on the line,
            // pass the edge to the algorithm to handle it, and print the new
            // estimation of the number of triangles.

            try {
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    algo.handleEdge(new Edge(line.trim()));
                    System.out.println(algo.getEstimate());
                }
            } catch (IOException io) {
                System.err.println("Error reading the file:" + io);
                System.exit(1);
            }
        }


    }


