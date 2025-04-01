package common;

import lombok.Getter;
import structure.entities.Variable;
import structure.service.StructureService;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.regex.Pattern;

import static java.nio.file.StandardWatchEventKinds.*;

public class Common {

    @Getter
    private static final String fileSeparator = FileSystems.getDefault().getSeparator();

    private static final String START_INPUT_SYMBOLS = "{{";
    private static final String END_INPUT_SYMBOLS = "}}";

    public static final String VARIABLE_PATTERN = "\\{\\{[\\w\\.]{1,}\\}\\}";

    public static final String CREATE_VARIABLE_PATTERN = "\\{\\{[\\w.]{1,}[\\s]{0,}=[\\s]{0,}.{1,}\\}\\}";

    public static final String USE_FUNCTION_PROCESSOR = "\\{\\{[\\w.]{1,}[\\s]{0,}:[\\s]{0,}.{1,}\\}\\}";

    public static final String SPLIT_FUNCTION_PROCESSOR_DEFINITION_PATTERN = "[\\s]{0,}:[\\s]{0,}";

    public static final String SPLIT_VAR_DEFINITION_PATTERN = "[\\s]{0,}=[\\s]{0,}";

    public static final String GLOBAL = "global";
    public static final String LOCAL = "this";

    public static final String VARS = "vars";
    public static final String ITEM = "item";

    public static final String FUNCTION_USE_TEMPLATE = "useTemplate";

    public static final String TEMPLATES = "templates";

    public static final Pattern variablePattern = Pattern.compile(VARIABLE_PATTERN);
    ;
    public static final Pattern createVariablePattern = Pattern.compile(CREATE_VARIABLE_PATTERN);
    public static final Pattern useFunctionProcessorPattern = Pattern.compile(USE_FUNCTION_PROCESSOR);

    public static String removeWrapper(String varInput) {
        return varInput
                .replace(START_INPUT_SYMBOLS, "")
                .replace(END_INPUT_SYMBOLS, "");
    }

    public static String replaceVar(String string, String varValue, int start, int end) {
        return string.substring(0, start) + varValue + string.substring(end);
    }

    public static String findVarValue(String varString, Map<String, Variable> varsMap, StructureService structureService) {
        String[] var = varString.split("\\.");

        String result = "";
        if (var[1].equals(VARS)) {
            switch (var[0]) {
                case GLOBAL ->
                        result = structureService.getGlobalVar(var[2]) != null ? structureService.getGlobalVar(var[2]).getValue() : "";
                case LOCAL -> result = varsMap.get(var[2]) != null ? varsMap.get(var[2]).getValue() : "";
            }
        }

        return result;
    }

    public static void writeFile(String path, String text) throws IOException {
        try (FileWriter writer = new FileWriter(path, false)) {
            writer.write(text);
            writer.flush();
        }
    }

    public static void registerRecursive(final Path root, WatchService watchService) throws IOException {
        // register all subfolders
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                dir.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
                return FileVisitResult.CONTINUE;
            }
        });
    }

}
