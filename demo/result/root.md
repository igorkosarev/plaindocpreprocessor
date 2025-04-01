# Hello world

| Var         | Value                                       |
| ----------- | ------------------------------------------- |
| Example     | Example text           |
| Two strings | First <br/> Second |
| Creator     | Igor Kosarev      |


# Header

Here is some info.

# Header

Now it is header from variable.


It's an example with using global var: \{\{global.vars.ex\}\} with value "Example text"    

This software was created by Igor Kosarev

And it is possible to use variables with many strings, for example:

First <br/> Second

It is possble to use template to process many files in one folder:


| Foo | ORR | Creator |
| --- | --- | ------- |
| Foo from e1  | ORR from e1   | Igor  |
| Foo from e2  | or from e2   | Ivan  |
| Foo from 3  | from 3   | Alex  |



| Doc Name | ORR | Creator |
| -------- | --- | ------- |
| e1  | ORR from e1   | Igor  |
| e2  | or from e2   | Ivan  |
| e3  | from 3   | Alex  |



And create registries of the documents like ADR Repository

| Code | Name | Resolution | Creator |
| ---- | ---- | ---------- | ------- |
| ADR-001 | My first ADR | ACCEPTED |Igor  |
| ADR-002 | My second ADR | NEW |Igor  |
| ADR-003 | My 3rd ADR | REJECTED |Igor  |
| ADR-004 | My 4th ADR | NEW |Igor  |
| ADR-005 | My 5th ADR | NEW |Igor  |


Lets fun begin!

For example it is how to templates works: 

## e1

This doc was created by Igor with ORR: ORR from e1

------ 
## e2

This doc was created by Ivan with ORR: or from e2

------ 
## e3

This doc was created by Alex with ORR: from 3

------ 

