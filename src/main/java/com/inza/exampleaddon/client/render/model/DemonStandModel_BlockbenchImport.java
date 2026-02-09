package com.inza.exampleaddon.client.render.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Blockbench import helper model.
 *
 * This file is only for re-importing/editing UV in Blockbench.
 * It is not used by the in-game renderer.
 */
public class DemonStandModel_BlockbenchImport extends EntityModel<Entity> {
    private final ModelRenderer root;
    private final ModelRenderer head;
    private final ModelRenderer torso;
    private final ModelRenderer leftArm;
    private final ModelRenderer rightArm;
    private final ModelRenderer leftForeArm;
    private final ModelRenderer rightForeArm;
    private final ModelRenderer leftLeg;
    private final ModelRenderer rightLeg;
    private final ModelRenderer leftLowerLeg;
    private final ModelRenderer rightLowerLeg;
    private final ModelRenderer leftArmJoint;
    private final ModelRenderer rightArmJoint;
    private final ModelRenderer leftLegJoint;
    private final ModelRenderer rightLegJoint;
    private final ModelRenderer leftShoulder;
    private final ModelRenderer rightShoulder;
    private final ModelRenderer leftKnee;
    private final ModelRenderer rightKnee;

    public DemonStandModel_BlockbenchImport() {
        texWidth = 128;
        texHeight = 128;

        root = new ModelRenderer(this);
        root.setPos(0.0F, 24.0F, 0.0F);

        head = new ModelRenderer(this);
        head.setPos(0.0F, -24.0F, 0.0F);
        root.addChild(head);
        head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        head.texOffs(26, 9).addBox(-3.5F, -8.5F, -4.0F, 1.0F, 1.0F, 8.0F, -0.1F, false);
        head.texOffs(26, 9).addBox(2.5F, -8.5F, -4.0F, 1.0F, 1.0F, 8.0F, -0.1F, false);
        head.texOffs(9, 16).addBox(-0.5F, -1.5F, -4.25F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        torso = new ModelRenderer(this);
        torso.setPos(0.0F, -24.0F, 0.0F);
        root.addChild(torso);
        torso.texOffs(0, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
        torso.texOffs(2, 26).addBox(-4.0F, 0.0F, -2.4F, 8.0F, 5.0F, 1.0F, -0.1F, false);
        torso.texOffs(24, 38).addBox(-3.0F, 4.0F, -2.2F, 6.0F, 8.0F, 2.0F, 0.0F, false);

        leftArmJoint = new ModelRenderer(this);
        leftArmJoint.setPos(6.0F, -22.0F, 0.0F);
        root.addChild(leftArmJoint);
        leftArmJoint.texOffs(0, 92).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, -0.1F, false);
        leftArmJoint.texOffs(12, 118).addBox(-1.5F, -1.5F, 1.25F, 3.0F, 3.0F, 1.0F, -0.1F, false);

        leftArm = new ModelRenderer(this);
        leftArm.setPos(0.0F, 0.0F, 0.0F);
        leftArmJoint.addChild(leftArm);
        leftArm.texOffs(0, 108).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
        leftArm.texOffs(0, 102).addBox(1.25F, -2.25F, -1.5F, 1.0F, 3.0F, 3.0F, -0.1F, false);

        leftShoulder = new ModelRenderer(this);
        leftShoulder.setPos(1.75F, -2.0F, 0.75F);
        leftArm.addChild(leftShoulder);
        setRotationAngle(leftShoulder, -0.7854F, 0.0F, 0.0F);
        leftShoulder.texOffs(8, 105).addBox(-0.5F, -1.5F, -0.25F, 1.0F, 2.0F, 1.0F, -0.11F, false);

        leftForeArm = new ModelRenderer(this);
        leftForeArm.setPos(0.0F, 4.0F, 0.0F);
        leftArm.addChild(leftForeArm);
        leftForeArm.texOffs(0, 118).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
        leftForeArm.texOffs(7, 99).addBox(1.25F, 5.25F, -2.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);
        leftForeArm.texOffs(7, 99).addBox(1.25F, 5.25F, -1.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);
        leftForeArm.texOffs(7, 99).addBox(1.25F, 5.25F, 0.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);
        leftForeArm.texOffs(7, 99).addBox(1.25F, 5.25F, 1.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);

        rightArmJoint = new ModelRenderer(this);
        rightArmJoint.setPos(-6.0F, -22.0F, 0.0F);
        root.addChild(rightArmJoint);
        rightArmJoint.texOffs(0, 92).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, -0.1F, false);
        rightArmJoint.texOffs(32, 118).addBox(-1.5F, -1.5F, 1.25F, 3.0F, 3.0F, 1.0F, -0.1F, false);

        rightArm = new ModelRenderer(this);
        rightArm.setPos(0.0F, 0.0F, 0.0F);
        rightArmJoint.addChild(rightArm);
        rightArm.texOffs(20, 108).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
        rightArm.texOffs(22, 102).addBox(-2.25F, -2.25F, -1.5F, 1.0F, 3.0F, 3.0F, -0.1F, false);

        rightShoulder = new ModelRenderer(this);
        rightShoulder.setPos(-1.75F, -2.0F, 2.0F);
        rightArm.addChild(rightShoulder);
        setRotationAngle(rightShoulder, -0.7854F, 0.0F, 0.0F);
        rightShoulder.texOffs(30, 105).addBox(-0.5F, -0.7929F, -1.2071F, 1.0F, 2.0F, 1.0F, -0.11F, false);

        rightForeArm = new ModelRenderer(this);
        rightForeArm.setPos(0.0F, 4.0F, 0.0F);
        rightArm.addChild(rightForeArm);
        rightForeArm.texOffs(20, 118).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
        rightForeArm.texOffs(7, 99).addBox(-2.25F, 5.25F, -2.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);
        rightForeArm.texOffs(7, 99).addBox(-2.25F, 5.25F, -1.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);
        rightForeArm.texOffs(7, 99).addBox(-2.25F, 5.25F, 0.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);
        rightForeArm.texOffs(7, 99).addBox(-2.25F, 5.25F, 1.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);

        leftLegJoint = new ModelRenderer(this);
        leftLegJoint.setPos(2.0F, -12.0F, 0.0F);
        root.addChild(leftLegJoint);
        leftLegJoint.texOffs(0, 92).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, -0.1F, false);
        leftLegJoint.texOffs(52, 118).addBox(-1.5F, -1.5F, -2.25F, 3.0F, 3.0F, 1.0F, -0.1F, false);

        leftLeg = new ModelRenderer(this);
        leftLeg.setPos(0.0F, 0.0F, 0.0F);
        leftLegJoint.addChild(leftLeg);
        leftLeg.texOffs(40, 108).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

        leftKnee = new ModelRenderer(this);
        leftKnee.setPos(1.0F, -1.25F, -1.75F);
        leftLegJoint.addChild(leftKnee);
        setRotationAngle(leftKnee, 0.0F, 0.0F, 0.7854F);
        leftKnee.texOffs(52, 109).addBox(-0.5F, -1.25F, -0.5F, 1.0F, 2.0F, 1.0F, -0.11F, false);

        leftLowerLeg = new ModelRenderer(this);
        leftLowerLeg.setPos(0.0F, 6.0F, 0.0F);
        leftLeg.addChild(leftLowerLeg);
        leftLowerLeg.texOffs(40, 118).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

        rightLegJoint = new ModelRenderer(this);
        rightLegJoint.setPos(-2.0F, -12.0F, 0.0F);
        root.addChild(rightLegJoint);
        rightLegJoint.texOffs(0, 92).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, -0.1F, false);
        rightLegJoint.texOffs(72, 118).addBox(-1.5F, -1.5F, -2.25F, 3.0F, 3.0F, 1.0F, -0.1F, false);

        rightLeg = new ModelRenderer(this);
        rightLeg.setPos(0.0F, 0.0F, 0.0F);
        rightLegJoint.addChild(rightLeg);
        rightLeg.texOffs(60, 108).addBox(-2.1F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

        rightKnee = new ModelRenderer(this);
        rightKnee.setPos(-1.0F, -1.25F, -1.75F);
        rightLegJoint.addChild(rightKnee);
        setRotationAngle(rightKnee, 0.0F, 0.0F, -0.7854F);
        rightKnee.texOffs(72, 109).addBox(-0.5F, -1.25F, -0.5F, 1.0F, 2.0F, 1.0F, -0.11F, false);

        rightLowerLeg = new ModelRenderer(this);
        rightLowerLeg.setPos(0.0F, 6.0F, 0.0F);
        rightLeg.addChild(rightLowerLeg);
        rightLowerLeg.texOffs(60, 118).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // Import helper: animation not required.
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        root.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    private void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
