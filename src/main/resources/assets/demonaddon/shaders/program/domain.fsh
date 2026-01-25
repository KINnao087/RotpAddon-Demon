#version 110

uniform sampler2D DiffuseSampler;

uniform float Intensity;  // 0..1
uniform float Flash;      // 0..1

// 可选：你也可以不加这个 uniform，下面直接写死 1.0
uniform float KeepRed;    // 0/1 是否保留红色（Defile风格）

varying vec2 texCoord;

float luma(vec3 c) {
    return dot(c, vec3(0.299, 0.587, 0.114));
}

// 简化版 rgb->hsv（自己写的，不抄 Defile 那份）
vec3 rgb2hsv_simple(vec3 c) {
    float cmax = max(c.r, max(c.g, c.b));
    float cmin = min(c.r, min(c.g, c.b));
    float d = cmax - cmin;

    float h = 0.0;
    if (d > 1e-6) {
        if (cmax == c.r) h = mod((c.g - c.b) / d, 6.0);
        else if (cmax == c.g) h = ((c.b - c.r) / d) + 2.0;
        else h = ((c.r - c.g) / d) + 4.0;
        h /= 6.0;
        if (h < 0.0) h += 1.0;
    }
    float s = (cmax <= 1e-6) ? 0.0 : (d / cmax);
    float v = cmax;
    return vec3(h, s, v);
}

void main() {
    vec3 src = texture2D(DiffuseSampler, texCoord).rgb;

    float t = clamp(Intensity, 0.0, 1.0);
    float f = clamp(Flash, 0.0, 1.0);

    // 目标灰度
    float g = luma(src);
    vec3 gray = vec3(g);

    // ===== Defile核心：红色区域豁免去色 =====
    // “红”的 HSV 条件：h 接近 0 或 1 + 饱和度较高
    vec3 hsv = rgb2hsv_simple(src);
    float h = hsv.x;
    float s = hsv.y;
    float v = hsv.z;

    // redRange：h 在 [0.92,1.0] 或 [0.0,0.06] 且 s>0.5
    float redRange = 0.0;
    if ((h > 0.92 || h < 0.06) && s > 0.50 && v > 0.10) {
        redRange = 1.0;
    }

    // KeepRed=1 时：红色区域保持原色，其余做去色
    float kr = KeepRed; // 你不想加 uniform 就改成 1.0
    vec3 base = mix(src, gray, t);                 // 普通去色
    vec3 keep = mix(base, src, redRange * kr * t); // 红色豁免

    vec3 outCol = keep;

    // 闪红（你原来的逻辑）
    outCol.r = min(1.0, outCol.r + f * 0.75 * t);
    outCol.g = outCol.g * (1.0 - f * 0.65 * t);
    outCol.b = outCol.b * (1.0 - f * 0.65 * t);

    gl_FragColor = vec4(outCol, 1.0);
}
