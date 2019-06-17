package org.sqlunit4j.plugin;

import org.gradle.api.Project

class SQLUnitPluginExtension {
    String rootPackage = '/'
	Project project;
	
	def sourceFolder
	
		File getSourceFolder() {
			sourceFolder?project.file(sourceFolder):null
		}
	
}