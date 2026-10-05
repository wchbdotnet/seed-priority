package net.wchbdotnet.seedpriority;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class SeedPriorityPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(SeedPriorityPlugin.class);
		RuneLite.main(args);
	}
}
