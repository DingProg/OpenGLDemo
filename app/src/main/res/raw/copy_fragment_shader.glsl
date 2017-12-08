uniform sampler2D sTexture;
varying vec2 vTexCoord;

const float in_circle_radius =250.;             //从客户端传入的放大镜圆半径
const float in_zoom_times = 1.;                 //从客户端传入的放大镜放大倍数
  
const float imageWidth = 1080.;                  //从客户端传入的图片宽数据
const float imageHeight = 1920.;                 //从客户端传入的图片高数据
vec2 in_circle_pos = vec2(600., 900.);    //从客户端传入的放大镜圆心位置
  
  
// 转换为纹理范围  
vec2 transForTexPosition(vec2 pos)  
{  
    return vec2(float(pos.x/imageWidth), float(pos.y/imageHeight));  
}  
  
// Distance of Points  
float getDistance(vec2 pos_src, vec2 pos_dist)  
{  
    float quadratic_sum = pow((pos_src.x - pos_dist.x), 2.) + pow((pos_src.y - pos_dist.y), 2.);  
    return sqrt(quadratic_sum);  
}  
   
vec2 getZoomPosition(float distance)
{   //zoom_times>1. 是放大， 0.< zoom_times <1.是缩小
//    float zoom_x = float(gl_FragCoord.x-in_circle_pos.x) / in_zoom_times;
//    float zoom_y = float(in_circle_pos.y - gl_FragCoord.y) / in_zoom_times;
//
//    return vec2(float(in_circle_pos.x + zoom_x), float(in_circle_pos.y + zoom_y));

       float sinA = float(gl_FragCoord.x-in_circle_pos.x) / distance;
       float cosA = float(in_circle_pos.y - gl_FragCoord.y) / distance;
       float scaleFactor = float(distance/in_circle_radius - 1.0f);
       scaleFactor = float(1.0f - scaleFactor * scaleFactor * (distance/ in_circle_radius) * 0.2f);
       distance = distance * scaleFactor;

       float x = in_circle_pos.x +  distance * sinA;
       if(x < 0.0f){
            x = 0.0f;
       }else if(x > imageWidth){
          x = imageWidth;
       }
       float y = in_circle_pos.y + distance * cosA;
         if(y < 0.0f){
             y = 0.0f;
         }else if(y > imageHeight){
             y = imageWidth;
         }
       return vec2(x,y);

}  
  
vec4 getColor(float distance)
{  
     vec2 pos = getZoomPosition(distance);
    //vec2 pos = vec2(gl_FragCoord.x,1.0f-gl_FragCoord.y);
  
    float _x = floor(pos.x);  
    float _y = floor(pos.y);  
  
    float u = pos.x - _x;  
    float v = pos.y - _y;  
    //双线性插值采样  
    vec4 data_00 = texture2D(sTexture, transForTexPosition(vec2(_x, _y)));
  
    vec4 data_01 = texture2D(sTexture, transForTexPosition(vec2(_x, _y + 1.)));
  
    vec4 data_10 = texture2D(sTexture, transForTexPosition(vec2(_x + 1., _y)));
  
    vec4 data_11 = texture2D(sTexture, transForTexPosition(vec2(_x + 1., _y + 1.)));
  
    return (1. - u) * (1. - v) * data_00 + (1. - u) * v * data_01 + u * (1. - v) * data_10 + u * v * data_11;  
  
}  
  
void main(void)  
{   
  vec2 frag_pos = vec2(gl_FragCoord.x, gl_FragCoord.y);
  //若当前片段位置距放大镜圆心距离大于圆半径时，直接从纹理中采样输出片段颜色   

  float distance = getDistance(in_circle_pos, frag_pos);
  if (distance > in_circle_radius)
    gl_FragColor = texture2D(sTexture, vTexCoord);
  else   
    //距离小于半径的片段，二次线性插值获得顔色。
    gl_FragColor = getColor(distance);
}  