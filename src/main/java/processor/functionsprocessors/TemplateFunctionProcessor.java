package processor.functionsprocessors;

import structure.entities.Item;
import structure.entities.Template;
import structure.service.StructureService;

import java.io.*;
import java.util.Map;
import java.util.regex.Pattern;

import static common.Common.*;
import static common.Common.replaceVar;

public class TemplateFunctionProcessor {

    public static String processUseTemplate(StructureService structureService, String[] functionProcessorDefinition, Pattern variablePattern) throws IOException {

        String templateName = functionProcessorDefinition[0].split("\\.")[1];
        Template template = structureService.getTemplate(templateName);

        String templateString;

        if (template.getTemplate() != null){
            templateString = template.getTemplate();
        } else {
            StringBuilder templateStringBuilder = new StringBuilder();

            File templateFile = new File(template.getPath());
            InputStream inputStream = new FileInputStream(templateFile);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                boolean isFirstLine = true;

                while ((line = reader.readLine()) != null) {
                    if (isFirstLine){
                        templateStringBuilder.append(line);
                        isFirstLine = false;
                    } else {
                        templateStringBuilder.append("\n");
                        templateStringBuilder.append(line);
                    }
                }
            }
            templateString = templateStringBuilder.toString();
        }

        String directoryPath = functionProcessorDefinition[1];

        Item directoryItem = structureService.getItemByPath(directoryPath);

        if (directoryItem == null) {
            return templateString;
        }

        StringBuilder resultStringBuilder = new StringBuilder();

        if (!directoryItem.getItems().isEmpty()) {
            for (Map.Entry<String, Item> entryItem : directoryItem.getItems().entrySet()) {

                if (!entryItem.getValue().getIsDirectory()) {

                    int i = 0;

                    String res = templateString;
                    while (i < templateString.length()) {
                        if (i + 2 < res.length()) {
                            if (res.substring(i, i + 2).equals("{{")) {
                                int j = i + 2;
                                while (j < res.length()) {
                                    if (res.substring(j - 2, j).equals("}}")) {
                                        break;
                                    }
                                    j++;
                                }

                                String entry = res.substring(i, j);

                                if (variablePattern.matcher(entry).find()) {


                                    String varValue = findVarValue(removeWrapper(entry), entryItem.getValue().getVars(), structureService);

                                    if (varValue != null) {
                                        res = replaceVar(res, varValue, i, j);
                                        i = i + varValue.length();
                                        continue;
                                    }
                                }
                            }
                        }
                        i++;
                    }
                    resultStringBuilder.append(res).append("\n");

                }

            }
        }

        return resultStringBuilder.toString();
    }
}
