package net.wchbdotnet.seedpriority;

import java.lang.reflect.Proxy;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SeedPriorityRulesTest
{
	@Test
	public void extractsDestinationFromColouredUseTarget()
	{
		assertEquals(
			"teak bird house",
			SeedPriorityRules.destinationName("<col=ff9040>Barley seed<col=ffffff> -> <col=ffff>Teak bird house"));
	}

	@Test
	public void recognisesCoreFarmingItems()
	{
		assertTrue(SeedPriorityRules.isFarmingItem("Ranarr seed"));
		assertTrue(SeedPriorityRules.isFarmingItem("Oak sapling"));
		assertTrue(SeedPriorityRules.isFarmingItem("Oak seedling (w)"));
		assertTrue(SeedPriorityRules.isFarmingItem("Ultracompost"));
		assertTrue(SeedPriorityRules.isFarmingItem("Bottomless compost bucket"));
		assertTrue(SeedPriorityRules.isFarmingItem("Bucket of supercompost"));
		assertTrue(SeedPriorityRules.isFarmingItem("Watering can(8)"));
		assertFalse(SeedPriorityRules.isFarmingItem("Rune scimitar"));
	}

	@Test
	public void prioritisesSeedOnBirdHouseOverPlayer()
	{
		assertEquals(400, SeedPriorityRules.destinationPriority(
			MenuAction.WIDGET_TARGET_ON_GAME_OBJECT,
			"Barley seed -> Teak bird house",
			"Barley seed"));
		assertEquals(0, SeedPriorityRules.destinationPriority(
			MenuAction.WIDGET_TARGET_ON_PLAYER,
			"Barley seed -> Other player",
			"Barley seed"));
	}

	@Test
	public void doesNotTreatUnrelatedItemAsPatchIntent()
	{
		assertEquals(0, SeedPriorityRules.destinationPriority(
			MenuAction.WIDGET_TARGET_ON_GAME_OBJECT,
			"Rune scimitar -> Herb patch",
			"Rune scimitar"));
	}

	@Test
	public void prioritisesCompostBinsAndToolLeprechauns()
	{
		assertEquals(300, SeedPriorityRules.destinationPriority(
			MenuAction.WIDGET_TARGET_ON_GAME_OBJECT,
			"Watermelon -> Compost Bin",
			"Watermelon"));
		assertEquals(100, SeedPriorityRules.destinationPriority(
			MenuAction.WIDGET_TARGET_ON_NPC,
			"Cabbage -> Tool Leprechaun",
			"Cabbage"));
	}

	@Test
	public void prioritisesBottomlessBucketOnNamedHerbsOverPlayer()
	{
		MenuEntry herbs = menuEntry(
			MenuAction.WIDGET_TARGET_ON_GAME_OBJECT,
			"Bottomless compost bucket -> Ranarr");
		MenuEntry player = menuEntry(
			MenuAction.WIDGET_TARGET_ON_PLAYER,
			"Bottomless compost bucket -> jobergs (level-90)");

		assertEquals(0, SeedPriorityRules.bestDestinationIndex(
			new MenuEntry[]{herbs, player},
			"Bottomless compost bucket",
			entry -> entry == herbs));
	}

	@Test
	public void doesNotTreatUnrelatedInspectObjectAsPatch()
	{
		assertEquals(0, SeedPriorityRules.destinationPriority(
			MenuAction.WIDGET_TARGET_ON_GAME_OBJECT,
			"Bottomless compost bucket -> Display case",
			"Bottomless compost bucket",
			false));
	}

	@Test
	public void recognisesFarmingPatchActionSignature()
	{
		assertTrue(SeedPriorityRules.hasFarmingPatchActions(
			new String[]{"Pick", "Inspect", null, "Guide", null}));
		assertFalse(SeedPriorityRules.hasFarmingPatchActions(
			new String[]{"Inspect", null, null, null, null}));
		assertFalse(SeedPriorityRules.hasFarmingPatchActions(null));
	}

	private static MenuEntry menuEntry(MenuAction action, String target)
	{
		return (MenuEntry) Proxy.newProxyInstance(
			MenuEntry.class.getClassLoader(),
			new Class<?>[]{MenuEntry.class},
			(proxy, method, args) ->
			{
				if ("getType".equals(method.getName()))
				{
					return action;
				}
				if ("getTarget".equals(method.getName()))
				{
					return target;
				}
				throw new UnsupportedOperationException(method.getName());
			});
	}
}
