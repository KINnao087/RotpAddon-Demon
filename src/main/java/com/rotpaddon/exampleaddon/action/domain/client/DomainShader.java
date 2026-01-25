package com.rotpaddon.exampleaddon.action.domain.client;

import com.github.standobyte.jojo.util.mc.reflection.ClientReflection;
import com.rotpaddon.exampleaddon.AddonMain;
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

    // 你自己调
    private static final float INTENSITY_LERP = 0.35f;
    private static final float FLASH_DECAY   = 0.45f;

    public static void tick(Minecraft mc, boolean inDomain) {
        if (mc == null || mc.level == null) return;

        // ===== 进入边沿：闪一下 =====
        if (!lastInDomain && inDomain) {
            flash = 1.0f;
        }
        lastInDomain = inDomain;

        // ===== 确保 effect 存在（视角切换/资源重载可能让它变 null）=====
        ShaderGroup cur = getPostShaderGroup(mc);

        if (inDomain) {
            if (!shaderOn || cur == null) {
                mc.gameRenderer.loadEffect(DOMAIN_POST);
                shaderOn = true;
                // 刚 load 完这一帧可能还拿不到 group，下面会再尝试一次
                flash = 1.0f;
            }
        } else {
            if (shaderOn) {
                mc.gameRenderer.shutdownEffect();
                shaderOn = false;
            }
        }

        // ===== 强度（朴素版：领域内直接 1，外面 0；也给你留了 lerp）=====
        float target = inDomain ? 1.0f : 0.0f;
        intensity += (target - intensity) * INTENSITY_LERP;

        // ===== 再取一次（loadEffect 后可能这次就拿到了）=====
        cur = getPostShaderGroup(mc);

        // ===== 写 uniform =====
        if (shaderOn && cur != null) {
            setUniform(cur, "Intensity", intensity);
            setUniform(cur, "Flash", flash);
        }

        // ===== 最后衰减 flash（让它真的是“闪一下”）=====
        flash = Math.max(0f, flash - FLASH_DECAY);
    }

    /** 从 GameRenderer 里抓当前的 ShaderGroup（最朴素反射） */
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

    /** 给 ShaderGroup 的所有 pass 写 uniform（RotP 里也是这么拿 passes 的） */
    private static void setUniform(ShaderGroup group, String name, float v) {
        List<Shader> passes = ClientReflection.getShaderGroupPasses(group);
        if (passes == null) return;

        for (Shader pass : passes) {
            try {
                // 1.16.5：pass.getEffect() 是 ShaderInstance，uniform 在它里面
                ShaderUniform u = pass.getEffect().getUniform(name);
                if (u != null) u.set(v);
            } catch (Throwable ignored) {}
        }
    }
}
