//package com.sandymandy.blockhard.mixin;
//
//import net.minecraft.client.gl.ShaderProgram;
//import net.minecraft.util.Identifier;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Redirect;
//
//@Mixin(ShaderProgram.class)
//public class ShaderProgramMixin {
//
//    // This intercepts the Identifier.ofVanilla(...) call inside ShaderProgram's constructor
//    @Redirect(
//            method = "<init>",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;"
//            )
//    )
//    private Identifier blockhard$useCustomShaderNamespace(String path) {
//        // path will be like: "shaders/core/blockhard:rendertype_entity_unlit_translucent.json"
//        // We need to parse the namespace from it
//
//        // Strip the "shaders/core/" prefix
//        String stripped = path.substring("shaders/core/".length());
//
//        if (stripped.contains(":")) {
//            String[] parts = stripped.split(":", 2);
//            String namespace = parts[0];
//            String name = parts[1];
//            return Identifier.of(namespace, "shaders/core/" + name);
//        }
//
//        // fallback to default vanilla behavior
//        return Identifier.ofVanilla(path);
//    }
//}
