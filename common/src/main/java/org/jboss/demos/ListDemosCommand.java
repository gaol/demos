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
@CommandDefinition(name="list-demos", description="List Demos")
public class ListDemosCommand implements Command<CommandInvocation> {

	@Override
	public CommandResult execute(CommandInvocation ci)
			throws IOException {
		Demos demos = DemosRunner.getSingleRunner().getDemos();
		StringBuilder sb = new StringBuilder("Demos List:\n\t");
		int i = 0;
		int size = 6;
		for (Demo demo: demos.getDemos()) {
			sb.append(demo.getName() + "\t");
			if (i % size == 0) {
				sb.append("\n\t");
			}
		}
		ci.getShell().out().println(sb.toString());
		return CommandResult.SUCCESS;
	}

	

}
