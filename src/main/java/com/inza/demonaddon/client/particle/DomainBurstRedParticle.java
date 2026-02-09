package com.inza.demonaddon.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

public class DomainBurstRedParticle extends SpriteTexturedParticle {
    private final IAnimatedSprite spriteSet;

    protected DomainBurstRedParticle(ClientWorld world, double x, double y, double z,
                                     double vx, double vy, double vz, IAnimatedSprite spriteSet) {
        super(world, x, y, z, vx, vy, vz);
        this.spriteSet = spriteSet;

        this.xd = vx;
        this.yd = vy;
        this.zd = vz;

        this.rCol = 1.0f;
        this.gCol = 0.08f + random.nextFloat() * 0.08f;
        this.bCol = 0.08f + random.nextFloat() * 0.08f;
        this.alpha = 0.95f;
        this.quadSize = 0.14f + random.nextFloat() * 0.12f;
        this.lifetime = 18 + random.nextInt(10);
        this.gravity = 0.0f;
        this.hasPhysics = false;
        this.pickSprite(this.spriteSet);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        // Explosion-like motion: keep high velocity with mild drag.
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.96;
        this.yd *= 0.96;
        this.zd *= 0.96;

        float t = (float) this.age / (float) this.lifetime;
        this.alpha = 1.0f - t;
        this.quadSize *= 0.985f;
        this.setSpriteFromAge(this.spriteSet);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(BasicParticleType type, ClientWorld world,
                                       double x, double y, double z,
                                       double vx, double vy, double vz) {
            return new DomainBurstRedParticle(world, x, y, z, vx, vy, vz, spriteSet);
        }
    }
}
