# Seed Priority

Seed Priority makes the farming interaction you intended win when several targets occupy the same tile.

You select a seed and click the bird house beneath your character, but another player receives the `Use` action instead. Or a sapling loses to an unrelated target standing over its patch. Seed Priority removes that friction by moving the relevant farming destination to the top of RuneLite's existing menu.

No clicks are automated and no options disappear. The intended farming interaction simply becomes the normal left-click choice.

## Supported interactions

1. **Bird houses:** Seeds are prioritised over players, ground items, and other unrelated targets.
2. **Compost bins and Big Compost Bins:** Compostable material is prioritised when filling a bin.
3. **Farming patches and allotments:** Seeds, saplings, seedlings, compost, farming tools, and supplies are prioritised for their relevant patch.
4. **Tool Leprechauns:** Selected items are prioritised when used on a Tool Leprechaun.

Bird houses have the highest priority, followed by compost bins, patches, and Tool Leprechauns. If more than one valid destination is under the cursor, the most specific farming interaction wins.

Recognised farming supplies include:

- seeds, saplings, seedlings, and watered seedlings;
- compost, supercompost, ultracompost, their buckets, and the Bottomless compost bucket;
- rakes, spades, seed dibbers, gardening trowels, secateurs, and magic secateurs;
- watering cans, plant pots, plant cure, and scarecrows; and
- volcanic ash, sulphurous fertiliser, and Gricoller's fertiliser.

Compost-bin and Tool Leprechaun interactions intentionally accept any selected item. This covers the wide range of compostable produce and notable farming produce without maintaining a brittle item-by-item list.

## How it works

When an inventory item is selected, RuneLite builds a menu containing every target under the cursor. Seed Priority waits for RuneLite's normal menu sorting, identifies any relevant farming destination, and moves the best existing entry into the left-click position.

The plugin examines only the current menu. It does not scan the scene, inspect other players, or alter the action sent by the game.

## What it does not do

Seed Priority does not click, send actions, add server interactions, or remove menu entries. It only reorders menu entries RuneLite and Old School RuneScape already provide. Player and unrelated-item options remain available in the right-click menu.

The plugin is deliberately limited to selected-item `Use` interactions. Normal object options, inventory item-on-item interactions, PvP attack options, Construction, and blackjacking are not changed.

## Why

Farming and bird house runs involve repeated item-on-object interactions in busy areas. Those interactions should be predictable even when players, pets, or dropped items overlap the intended target. The plugin removes that small but recurring source of misclicks while preserving every original choice.

## Installation

After Plugin Hub approval, search for **Seed Priority** in RuneLite's Plugin Hub and select **Install**. The plugin has no configuration: enabling or disabling it is the complete control.

## Building from source

Seed Priority targets Java 11 and follows the standard RuneLite external-plugin layout.

```text
./gradlew build
```

To launch a developer RuneLite client with the plugin loaded:

```text
./gradlew run
```

## Testing in game

1. Enable Seed Priority.
2. Select seeds and click a placed bird house while another player occupies the same tile.
3. Select a seed or sapling and click its patch through an overlapping player or ground item.
4. Use compostable produce on a compost bin through an overlapping target.
5. Confirm the non-farming targets are still present in the right-click menu.

Automated tests cover menu-target parsing, seeds, saplings, watered seedlings, compost variants, watering cans, bird houses, patches, compost bins, Tool Leprechauns, players, and unrelated items.

## Author

Created by **IVIystical**.

## License

[BSD 2-Clause](LICENSE)
