package com.sandymandy.pleasurecraft.client.renderers;

import com.sandymandy.pleasurecraft.client.models.LucyModel;
import com.sandymandy.pleasurecraft.entity.girls.LucyEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class LucyRenderer extends AbstractGirlRenderer<LucyEntity> {
    public LucyRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new LucyModel());
    }


}