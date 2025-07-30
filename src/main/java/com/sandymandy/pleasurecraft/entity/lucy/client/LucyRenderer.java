package com.sandymandy.pleasurecraft.entity.lucy.client;

import com.sandymandy.pleasurecraft.util.entity.client.AbstractGirlRenderer;
import com.sandymandy.pleasurecraft.entity.lucy.LucyEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class LucyRenderer extends AbstractGirlRenderer<LucyEntity> {
    public LucyRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new LucyModel());
    }
}