/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 */
package net.minecraft.util;

import com.google.common.base.Charsets;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;

public class DirectoryLock
implements AutoCloseable {
    private final FileChannel lockFile;
    private final FileLock lock;
    private static final ByteBuffer DUMMY;

    public static DirectoryLock create(Path path) throws IOException {
        Path path2 = path.resolve("session.lock");
        if (!Files.isDirectory(path, new LinkOption[0])) {
            Files.createDirectories(path, new FileAttribute[0]);
        }
        FileChannel fileChannel = FileChannel.open(path2, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        try {
            fileChannel.write(DUMMY.duplicate());
            fileChannel.force(true);
            FileLock fileLock = fileChannel.tryLock();
            if (fileLock == null) {
                throw LockException.alreadyLocked(path2);
            }
            return new DirectoryLock(fileChannel, fileLock);
        }
        catch (IOException iOException) {
            try {
                fileChannel.close();
            }
            catch (IOException iOException2) {
                iOException.addSuppressed(iOException2);
            }
            throw iOException;
        }
    }

    private DirectoryLock(FileChannel fileChannel, FileLock fileLock) {
        this.lockFile = fileChannel;
        this.lock = fileLock;
    }

    @Override
    public void close() throws IOException {
        try {
            if (this.lock.isValid()) {
                this.lock.release();
            }
        }
        finally {
            if (this.lockFile.isOpen()) {
                this.lockFile.close();
            }
        }
    }

    public boolean isValid() {
        return this.lock.isValid();
    }

    /*
     * Exception decompiling
     */
    public static boolean isLocked(Path var0) throws IOException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [1[TRYBLOCK]], but top level block is 5[TRYBLOCK]
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:429)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:478)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:728)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:806)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:258)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:192)
         * org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         * org.benf.cfr.reader.entities.Method.analyse(Method.java:521)
         * org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1035)
         * org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:922)
         * org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:253)
         * org.benf.cfr.reader.Driver.doJar(Driver.java:135)
         * org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:65)
         * org.benf.cfr.reader.Main.main(Main.java:49)
         */
        throw new IllegalStateException(Decompilation failed);
    }

    static {
        byte[] arrby = "\u2603".getBytes(Charsets.UTF_8);
        DUMMY = ByteBuffer.allocateDirect(arrby.length);
        DUMMY.put(arrby);
        DUMMY.flip();
    }

    public static class LockException
    extends IOException {
        private LockException(Path path, String string) {
            super(path.toAbsolutePath() + ": " + string);
        }

        public static LockException alreadyLocked(Path path) {
            return new LockException(path, "already locked (possibly by other Minecraft instance?)");
        }
    }
}

