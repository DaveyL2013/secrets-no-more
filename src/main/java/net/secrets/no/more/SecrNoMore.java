package net.secrets.no.more;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import org.lwjgl.glfw.GLFW;

import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

public class SecrNoMore implements ClientModInitializer {

    int shaderIndex = 0;
    boolean shadersOn = false;
    Collection<Identifier> ids;
    List<ManagedShaderEffect> shaders;

    @Override
    public void onInitializeClient() {
        List<String> ids = ImmutableList.of("shaders/post/antialias.json", "shaders/post/art.json",
                "shaders/post/bits.json", "shaders/post/blobs.json", "shaders/post/blobs2.json",
                "shaders/post/blur.json", "shaders/post/bumpy.json", "shaders/post/color_convolve.json",
                "shaders/post/creeper.json", "shaders/post/deconverge.json", "shaders/post/desaturate.json",
                "shaders/post/flip.json", "shaders/post/fxaa.json", "shaders/post/green.json",
                "shaders/post/invert.json", "shaders/post/notch.json", "shaders/post/ntsc.json",
                "shaders/post/outline.json", "shaders/post/pencil.json", "shaders/post/phosphor.json",
                "shaders/post/scan_pincushion.json", "shaders/post/sobel.json", "shaders/post/spider.json",
                "shaders/post/wobble.json");
        List<ManagedShaderEffect> shaders = new ArrayList<ManagedShaderEffect>();
        for (String id : ids) {
            final ManagedShaderEffect add = ShaderEffectManager.getInstance().manage(new Identifier(id));
            shaders.add(add);
        }
        ShaderEffectRenderCallback.EVENT.register(rs -> {
            if (shadersOn) {
                shaders.get(shaderIndex).render(rs);
            }
        });

        FabricKeyBinding toggleShader = FabricKeyBinding.Builder
                .create(new Identifier("snm", "toggle"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F6, "Secrets No More")
                .build();

        KeyBindingRegistry.INSTANCE.register(toggleShader);

        ClientTickCallback.EVENT.register(ts -> {
            if (toggleShader.wasPressed()) {
                shadersOn = !shadersOn;
            }
        });

        FabricKeyBinding cycleShaderUp = FabricKeyBinding.Builder.create(new Identifier("snm", "cycleup"),
                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_KP_ADD, "Secrets No More").build();

        KeyBindingRegistry.INSTANCE.register(cycleShaderUp);

        FabricKeyBinding cycleShaderDown = FabricKeyBinding.Builder.create(new Identifier("snm", "cycledown"),
                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_KP_SUBTRACT, "Secrets No More").build();

        KeyBindingRegistry.INSTANCE.register(cycleShaderDown);

        ClientTickCallback.EVENT.register(csu -> {
            if (cycleShaderUp.wasPressed()) {
                if (shaderIndex + 2 > shaders.size()) {
                    shaderIndex = 0;
                } else if (shaderIndex < 0) {
                    shaderIndex = shaders.size() - 1;
                } else {
                    shaderIndex += 1;
                }
                ;
            }
        });

        ClientTickCallback.EVENT.register(csd -> {
            if (cycleShaderDown.wasPressed()) {
                if (shaderIndex > shaders.size()) {
                    shaderIndex = 0;
                } else if (shaderIndex - 1 < 0) {
                    shaderIndex = shaders.size() - 1;
                } else {
                    shaderIndex -= 1;
                }
            }
        });
    }

}
