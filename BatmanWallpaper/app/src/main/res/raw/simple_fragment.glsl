precision mediump float;


//varying vec4 v_Color;
varying vec2 v_TexturePosition;
varying vec3 v_Normal;

uniform sampler2D u_Texture;
uniform int u_UseTexture;
uniform int u_loopTexture;

void main()
{
    float diffuse = max(-dot(v_Normal, vec3(0 ,0, -1)), 0.0);
    //gl_FragColor = diffuse * vec4(1.0, 1.0, 1.0, 1.0);  //texture2D(u_Texture, v_TexturePosition);
    vec2 texturePosition = v_TexturePosition;
    if(u_loopTexture == 1)
    {
        if(texturePosition.x > 1.0)
        {
            texturePosition.x = texturePosition.x - 1.0;
        }
        if(texturePosition.y > 1.0)
        {
            texturePosition.y = texturePosition.y - 1.0;
        }
    }


    if(u_UseTexture > 0)
    {
        gl_FragColor = diffuse * texture2D(u_Texture, texturePosition);
    }
    else
    {
        gl_FragColor = diffuse * vec4(1.0, 1.0, 1.0, 1.0);
    }

    //gl_FragColor = v_Color; //vec4(1, 1, 1, 0);
    //gl_FragColor.a = 0.0;
}
