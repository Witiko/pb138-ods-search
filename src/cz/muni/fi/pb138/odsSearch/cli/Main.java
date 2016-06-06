package cz.muni.fi.pb138.odsSearch.cli;

import cz.muni.fi.pb138.odsSearch.common.Cell;
import cz.muni.fi.pb138.odsSearch.common.Spreadsheet;
import cz.muni.fi.pb138.odsSearch.common.SpreadsheetImpl;
import cz.muni.fi.pb138.odsSearch.common.SpreadsheetImplException;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ResourceBundle;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * This class represents the CLI of ods-search.
 * @author Petr Kratochvila
 */
public final class Main {

    private static final Options options = new Options();
    private static final ResourceBundle bundle = ResourceBundle.getBundle("cz/"
            + "muni/fi/pb138/odsSearch/cli/Main");
    private static final String applicationName = "odsSearch";

    /**
     * Construct and provide GNU-compatible Options.
     *
     * @return Options expected from command-line of GNU form.
     */
    public static Options constructGnuOptions() {
        final Options gnuOptions = new Options();
        gnuOptions.addOption("I", "ignore-case", false, 
                             bundle.getString("caseInsensitiveDescription"))
                  .addOption("x", "exact-match", false, 
                             bundle.getString("exactMatchDescription"))
                  .addOption("s", "string", true, 
                             bundle.getString("stringDescription"))
                  .addOption("h", "help", false, 
                             bundle.getString("helpDescription"));

        return gnuOptions;
    }

    /**
     * Processing Command-line arguments
     *
     * @param commandLineArguments Command-line arguments to be processed.
     */
    public static boolean processCommandLineArguments(
            final String[] commandLineArguments) {
        CommandLineParser cmdLineGnuParser = new DefaultParser();
        Options gnuOptions = constructGnuOptions();
        
        try {
            CommandLine commandLine = cmdLineGnuParser.parse(gnuOptions,
                    commandLineArguments, false);
            if (commandLine.hasOption("h") || commandLine.hasOption("help")) {
                // Print help when asked to.
                System.out.println(bundle.getString("appDescription"));
                printUsage();
                printHelp(constructGnuOptions(), 80, "\n", "\n", 5, 3, false,
                        System.out);
               
            } else if(!commandLine.hasOption("s")) {
                System.out.println(bundle.getString("noStringArgument"));
                printUsage();
            } else {
                // Otherwise, get the options ...
                String string = commandLine.getOptionValue("s");
                Boolean caseSensitive = commandLine.hasOption("I")
                        || commandLine.hasOption("ignore-case");
                Boolean exactMatching = commandLine.hasOption('x')
                        || commandLine.hasOption("exact-match");
                // ... convert the files to an internal representation ...
                for (String filename : commandLine.getArgs()) {
                    File file = new File(filename);
                    Spreadsheet spreadsheet;
                    try {
                        spreadsheet = new SpreadsheetImpl(file);
                        String output = "";
                        // ... and print out the results of the query.
                        for (Cell cell : spreadsheet.queryFixedString(string,
                                caseSensitive, exactMatching)) {
                            output += cell;
                        }
                        if (output.isEmpty()) {
                            return false;
                        }
                        System.out.println(output);
                    } catch (SpreadsheetImplException e) {
                        /* Be graceful about the exceptions. Do not let a single
                         document that cannot be parsed ruin the day for the
                         rest. */
                        System.err.println(bundle.getString(
                                "speadsheetProcessingError")
                                + ": " + e.getMessage());
                    }
                }
            }
        } catch (ParseException e) {
            System.err.println(bundle.getString("argumentProcessingError") + 
                               ": " + e.getMessage());
        }
        return true;
    }

    /**
     * Print usage information to provided OutputStream.
     *
     */
    public static void printUsage() {
        System.out.println(bundle.getString("usage") + 
                ": java -jar pb138-ods-search-cli.jar [-h] [-I] -s <arg> [-x]" +
                " [FILE]...");
    }

    /**
     * Write "help" to the provided OutputStream.
     * @param options
     * @param printedRowWidth
     * @param header
     * @param footer
     * @param spacesBeforeOption
     * @param spacesBeforeOptionDescription
     * @param displayUsage
     * @param out
     */
    public static void printHelp(final Options options, 
            final int printedRowWidth, final String header, final String footer, 
            final int spacesBeforeOption, 
            final int spacesBeforeOptionDescription, final boolean displayUsage,
            final OutputStream out) {
        final String commandLineSyntax = "";
        final PrintWriter writer = new PrintWriter(out);
        final HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(writer, printedRowWidth, commandLineSyntax,
                                header, options, spacesBeforeOption,
                                spacesBeforeOptionDescription, footer,
                                displayUsage);
        writer.flush();
    }

    /**
     * Main executable method to process command-line input.
     *
     * @param commandLineArguments Commmand-line arguments.
     */
    public static void main(final String[] commandLineArguments) {
        if (commandLineArguments.length < 1) {
            printUsage();
        } else {
            System.exit(processCommandLineArguments(commandLineArguments) ? 0 : 1);
        }

    }
}
