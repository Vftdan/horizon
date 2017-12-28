#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP 
#endif
varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
void main()
{
   vec4 c = v_color * texture2D(u_texture, v_texCoords);
   float avg = .3 * c.x + .59 * c.y + .11 * c.z;
   gl_FragColor = vec4(vec3(avg,  avg, avg), c.w);
}