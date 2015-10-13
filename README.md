# DECOIT SIMU Metadata Factory

This is a factory library based on ifmapj that allows easy creation of vendor-specific metadata and extended identifier that were specified during the [SIMU](http://www.simu-project.de/) research project.

## Installation ##

The following requirements must be met to compile and use this library:

* Java 7 or higher
* Maven 3

To compile this project the Oracle JDK is preferred but it may work as well on other JDK implementations. If the requirements are met, simply run `mvn install` inside the repository's root directory to install the library to your local Maven repository.

## Usage ##

To use this library in your application simply add the dependency to your pom.xml file:

```xml
<dependency>
    <groupId>de.decoit.simu</groupId>
    <artifactId>simu-metadata-factory</artifactId>
    <version>0.1-SNAPSHOT</version>
</dependency>
```

If included correctly create an instance of the `SimuMetadataFactoryImpl` class and use its methods to create the SIMU specific IF-MAP elements.

## License ##

The source code and all other contents of this repository are copyright by DECOIT GmbH and licensed under the terms of the [GNU General Public License Version 3](http://www.gnu.org/licenses/gpl.txt). A copy of the license may be found inside the LICENSE file.
