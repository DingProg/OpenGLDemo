precision highp float;

varying highp vec2 vTexCoord;
uniform sampler2D sTexture;

const  float scaleRatio = 0.5f;// 缩放系数，0无缩放，大于0则放大
const  float radius = 0.5f;// 缩放算法的作用域半径
const  vec2 leftEyeCenterPosition = vec2(0.5f, 0.4f); // 左眼控制点，越远变形越小
const float aspectRatio = 0.56f; // 所处理图像的宽高比

highp vec2 warpPositionToUse(vec2 centerPostion, vec2 currentPosition, float radius, float scaleRatio, float aspectRatio)
{
    vec2 positionToUse = currentPosition;

    vec2 currentPositionToUse = vec2(currentPosition.x, currentPosition.y * aspectRatio + 0.5 - 0.5 * aspectRatio);
    vec2 centerPostionToUse = vec2(centerPostion.x, centerPostion.y * aspectRatio + 0.5 - 0.5 * aspectRatio);

    float r = distance(currentPositionToUse, centerPostionToUse);

    if(r < radius)
    {
        float alpha = 1.0 - scaleRatio * pow(r / radius - 1.0, 2.0);
        positionToUse = centerPostion + alpha * (currentPosition - centerPostion);
    }

    return positionToUse;
}

void main()
{
     vec2 positionToUse = warpPositionToUse(leftEyeCenterPosition, vTexCoord, radius, scaleRatio, aspectRatio);

    //positionToUse = warpPositionToUse(rightEyeCenterPosition, positionToUse, radius, scaleRatio, aspectRatio);

    gl_FragColor = texture2D(sTexture, positionToUse);
}  