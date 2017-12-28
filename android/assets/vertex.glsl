attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
uniform mat4 u_projTrans;
varying vec4 v_color;
varying vec2 v_texCoords;

void main()
{
   v_color = a_color;
   v_color.a = v_color.a * (255.0/254.0);
   v_texCoords = a_texCoord0;
   gl_Position =  u_projTrans * a_position;
   //gl_Position = gl_Position + vec4(sin(gl_Position.y) * .7 + gl_Position.y * .1, cos(gl_Position.x) * .7 + gl_Position.x * .1, 0, 0);
}