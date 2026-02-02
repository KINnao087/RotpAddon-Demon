package com.rotpaddon.exampleaddon.client.render.model;

import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.client.render.entity.model.stand.HumanoidStandModel;
import com.github.standobyte.jojo.client.render.entity.pose.IModelPose;
import com.github.standobyte.jojo.client.render.entity.pose.ModelPose;
import com.github.standobyte.jojo.client.render.entity.pose.RotationAngle;
import com.github.standobyte.jojo.client.render.entity.pose.anim.PosedActionAnimation;
import com.github.standobyte.jojo.entity.stand.StandPose;
import com.rotpaddon.exampleaddon.entity.DemonStandEntity;
import com.rotpaddon.exampleaddon.init.InitStands;

import net.minecraft.client.renderer.model.ModelRenderer;

// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports


public class DemonStandModel extends HumanoidStandModel<DemonStandEntity> {
	@Override // TODO summon poses
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
			}
		};
    }

	public static final StandPose CHARGE_BURST = new StandPose("charge_burst");
    @Override
    protected void initActionPoses() { // TODO pickaxe throwing anim
        actionAnim.put(StandPose.RANGED_ATTACK, new PosedActionAnimation.Builder<DemonStandEntity>()
                .addPose(StandEntityAction.Phase.BUTTON_HOLD, new ModelPose<>(new RotationAngle[] {
                        new RotationAngle(body, 0.0F, -0.48F, 0.0F),
                        new RotationAngle(leftArm, 0.0F, 0.0F, -0.7854F),
                        new RotationAngle(leftForeArm, 0.0F, 0.0F, 0.6109F),
                        new RotationAngle(rightArm, -1.0908F, 0.0F, 1.5708F), 
                        new RotationAngle(rightForeArm, 0.0F, 0.0F, 0.0F)
                }))
                .build(idlePose));

		actionAnim.put(CHARGE_BURST, new PosedActionAnimation.Builder<DemonStandEntity>()

				// ① 按住蓄力：全身蜷缩（收手收脚、身体前倾）
				.addPose(StandEntityAction.Phase.BUTTON_HOLD, new ModelPose<>(new RotationAngle[] {
						// 身体略前倾 + 微收
						new RotationAngle(body,  0.55F, 0.0F, 0.0F),

						// 头也稍微低一点（如果你有 head）
						new RotationAngle(head,  0.35F, 0.0F, 0.0F),

						// 双臂收回胸前（上臂抬起内扣，前臂折叠）
						new RotationAngle(leftArm,      -0.90F, 0.20F, -0.90F),
						new RotationAngle(leftForeArm,  -0.90F, 0.0F,   0.70F),
						new RotationAngle(rightArm,     -0.90F,-0.20F,  0.90F),
						new RotationAngle(rightForeArm, -0.90F, 0.0F,  -0.70F),

						// 双腿蜷（大腿抬、膝盖折）——字段名按你自己的来
						new RotationAngle(leftLeg,       0.85F, 0.15F, 0.0F),
						new RotationAngle(leftLowerLeg,  1.05F, 0.0F,  0.0F),
						new RotationAngle(rightLeg,      0.85F,-0.15F, 0.0F),
						new RotationAngle(rightLowerLeg, 1.05F, 0.0F,  0.0F),
				}))

				// ② 释放瞬间：四肢展开“大”字（手水平张开、腿分开、身体立起来）
				.addPose(StandEntityAction.Phase.PERFORM, new ModelPose<>(new RotationAngle[] {
						// 身体回正
						new RotationAngle(body,  0.0F, 0.0F, 0.0F),
						new RotationAngle(head,  0.0F, 0.0F, 0.0F),

						// 手臂展开：近似水平张开（±90° roll / 或 yaw，视你模型轴向）
						// 这套是比较“横向展开”的感觉，你如果发现方向不对，就把 Z 改成 Y（或相反）
						new RotationAngle(leftArm,       0.0F, 0.0F, -1.5708F),
						new RotationAngle(leftForeArm,   0.0F, 0.0F,  0.0F),
						new RotationAngle(rightArm,      0.0F, 0.0F,  1.5708F),
						new RotationAngle(rightForeArm,  0.0F, 0.0F,  0.0F),

						// 腿分开（像大字站）：大腿外展一点
						new RotationAngle(leftLeg,       0.0F, 0.0F, -0.55F),
						new RotationAngle(leftLowerLeg,  0.0F, 0.0F,  0.0F),
						new RotationAngle(rightLeg,      0.0F, 0.0F,  0.55F),
						new RotationAngle(rightLowerLeg, 0.0F, 0.0F,  0.0F),
				}))

				.build(idlePose));
        super.initActionPoses();
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
				this.rightForeArm.visible = false;
				this.rightArmJoint.visible = false;
			}
			else {
				this.rightForeArm.visible = true;
				this.rightArmJoint.visible = true;
			}
		}
    }
    
    

    @Override // TODO idle pose
    protected ModelPose<DemonStandEntity> initIdlePose() {
		return new ModelPose<>(new RotationAngle[] {
				new RotationAngle(upperPart, 0, 0, 0),
				new RotationAngle(leftArm, -0.0425F, 0.7769F, -0.1841F),
				new RotationAngle(leftForeArm, 0.0F, 0.0F, 0.0F),
				new RotationAngle(rightArm, 0.0003F, -0.7816F, 0.1231F),
				new RotationAngle(rightForeArm, 0.0F, 0.0F, 0.0F),
				new RotationAngle(leftLeg, -0.2182F, 0.0F, -0.0873F),
				new RotationAngle(leftLowerLeg, 0.7854F, 0.0F, 0.0F),
				new RotationAngle(rightLeg, -0.1309F, 0.0F, 0.0873F),
				new RotationAngle(rightLowerLeg, 0.3054F, 0.0F, 0.0F)
		});
    }

    @Override
    protected IModelPose<DemonStandEntity> initIdlePose2Loop() {
		return new ModelPose<>(new RotationAngle[] {
				new RotationAngle(leftArm, -0.0852F, 0.7741F, -0.2451F),
				new RotationAngle(leftForeArm, 0.0F, 0.0F, 0.0F),
				new RotationAngle(rightArm, -0.0429F, -0.7807F, 0.1845F),
				new RotationAngle(rightForeArm, 0.0F, 0.0F, 0.0F)
		});
    }



	private ModelRenderer leftShoulder;
	private ModelRenderer rightShoulder;
	private ModelRenderer leftKnee;
	private ModelRenderer rightKnee;

	public DemonStandModel() {
        super(64, 64);
		addHumanoidBaseBoxes(null);
		texWidth = 128;
		texHeight = 128;

		head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
		head.texOffs(26, 9).addBox(-3.5F, -8.5F, -4.0F, 1.0F, 1.0F, 8.0F, -0.1F, false);
		head.texOffs(26, 9).addBox(2.5F, -8.5F, -4.0F, 1.0F, 1.0F, 8.0F, -0.1F, false);
		head.texOffs(9, 16).addBox(-0.5F, -1.5F, -4.25F, 1.0F, 2.0F, 2.0F, 0.0F, false);

		torso.texOffs(0, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
		torso.texOffs(2, 26).addBox(-4.0F, 0.0F, -2.4F, 8.0F, 5.0F, 1.0F, -0.1F, false);
		torso.texOffs(24, 38).addBox(-3.0F, 4.0F, -2.2F, 6.0F, 8.0F, 2.0F, 0.0F, false);

		leftArm.texOffs(0, 108).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
		leftArm.texOffs(0, 102).addBox(1.25F, -2.25F, -1.5F, 1.0F, 3.0F, 3.0F, -0.1F, false);

		leftShoulder = new ModelRenderer(this);
		leftShoulder.setPos(1.75F, -2.0F, 0.75F);
		leftArm.addChild(leftShoulder);
		setRotationAngle(leftShoulder, -0.7854F, 0.0F, 0.0F);
		leftShoulder.texOffs(8, 105).addBox(-0.5F, -1.5F, -0.25F, 1.0F, 2.0F, 1.0F, -0.11F, false);

		leftArmJoint.texOffs(0, 92).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, -0.1F, false);
		leftArmJoint.texOffs(12, 118).addBox(-1.5F, -1.5F, 1.25F, 3.0F, 3.0F, 1.0F, -0.1F, false);

		leftForeArm.texOffs(0, 118).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
		leftForeArm.texOffs(7, 99).addBox(1.25F, 5.25F, -2.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);
		leftForeArm.texOffs(7, 99).addBox(1.25F, 5.25F, -1.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);
		leftForeArm.texOffs(7, 99).addBox(1.25F, 5.25F, 0.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);
		leftForeArm.texOffs(7, 99).addBox(1.25F, 5.25F, 1.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);

		rightArm.texOffs(20, 108).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
		rightArm.texOffs(22, 102).addBox(-2.25F, -2.25F, -1.5F, 1.0F, 3.0F, 3.0F, -0.1F, false);

		rightShoulder = new ModelRenderer(this);
		rightShoulder.setPos(-1.75F, -2.0F, 2.0F);
		rightArm.addChild(rightShoulder);
		setRotationAngle(rightShoulder, -0.7854F, 0.0F, 0.0F);
		rightShoulder.texOffs(30, 105).addBox(-0.5F, -0.7929F, -1.2071F, 1.0F, 2.0F, 1.0F, -0.11F, false);

		rightArmJoint.texOffs(0, 92).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, -0.1F, false);
		rightArmJoint.texOffs(32, 118).addBox(-1.5F, -1.5F, 1.25F, 3.0F, 3.0F, 1.0F, -0.1F, false);

		rightForeArm.texOffs(20, 118).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
		rightForeArm.texOffs(7, 99).addBox(-2.25F, 5.25F, -2.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);
		rightForeArm.texOffs(7, 99).addBox(-2.25F, 5.25F, -1.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);
		rightForeArm.texOffs(7, 99).addBox(-2.25F, 5.25F, 0.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);
		rightForeArm.texOffs(7, 99).addBox(-2.25F, 5.25F, 1.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);

		leftLeg.texOffs(40, 108).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

		leftLegJoint.texOffs(0, 92).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, -0.1F, false);
		leftLegJoint.texOffs(52, 118).addBox(-1.5F, -1.5F, -2.25F, 3.0F, 3.0F, 1.0F, -0.1F, false);

		leftKnee = new ModelRenderer(this);
		leftKnee.setPos(1.0F, -1.25F, -1.75F);
		leftLegJoint.addChild(leftKnee);
		setRotationAngle(leftKnee, 0.0F, 0.0F, 0.7854F);
		leftKnee.texOffs(52, 109).addBox(-0.5F, -1.25F, -0.5F, 1.0F, 2.0F, 1.0F, -0.11F, false);

		leftLowerLeg.texOffs(40, 118).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

		rightLeg.texOffs(60, 108).addBox(-2.1F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

		rightLegJoint.texOffs(0, 92).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, -0.1F, false);
		rightLegJoint.texOffs(72, 118).addBox(-1.5F, -1.5F, -2.25F, 3.0F, 3.0F, 1.0F, -0.1F, false);

		rightKnee = new ModelRenderer(this);
		rightKnee.setPos(-1.0F, -1.25F, -1.75F);
		rightLegJoint.addChild(rightKnee);
		setRotationAngle(rightKnee, 0.0F, 0.0F, -0.7854F);
		rightKnee.texOffs(72, 109).addBox(-0.5F, -1.25F, -0.5F, 1.0F, 2.0F, 1.0F, -0.11F, false);

		rightLowerLeg.texOffs(60, 118).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
	}
}