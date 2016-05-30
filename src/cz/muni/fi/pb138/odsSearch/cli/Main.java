package cz.muni.fi.pb138.odsSearch.cli;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.ResourceBundle;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * This class represents the CLI of ods-search.
 *
 * @author Petr Kratochvila
 */
public class Main {

    private static final Options options = new Options();
    private static final ResourceBundle bundle = ResourceBundle.getBundle("cz/muni/fi/"
            + "pb138/odsSearch/cli/Main");

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
        final CommandLineParser cmdLineGnuParser = new GnuParser();
        final Options gnuOptions = constructGnuOptions();
        CommandLine commandLine;

        try {
            commandLine = cmdLineGnuParser.parse(gnuOptions, 
                          commandLineArguments, false);
            if (commandLine.hasOption("i") 
                || commandLine.hasOption("case-sensitive")) {
                System.out.println("Using case sensitive search\n");
            }
            if (commandLine.hasOption('x') 
                || commandLine.hasOption("exact-match")) {
                System.out.println("Using exact match search\n");
            }
            if (commandLine.hasOption("h") || commandLine.hasOption("help")) {
                System.out.println(bundle.getString("appDescription"));

                printHelp(constructGnuOptions(), 80, "\n", "\n", 5, 3, false, 
                          System.out);
            }

            // To be replaced
            System.out.println("Files: " 
                               + Arrays.toString(commandLine.getArgs()) + "\n"
                               + "String: " + commandLine.getOptionValue("s"));

        } catch (ParseException parseException) // checked exception
        {
            System.err.println(
                    "Encountered exception while parsing using GnuParser:\n"
                    + parseException.getMessage());
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
     */
    public static void printHelp(final Options options, 
            final int printedRowWidth, final String header, final String footer, 
            final int spacesBeforeOption, 
            final int spacesBeforeOptionDescription, final boolean displayUsage,
            final OutputStream out) {
        final String commandLineSyntax = "java -cp pb138-ods-search.jar [-h] [-i] [-s <arg>] [-x] [FILE]...";
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
