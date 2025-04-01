import handler.Handler;
import org.apache.commons.cli.*;
import ui.UIDelegate;

import java.io.*;
import java.nio.file.*;
import java.util.logging.Logger;
import static common.Common.registerRecursive;

public class Main {
    private static final String STRUCTURE_PATH_OPTION = "str";
    private static final String SOURCE_PATH_OPTION = "src";

    private static final String RESULT_PATH_OPTION = "res";
    private static final String ZIP_OPTION = "zip";

    private static final String LISTEN_FILES_OPTION = "listen";

    private static WatchService watchService;

    static Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException {
        log.info("Starting...");
        CommandLine cli;
        try {
            cli = new DefaultParser().parse(getOptions(), args);

        } catch (ParseException e) {
            log.warning("CLI error.  Reason: " + e.getMessage());
            return;
        }

        if (cli.hasOption(SOURCE_PATH_OPTION)
                && cli.hasOption(RESULT_PATH_OPTION)) {

            Handler handler = new Handler(log);

            if (cli.hasOption(LISTEN_FILES_OPTION)) {

                Path folderToWatch = Paths.get(cli.getOptionValue(SOURCE_PATH_OPTION));
                log.info("Start watching directory " + folderToWatch);

                try {
                    watchService = FileSystems.getDefault().newWatchService();

                    registerRecursive(folderToWatch, watchService);

                    while (true) {
                        WatchKey watchKey = watchService.take();

                        if (!watchKey.pollEvents().isEmpty()) {
                            handler.doYourJob(cli.getOptionValue(STRUCTURE_PATH_OPTION),
                                    cli.getOptionValue(SOURCE_PATH_OPTION),
                                    cli.getOptionValue(RESULT_PATH_OPTION));
                            watchKey.reset();

                            registerRecursive(folderToWatch, watchService);
                        }

                        int delay = Integer.parseInt(cli.getOptionValue(LISTEN_FILES_OPTION));
                        Thread.sleep(delay);
                    }

                } catch (Exception e) {
                    log.warning(e.getMessage());
                }


            } else {
                handler.doYourJob(cli.getOptionValue(STRUCTURE_PATH_OPTION),
                        cli.getOptionValue(SOURCE_PATH_OPTION),
                        cli.getOptionValue(RESULT_PATH_OPTION));


            }
        } else {
            UIDelegate uiDelegate = new UIDelegate(log);
            uiDelegate.useUI();
        }

    }

    private static Options getOptions() {
        Options options = new Options();

        Option structureFile = Option.builder(STRUCTURE_PATH_OPTION)
                .argName("file")
                .hasArg()
                .desc("Use given file for save structure in json")
                .build();

        Option sourceDirectory = Option.builder(SOURCE_PATH_OPTION)
                .argName("directory")
                .hasArg()
                .desc("Use given directory for source files")
                .build();

        Option resultDirectory = Option.builder(RESULT_PATH_OPTION)
                .argName("directory")
                .hasArg()
                .desc("Use given directory for results files")
                .build();

        Option listenFolders = Option.builder(LISTEN_FILES_OPTION)
                .argName("ms")
                .hasArg()
                .desc("How many seconds should wait service to check events in the folder")
                .build();

        Option needsToBeZip = new Option(ZIP_OPTION, "Zip results");

        options.addOption(structureFile);
        options.addOption(sourceDirectory);
        options.addOption(resultDirectory);
        options.addOption(needsToBeZip);
        options.addOption(listenFolders);

        return options;
    }
}