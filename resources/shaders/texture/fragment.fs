#version 330

in  vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform float opacity;

float getRGBChannel(float a_b, float a_s, float rgb_b, float rgb_s);
vec4 blend(vec4 c1, vec4 c2);

void main()
{
    //vec4 alpha = texture(texture_sampler, outTexCoord).aaaa;

    //vec4 negalpha = alpha*vec4(-1,-1,-1,1)+vec4(1,1,1,0);
    //fragColor = alpha*gl_Color+negalpha*vec4(1,1,1,0);

    fragColor = texture(texture_sampler, outTexCoord);
    fragColor.w = fragColor.w*opacity;



    if(fragColor.w < 0.02)
        discard;

    //blend(fragColor, fragColor);

    //gl_fragColor = blend(gl_fragColor, fragColor);
}


//float getAlphaChannel(float a_b, float a_s) {
//    return a_b + a_s - a_b * a_s;
//}

float getRGBChannel(float a_b, float a_s, float rgb_b, float rgb_s) {
    return  (1 - a_s) * a_b * rgb_b + a_s * rgb_s;
}
vec4 blend(vec4 c1, vec4 c2) {
    if(c2.w==1) {
        return c2;
    } else if(c2.w==0) {
        return c1;
    } else {
        float A3 = c1.w + c2.w - (c1.w*c2.w);

        float R3 = getRGBChannel(c1.w, c2.w, c1.x, c2.x) / A3;
        float G3 = getRGBChannel(c1.w, c2.w, c1.y, c2.y) / A3;
        float B3 = getRGBChannel(c1.w, c2.w, c1.z, c2.z) / A3;

        return vec4(R3/255, G3/255, B3/255, A3);
    }
}