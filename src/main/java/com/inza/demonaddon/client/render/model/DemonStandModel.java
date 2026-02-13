package com.inza.demonaddon.client.render.model;

import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.client.render.entity.model.stand.HumanoidStandModel;
import com.github.standobyte.jojo.client.render.entity.pose.*;
import com.github.standobyte.jojo.client.render.entity.pose.anim.PosedActionAnimation;
import com.github.standobyte.jojo.entity.stand.StandPose;
import com.inza.demonaddon.entity.DemonStandEntity;
import com.inza.demonaddon.init.StandPoses;
import com.inza.demonaddon.init.InitStands;

import net.minecraft.client.renderer.model.ModelRenderer;

public class DemonStandModel extends HumanoidStandModel<DemonStandEntity> {
	@Override
    protected RotationAngle[][] initSummonPoseRotations() {
        return new RotationAngle[][] {
			new RotationAngle[] {
					new RotationAngle(body, 0.0F, 0.7854F, 0.0F),
					new RotationAngle(leftArm, 0.0F, 0.0F, -0.9599F),
					new RotationAngle(leftForeArm, 0.0F, 1.5708F, 0.7418F),
					new RotationAngle(rightArm, -1.5708F, 0.7854F, 0.0F),
					new RotationAngle(rightForeArm, 0.0F, 0.0F, -1.5708F),
					new RotationAngle(leftLeg, -0.3054F, 0.0F, 0.0F),
					new RotationAngle(leftLowerLeg, 0.48F, 0.0F, 0.0F),
					new RotationAngle(rightLeg, 0.3927F, 0.0F, 0.0F),
					new RotationAngle(rightLowerLeg, 0.3054F, 0.0F, 0.0F),
			},
			new RotationAngle[] {
					new RotationAngle(head, -0.2618F, 0.0F, 0.0F),
					new RotationAngle(leftArm, -2.3562F, 1.2217F, -1.5708F),
					new RotationAngle(leftForeArm, -1.9635F, -0.3747F, 0.2291F),
					new RotationAngle(rightArm, 0.0F, 0.2182F, 0.7854F),
					new RotationAngle(rightForeArm, 0.2618F, 0.0F, -1.5708F),
					new RotationAngle(leftLeg, -1.6581F, 0.0F, 0.0F),
					new RotationAngle(leftLegJoint, 0.7418F, 0.0F, 0.0F),
					new RotationAngle(leftLowerLeg, 1.789F, 0.0F, 0.0F),
					new RotationAngle(rightLeg, -0.1745F, 0.0F, 0.0F),
					new RotationAngle(rightLowerLeg, 0.3491F, 0.0F, 0.0F),
			},
			new RotationAngle[] {
					
					new RotationAngle(body, 0.0F, 0.7854F, 0.0F),

					new RotationAngle(leftArm,      -0.7094236F,  0.37985975F,  0.36786723F),
					new RotationAngle(leftForeArm,   0.0F,         0.0F,         0.87266463F),

					new RotationAngle(rightArm,     -0.93040955F, -0.28396752F, -0.20580888F),
					new RotationAngle(rightForeArm, -0.2617994F,  -0.21816616F, -1.3526301F),

					new RotationAngle(leftLeg,      -0.08726646F,  0.0F,        -0.1308997F),
					new RotationAngle(leftLowerLeg,  0.1308997F,   0.0F,         0.0F),

					new RotationAngle(rightLeg,     -0.08726646F,  0.0F,         0.08726646F),
					new RotationAngle(rightLowerLeg, 0.1308997F,   0.0F,         0.0F),
			}
		};
    }

	private static float burstToIdleEasing(float t) {
		if (t <= 0.2F) {
			return (t / 0.2F) * 0.7F;
		}
		float x = (t - 0.2F) / 0.8F;
		float u = 1.0F - x;
		float easeOut = 1.0F - u * u * u;
		return 0.7F + easeOut * 0.3F;
	}

	private void initChargePose() {
		final ModelPose<DemonStandEntity> chargePose = new ModelPose<>(new RotationAngle[] {
				new RotationAngle(body,        0.21816616F,  0.0F, 0.0F),
				new RotationAngle(head,        1.13446401F,  0.0F, 0.0F),

				new RotationAngle(leftArm,    -0.10491982F,  1.17580578F, 0.11360033F),
				new RotationAngle(leftForeArm,-1.61442956F,  0.0F,        0.0F),

				new RotationAngle(rightArm,    0.0F,        -1.00356432F, 0.0F),
				new RotationAngle(rightForeArm,-1.57079633F, 0.0F,        0.0F),

				new RotationAngle(leftLeg,    -1.40325786F, -0.00013878F,-0.00061242F),
				new RotationAngle(leftLowerLeg, 1.65806279F, 0.0F,        0.0F),

				new RotationAngle(rightLeg,   -1.53286928F, -0.04882435F,-0.00061242F),
				new RotationAngle(rightLowerLeg, 1.65806279F,0.0F,        0.0F)
		});

		final ModelPose<DemonStandEntity> burstPose = new ModelPose<>(new RotationAngle[] {
				new RotationAngle(head, -0.34906585F, 0.0F, 0.0F),
				new RotationAngle(leftArm, 0.35590754F, -0.21927723F, -0.93350837F),
				new RotationAngle(leftForeArm, -0.61086524F, 0.0F, 0.0F),
				new RotationAngle(rightArm, 0.25481790F, 0.37360239F, 0.76521500F),
				new RotationAngle(rightForeArm, -0.59398756F, 0.03840335F, -0.06747827F),
				new RotationAngle(leftLeg, -0.27951029F, -0.36983754F, -0.32945201F),
				new RotationAngle(leftLowerLeg, 0.52359878F, 0.0F, 0.0F),
				new RotationAngle(rightLeg, -0.32586449F, 0.37620694F, 0.35618959F),
				new RotationAngle(rightLowerLeg, 0.61086524F, 0.0F, 0.0F)
		});

		final ModelPoseTransition<DemonStandEntity> idleToCharge =
				new ModelPoseTransition<>(idlePose, chargePose)
						.setEasing(DemonStandModel::burstToIdleEasing);

		final ModelPoseTransition<DemonStandEntity> performTransition =
				new ModelPoseTransition<>(chargePose, burstPose)
						.setEasing(t -> {
							if (t <= 0.1F) {
								float x = t / 0.2F;          
								float u = 1.0F - x;
								return 1.0F - u*u*u;         
							}
							return 1.0F;});

		final ModelPoseTransition<DemonStandEntity> burstToIdle =
				new ModelPoseTransition<>(burstPose, idlePose)
						.setEasing(DemonStandModel::burstToIdleEasing);

		actionAnim.put(StandPoses.CHARGE_BURST,
				new PosedActionAnimation.Builder<DemonStandEntity>()
						.addPose(StandEntityAction.Phase.BUTTON_HOLD, idleToCharge)
						.addPose(StandEntityAction.Phase.PERFORM, performTransition)
						.addPose(StandEntityAction.Phase.RECOVERY, burstToIdle)
						.build(idlePose)
		);
	}

	private void initHeartPunchPose() {
		ModelPose<DemonStandEntity> windup = new ModelPose<>(new RotationAngle[] {
				new RotationAngle(body,         0.10068159F,  0.52140315F,  0.05027700F),

				new RotationAngle(leftArm,     -1.42374239F,  0.54155490F, -1.39352934F),
				new RotationAngle(leftForeArm, -0.78539816F,  0.0F,        0.0F),

				new RotationAngle(rightArm,     0.95993109F,  0.0F,        0.0F),
				new RotationAngle(rightForeArm,-1.48352986F,  0.0F,        0.0F),

				new RotationAngle(leftLeg,     -0.34906585F,  0.0F,        0.0F),
				new RotationAngle(rightLeg,     0.39269908F,  0.0F,        0.0F),
				new RotationAngle(leftLowerLeg, 0.74176493F,  0.0F,        0.0F),
				new RotationAngle(rightLowerLeg,0.30543262F,  0.0F,        0.0F),

		});

		ModelPose<DemonStandEntity> hit = new ModelPose<>(new RotationAngle[] {
				new RotationAngle(body, 0.05037351F, -0.52304983F, -0.02517944F),
				new RotationAngle(leftArm, 0.55069992F, -0.05316437F, 0.05262103F),
				new RotationAngle(leftForeArm, -1.26536369F, 0.0F, 0.0F),
				new RotationAngle(rightArm, -1.04719755F, 0.0F, 0.0F),
				new RotationAngle(rightForeArm, -0.43633231F, 0.0F, 0.0F),
				new RotationAngle(leftLeg, -0.34906585F, 0.0F, 0.0F),
				new RotationAngle(rightLeg, 0.08726646F, 0.0F, 0.0F),
				new RotationAngle(leftLowerLeg, 0.74176493F, 0.0F, 0.0F),
				new RotationAngle(rightLowerLeg, 0.30543262F, 0.0F, 0.0F),
		});

		ModelPose<DemonStandEntity> recover = new ModelPose<>(new RotationAngle[] {
				new RotationAngle(body, 0.04363323F, 0.0F, 0.0F),
				new RotationAngle(rightArm, -1.0471976F, 0.0F, 0.0F),
				new RotationAngle(rightForeArm, -0.43633232F, 0.0F, 0.0F),
				new RotationAngle(leftArm, 0.5506999F, -0.0531644F, 0.0526201F),
				new RotationAngle(leftForeArm, -1.2653637F, 0.0F, 0.0F),
				new RotationAngle(leftLeg, -0.34906585F, 0.0F, 0.0F),
				new RotationAngle(rightLeg, 0.08726646F, 0.0F, 0.0F),
				new RotationAngle(leftLowerLeg, 0.74176493F, 0.0F, 0.0F),
				new RotationAngle(rightLowerLeg, 0.30543262F, 0.0F, 0.0F),
		});

		final ModelPoseTransition<DemonStandEntity> idleToCharge =
				new ModelPoseTransition<>(idlePose, windup)
						.setEasing(DemonStandModel::burstToIdleEasing);
		final ModelPoseTransition<DemonStandEntity> performTransition =
				new ModelPoseTransition<>(windup, hit)
						.setEasing(t -> {
							if (t <= 0.1F) {
								float x = t / 0.2F;          
								float u = 1.0F - x;
								return 1.0F - u*u*u;         
							}
							return 1.0F;
						});
		final ModelPoseTransition<DemonStandEntity> burstToIdle =
				new ModelPoseTransition<>(hit, idlePose)
						.setEasing(DemonStandModel::burstToIdleEasing);

		actionAnim.put(StandPoses.HEART_PIERCING_PUNCH,
				new PosedActionAnimation.Builder<DemonStandEntity>()
						.addPose(StandEntityAction.Phase.BUTTON_HOLD, idleToCharge)
						.addPose(StandEntityAction.Phase.PERFORM, performTransition)
						.addPose(StandEntityAction.Phase.RECOVERY, burstToIdle)
						.build(idlePose));
	}

    @Override
    protected void initActionPoses() {
		super.initActionPoses();
        actionAnim.put(StandPose.RANGED_ATTACK, new PosedActionAnimation.Builder<DemonStandEntity>()
                .addPose(StandEntityAction.Phase.BUTTON_HOLD, new ModelPose<>(new RotationAngle[] {
                        new RotationAngle(body, 0.0F, -0.48F, 0.0F),
                        new RotationAngle(leftArm, 0.0F, 0.0F, -0.7854F),
                        new RotationAngle(leftForeArm, 0.0F, 0.0F, 0.6109F),
                        new RotationAngle(rightArm, -1.0908F, 0.0F, 1.5708F),
                        new RotationAngle(rightForeArm, 0.0F, 0.0F, 0.0F)
                }))
                .build(idlePose));

		initChargePose();
		initHeartPunchPose();
    }

    @Override
    public void prepareMobModel(DemonStandEntity entity, float walkAnimPos, float walkAnimSpeed, float partialTick) {
		super.prepareMobModel(entity, walkAnimPos, walkAnimSpeed, partialTick);
        if (actionAnim == null || idlePose == null
                || body == null || leftArm == null || rightArm == null
                || leftForeArm == null || rightForeArm == null) {
            return;
        }
		if (entity != null){
			if (entity.getCurrentTaskAction() == InitStands.DEMON_STAND_BARRAGE.get()){
				this.rightForeArm.visible = true;
				this.rightArmJoint.visible = true;
			}
			else {
				this.rightForeArm.visible = true;
				this.rightArmJoint.visible = true;
			}
		}
    }
    
    

    @Override
    protected ModelPose<DemonStandEntity> initIdlePose() {
		return new ModelPose<>(new RotationAngle[] {
				new RotationAngle(upperPart, 0.0F, 0.0F, 0.0F),

				new RotationAngle(leftArm,      0.23338368F,  0.38560135F, -0.27814300F),
				new RotationAngle(leftForeArm, -0.56723201F,  0.0F,         0.0F),

				new RotationAngle(rightArm,     0.18732252F,  0.01707124F,  0.19792296F),
				new RotationAngle(rightForeArm,-0.39269908F,  0.0F,         0.0F),

				new RotationAngle(leftLeg,     -0.13077770F, -0.13703906F, -0.21879517F),
				new RotationAngle(rightLeg,    -0.16948927F,  0.19666894F,  0.09034819F),

				new RotationAngle(leftLowerLeg,  0.52359878F, 0.0F,         0.0F),
				new RotationAngle(rightLowerLeg, 0.52359878F, 0.0F,         0.0F)
		});
    }

    @Override
    protected IModelPose<DemonStandEntity> initIdlePose2Loop() {
		return new ModelPose<>(new RotationAngle[] {
		});
    }



	private ModelRenderer leftShoulder;
	private ModelRenderer rightShoulder;
	private ModelRenderer leftKnee;
	private ModelRenderer rightKnee;
	private final ModelRenderer horn;
	private final ModelRenderer hornR_r1;
	private final ModelRenderer hornL_r1;
	private final ModelRenderer hornL_r2;
	private final ModelRenderer hornL_r3;
	private final ModelRenderer hornL_r4;
	private final ModelRenderer hornL_r5;
	private final ModelRenderer hornL_r6;
	private final ModelRenderer hornR_r2;
	private final ModelRenderer hornR_r3;
	private final ModelRenderer hornR_r4;


	public DemonStandModel() {
		super();

		head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

		horn = new ModelRenderer(this);
		horn.setPos(0.0F, -1.75F, 1.75F);
		head.addChild(horn);
		setRotationAngle(horn, 0.3054F, 0.0F, 0.0F);
		horn.texOffs(56, 16).addBox(-8.0F, -10.5F, 1.25F, 2.0F, 2.0F, 3.0F, 0.0F, false);
		horn.texOffs(56, 21).addBox(6.0F, -10.5F, 1.25F, 2.0F, 2.0F, 3.0F, 0.0F, false);

		hornR_r1 = new ModelRenderer(this);
		hornR_r1.setPos(-4.0F, -6.25F, -0.75F);
		horn.addChild(hornR_r1);
		setRotationAngle(hornR_r1, 0.1745F, 0.0F, 0.2618F);
		hornR_r1.texOffs(30, 46).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 3.0F, 3.0F, 0.0F, false);

		hornL_r1 = new ModelRenderer(this);
		hornL_r1.setPos(3.5F, -5.25F, -0.5F);
		horn.addChild(hornL_r1);
		setRotationAngle(hornL_r1, 1.5082F, -0.2585F, -0.8208F);
		hornL_r1.texOffs(56, 11).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 3.0F, 2.0F, 0.0F, false);

		hornL_r2 = new ModelRenderer(this);
		hornL_r2.setPos(4.0F, -6.25F, -0.75F);
		horn.addChild(hornL_r2);
		setRotationAngle(hornL_r2, 0.1745F, 0.0F, -0.2618F);
		hornL_r2.texOffs(16, 46).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 3.0F, 3.0F, 0.0F, false);

		hornL_r3 = new ModelRenderer(this);
		hornL_r3.setPos(6.0F, -7.0F, 4.25F);
		horn.addChild(hornL_r3);
		setRotationAngle(hornL_r3, -0.1309F, 0.0F, 0.0F);
		hornL_r3.texOffs(12, 58).addBox(0.0F, -4.0F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		hornL_r3.texOffs(0, 58).addBox(-13.0F, -4.0F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		hornL_r4 = new ModelRenderer(this);
		hornL_r4.setPos(4.75F, -7.25F, 0.0F);
		horn.addChild(hornL_r4);
		setRotationAngle(hornL_r4, -0.3953F, 0.3398F, 0.4387F);
		hornL_r4.texOffs(48, 52).addBox(0.0F, -2.0F, -3.0F, 3.0F, 3.0F, 3.0F, 0.0F, false);

		hornL_r5 = new ModelRenderer(this);
		hornL_r5.setPos(6.25F, -8.0F, -0.25F);
		horn.addChild(hornL_r5);
		setRotationAngle(hornL_r5, 0.0F, 0.0F, 0.2618F);
		hornL_r5.texOffs(36, 52).addBox(-1.0F, -2.0F, -1.0F, 3.0F, 3.0F, 3.0F, 0.0F, false);

		hornL_r6 = new ModelRenderer(this);
		hornL_r6.setPos(5.5F, -6.5F, 4.5F);
		horn.addChild(hornL_r6);
		setRotationAngle(hornL_r6, -0.4363F, 0.0F, 0.0F);
		hornL_r6.texOffs(56, 48).addBox(0.0F, -4.0F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		hornL_r6.texOffs(6, 58).addBox(-12.0F, -4.0F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		hornR_r2 = new ModelRenderer(this);
		hornR_r2.setPos(-3.5F, -5.25F, -0.5F);
		horn.addChild(hornR_r2);
		setRotationAngle(hornR_r2, 1.5082F, 0.2585F, 0.8208F);
		hornR_r2.texOffs(56, 6).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 3.0F, 2.0F, 0.0F, false);

		hornR_r3 = new ModelRenderer(this);
		hornR_r3.setPos(-4.75F, -7.25F, 0.0F);
		horn.addChild(hornR_r3);
		setRotationAngle(hornR_r3, -0.3953F, -0.3398F, -0.4387F);
		hornR_r3.texOffs(24, 52).addBox(-3.0F, -2.0F, -3.0F, 3.0F, 3.0F, 3.0F, 0.0F, false);

		hornR_r4 = new ModelRenderer(this);
		hornR_r4.setPos(-6.25F, -8.0F, -0.25F);
		horn.addChild(hornR_r4);
		setRotationAngle(hornR_r4, 0.0F, 0.0F, -0.2618F);
		hornR_r4.texOffs(12, 52).addBox(-2.0F, -2.0F, -1.0F, 3.0F, 3.0F, 3.0F, 0.0F, false);

		torso.texOffs(0, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
		torso.texOffs(40, 30).addBox(-4.0F, 0.0F, -2.4F, 8.0F, 5.0F, 1.0F, -0.1F, false);
		torso.texOffs(16, 36).addBox(-3.0F, 4.0F, -2.2F, 6.0F, 8.0F, 2.0F, 0.0F, false);

		leftArm.texOffs(24, 16).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
		leftArm.texOffs(32, 10).addBox(1.25F, -2.25F, -1.5F, 1.0F, 3.0F, 3.0F, -0.1F, false);

		leftArmJoint.texOffs(44, 46).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, -0.1F, false);
		leftArmJoint.texOffs(16, 32).addBox(-1.5F, -1.5F, 1.25F, 3.0F, 3.0F, 1.0F, -0.1F, false);

		leftForeArm.texOffs(24, 26).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
		leftForeArm.texOffs(18, 58).addBox(1.25F, 5.25F, -2.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);
		leftForeArm.texOffs(22, 58).addBox(1.25F, 5.25F, -1.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);
		leftForeArm.texOffs(26, 58).addBox(1.25F, 5.25F, 0.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);
		leftForeArm.texOffs(30, 58).addBox(1.25F, 5.25F, 1.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);

		rightArm.texOffs(32, 36).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
		rightArm.texOffs(56, 42).addBox(-2.25F, -2.25F, -1.5F, 1.0F, 3.0F, 3.0F, -0.1F, false);

		rightArmJoint.texOffs(48, 36).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, -0.1F, false);
		rightArmJoint.texOffs(48, 42).addBox(-1.5F, -1.5F, 1.25F, 3.0F, 3.0F, 1.0F, -0.1F, false);

		rightForeArm.texOffs(40, 10).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
		rightForeArm.texOffs(58, 30).addBox(-2.25F, 5.25F, -2.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);
		rightForeArm.texOffs(58, 32).addBox(-2.25F, 5.25F, -1.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);
		rightForeArm.texOffs(34, 58).addBox(-2.25F, 5.25F, 0.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);
		rightForeArm.texOffs(58, 34).addBox(-2.25F, 5.25F, 1.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);

		leftLeg.texOffs(0, 32).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

		leftLegJoint.texOffs(48, 0).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, -0.1F, false);
		leftLegJoint.texOffs(48, 6).addBox(-1.5F, -1.5F, -2.25F, 3.0F, 3.0F, 1.0F, -0.1F, false);

		leftKnee = new ModelRenderer(this);
		leftKnee.setPos(1.0F, -1.25F, -1.75F);
		leftLegJoint.addChild(leftKnee);
		setRotationAngle(leftKnee, 0.0F, 0.0F, 0.7854F);
		leftKnee.texOffs(52, 109).addBox(-0.5F, -1.25F, -0.5F, 1.0F, 2.0F, 1.0F, -0.11F, false);

		leftLowerLeg.texOffs(32, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

		rightLeg.texOffs(40, 20).addBox(-2.1F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

		rightLegJoint.texOffs(0, 52).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, -0.1F, false);
		rightLegJoint.texOffs(56, 26).addBox(-1.5F, -1.5F, -2.25F, 3.0F, 3.0F, 1.0F, -0.1F, false);

		rightKnee = new ModelRenderer(this);
		rightKnee.setPos(-1.0F, -1.25F, -1.75F);
		rightLegJoint.addChild(rightKnee);
		setRotationAngle(rightKnee, 0.0F, 0.0F, -0.7854F);
		rightKnee.texOffs(72, 109).addBox(-0.5F, -1.25F, -0.5F, 1.0F, 2.0F, 1.0F, -0.11F, false);

		rightLowerLeg.texOffs(0, 42).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
	}

}
