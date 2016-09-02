# Living documentation Doclet

[![DevOps By Rultor.com](http://www.rultor.com/b/myunusov/maxur-ldoc)](http://www.rultor.com/p/myunusov/maxur-ldoc)

[![We recommend IntelliJ IDEA](http://img.teamed.io/intellij-idea-recommend.svg)](https://www.jetbrains.com/idea/)

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/myunusov/maxur-ldoc/blob/master/LICENSE)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.maxur/maxur-ldoc/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.maxur/maxur-ldoc)

[![Build Status](https://travis-ci.org/myunusov/maxur-ldoc.svg?branch=master)](https://travis-ci.org/myunusov/maxur-ldoc)
[![Coverage Status](https://coveralls.io/repos/github/myunusov/maxur-ldoc/badge.svg?branch=master)](https://coveralls.io/github/myunusov/maxur-ldoc?branch=master)

## Overview

Doclet for the JavaDoc tool that generates Living documents from the code.

This doclet uses the analyzed information from the JavaDoc tool. It automatically generates glossary and context map from your code as md and png files.

## Links  

* [Living Document (WiKi)](https://en.wikipedia.org/wiki/Living_document)
* [Living Documentation by design, with Domain-Driven Design (eBook)](https://leanpub.com/livingdocumentation)

## Documentation 

### Living Glossary

```
Extract the glossary of the Ubiquitous Language from the source code. Consider
the source code as the Single Source of Truth, and take great care of the naming of each class,
interface and public method whenever they represent domain concepts. Add the description of
the domain concept directly into the source code, as structured comments that can be extracted
by a tool. When extracting the glossary, find a way to filter out code that is not expressing the
domain.
```

### Living Context Map

```
Context Mapping is a general purpose technique, part of the Domain Driven Design (DDD) toolkit, 
helps the architects and developers manage the many types of complexity they face in software development projects
```

## Highlighted Core

```
In the book Domain-Driven Design, Eric Evans explains that when a domain grows to a large
number of elements, it becomes difficult to understand, even if only a small subset of them are
really important. A simple way to guide developers focus on these particular subset is to highlight
it in the code repository itself.

Flag each element of the CORE DOMAIN within the primary repository of the model, without
particularly trying to elucidate its role. Make it effortless for a developer to know what is in
or out of the CORE.

Using annotations to flag the core concepts directly into the code is a natural approach, one which
evolves well over time. Code elements like classes or interfaces get renamed, move from one module
to another, and sometime end up deleted.
```

### BusinessDomain (Package)

The BusinessDomain annotation represents SubDomain and Bounded Context.

This is a simple example of curation by annotations.

package-info.java

```java
@BusinessDomain(
    name = "Authorization",
    description = "for authorization of users to ensure they have the access control rights (permissions) required to do the actions performed"
)
@Link(related = "core")
@Link(related = "common")
package org.maxur.usrv.smpl.domain.auth;

import org.maxur.ldoc.annotation.BusinessDomain;
import org.maxur.ldoc.annotation.Link;
```

### Concept (Class)

The Concept annotation represents Business Domain Concepts.

This is a simple example of curation by annotations.

```java
@Concept(name = "User", description = "System's User")
public class User {
}
```

 
## Maven 2
 
To configure Maven to use Living documentation Doclet, you need to adjust your pom.xml file accordingly: 
 
```xml
...
<dependencies>
    ...
    <dependency>
        <groupId>org.maxur</groupId>
        <artifactId>maxur-ldoc</artifactId>
        <version>0.2</version>
    </dependency>
    ...
</dependencies>
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
                        <version>0.2</version>
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


Any Living documentation Doclet specific options go into the *additionalparam* argument. 

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
```

Replace additionalParamName and additionalParamValue with the name and value of each additional parameter you need.
Note: The initial dash - of additional parameters will automatically be added by the Gradle javadoc task and should therefore be omitted from the configuration.

The additional parameters for this doclet are described below.



##Ant

In ant, the javadoc task needs to be told to use the Living documentation Doclet in a similar way.

```xml
<javadoc destdir="target/javadoc" sourcepath="src">
    <doclet name="org.maxur.ldoc.LivingDocumentation" pathref="livingDocumentationDoclet.classpath"> 
        <param name="additionalParamName" value="additionalParamValue" />
    </doclet>
</javadoc>
```
Make sure a path reference is defined for livingDocumentationDoclet.classpath pointing to maxur-ldoc-{VERSION}.jar. It may be a good idea to use Ivy in this case.
Replace additionalParamName and additionalParamValue with the name and value of each additional parameter you need.

The additional parameters for this doclet are described below.


##Commandline

Probably not many people run JavaDoc regularly from the commandline, but in case you do, make sure to provide the options -doclet org.maxur.ldoc.LivingDocumentation and -docletpath {PATH_TO_JAR}, where {PATH_TO_JAR} is the location of the maxur-ldoc-{VERSION}.jar.
The latest version of the jar file can be found on [Maven Central](http://repo1.maven.org/maven2/org/maxur/maxur-ldoc/0.2/maxur-ldoc-0.2.jar)

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
