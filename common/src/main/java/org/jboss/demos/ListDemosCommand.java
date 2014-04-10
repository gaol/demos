/**
 * 
 */
package org.jboss.demos;

import java.io.IOException;

import org.jboss.aesh.cl.CommandDefinition;
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
@CommandDefinition(name="list", description="List Demos")
public class ListDemosCommand implements Command<CommandInvocation> {

	@Override
	public CommandResult execute(CommandInvocation ci)
			throws IOException {
		Demos demos = DemosRunner.getSingleRunner().getDemos();
		StringBuilder sb = new StringBuilder("Demos List:\n  ");
		for (Demo demo: demos.getDemos()) {
			sb.append(Main.formatLens(demo.getName(), 25) + "\t");
			sb.append(demo.getDescription());
			sb.append("\n  ");
		}
		ci.getShell().out().println(sb.toString());
		return CommandResult.SUCCESS;
	}

	

}
