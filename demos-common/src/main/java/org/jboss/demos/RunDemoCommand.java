/**
 * 
 */
package org.jboss.demos;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.jboss.demos.run.Demos;
import org.jboss.demos.run.Demos.Demo;
import org.jboss.demos.run.DemosRunner;

/**
 * @author lgao
 *
 */
@CommandDefinition(name="run", description="Run a demo")
public class RunDemoCommand implements Command<CommandInvocation> {

	@Option(required = true, description = "specify which demo will be run.")
	private String demoName;
	
	@Override
	public CommandResult execute(CommandInvocation ci)
			throws IOException {
		Demos demos = DemosRunner.getSingleRunner().getDemos();
		Demo demo = demos.getDemo(demoName);
		if (demo == null) {
			ci.getShell().err().println("Can't find demo: " + demoName);
			return CommandResult.FAILURE;
		}
		try {
			DemosRunner.getSingleRunner().run(demoName);
		} catch (Exception e) {
			e.printStackTrace(ci.getShell().err());
		}
		return CommandResult.SUCCESS;
	}

	

}
