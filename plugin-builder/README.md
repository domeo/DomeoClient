------------------------------------------------------------
DOMEOCLIENT PLUGIN BUILDER TOOL
------------------------------------------------------------

Version: 1.0

Author: Richard D. Boyce and Yifan Ning

Requirements: The Ant build tool

------------------------------
Overview
------------------------------
The purpose of this tool is to help you get started with a new
DomeoClient plugin. The Ant build script (build.xml) has a number of
tasks that help to contruct the plugin folder heirarchy and create
templated files sufficient for getting you started with plugin
development. 

------------------------------
How it works:
------------------------------
1) Open up plugin.properties in a text editor and edit the
   configuration variables

2) run the following Ant tasks from the commandline

# Create the folder heirarchy and copy the default gwt configuration for DomeoClient plugins
ant create-plugin-folders

# Copy templated files into new Java and GWT files
ant create-template
#... more to come...


