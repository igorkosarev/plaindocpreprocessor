package structure.service;

import structure.entities.Variable;

import java.io.*;
import java.util.regex.Pattern;

import static common.Common.removeWrapper;

public class VarFinder {
    private static final String CREATE_VARIABLE_PATTERN = "\\{\\{[\\w.]{1,}[\\s]{0,}=[\\s]{0,}.{1,}\\}\\}";
    private static final String SPLIT_PATTERN = "[\\s]{0,}=[\\s]{0,}";

    private static final String VARS = "vars";

    private static final String VARS_GLOBAL = "global";

    private static final String VARS_LOCAL = "this";


    public static VarFinderEntity getVarsFromFile(File file) throws FileNotFoundException {

        VarFinderEntity result = new VarFinderEntity();

        InputStream inputStream = new FileInputStream(file);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            Pattern createVariablePattern = Pattern.compile(CREATE_VARIABLE_PATTERN);

            String line;
            while ((line = reader.readLine()) != null) {
                int i = 0;

                while (i < line.length()) {

                    if (i + 2 < line.length()) {
                        if (line.substring(i, i + 2).equals("{{")) {
                            int j = i + 2;
                            while (j < line.length()) {
                                if (line.substring(j - 2, j).equals("}}")) {
                                    break;
                                }
                                j++;
                            }

                            String entry = line.substring(i, j);


                            if (createVariablePattern.matcher(entry).find()) {
                                String varDefinitionString = removeWrapper(entry);
                                String[] varDefinition = varDefinitionString.split(SPLIT_PATTERN);

                                String[] varType = varDefinition[0].split("\\.");

                                if (varType[1].equals(VARS)){
                                    if (varType[0].equals(VARS_GLOBAL)){
                                        result.getGlogalVars().put(varType[2], new Variable(varType[2], varDefinition[1]));
                                    }
                                    if(varType[0].equals(VARS_LOCAL)){
                                        result.getLocalVars().put(varType[2], new Variable(varType[2], varDefinition[1]));
                                    }
                                }
                                i = i + varDefinition[1].length();
                                continue;
                            }
                        }
                    }
                    i++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return result;
    }

}
