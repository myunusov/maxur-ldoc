# Living documentation Doclet

## Overview

Doclet for the JavaDoc tool that generates Living documents from the code.

This doclet uses the analyzed information from the JavaDoc tool. It automatically generates glossary and context map from your code as md and png files.


## Links  

* [Living Document (WiKi)](https://en.wikipedia.org/wiki/Living_document)
* [Living Documentation by design, with Domain-Driven Design (eBook)](https://leanpub.com/livingdocumentation)


 
## Maven 2
 
To configure Maven to use Living documentation Doclet, you need to adjust your pom.xml file accordingly: 
 
```xml
...
<build>
  <plugins>
    ...
    <plugin>
        <artifactId>maven-javadoc-plugin</artifactId>
        <executions>
            <execution>
                <id>ldoc</id>
                <phase>post-integration-test</phase>
                <configuration>
                    <doclet>org.maxur.ldoc.LivingDocumentation</doclet>
                    <docletArtifact>
                        <groupId>org.maxur</groupId>
                        <artifactId>maxur-ldoc</artifactId>
                        <version>0.01</version>
                    </docletArtifact>
                    <useStandardDocletOptions>false</useStandardDocletOptions>
                    <additionalparam>
                        -glossary
                        -contextMap
                        -basedir ${project.basedir}/src/site/resources
                    </additionalparam>
                </configuration>
                <goals>
                    <goal>javadoc</goal>
                </goals>
            </execution>
    
        </executions>
    </plugin>
    ...
  </plugins>
</build>
...    
```


Any Living documentation Doclet specific options go into the <additionalparam> argument. 

```xml
...
    <additionalparam>
        -glossary
        -contextMap
        -basedir ${project.basedir}/src/site/resources
    </additionalparam>
...
```
The additional parameters for this doclet are described below.

## Gradle

In gradle, the doclet and its dependency need to be declared. From there on, the configuration is the same as your regular JavaDoc configuration.

```groovy
apply plugin: 'java'

configurations {
    livingDocumentationDoclet
}

dependencies {
    livingDocumentationDoclet "org.maxur:maxur-ldoc:0.01"
}

javadoc {
    source = sourceSets.main.allJava
    options.docletPath = configurations.livingDocumentationDoclet.files.asType(List)
    options.doclet = "org.maxur.ldoc.LivingDocumentation"
    options.addStringOption "additionalParamName", "additionalParamValue"
}
Replace additionalParamName and additionalParamValue with the name and value of each additional parameter you need.
Note: The initial dash - of additional parameters will automatically be added by the Gradle javadoc task and should therefore be omitted from the configuration.

The additional parameters for this doclet are described below.
```


##Ant

In ant, the javadoc task needs to be told to use the Living documentation Doclet in a similar way.

```xml
<javadoc destdir="target/javadoc" sourcepath="src">
    <doclet name="org.maxur.ldoc.LivingDocumentation" pathref="livingDocumentationDoclet.classpath"> 
        <param name="additionalParamName" value="additionalParamValue" />
    </doclet>
</javadoc>
```
Make sure a path reference is defined for livingDocumentationDoclet.classpath pointing to lDocDoclet-{VERSION}.jar. It may be a good idea to use Ivy in this case.
Replace additionalParamName and additionalParamValue with the name and value of each additional parameter you need.

The additional parameters for this doclet are described below.


##Commandline

Probably not many people run JavaDoc regularly from the commandline, but in case you do, make sure to provide the options -doclet org.maxur.ldoc.LivingDocumentation and -docletpath {PATH_TO_JAR}, where {PATH_TO_JAR} is the location of the lDocDoclet-{VERSION}.jar.
The latest version of the jar file can be found on *TBD*

For more details on commandline javadoc, please see the [official documentation from Oracle](http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/javadoc.html).

## Additional parameters
                                                                                                           
| Parameter      | Type     | Default value | Description |
|----------------|----------|---------------|-------------|                                                                                                              
| -all           | Boolean  | `false`       | Specifies whether or not all documents are included to output.|
| -glossary      | Boolean  | `false`       | Specifies whether or not the glossary is included to output.|
| -contextMap    | Boolean  | `false`       | Specifies whether or not the context map is included to output.|
| -basedir       | String   | _none_        | The name of the handlebars templates directory. If this parameter is not specified doclet use default template.|


##Template

###Glossary Template (Example)
glossary.hbs
```text
#Glossary

##Bounded Context "{{title}}" (*{{name}}*)
{{description}}


{{#concepts}}
1. **{{title}}** (*{{name}}*) - {{description}}
{{/concepts}}
```