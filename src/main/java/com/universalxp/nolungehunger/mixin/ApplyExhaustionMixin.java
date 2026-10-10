package com.universalxp.nolungehunger.mixin;

import com.universalxp.nolungehunger.NoLungeHungerConfig;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.entity.ApplyExhaustionEnchantmentEffect;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * In 1.21.11 the spear's Lunge enchantment pays for its dash with the
 * "minecraft:apply_exhaustion" enchantment effect. That effect is what drains
 * hunger and saturation. Skipping it removes only the Lunge hunger cost.
 * Normal exhaustion (sprinting, jumping, healing, etc.) is untouched, and
 * Lunge's other rules (dash, minimum food level) stay vanilla.
 *
 * The skip only happens while the mod is enabled in config/nolungehunger.json.
 */
@Mixin(ApplyExhaustionEnchantmentEffect.class)
public class ApplyExhaustionMixin {

	@Inject(method = "apply", at = @At("HEAD"), cancellable = true)
	private void nolungehunger$skipExhaustion(ServerWorld world, int level, EnchantmentEffectContext context,
											   Entity user, Vec3d pos, CallbackInfo ci) {
		if (NoLungeHungerConfig.isEnabled()) {
			ci.cancel();
		}
	}
}
