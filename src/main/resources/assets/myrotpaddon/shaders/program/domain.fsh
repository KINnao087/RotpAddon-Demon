#version 110

uniform sampler2D DiffuseSampler;
uniform float Intensity;  // 0..1  滤镜强度
uniform float Flash;      // 0..1  闪红强度（短促）

varying vec2 texCoord;

float luma(vec3 c) {
    return dot(c, vec3(0.299, 0.587, 0.114));
}

void main() {
    vec4 col = texture2D(DiffuseSampler, texCoord);

    // 基础灰度
    float g = luma(col.rgb);
    vec3 gray = vec3(g);

    // “偏红” mask：红比绿蓝明显才算红（保留红色信息）
    float r = col.r;
    float gb = max(col.g, col.b);
    float redish = clamp(r - gb, 0.0, 1.0);
    redish = smoothstep(0.05, 0.35, redish);

    // 灰 + 红保留（红的地方往红拉）
    vec3 bwRed = mix(gray, vec3(1.0, 0.0, 0.0), redish);

    // 持续滤镜强度
    float t = clamp(Intensity, 0.0, 1.0);
    vec3 outCol = mix(col.rgb, bwRed, t);

    // 闪红：增加红、压绿蓝（不会直接纯红糊屏）
    float f = clamp(Flash, 0.0, 1.0);
    outCol.r = min(1.0, outCol.r + f * 0.75);
    outCol.g = outCol.g * (1.0 - f * 0.65);
    outCol.b = outCol.b * (1.0 - f * 0.65);

    gl_FragColor = vec4(outCol, 1.0);
}
