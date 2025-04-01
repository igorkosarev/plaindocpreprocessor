package processor;

import common.Common;
import structure.entities.Item;
import structure.entities.Variable;
import structure.service.StructureService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;
import java.util.logging.Logger;

import static common.Common.*;
import static processor.functionsprocessors.FunctionProcessor.useFunctionProcessor;

public class Processor {
    private static StructureService structureService;

    private static String resultFolder;

    static Logger log;


    public Processor(StructureService structureService, String resultFolder, Logger log) {
        this.resultFolder = resultFolder;
        this.structureService = structureService;
        this.log = log;
    }


    public void process() throws IOException {
        deleteDirectory(resultFolder);
        fillVars(structureService.getRootItem(), resultFolder);
    }

    private void fillVars(Item item, String path) throws IOException {
        createDirectory(path);
        fillVarsHelper(item, path);
    }

    private void fillVarsHelper(Item item, String path) throws IOException {

        if (item.getIsDirectory()) {

            String currentPath = path;
            if (!item.equals(structureService.getStructure().getRootItem())) { //TODO очень некрасивое решение, но работает
                //currentPath += "/" + item.getName();
                currentPath += Common.getFileSeparator() + item.getName();
                createDirectory(currentPath);
            }

            for (Map.Entry<String, Item> entry : item.getItems().entrySet()) {
                fillVarsHelper(entry.getValue(), currentPath);
            }

        } else {
            log.info("Processing file: " + item.getFileName() + "  : " + item.getPath());
            File file = new File(item.getPath());


            InputStream inputStream = new FileInputStream(file);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;

                try (FileWriter writer = new FileWriter(path + Common.getFileSeparator() + item.getFileName(), false)) {
                    while ((line = reader.readLine()) != null) {
                        writer.write(getProcessedString(line, item.getVars()));
                        writer.append('\n');
                    }
                    writer.flush();
                }
            }


        }
    }

    private static String getProcessedString(String rawString, Map<String, Variable> varsMap) throws IOException {

        String result = rawString;

        int i = 0;
        while (i < result.length()) {

            if (i + 2 < result.length()) {
                if (result.substring(i, i + 2).equals("{{")) {
                    int j = i + 2;
                    while (j < result.length()) {
                        if (result.substring(j - 2, j).equals("}}")) {
                            break;
                        }
                        j++;
                    }

                    String entry = result.substring(i, j);

                    if (variablePattern.matcher(entry).find()) {
                        String varValue = findVarValue(removeWrapper(entry), varsMap, structureService);

                        if (varValue != null) {
                            result = replaceVar(result, varValue, i, j);
                            i = i + varValue.length();
                            continue;
                        }
                    }

                    if (createVariablePattern.matcher(entry).find()) {
                        String varDefinitionString = removeWrapper(entry);
                        String[] varDefinition = varDefinitionString.split(SPLIT_VAR_DEFINITION_PATTERN);

                        result = replaceVar(result, varDefinition[1], i, j);

                        i = i + varDefinition[1].length();
                        continue;
                    }

                    if (useFunctionProcessorPattern.matcher(entry).find()) {
                        result = useFunctionProcessor(structureService, entry);
                        break;
                    }
                }
            }

            i++;
        }

        return result;
    }

    private void deleteDirectory(String path) throws IOException {

        File directory = new File(path);
        if (directory.exists()){

            Files.walk(Path.of(path))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            log.info("Directory \"" + directory.getName() + "\" was deleted successfully" + "  : " + directory.getName());
        }
    }


    private void createDirectory(String path) {
        File directory = new File(path);

        if (directory.mkdir()) {
            log.info("Directory \"" + directory.getName() + "\" was created successfully" + "  : " + directory.getPath());
        }
    }

}
