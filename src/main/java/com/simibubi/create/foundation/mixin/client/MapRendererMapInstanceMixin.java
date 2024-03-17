package com.simibubi.create.foundation.mixin.client;

import java.util.Iterator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.simibubi.create.foundation.map.CustomRenderedMapDecoration;

import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

// fabric: we have an AW for it, and compiler complains if specified by string
@Mixin(value = MapRenderer.MapInstance.class, priority = 1100) // apply after porting lib's current busted mixin here
public class MapRendererMapInstanceMixin {
	@Shadow
	private MapItemSavedData data;

//	@Group(name = "custom_decoration_rendering", min = 1, max = 1)
	// fabric: ignore warnings, the target exists at runtime but not compile time
	@Inject(method = "draw(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ZI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/saveddata/maps/MapDecoration;render(I)Z", remap = false), locals = LocalCapture.CAPTURE_FAILHARD)
	private void create$onDraw(PoseStack poseStack, MultiBufferSource bufferSource, boolean active, int packedLight, CallbackInfo ci, int i, int j, float f, Matrix4f matrix4f, VertexConsumer vertexConsumer, int index, Iterator<MapDecoration> iterator, MapDecoration decoration) {
		if (decoration instanceof CustomRenderedMapDecoration renderer) {
			renderer.render(poseStack, bufferSource, active, packedLight, data, index);
		}
	}

	// fabric: optifine is not supported
//	@Group(name = "custom_decoration_rendering")
//	@Inject(method = "draw(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ZI)V", at = @At(value = "FIELD", target = "net/optifine/reflect/Reflector.ForgeMapDecoration_render:Lnet/optifine/reflect/ReflectorMethod;", opcode = Opcodes.GETSTATIC, ordinal = 1, remap = false), locals = LocalCapture.CAPTURE_FAILHARD)
//	private void create$onDrawOptifine(PoseStack poseStack, MultiBufferSource bufferSource, boolean active, int packedLight, CallbackInfo ci, int i, int j, float f, Matrix4f matrix4f, VertexConsumer vertexConsumer, int index, Iterator<MapDecoration> iterator, MapDecoration decoration) {
//		if (decoration instanceof CustomRenderedMapDecoration renderer) {
//			renderer.render(poseStack, bufferSource, active, packedLight, data, index);
//		}
//	}
}
