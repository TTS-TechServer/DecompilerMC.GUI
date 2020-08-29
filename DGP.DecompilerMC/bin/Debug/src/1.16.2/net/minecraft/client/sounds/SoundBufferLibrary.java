/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.client.sounds;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.audio.OggAudioStream;
import com.mojang.blaze3d.audio.SoundBuffer;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import net.minecraft.Util;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.sounds.AudioStream;
import net.minecraft.client.sounds.LoopingAudioStream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

public class SoundBufferLibrary {
    private final ResourceManager resourceManager;
    private final Map<ResourceLocation, CompletableFuture<SoundBuffer>> cache = Maps.newHashMap();

    public SoundBufferLibrary(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public CompletableFuture<SoundBuffer> getCompleteBuffer(ResourceLocation resourceLocation2) {
        return this.cache.computeIfAbsent(resourceLocation2, resourceLocation -> CompletableFuture.supplyAsync(() -> {
            /*
             * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
             * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [2[TRYBLOCK]], but top level block is 9[TRYBLOCK]
             * org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:429)
             * org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:478)
             * org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:728)
             * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:806)
             * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:258)
             * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:192)
             * org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
             * org.benf.cfr.reader.entities.Method.getAnalysis(Method.java:510)
             * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.rewriteDynamicExpression(LambdaRewriter.java:350)
             * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.rewriteDynamicExpression(LambdaRewriter.java:166)
             * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.rewriteExpression(LambdaRewriter.java:104)
             * org.benf.cfr.reader.bytecode.analysis.parse.rewriters.ExpressionRewriterHelper.applyForwards(ExpressionRewriterHelper.java:12)
             * org.benf.cfr.reader.bytecode.analysis.parse.expression.StaticFunctionInvokation.applyExpressionRewriterToArgs(StaticFunctionInvokation.java:80)
             * org.benf.cfr.reader.bytecode.analysis.parse.expression.StaticFunctionInvokation.applyExpressionRewriter(StaticFunctionInvokation.java:68)
             * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.rewriteExpression(LambdaRewriter.java:102)
             * org.benf.cfr.reader.bytecode.analysis.structured.statement.StructuredReturn.rewriteExpressions(StructuredReturn.java:91)
             * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.rewrite(LambdaRewriter.java:87)
             * org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.rewriteLambdas(Op04StructuredStatement.java:1125)
             * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:868)
             * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:258)
             * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:185)
             * org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
             * org.benf.cfr.reader.entities.Method.analyse(Method.java:521)
             * org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1030)
             * org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:922)
             * org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:253)
             * org.benf.cfr.reader.Driver.doJar(Driver.java:135)
             * org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:65)
             * org.benf.cfr.reader.Main.main(Main.java:49)
             */
            throw new IllegalStateException(Decompilation failed);
        }, Util.backgroundExecutor()));
    }

    public CompletableFuture<AudioStream> getStream(ResourceLocation resourceLocation, boolean bl) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Resource resource = this.resourceManager.getResource(resourceLocation);
                InputStream inputStream = resource.getInputStream();
                return bl ? new LoopingAudioStream(OggAudioStream::new, inputStream) : new OggAudioStream(inputStream);
            }
            catch (IOException iOException) {
                throw new CompletionException(iOException);
            }
        }, Util.backgroundExecutor());
    }

    public void clear() {
        this.cache.values().forEach(completableFuture -> completableFuture.thenAccept(SoundBuffer::discardAlBuffer));
        this.cache.clear();
    }

    public CompletableFuture<?> preload(Collection<Sound> collection) {
        return CompletableFuture.allOf((CompletableFuture[])collection.stream().map(sound -> this.getCompleteBuffer(sound.getPath())).toArray(CompletableFuture[]::new));
    }
}

