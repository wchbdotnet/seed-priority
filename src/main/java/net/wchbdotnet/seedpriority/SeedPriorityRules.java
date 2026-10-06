package net.wchbdotnet.seedpriority;

import java.util.Locale;
import java.util.function.Predicate;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.client.util.Text;

final class SeedPriorityRules
{
	private static final int TOOL_LEPRECHAUN_PRIORITY = 100;
	private static final int PATCH_PRIORITY = 200;
	private static final int COMPOST_BIN_PRIORITY = 300;
	private static final int BIRD_HOUSE_PRIORITY = 400;

	private SeedPriorityRules()
	{
	}

	static int bestDestinationIndex(
		MenuEntry[] entries,
		String selectedItemName,
		Predicate<MenuEntry> hasFarmingPatchActions)
	{
		int bestIndex = -1;
		int bestPriority = 0;
		for (int index = 0; index < entries.length; index++)
		{
			MenuEntry entry = entries[index];
			int priority = destinationPriority(
				entry.getType(),
				entry.getTarget(),
				selectedItemName,
				hasFarmingPatchActions.test(entry));
			if (priority >= bestPriority && priority > 0)
			{
				bestIndex = index;
				bestPriority = priority;
			}
		}
		return bestIndex;
	}

	static int destinationPriority(MenuAction action, String target, String selectedItemName)
	{
		return destinationPriority(action, target, selectedItemName, false);
	}

	static int destinationPriority(
		MenuAction action,
		String target,
		String selectedItemName,
		boolean hasFarmingPatchActions)
	{
		String destination = destinationName(target);
		String itemName = normalize(selectedItemName);

		if (action == MenuAction.WIDGET_TARGET_ON_GAME_OBJECT)
		{
			if (isBirdHouse(destination) && isSeed(itemName))
			{
				return BIRD_HOUSE_PRIORITY;
			}
			if (isCompostBin(destination))
			{
				return COMPOST_BIN_PRIORITY;
			}
			if ((isPatch(destination) || hasFarmingPatchActions) && isFarmingItem(itemName))
			{
				return PATCH_PRIORITY;
			}
		}
		else if (action == MenuAction.WIDGET_TARGET_ON_NPC && isToolLeprechaun(destination))
		{
			return TOOL_LEPRECHAUN_PRIORITY;
		}

		return 0;
	}

	static String destinationName(String target)
	{
		String plainTarget = normalize(target == null ? "" : Text.removeTags(target));
		int separator = plainTarget.lastIndexOf("->");
		return separator >= 0 ? plainTarget.substring(separator + 2).trim() : plainTarget;
	}

	static boolean isFarmingItem(String itemName)
	{
		String name = normalize(itemName);
		return isSeed(name)
			|| name.endsWith(" sapling")
			|| name.endsWith(" seedling")
			|| name.endsWith(" seedling (w)")
			|| name.contains("compost")
			|| name.equals("weeds")
			|| name.equals("rake")
			|| name.equals("spade")
			|| name.equals("seed dibber")
			|| name.equals("gardening trowel")
			|| name.equals("secateurs")
			|| name.equals("magic secateurs")
			|| name.startsWith("watering can")
			|| name.endsWith("plant pot")
			|| name.equals("plant cure")
			|| name.equals("scarecrow")
			|| name.equals("volcanic ash")
			|| name.equals("sulphurous fertiliser")
			|| name.equals("gricoller's fertiliser");
	}

	static boolean hasFarmingPatchActions(String[] actions)
	{
		if (actions == null)
		{
			return false;
		}

		boolean inspect = false;
		boolean guide = false;
		for (String action : actions)
		{
			if ("Inspect".equalsIgnoreCase(action))
			{
				inspect = true;
			}
			else if ("Guide".equalsIgnoreCase(action))
			{
				guide = true;
			}
		}
		return inspect && guide;
	}

	private static boolean isSeed(String itemName)
	{
		return normalize(itemName).endsWith(" seed") || normalize(itemName).endsWith(" seeds");
	}

	private static boolean isBirdHouse(String destination)
	{
		return destination.contains("bird house") || destination.contains("birdhouse");
	}

	private static boolean isCompostBin(String destination)
	{
		return destination.contains("compost bin");
	}

	private static boolean isPatch(String destination)
	{
		return destination.contains("patch") || destination.equals("allotment");
	}

	private static boolean isToolLeprechaun(String destination)
	{
		return destination.equals("tool leprechaun");
	}

	private static String normalize(String value)
	{
		return value == null ? "" : value.trim().toLowerCase(Locale.ENGLISH);
	}
}
