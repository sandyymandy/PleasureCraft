//package com.sandymandy.blockhard.util.renderlayers;
//
//import net.minecraft.client.render.*;
//import net.minecraft.util.Identifier;
//import net.minecraft.util.Util;
//import com.sandymandy.blockhard.BlockHardClient;
//import java.util.function.Function;
//
//public class CustomRenderLayers {
//    public static final RenderPhase.ShaderProgram ENTITY_UNLIT_TRANSLUCENT_PROGRAM = new RenderPhase.ShaderProgram(BlockHardClient::getEntityUnlitTranslucentShader);
//
//    public static final Function<Identifier, RenderLayer> UNLIT_TRANSLUCENT = Util.memoize(texture -> {
//        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder()
//                .program(ENTITY_UNLIT_TRANSLUCENT_PROGRAM) // Unlit shader
//                .texture(new RenderPhase.Texture(texture, false, false))
//                .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
//                .cull(RenderPhase.DISABLE_CULLING) // No cull like Forge
//                .lightmap(RenderPhase.ENABLE_LIGHTMAP) // Disable lighting
//                .overlay(RenderPhase.ENABLE_OVERLAY_COLOR) // Keep overlay (optional)
//                .build(true);
//
//        return RenderLayer.of(
//                "unlit_translucent",
//                VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
//                VertexFormat.DrawMode.QUADS,
//                256,
//                true,
//                true,
//                multiPhaseParameters
//        );
//    });
//
//    public static RenderLayer getUnlitTranslucent(Identifier texture) {
//        return UNLIT_TRANSLUCENT.apply(texture);
//    }
//}
