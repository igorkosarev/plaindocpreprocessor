package ui;

import common.Common;
import handler.Handler;

import java.nio.file.*;
import java.util.logging.Logger;

import static common.Common.registerRecursive;

public class UIController {
    private static boolean isWorking;
    private final Logger log;

    public UIController(Logger log) {
        this.log = log;
    }

    public void start(String sourcePath, String resultPath) {
        start(sourcePath, resultPath, null, 1000);
    }

    public void start(String sourcePath, String resultPath, int delay) {
        start(sourcePath, resultPath, null, delay);
    }

    public void start(String sourcePath, String resultPath, String structurePath, int delay) {
        isWorking = true;
        Runnable runnable = new UsingHandler(log, sourcePath, resultPath, structurePath, delay);
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void stop() {
        log.info("Stop watching directory");
        isWorking = false;
    }


    /**
     * @param sourcePath private static boolean isWorking;
     */
    private record UsingHandler(Logger log, String sourcePath, String resultPath, String structurePath,
                                int delay) implements Runnable {
        private UsingHandler(Logger log, String sourcePath, String resultPath, String structurePath, int delay) {
            this.sourcePath = sourcePath;
            this.resultPath = resultPath;
            this.structurePath = structurePath != null ? structurePath + Common.getFileSeparator() + "structure.json" : null;
            this.delay = delay;
            this.log = log;
        }

        @Override
        public void run() {
            log.info("Thread started: " + Thread.currentThread().getName());
            Handler handler = new Handler(log);

            Path folderToWatch = Paths.get(sourcePath);
            log.info("Start watching directory " + sourcePath);

            try {
                WatchService watchService = FileSystems.getDefault().newWatchService();
                registerRecursive(folderToWatch, watchService);

                while (isWorking) {

                    WatchKey watchKey = watchService.take();
                    if (!isWorking) {
                        break;
                    }

                    if (!watchKey.pollEvents().isEmpty()) {
                        handler.doYourJob(structurePath,
                                sourcePath,
                                resultPath);
                        watchKey.reset();
                        registerRecursive(folderToWatch, watchService);
                    }

                    Thread.sleep(delay);
                }
                log.info("Thread stopped: " + Thread.currentThread().getName());

            } catch (Exception e) {
                log.warning(e.getMessage());
            }


        }
    }
}

