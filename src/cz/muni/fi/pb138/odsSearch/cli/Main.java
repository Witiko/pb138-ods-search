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

    /**
     * Construct and provide GNU-compatible Options.
     *
     * @return Options expected from command-line of GNU form.
     */
    public static Options constructGnuOptions() {
        final Options gnuOptions = new Options();
        gnuOptions.addOption("i", "case-sensitive", false, 
                             bundle.getString("caseSensitiveDescription"))
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
    public static void processCommandLineArguments(
            final String[] commandLineArguments) {
        CommandLineParser cmdLineGnuParser = new DefaultParser();
        Options gnuOptions = constructGnuOptions();
        
        try {
            CommandLine commandLine = cmdLineGnuParser.parse(gnuOptions,
                    commandLineArguments, false);
            if (commandLine.hasOption("h") || commandLine.hasOption("help")) {
                // Print help when asked to.
                System.out.println(bundle.getString("appDescription"));
                printHelp(constructGnuOptions(), 80, "\n", "\n", 5, 3, false,
                        System.out);
            } else {
                // Otherwise, get the options ...
                String string = commandLine.getOptionValue("s");
                Boolean caseSensitive = !commandLine.hasOption("i")
                        || commandLine.hasOption("ignore-case");
                Boolean exactMatching = commandLine.hasOption('x')
                        || commandLine.hasOption("exact-match");
                // ... convert the files to an internal representation ...
                for (String filename : commandLine.getArgs()) {
                    File file = new File(filename);
                    Spreadsheet spreadsheet;
                    try {
                        spreadsheet = new SpreadsheetImpl(file);
                        // ... and print out the results of the query. TODO: Discuss retruning false value in case of no matches found
                        for (Cell cell : spreadsheet.queryFixedString(string,
                                caseSensitive, exactMatching)) {
                            System.out.println(cell);
                        }
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
            System.err.println("Could not parse the options: " + e);
        }
    }

    /**
     * Print usage information to provided OutputStream.
     *
     * @param applicationName Name of application to list in usage.
     * @param options Command-line options to be part of usage.
     * @param out OutputStream to which to write the usage information.
     */
    public static void printUsage(final String applicationName,
            final Options options, final OutputStream out) {
        final PrintWriter writer = new PrintWriter(out);
        final HelpFormatter usageFormatter = new HelpFormatter();
        usageFormatter.printUsage(writer, 80, applicationName, options);
        writer.flush();
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
        final String commandLineSyntax = "java -jar pb138-ods-search-cli.jar "
                + "[-h] [-i] -s <arg> [-x] [FILE]...";
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
        final String applicationName = "odsSearch";
        if (commandLineArguments.length < 1) {
            printUsage(applicationName, constructGnuOptions(), System.out);
        }

        processCommandLineArguments(commandLineArguments);

    }
}
