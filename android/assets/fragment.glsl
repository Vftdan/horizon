#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP 
#endif
varying LOWP vec4 v_color;
varying vec2 v_texCoords;
varying vec2 v_position;
uniform float u_scale;
uniform sampler2D u_texture;

void main()
{
   vec4 c = v_color * texture2D(u_texture, v_texCoords);
   c.a *= float(1f) - float(distance(v_position, vec2(0f, 0f)) * u_scale);
   gl_FragColor = c;
}