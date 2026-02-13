package com.inza.demonaddon.action.domain.client;

import com.github.standobyte.jojo.util.mc.reflection.ClientReflection;
import com.inza.demonaddon.AddonMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Field;
import java.util.List;

public final class DomainShader {
    private DomainShader() {}

    private static final ResourceLocation DOMAIN_POST =
            new ResourceLocation(AddonMain.MOD_ID, "shaders/post/domain.json");

    private static boolean shaderOn = false;
    private static boolean lastInDomain = false;

    
    private static float intensity = 0f;
    private static float flash = 0f;

    
    
    private static final float INTENSITY_STEP_IN  = 0.18f; 
    private static final float INTENSITY_STEP_OUT = 0.12f; 
    private static final float FLASH_DECAY = 0.10f;        

    
    private static final float EDGE_STRENGTH_MAX = 1.05f; 
    private static final float VIGNETTE_MAX = 0.45f;      

    public static void tick(Minecraft mc, boolean inDomain) {
        if (mc == null || mc.level == null) return;


        
        if (!lastInDomain && inDomain) {
            flash = 1.0f;

        }
        lastInDomain = inDomain;


        ShaderGroup cur = getPostShaderGroup(mc);

        
        boolean needShader = inDomain || intensity > 0.01f || flash > 0.01f;

        if (needShader) {
            if (!shaderOn || cur == null) {
                mc.gameRenderer.loadEffect(DOMAIN_POST);
                shaderOn = true;
                cur = getPostShaderGroup(mc);
            }
        } else {
            if (shaderOn) {
                mc.gameRenderer.shutdownEffect();
                shaderOn = false;
            }
            return;
        }

        
        float target = inDomain ? 1.0f : 0.0f;
        float step = inDomain ? INTENSITY_STEP_IN : INTENSITY_STEP_OUT;
        intensity = approach(intensity, target, step);

        
        flash = clamp(flash - FLASH_DECAY, 0f, 1f);

        
        if (shaderOn && cur != null) {
            setUniform(cur, "Intensity", intensity);
            setUniform(cur, "Flash", flash);

            
            setUniform(cur, "EdgeStrength", EDGE_STRENGTH_MAX);
            setUniform(cur, "Vignette", VIGNETTE_MAX);
        }
    }

    private static float approach(float cur, float target, float step) {
        
        return cur + (target - cur) * clamp(step, 0f, 1f);
    }

    private static float clamp(float v, float min, float max) {
        return v < min ? min : (v > max ? max : v);
    }

    private static ShaderGroup getPostShaderGroup(Minecraft mc) {
        Object gr = mc.gameRenderer;
        if (gr == null) return null;

        Class<?> c = gr.getClass();
        while (c != null) {
            for (Field f : c.getDeclaredFields()) {
                if (ShaderGroup.class.isAssignableFrom(f.getType())) {
                    try {
                        f.setAccessible(true);
                        return (ShaderGroup) f.get(gr);
                    } catch (Throwable ignored) {}
                }
            }
            c = c.getSuperclass();
        }
        return null;
    }

    private static void setUniform(ShaderGroup group, String name, float v) {
        List<Shader> passes = ClientReflection.getShaderGroupPasses(group);
        if (passes == null) return;

        for (Shader pass : passes) {
            try {
                ShaderUniform u = pass.getEffect().getUniform(name);
                if (u != null) u.set(v);
            } catch (Throwable ignored) {}
        }
    }
}
