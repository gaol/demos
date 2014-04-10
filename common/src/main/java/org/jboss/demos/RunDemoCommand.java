/**
 * 
 */
package org.jboss.demos;

import java.io.IOException;
import java.util.List;

import org.jboss.aesh.cl.Arguments;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.completer.OptionCompleter;
import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.completer.CompleterInvocation;
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

	@Arguments(description = "specify which demo will be run", completer = DemoCompletor.class)
	private List<String> demoNames;
	
	@Override
	public CommandResult execute(CommandInvocation ci)
			throws IOException {
		Demos demos = DemosRunner.getSingleRunner().getDemos();
		String demoName = null;
		if (this.demoNames != null) {
			demoName = demoNames.get(0); // select the first one
		}
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

	
	public class DemoCompletor implements OptionCompleter<CompleterInvocation> {

		@Override
		public void complete(CompleterInvocation completerInvocation) {
			String currentValue = completerInvocation.getGivenCompleteValue();
			List<Demo> demos = Demos.scanCurrentJar().getDemos();
			for (Demo demo: demos) {
				if (demo.getName().startsWith(currentValue)) {
					completerInvocation.addCompleterValue(demo.getName());
				}
			}
		}
		
	}
	

}
