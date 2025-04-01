# Hello world

| Var         | Value                                       |
| ----------- | ------------------------------------------- |
| Example     | {{global.vars.ex = Example text}}           |
| Two strings | {{global.vars.twostr = First <br/> Second}} |
| Creator     | {{global.vars.creator = Igor Kosarev}}      |


{{global.vars.headerName = # Header}}

Here is some info.

{{global.vars.headerName}}

Now it is header from variable.


It's an example with using global var: \{\{global.vars.ex\}\} with value "{{global.vars.ex}}"    

This software was created by {{global.vars.creator}}

And it is possible to use variables with many strings, for example:

{{global.vars.twostr}}

It is possble to use template to process many files in one folder:


| Foo | ORR | Creator |
| --- | --- | ------- |
{{useTemplate.template1.forEach : /examples }}


| Doc Name | ORR | Creator |
| -------- | --- | ------- |
{{useTemplate.template2.forEach : /examples }}


And create registries of the documents like ADR Repository

| Code | Name | Resolution | Creator |
| ---- | ---- | ---------- | ------- |
{{useTemplate.adr_template.forEach : /ADR_repository/ADRS }}:

Lets fun begin!

For example it is how to templates works: 

{{useTemplate.template3.forEach : /examples }}