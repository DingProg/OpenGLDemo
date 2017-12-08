attribute vec4 aPosition;
attribute vec2 aTexCoord;
uniform mat4 uMatrix;
varying vec2 vTexCoord;

void main() {
    vTexCoord = aTexCoord;
     gl_Position = aPosition;
}
