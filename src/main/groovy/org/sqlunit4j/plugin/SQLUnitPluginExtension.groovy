package org.sqlunit4j.plugin;
class SQLUnitPluginExtension {
    String rootPackage = ''
	
	def sourceFolder
	
		File getSourceFolder() {
			project.file(sourceFolder)
		}
	
}