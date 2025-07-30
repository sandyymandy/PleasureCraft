//package com.sandymandy.blockhard.util.renderlayers.shaders;
//
//import com.sandymandy.blockhard.BlockHardClient;
//import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
//import net.minecraft.client.gl.ShaderProgram;
//import net.minecraft.client.render.VertexFormats;
//import net.minecraft.resource.ResourceManager;
//import net.minecraft.resource.ResourceReloader;
//import net.minecraft.util.Identifier;
//import net.minecraft.util.profiler.Profiler;
//
//import java.io.IOException;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.Executor;
//
//public class UnlitTranslucentShaderReloader implements IdentifiableResourceReloadListener {
//    private static final Identifier ID = Identifier.of("blockhard", "entity_unlit_translucent");
//
//    @Override
//    public Identifier getFabricId() {
//        return ID;
//    }
//
//    @Override
//    public CompletableFuture<Void> reload(
//            Synchronizer synchronizer,
//            ResourceManager manager,
//            Profiler prepareProfiler,
//            Profiler applyProfiler,
//            Executor prepareExecutor,
//            Executor applyExecutor
//    ) {
//        // Preparation stage (can load metadata etc. here if needed)
//        return CompletableFuture.supplyAsync(() -> {
//            // Nothing to prepare in this case, just return null
//            return null;
//        }, prepareExecutor).thenCompose(ignored -> {
//            // Apply stage (actually load shader)
//            return CompletableFuture.runAsync(() -> {
//                try {
//                    BlockHardClient.ENTITY_UNLIT_TRANSLUCENT_SHADER = new ShaderProgram(
//                            manager,
//                            "blockhard:rendertype_entity_unlit_translucent",
//                            VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL
//                    );
//                } catch (IOException e) {
//                    throw new RuntimeException("Failed to load custom unlit translucent shader", e);
//                }
//            }, applyExecutor);
//        });
//    }
//}
