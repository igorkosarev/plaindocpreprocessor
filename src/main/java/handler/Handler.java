package handler;

import org.codehaus.jackson.map.ObjectMapper;
import processor.Processor;
import structure.service.StructureService;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import static common.Common.writeFile;

public class Handler {
    private final Logger log;

    public Handler(Logger log){
        this.log = log;
    }
    public void doYourJob(String structurePath, String sourcePath, String resultPath) throws IOException {
        File file = new File(sourcePath);

        StructureService structureService = new StructureService(file.getPath(), log);;

        if (structurePath != null) {
            String jsonResult = null;
            try {
                jsonResult = new ObjectMapper()
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(structureService.getStructure());
            } catch (IOException e) {
                log.warning(e.getMessage());
            }

            if (jsonResult != null) {
                try {
                    writeFile(structurePath, jsonResult);
                } catch (IOException e) {
                    log.warning(e.getMessage());
                }
            }

            log.info("Structure file was recorded: " + structurePath);
        }

        try {
            new Processor(structureService, resultPath, log).process();
        } catch (IOException e) {
            log.warning(e.getMessage());
        }
    }
}
