package com.sandymandy.pleasurecraft.util.renderer;

import net.minecraft.client.render.VertexConsumer;

/**
 * A wrapper for VertexConsumer that offsets texture coordinates (u,v).
 */
public class OffsetVertexConsumer implements VertexConsumer {
    private VertexConsumer delegate;
    private float uOffset;
    private float vOffset;

    public void setup(VertexConsumer delegate, float uOffset, float vOffset) {
        if (delegate instanceof OffsetVertexConsumer offsetVertexConsumer) {
            this.delegate = offsetVertexConsumer.getDelegate();
        } else {
            this.delegate = delegate;
        }
        this.uOffset = uOffset;
        this.vOffset = vOffset;
    }

    public VertexConsumer getDelegate() {
        return this.delegate;
    }

    // ---- VertexConsumer overrides ----

    @Override
    public VertexConsumer vertex(float x, float y, float z) {
        return delegate.vertex(x, y, z);
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alpha) {
        return delegate.color(red, green, blue, alpha);
    }

    @Override
    public VertexConsumer texture(float u, float v) {
        if (delegate == null) return this;
        // Apply the offset here
        return delegate.texture(u + uOffset, v + vOffset);
    }

    @Override
    public VertexConsumer overlay(int u, int v) {
        return delegate.overlay(u, v);
    }

    @Override
    public VertexConsumer light(int u, int v) {
        return delegate.light(u, v);
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        return delegate.normal(x, y, z);
    }
}
