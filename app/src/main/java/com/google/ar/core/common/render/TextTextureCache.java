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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.opengl.GLES30;
import com.google.ar.core.common.samplerender.GLError;
import com.google.ar.core.common.samplerender.SampleRender;
import com.google.ar.core.common.samplerender.Texture;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Generates and caches GL textures for label names.
 */
public class TextTextureCache {
    private static final String TAG = "TextTextureCache";

    private final Map<String, Texture> cacheMap = new HashMap<>();

    private final Paint textPaint = new Paint();
    private final Paint strokePaint = new Paint(textPaint);

    public TextTextureCache() {
        textPaint.setTextSize(26f);
        textPaint.setARGB(0xff, 0xea, 0x43, 0x35);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setStrokeWidth(2f);

        strokePaint.setARGB(0xff, 0x00, 0x00, 0x00);
        strokePaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * Get a texture for a given string. If that string hasn't been used yet, create a texture for it
     * and cache the result.
     */
    public Texture get(SampleRender render, String string) {
        return cacheMap.computeIfAbsent(string, s -> generateTexture(render, s));
    }

    private Texture generateTexture(SampleRender render, String string) {
        Texture texture = new Texture(render, Texture.Target.TEXTURE_2D, Texture.WrapMode.CLAMP_TO_EDGE);

        Bitmap bitmap = generateBitmapFromString(string);
        ByteBuffer buffer = ByteBuffer.allocateDirect(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(buffer);
        buffer.rewind();

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture.getTextureId());
        GLError.maybeThrowGLException("Failed to bind texture", "glBindTexture");
        GLES30.glTexImage2D(
                GLES30.GL_TEXTURE_2D,
                0,
                GLES30.GL_RGBA8,
                bitmap.getWidth(),
                bitmap.getHeight(),
                0,
                GLES30.GL_RGBA,
                GLES30.GL_UNSIGNED_BYTE,
                buffer
        );
        GLError.maybeThrowGLException("Failed to populate texture data", "glTexImage2D");
        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D);
        GLError.maybeThrowGLException("Failed to generate mipmaps", "glGenerateMipmap");

        return texture;
    }

    private Bitmap generateBitmapFromString(String string) {
        int w = 256;
        int h = 256;
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(0);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(string, w / 2f, h / 2f, strokePaint);
        canvas.drawText(string, w / 2f, h / 2f, textPaint);

        return bitmap;
    }
}

