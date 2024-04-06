/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.ar.core.common.render;

import com.google.ar.core.Pose;
import com.google.ar.core.common.samplerender.Mesh;
import com.google.ar.core.common.samplerender.SampleRender;
import com.google.ar.core.common.samplerender.Shader;
import com.google.ar.core.common.samplerender.VertexBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Draws a label. See {@link #draw}.
 */
public class LabelRender {
    private static final String TAG = "LabelRender";
    private static final int COORDS_BUFFER_SIZE = 2 * 4 * 4;

    /*
     * Vertex buffer data for the mesh quad.
     */
    private static final FloatBuffer NDC_QUAD_COORDS_BUFFER = ByteBuffer.allocateDirect(COORDS_BUFFER_SIZE)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(new float[]{
                    /*0:*/ -1.5f, -1.5f,
                    /*1:*/ 1.5f, -1.5f,
                    /*2:*/ -1.5f, 1.5f,
                    /*3:*/ 1.5f, 1.5f,
            });

    /**
     * Vertex buffer data for texture coordinates.
     */
    private static final FloatBuffer SQUARE_TEX_COORDS_BUFFER = ByteBuffer.allocateDirect(COORDS_BUFFER_SIZE)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(new float[]{
                    /*0:*/ 0f, 0f,
                    /*1:*/ 1f, 0f,
                    /*2:*/ 0f, 1f,
                    /*3:*/ 1f, 1f,
            });

    private final TextTextureCache cache = new TextTextureCache();

    private Mesh mesh;
    private Shader shader;

    public void onSurfaceCreated(SampleRender render) throws IOException {
        shader = Shader.createFromAssets(render, "shaders/label.vert", "shaders/label.frag", null)
                .setBlend(
                        Shader.BlendFactor.ONE, // ALPHA (src)
                        Shader.BlendFactor.ONE_MINUS_SRC_ALPHA // ALPHA (dest)
                )
                .setDepthTest(false)
                .setDepthWrite(false);

        VertexBuffer[] vertexBuffers = new VertexBuffer[]{
                new VertexBuffer(render, 2, NDC_QUAD_COORDS_BUFFER),
                new VertexBuffer(render, 2, SQUARE_TEX_COORDS_BUFFER),
        };
        mesh = new Mesh(render, Mesh.PrimitiveMode.TRIANGLE_STRIP, null, vertexBuffers);
    }

    private final float[] labelOrigin = new float[3];

    /**
     * Draws a label quad with text {@code label} at {@code pose}. The label will rotate to face {@code cameraPose} around the Y-axis.
     */
    public void draw(
            SampleRender render,
            float[] viewProjectionMatrix,
            Pose pose,
            Pose cameraPose,
            String label
    ) {
        labelOrigin[0] = pose.tx();
        labelOrigin[1] = pose.ty();
        labelOrigin[2] = pose.tz();
        shader
                .setMat4("u_ViewProjection", viewProjectionMatrix)
                .setVec3("u_LabelOrigin", labelOrigin)
                .setVec3("u_CameraPos", cameraPose.getTranslation())
                .setTexture("uTexture", cache.get(render, label));
        render.draw(mesh, shader);
    }
}

