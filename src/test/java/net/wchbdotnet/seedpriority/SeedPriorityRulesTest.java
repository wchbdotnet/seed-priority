package net.wchbdotnet.seedpriority;

import net.runelite.api.MenuAction;
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
		assertTrue(SeedPriorityRules.isFarmingItem("Ultracompost"));
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
}
