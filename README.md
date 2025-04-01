# README

---
1. [Introduction](#introduction)
   1. [Technologies](#technologies)
   2. [Main parameters of the tool](#main-parameters-of-the-tool)
2. [Features](#features)
   1. [Variables](#variables)
   2. [Functions](#functions)
3. [Installation](#installation)
4. [Usage](#usage)
   1. [GUI](#gui)
   2. [CLI](#cli)

---

## Introduction

This is a tool for text preprocessing. It makes possible to use variables and functions in text files.

To use tool features use commands inside brackets:
``` 
{{ feature call }}
```

To use `{{ }}` without use tool feature use special character escaping symbol: "\".

### Technologies

* Java 17
* Lombok
* Jackson
* Commons CLI
* Log4j

### Main parameters of the tool

* Source directory - directory with files need to be preprocessed;
* Result directory - directory with preprocessed files;
* Structure - JSON file with structure of all documents. Consists variables, templates and file structure;
* Delay - time to check changes in source directory.

## Features

### Variables

All variables have a type of `String`.

The tool can work with the next kind of variables:

* `Global variables` - variable can be used in all documents;
* `Local variables` - variable can be used only in the document which was declared in.

To declared variables you should use the following construct:

For global vars:
```
{{global.vars.varName = Var value}}
 ```

For local vars:
```
{{this.vars.varName = Var value}}
```

After preprocessing the values of the declared variables will be on the place where it was declared.

To use variable use the following construct:

For global vars:
```
{{global.vars.varName}}
```

For local vars:
```
{{this.vars.varName}}
```


### Functions

#### Function `useTemplate` 

This function allows you to use templates in loops and applies for all files in the directory specified in function's call.

To use `useTemplate` function use the following construct:

```
{{useTemplate.TemplateName.forEach : /root/specified/directory/path }}
```

Templates for this function are locate in `/templates` directory in the root of the source directory.

You can use this function to call render data by templates and use global and local variables in templates like in the regular documents, and they will be applied from files in the specified directory.

## Installation

Install `Java 17 JRE` and use the tools with GUI or CLI.

## Usage

### GUI

There are two ways to start the tool:

1. Open JAR file by double click
2. Open JAR file without arguments by terminal


### CLI

Example of the command:

```
java -jar jarFileName.jar -src input -res result -str structure.json -listen 1000
```

* `src` - path to source directory
* `res` - path to result directory
* `str` - path to the structure JSON file
* `listen` - time in ms to check updates of the source directory



