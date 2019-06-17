package org.sqlunit4j.plugin;

import java.lang.annotation.Annotation

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.junit.runner.notification.RunListener
import org.junit.runner.notification.RunNotifier
import org.sqlunit4j.SQLUnitSuite

import com.github.iounit.IOUnitTestRunner
import com.github.iounit.annotations.IOTest

public class SQLUnitPlugin implements Plugin<Project> {

	@Override
    public void apply(final Project project) {
		final def extension = project.extensions.create('sqlunit4j', SQLUnitPluginExtension)
		extension.setProject(project);
        final def mytask = project.task('sqlunit4j') {
            doLast {
				final IOUnitTestRunner runner = new IOUnitTestRunner(SQLUnitSuite.class,new IOTest() {

					@Override
					public Class<? extends Annotation> annotationType() {
						return "";
					}

					@Override
					public String inputMatches() {
						return "";
					}

					@Override
					public String inputExtension() {
						return "";
					}

					@Override
					public String outputExtension() {
						return "";
					}

					@Override
					public String inputExclude() {
						return "";
					}

					@Override
					public String inputFolder() {
						return "";
					}

					@Override
					public String sourceFolder() {
						return extension.getSourceFolder()?:"";
					}

					@Override
					public String sourcePackage() {
						return extension.rootPackage?:"";
					}

					@Override
					public boolean saveFailedOutput() {
						return false;
					}

				});
				final RunNotifier runNotifier = new RunNotifier();
				runNotifier.addFirstListener(new JUnitExecutionListener());
				runner.run(runNotifier);
            }
        }
		project.test.dependsOn mytask
		project.task('sqlunit4j-init') {
			doLast {
				def targFolder = new java.io.File("src/test/resources");
				if(!targFolder.exists()) {
					targFolder.mkdirs();
					print("Created " + targFolder.getPath());
				}
			    def console = System.console()
			    if (console) {
			        def packageName = console.readLine('> Please enter your package name (example: com.company.mytests): ')
					def targFolder2 = new java.io.File("src/test/resources/" + packageName.replace('.','/'));
					if(!targFolder2.exists()) {
						targFolder2.mkdirs();
						print("Created " + targFolder2.getPath());
					}
					print("Create a .sql file in " + targFolder2.getPath()+ " to start your first test.")
				}
			}
		}
    }

	public static void main(final String ... args) {
		final IOUnitTestRunner runner = new IOUnitTestRunner(SQLUnitSuite.class);
		final RunNotifier runNotifier = new RunNotifier();
		runNotifier.addFirstListener(new JUnitExecutionListener());
		runner.run(runNotifier);
	}
	
}