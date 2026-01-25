// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports

package com.rotpaddon.exampleaddon.client.render.model;

import com.github.standobyte.jojo.client.render.entity.model.stand.HumanoidStandModel;
import com.rotpaddon.exampleaddon.entity.DemonStandEntity;
import net.minecraft.client.renderer.model.ModelRenderer;

public class DemonStandModel_ extends HumanoidStandModel<DemonStandEntity> {
//	private final ModelRenderer root;
//	private final ModelRenderer head;
	private final ModelRenderer head_r1;
	private final ModelRenderer head_r2;
	private final ModelRenderer horn;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer horn2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;
	private final ModelRenderer horn4;
	private final ModelRenderer cube_r5;
	private final ModelRenderer cube_r6;
	private final ModelRenderer horn7;
	private final ModelRenderer cube_r7;
	private final ModelRenderer cube_r8;
	private final ModelRenderer horn5;
	private final ModelRenderer cube_r9;
	private final ModelRenderer cube_r10;
	private final ModelRenderer cube_r11;
	private final ModelRenderer cube_r12;
	private final ModelRenderer horn6;
	private final ModelRenderer cube_r13;
	private final ModelRenderer cube_r14;
	private final ModelRenderer cube_r15;
	private final ModelRenderer cube_r16;
//	private final ModelRenderer torso;
	private final ModelRenderer torso_r1;
	private final ModelRenderer torso_r2;
	private final ModelRenderer bone15;
	private final ModelRenderer cube_r17;
	private final ModelRenderer cube_r18;
	private final ModelRenderer cube_r19;
	private final ModelRenderer cube_r20;
	private final ModelRenderer cube_r21;
	private final ModelRenderer cube_r22;
	private final ModelRenderer bone;
//	private final ModelRenderer leftArmJoint;
	private final ModelRenderer leftArmJoint_r1;
//	private final ModelRenderer leftArm;
	private final ModelRenderer leftArm_r1;
//	private final ModelRenderer leftForeArm;
	private final ModelRenderer cube_r23;
	private final ModelRenderer cube_r24;
	private final ModelRenderer cube_r25;
	private final ModelRenderer cube_r26;
	private final ModelRenderer cube_r27;
	private final ModelRenderer cube_r28;
	private final ModelRenderer leftShoulder;
	private final ModelRenderer cube_r29;
	private final ModelRenderer cube_r30;
	private final ModelRenderer cube_r31;
	private final ModelRenderer cube_r32;
	private final ModelRenderer cube_r33;
//	private final ModelRenderer rightArmJoint;
	private final ModelRenderer rightArmJoint_r1;
//	private final ModelRenderer rightArm;
	private final ModelRenderer cube_r34;
//	private final ModelRenderer rightForeArm;
	private final ModelRenderer cube_r35;
	private final ModelRenderer cube_r36;
	private final ModelRenderer cube_r37;
	private final ModelRenderer cube_r38;
	private final ModelRenderer cube_r39;
	private final ModelRenderer cube_r40;
	private final ModelRenderer cube_r41;
	private final ModelRenderer rightShoulder;
	private final ModelRenderer cube_r42;
	private final ModelRenderer cube_r43;
	private final ModelRenderer cube_r44;
	private final ModelRenderer bone6;
	private final ModelRenderer cube_r45;
//	private final ModelRenderer leftLegJoint;
//	private final ModelRenderer leftLeg;
//	private final ModelRenderer leftLowerLeg;
//	private final ModelRenderer rightLegJoint;
//	private final ModelRenderer rightLeg;
//	private final ModelRenderer rightLowerLeg;

	public DemonStandModel_() {
		super(256, 256);
		addHumanoidBaseBoxes(null);

		texWidth = 256;
		texHeight = 256;

		// ===== IMPORTANT: don't create / re-parent base parts =====
		// Just set pos/rot if you need to match your blockbench export coordinates.
		// (These are from your commented-out "new ModelRenderer" blocks.)
		// If your pose looks off, tweak these, but DO NOT addChild between base parts.
		// head is already in the base tree, but setPos is safe.
		head.setPos(0.0F, -22.0F, 0.0F);

		torso.setPos(0.0F, -18.0F, 0.0F);

		leftArmJoint.setPos(0.0F, -18.0F, 0.0F);
		setRotationAngle(leftArmJoint, 0.0F, 0.0F, -0.0436F);

		rightArmJoint.setPos(0.0F, -18.0F, 0.0F);
		setRotationAngle(rightArmJoint, 0.0F, 0.0F, 0.0436F);

		leftLegJoint.setPos(-1.0F, 0.0F, 32.0F);
		setRotationAngle(leftLegJoint, 0.0F, -0.0436F, -0.0436F);

		rightLegJoint.setPos(0.0F, -18.0F, 0.0F);

		// =========================
		// Head
		// =========================
		head.texOffs(26, 85).addBox(-4.0F, -29.0F, -5.0F, 6.0F, 9.0F, 9.0F, 0.0F, false);

		head_r1 = new ModelRenderer(this);
		head_r1.setPos(1.0F, -22.0F, 0.0F);
		head.addChild(head_r1);
		setRotationAngle(head_r1, 0.0F, 0.0F, 2.0071F);
		head_r1.texOffs(90, 109).addBox(0.0F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		head_r2 = new ModelRenderer(this);
		head_r2.setPos(0.0F, -23.0F, 0.0F);
		head.addChild(head_r2);
		setRotationAngle(head_r2, 0.0F, 0.0F, 2.3998F);
		head_r2.texOffs(46, 133).addBox(-1.0F, -3.0F, -2.0F, 2.0F, 3.0F, 3.0F, 0.0F, false);

		horn = new ModelRenderer(this);
		horn.setPos(3.0F, 7.0F, -19.0F);
		head.addChild(horn);

		cube_r1 = new ModelRenderer(this);
		cube_r1.setPos(-1.0F, -37.0F, 17.0F);
		horn.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.305F, -0.0671F, 0.0709F);
		cube_r1.texOffs(80, 85).addBox(0.0F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r2 = new ModelRenderer(this);
		cube_r2.setPos(0.0F, -42.0F, 14.0F);
		horn.addChild(cube_r2);
		setRotationAngle(cube_r2, 1.9966F, -0.0389F, 0.049F);
		cube_r2.texOffs(56, 133).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);

		horn2 = new ModelRenderer(this);
		horn2.setPos(2.0F, 7.0F, 18.0F);
		head.addChild(horn2);
		setRotationAngle(horn2, 0.0F, 3.1416F, 0.0F);

		cube_r3 = new ModelRenderer(this);
		cube_r3.setPos(-1.0F, -37.0F, 17.0F);
		horn2.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.305F, -0.0671F, 0.0709F);
		cube_r3.texOffs(80, 88).addBox(0.0F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r4 = new ModelRenderer(this);
		cube_r4.setPos(0.0F, -42.0F, 14.0F);
		horn2.addChild(cube_r4);
		setRotationAngle(cube_r4, 1.9966F, -0.0389F, 0.049F);
		cube_r4.texOffs(132, 133).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);

		horn4 = new ModelRenderer(this);
		horn4.setPos(0.0F, -29.0F, -35.0F);
		head.addChild(horn4);
		setRotationAngle(horn4, -1.309F, 0.0F, 0.0F);

		cube_r5 = new ModelRenderer(this);
		cube_r5.setPos(-1.0F, -37.0F, 17.0F);
		horn4.addChild(cube_r5);
		setRotationAngle(cube_r5, 0.5364F, 0.0297F, 0.1498F);
		cube_r5.texOffs(102, 42).addBox(0.0F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r6 = new ModelRenderer(this);
		cube_r6.setPos(0.0F, -42.0F, 14.0F);
		horn4.addChild(cube_r6);
		setRotationAngle(cube_r6, 1.9966F, -0.0389F, 0.049F);
		cube_r6.texOffs(134, 0).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);

		horn7 = new ModelRenderer(this);
		horn7.setPos(0.0F, 22.0F, 28.0F);
		head.addChild(horn7);
		setRotationAngle(horn7, 0.4363F, 0.0F, 0.0F);

		cube_r7 = new ModelRenderer(this);
		cube_r7.setPos(-1.0F, -55.0F, -11.0F);
		horn7.addChild(cube_r7);
		setRotationAngle(cube_r7, 0.305F, -0.0671F, 0.0709F);
		cube_r7.texOffs(90, 103).addBox(0.0F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r8 = new ModelRenderer(this);
		cube_r8.setPos(0.0F, -60.0F, -14.0F);
		horn7.addChild(cube_r8);
		setRotationAngle(cube_r8, 1.9966F, -0.0389F, 0.049F);
		cube_r8.texOffs(134, 5).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);

		horn5 = new ModelRenderer(this);
		horn5.setPos(0.0F, 3.0F, 0.0F);
		head.addChild(horn5);

		cube_r9 = new ModelRenderer(this);
		cube_r9.setPos(-2.0F, -38.0F, 11.0F);
		horn5.addChild(cube_r9);
		setRotationAngle(cube_r9, -0.3488F, 0.0149F, 0.041F);
		cube_r9.texOffs(90, 106).addBox(0.0F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r10 = new ModelRenderer(this);
		cube_r10.setPos(0.0F, -43.0F, -14.0F);
		horn5.addChild(cube_r10);
		setRotationAngle(cube_r10, 1.9966F, -0.0389F, 0.049F);
		cube_r10.texOffs(134, 10).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);

		cube_r11 = new ModelRenderer(this);
		cube_r11.setPos(-1.0F, -34.0F, -9.0F);
		horn5.addChild(cube_r11);
		setRotationAngle(cube_r11, 2.2023F, -0.0324F, -0.0319F);
		cube_r11.texOffs(114, 125).addBox(-1.0F, -2.0F, -3.0F, 3.0F, 3.0F, 6.0F, 0.0F, false);

		cube_r12 = new ModelRenderer(this);
		cube_r12.setPos(-1.0F, -31.0F, -7.0F);
		horn5.addChild(cube_r12);
		setRotationAngle(cube_r12, 2.5202F, -0.0389F, 0.049F);
		cube_r12.texOffs(108, 55).addBox(-2.0F, -2.0F, -8.0F, 4.0F, 4.0F, 10.0F, 0.0F, false);

		horn6 = new ModelRenderer(this);
		horn6.setPos(-2.0F, 3.0F, -1.0F);
		head.addChild(horn6);
		setRotationAngle(horn6, 0.0F, -3.098F, 0.0F);

		cube_r13 = new ModelRenderer(this);
		cube_r13.setPos(-3.0F, -38.0F, 11.0F);
		horn6.addChild(cube_r13);
		setRotationAngle(cube_r13, -0.3488F, 0.0149F, 0.041F);
		cube_r13.texOffs(48, 128).addBox(0.0F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		cube_r14 = new ModelRenderer(this);
		cube_r14.setPos(0.0F, -43.0F, -14.0F);
		horn6.addChild(cube_r14);
		setRotationAngle(cube_r14, 1.9966F, -0.0389F, 0.049F);
		cube_r14.texOffs(112, 134).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);

		cube_r15 = new ModelRenderer(this);
		cube_r15.setPos(-1.0F, -34.0F, -9.0F);
		horn6.addChild(cube_r15);
		setRotationAngle(cube_r15, 2.2023F, -0.0324F, -0.0319F);
		cube_r15.texOffs(0, 126).addBox(-1.0F, -2.0F, -3.0F, 3.0F, 3.0F, 6.0F, 0.0F, false);

		cube_r16 = new ModelRenderer(this);
		cube_r16.setPos(-1.0F, -31.0F, -7.0F);
		horn6.addChild(cube_r16);
		setRotationAngle(cube_r16, 2.5202F, -0.0389F, 0.049F);
		cube_r16.texOffs(108, 21).addBox(-2.0F, -2.0F, -8.0F, 4.0F, 4.0F, 10.0F, 0.0F, false);

		// =========================
		// Torso
		// =========================
		torso.texOffs(0, 67).addBox(-5.0F, -11.0F, -7.0F, 8.0F, 3.0F, 13.0F, 0.0F, false);
		torso.texOffs(78, 0).addBox(-5.0F, -13.0F, -6.0F, 8.0F, 2.0F, 11.0F, 0.0F, false);
		torso.texOffs(42, 69).addBox(-5.0F, -16.0F, -7.0F, 8.0F, 3.0F, 13.0F, 0.0F, false);
		torso.texOffs(62, 46).addBox(-5.0F, -24.0F, -8.0F, 8.0F, 8.0F, 15.0F, 0.0F, false);

		torso_r1 = new ModelRenderer(this);
		torso_r1.setPos(2.0F, -28.0F, 0.0F);
		torso.addChild(torso_r1);
		setRotationAngle(torso_r1, 0.0F, 0.0F, 1.6144F);
		torso_r1.texOffs(132, 125).addBox(4.0F, -2.0F, -1.0F, 7.0F, 2.0F, 1.0F, 0.0F, false);

		torso_r2 = new ModelRenderer(this);
		torso_r2.setPos(1.0F, -20.0F, 0.0F);
		torso.addChild(torso_r2);
		setRotationAngle(torso_r2, 0.0F, 0.0F, 1.4835F);
		torso_r2.texOffs(32, 133).addBox(7.0F, -2.0F, -1.0F, 6.0F, 2.0F, 1.0F, 0.0F, false);

		bone15 = new ModelRenderer(this);
		bone15.setPos(0.0F, 0.0F, 0.0F);
		torso.addChild(bone15);
		bone15.texOffs(64, 24).addBox(-5.0F, -7.0F, -8.0F, 8.0F, 3.0F, 15.0F, 0.0F, false);

		cube_r17 = new ModelRenderer(this);
		cube_r17.setPos(-4.0F, -9.0F, 13.0F);
		bone15.addChild(cube_r17);
		setRotationAngle(cube_r17, -2.4368F, 1.5519F, 2.6899F);
		cube_r17.texOffs(0, 0).addBox(6.0F, 0.0F, -25.0F, 15.0F, 0.0F, 24.0F, 0.0F, false);

		cube_r18 = new ModelRenderer(this);
		cube_r18.setPos(-16.0F, -10.0F, -7.0F);
		bone15.addChild(cube_r18);
		setRotationAngle(cube_r18, 1.3931F, 0.0164F, -0.0142F);
		cube_r18.texOffs(0, 46).addBox(11.0F, 0.0F, -25.0F, 10.0F, 0.0F, 21.0F, 0.0F, false);

		cube_r19 = new ModelRenderer(this);
		cube_r19.setPos(4.0F, -5.0F, -15.0F);
		bone15.addChild(cube_r19);
		setRotationAngle(cube_r19, -0.4143F, -1.5538F, -0.5889F);
		cube_r19.texOffs(110, 81).addBox(7.0F, 0.0F, -1.0F, 15.0F, 0.0F, 4.0F, 0.0F, false);
		cube_r19.texOffs(64, 42).addBox(7.0F, 0.0F, -1.0F, 15.0F, 0.0F, 4.0F, 0.0F, false);

		cube_r20 = new ModelRenderer(this);
		cube_r20.setPos(13.0F, -10.0F, 7.0F);
		bone15.addChild(cube_r20);
		setRotationAngle(cube_r20, -1.7371F, 0.0294F, 3.1335F);
		cube_r20.texOffs(0, 24).addBox(8.0F, 0.0F, -25.0F, 10.0F, 0.0F, 22.0F, 0.0F, false);

		cube_r21 = new ModelRenderer(this);
		cube_r21.setPos(3.0F, -6.0F, -15.0F);
		bone15.addChild(cube_r21);
		setRotationAngle(cube_r21, 0.9819F, -1.5538F, -0.5889F);
		cube_r21.texOffs(110, 37).addBox(7.0F, 0.0F, -1.0F, 15.0F, 0.0F, 4.0F, 0.0F, false);

		cube_r22 = new ModelRenderer(this);
		cube_r22.setPos(4.0F, -7.0F, -15.0F);
		bone15.addChild(cube_r22);
		setRotationAngle(cube_r22, 1.5055F, -1.5538F, -0.5889F);
		cube_r22.texOffs(64, 42).addBox(7.0F, 0.0F, -1.0F, 15.0F, 0.0F, 4.0F, 0.0F, false);

		bone = new ModelRenderer(this);
		bone.setPos(-3.0F, 14.0F, 5.0F);
		bone15.addChild(bone);

		// =========================
		// Left arm joint / arm / forearm
		// =========================
		leftArmJoint_r1 = new ModelRenderer(this);
		leftArmJoint_r1.setPos(0.0F, -24.0F, 10.0F);
		leftArmJoint.addChild(leftArmJoint_r1);
		setRotationAngle(leftArmJoint_r1, 0.0F, 0.0436F, 0.0F);
		leftArmJoint_r1.texOffs(26, 103).addBox(-5.0F, -2.0F, -3.0F, 9.0F, 5.0F, 7.0F, 0.0F, false);

		// DO NOT re-parent: leftArmJoint.addChild(leftArm)  (base already has correct hierarchy)
		leftArm_r1 = new ModelRenderer(this);
		leftArm_r1.setPos(-5.0F, -17.0F, 8.0F);
		leftArm.addChild(leftArm_r1);
		setRotationAngle(leftArm_r1, -0.769F, -1.4503F, 0.805F);
		leftArm_r1.texOffs(0, 107).addBox(-2.0F, -6.0F, -5.0F, 6.0F, 13.0F, 6.0F, 0.0F, false);

		// Forearm boxes: use BASE leftForeArm (no shadow field)
		leftForeArm.setPos(0.0F, 0.0F, 0.0F);
		leftForeArm.texOffs(116, 0).addBox(-8.0F, -12.0F, 5.0F, 2.0F, 8.0F, 7.0F, 0.0F, false);

		cube_r23 = new ModelRenderer(this);
		cube_r23.setPos(1.0F, -13.0F, 5.0F);
		leftForeArm.addChild(cube_r23);
		setRotationAngle(cube_r23, 0.0059F, 0.0848F, 0.0039F);
		cube_r23.texOffs(52, 118).addBox(-1.0F, 1.0F, -1.0F, 2.0F, 8.0F, 7.0F, 0.0F, false);

		cube_r24 = new ModelRenderer(this);
		cube_r24.setPos(-3.0F, -11.0F, 4.0F);
		leftForeArm.addChild(cube_r24);
		setRotationAngle(cube_r24, 0.0F, -1.5272F, 0.0F);
		cube_r24.texOffs(90, 112).addBox(-1.0F, -1.0F, -5.0F, 2.0F, 8.0F, 10.0F, 0.0F, false);

		cube_r25 = new ModelRenderer(this);
		cube_r25.setPos(-7.0F, -17.0F, 2.0F);
		leftForeArm.addChild(cube_r25);
		setRotationAngle(cube_r25, 0.4808F, 0.042F, 0.0284F);
		cube_r25.texOffs(122, 134).addBox(2.0F, -3.0F, -1.0F, 3.0F, 3.0F, 2.0F, 0.0F, false);

		cube_r26 = new ModelRenderer(this);
		cube_r26.setPos(-7.0F, -14.0F, 3.0F);
		leftForeArm.addChild(cube_r26);
		setRotationAngle(cube_r26, 0.4372F, 0.042F, 0.0284F);
		cube_r26.texOffs(32, 128).addBox(1.0F, -3.0F, -1.0F, 6.0F, 3.0F, 2.0F, 0.0F, false);

		cube_r27 = new ModelRenderer(this);
		cube_r27.setPos(-7.0F, -10.0F, 5.0F);
		leftForeArm.addChild(cube_r27);
		setRotationAngle(cube_r27, 0.5244F, 0.042F, 0.0284F);
		cube_r27.texOffs(56, 85).addBox(-1.0F, -5.0F, -1.0F, 10.0F, 5.0F, 2.0F, 0.0F, false);

		cube_r28 = new ModelRenderer(this);
		cube_r28.setPos(-4.0F, -9.0F, 11.0F);
		leftForeArm.addChild(cube_r28);
		setRotationAngle(cube_r28, 0.0907F, 0.0003F, 1.6067F);
		cube_r28.texOffs(110, 69).addBox(-1.0F, -4.0F, -6.0F, 9.0F, 6.0F, 6.0F, 0.0F, false);

		leftShoulder = new ModelRenderer(this);
		leftShoulder.setPos(0.0F, 0.0F, 0.0F);
		leftArm.addChild(leftShoulder);

		cube_r29 = new ModelRenderer(this);
		cube_r29.setPos(-2.0F, -24.0F, 4.0F);
		leftShoulder.addChild(cube_r29);
		setRotationAngle(cube_r29, 2.5202F, -0.0389F, 0.049F);
		cube_r29.texOffs(114, 112).addBox(-3.0F, -2.0F, -9.0F, 5.0F, 4.0F, 9.0F, 0.0F, false);

		cube_r30 = new ModelRenderer(this);
		cube_r30.setPos(-2.0F, -27.0F, 2.0F);
		leftShoulder.addChild(cube_r30);
		setRotationAngle(cube_r30, 2.2023F, -0.0324F, -0.0319F);
		cube_r30.texOffs(18, 128).addBox(-2.0F, -2.0F, -4.0F, 3.0F, 3.0F, 4.0F, 0.0F, false);

		cube_r31 = new ModelRenderer(this);
		cube_r31.setPos(-1.0F, -35.0F, -1.0F);
		leftShoulder.addChild(cube_r31);
		setRotationAngle(cube_r31, 1.9966F, -0.0389F, 0.049F);
		cube_r31.texOffs(10, 135).addBox(-1.0F, -2.0F, -9.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		cube_r32 = new ModelRenderer(this);
		cube_r32.setPos(-3.0F, -25.0F, 11.0F);
		leftShoulder.addChild(cube_r32);
		setRotationAngle(cube_r32, -3.1416F, 0.0436F, 3.1416F);
		cube_r32.texOffs(18, 135).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		cube_r33 = new ModelRenderer(this);
		cube_r33.setPos(-3.0F, -23.0F, 11.0F);
		leftShoulder.addChild(cube_r33);
		setRotationAngle(cube_r33, 2.8798F, 0.0436F, 3.1416F);
		cube_r33.texOffs(100, 130).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 2.0F, 3.0F, 0.0F, false);

		// =========================
		// Right arm joint / arm / forearm
		// =========================
		rightArmJoint_r1 = new ModelRenderer(this);
		rightArmJoint_r1.setPos(-1.0F, -23.0F, -11.0F);
		rightArmJoint.addChild(rightArmJoint_r1);
		setRotationAngle(rightArmJoint_r1, -0.1745F, 0.0436F, 0.0F);
		rightArmJoint_r1.texOffs(58, 103).addBox(-5.0F, -2.0F, -3.0F, 9.0F, 5.0F, 7.0F, 0.0F, false);

		cube_r34 = new ModelRenderer(this);
		cube_r34.setPos(-2.0F, -17.0F, -9.0F);
		rightArm.addChild(cube_r34);
		setRotationAngle(cube_r34, -0.1309F, 0.0F, 0.0F);
		cube_r34.texOffs(94, 93).addBox(-3.0F, -6.0F, -5.0F, 7.0F, 13.0F, 6.0F, 0.0F, false);

		// Forearm: use BASE rightForeArm (no new / no re-parent)
		rightForeArm.setPos(0.0F, 0.0F, 0.0F);

		cube_r35 = new ModelRenderer(this);
		cube_r35.setPos(-2.0F, -9.0F, -9.0F);
		rightForeArm.addChild(cube_r35);
		setRotationAngle(cube_r35, 0.0F, 0.1745F, 1.5708F);
		cube_r35.texOffs(108, 42).addBox(-1.0F, -4.0F, -6.0F, 9.0F, 7.0F, 6.0F, 0.0F, false);

		cube_r36 = new ModelRenderer(this);
		cube_r36.setPos(-6.0F, -12.0F, -8.0F);
		rightForeArm.addChild(cube_r36);
		setRotationAngle(cube_r36, -1.3765F, 1.3484F, -1.3717F);
		cube_r36.texOffs(120, 100).addBox(0.0F, 0.0F, -1.0F, 8.0F, 8.0F, 2.0F, 0.0F, false);

		cube_r37 = new ModelRenderer(this);
		cube_r37.setPos(-5.0F, -19.0F, -18.0F);
		rightForeArm.addChild(cube_r37);
		setRotationAngle(cube_r37, 0.3474F, -0.0411F, -0.0095F);
		cube_r37.texOffs(0, 135).addBox(2.0F, -3.0F, -1.0F, 3.0F, 3.0F, 2.0F, 0.0F, false);

		cube_r38 = new ModelRenderer(this);
		cube_r38.setPos(4.0F, -13.0F, -15.0F);
		rightForeArm.addChild(cube_r38);
		setRotationAngle(cube_r38, -1.7651F, -1.3484F, 1.7698F);
		cube_r38.texOffs(70, 118).addBox(-1.0F, 0.0F, -1.0F, 8.0F, 8.0F, 2.0F, 0.0F, false);

		cube_r39 = new ModelRenderer(this);
		cube_r39.setPos(-4.0F, -13.0F, -15.0F);
		rightForeArm.addChild(cube_r39);
		setRotationAngle(cube_r39, -0.2182F, 0.0F, 0.0F);
		cube_r39.texOffs(120, 91).addBox(-1.0F, 1.0F, -1.0F, 8.0F, 7.0F, 2.0F, 0.0F, false);

		cube_r40 = new ModelRenderer(this);
		cube_r40.setPos(-5.0F, -12.0F, -15.0F);
		rightForeArm.addChild(cube_r40);
		setRotationAngle(cube_r40, 0.391F, -0.0411F, -0.0095F);
		cube_r40.texOffs(116, 15).addBox(-1.0F, -5.0F, -1.0F, 10.0F, 5.0F, 2.0F, 0.0F, false);

		cube_r41 = new ModelRenderer(this);
		cube_r41.setPos(-5.0F, -16.0F, -17.0F);
		rightForeArm.addChild(cube_r41);
		setRotationAngle(cube_r41, 0.3037F, -0.0411F, -0.0095F);
		cube_r41.texOffs(84, 130).addBox(1.0F, -3.0F, -1.0F, 6.0F, 3.0F, 2.0F, 0.0F, false);

		rightShoulder = new ModelRenderer(this);
		rightShoulder.setPos(0.0F, 0.0F, 0.0F);
		rightArm.addChild(rightShoulder);

		cube_r42 = new ModelRenderer(this);
		cube_r42.setPos(-1.0F, -25.0F, -15.0F);
		rightShoulder.addChild(cube_r42);
		setRotationAngle(cube_r42, 2.5202F, -0.0389F, 0.049F);
		cube_r42.texOffs(24, 115).addBox(-3.0F, -2.0F, -9.0F, 5.0F, 4.0F, 9.0F, 0.0F, false);

		cube_r43 = new ModelRenderer(this);
		cube_r43.setPos(-1.0F, -28.0F, -17.0F);
		rightShoulder.addChild(cube_r43);
		setRotationAngle(cube_r43, 2.2023F, -0.0324F, -0.0319F);
		cube_r43.texOffs(70, 128).addBox(-2.0F, -2.0F, -4.0F, 3.0F, 3.0F, 4.0F, 0.0F, false);

		cube_r44 = new ModelRenderer(this);
		cube_r44.setPos(-1.0F, -36.0F, -20.0F);
		rightShoulder.addChild(cube_r44);
		setRotationAngle(cube_r44, 1.9966F, -0.0389F, 0.049F);
		cube_r44.texOffs(74, 135).addBox(-1.0F, -2.0F, -9.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		bone6 = new ModelRenderer(this);
		bone6.setPos(-2.0F, 0.0F, 0.0F);
		rightShoulder.addChild(bone6);
		setRotationAngle(bone6, 0.0F, -3.098F, 0.0F);
		bone6.texOffs(66, 135).addBox(-2.0F, -27.0F, 7.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		cube_r45 = new ModelRenderer(this);
		cube_r45.setPos(-1.0F, -23.0F, 8.0F);
		bone6.addChild(cube_r45);
		setRotationAngle(cube_r45, -0.2618F, 0.0F, 0.0F);
		cube_r45.texOffs(132, 128).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 2.0F, 3.0F, 0.0F, false);

		// =========================
		// Legs (base parts only; do not re-parent)
		// =========================
		leftLeg.texOffs(84, 69).addBox(-5.0F, -22.0F, -32.0F, 7.0F, 11.0F, 6.0F, 0.0F, false);

		leftLowerLeg.texOffs(56, 93).addBox(-5.0F, -4.0F, -32.0F, 13.0F, 4.0F, 6.0F, 0.0F, false);
		leftLowerLeg.texOffs(84, 69).addBox(-5.0F, -11.0F, -32.0F, 7.0F, 7.0F, 6.0F, 0.0F, false);

		rightLeg.texOffs(84, 69).addBox(-6.0F, -4.0F, -8.0F, 7.0F, 11.0F, 6.0F, 0.0F, false);

		rightLowerLeg.texOffs(56, 93).addBox(-6.0F, 14.0F, -8.0F, 13.0F, 4.0F, 6.0F, 0.0F, false);
		rightLowerLeg.texOffs(84, 69).addBox(-6.0F, 7.0F, -8.0F, 7.0F, 7.0F, 6.0F, 0.0F, false);
	}
}