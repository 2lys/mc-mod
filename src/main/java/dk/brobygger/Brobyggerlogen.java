package dk.brobygger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

/**
 * A helmet renamed "brobyggerlogen" (anvil) hides the wearer from the
 * locator bar, exactly like a carved pumpkin does. No screen overlay,
 * no enderman effect - only the locator-bar part.
 *
 * Server-side logic: works on servers even if clients don't have the mod.
 */
public class Brobyggerlogen implements ModInitializer {
    public static final String MAGIC_NAME = "brobyggerlogen";
    private static final Identifier MODIFIER_ID =
            Identifier.fromNamespaceAndPath("brobyggerlogen", "hidden_from_locator_bar");

    @Override
    public void onInitialize() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                update(player);
            }
        });
    }

    private static boolean wearsMagicHelmet(ServerPlayer player) {
        ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
        if (head.isEmpty()) return false;
        Component name = head.getCustomName(); // set by anvil renaming
        return name != null && name.getString().trim().equalsIgnoreCase(MAGIC_NAME);
    }

    private static void update(ServerPlayer player) {
        AttributeInstance range = player.getAttribute(Attributes.WAYPOINT_TRANSMIT_RANGE);
        if (range == null) return;

        boolean should = wearsMagicHelmet(player);
        boolean has = range.getModifier(MODIFIER_ID) != null;

        if (should && !has) {
            // x(1 + -1) = 0 range -> not shown on anyone's locator bar
            range.addTransientModifier(new AttributeModifier(
                    MODIFIER_ID, -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        } else if (!should && has) {
            range.removeModifier(MODIFIER_ID);
        }
    }
}
