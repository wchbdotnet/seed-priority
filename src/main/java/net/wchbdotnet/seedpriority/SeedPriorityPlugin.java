package net.wchbdotnet.seedpriority;

import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.ItemComposition;
import net.runelite.api.Menu;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.ObjectComposition;
import net.runelite.api.events.PostMenuSort;
import net.runelite.api.widgets.Widget;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
	name = "Seed Priority",
	description = "Prioritises farming destinations when using farming items",
	tags = {"farming", "seed", "sapling", "birdhouse", "patch", "leprechaun"}
)
public class SeedPriorityPlugin extends Plugin
{
	@Inject
	private Client client;

	@Subscribe(priority = -1)
	public void onPostMenuSort(PostMenuSort event)
	{
		if (client.isMenuOpen())
		{
			return;
		}

		Widget selectedWidget = client.getSelectedWidget();
		if (selectedWidget == null || selectedWidget.getItemId() < 0)
		{
			return;
		}

		ItemComposition selectedItem = client.getItemDefinition(selectedWidget.getItemId());
		if (selectedItem == null)
		{
			return;
		}

		Menu menu = client.getMenu();
		MenuEntry[] entries = menu.getMenuEntries();
		int bestIndex = SeedPriorityRules.bestDestinationIndex(entries, selectedItem.getName(), this::hasFarmingPatchActions);
		int currentTopIndex = entries.length - 1;
		if (bestIndex < 0 || bestIndex == currentTopIndex)
		{
			return;
		}

		MenuEntry currentTop = entries[currentTopIndex];
		entries[currentTopIndex] = entries[bestIndex];
		entries[bestIndex] = currentTop;
		menu.setMenuEntries(entries);
	}

	private boolean hasFarmingPatchActions(MenuEntry entry)
	{
		if (entry.getType() != MenuAction.WIDGET_TARGET_ON_GAME_OBJECT)
		{
			return false;
		}

		ObjectComposition object = client.getObjectDefinition(entry.getIdentifier());
		if (object == null)
		{
			return false;
		}

		if (object.getImpostorIds() != null)
		{
			ObjectComposition impostor = object.getImpostor();
			if (impostor != null)
			{
				object = impostor;
			}
		}

		return SeedPriorityRules.hasFarmingPatchActions(object.getActions());
	}
}
