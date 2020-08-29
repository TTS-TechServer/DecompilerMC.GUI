/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.server.dedicated;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.Util;
import net.minecraft.server.dedicated.DedicatedServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerWatchdog
implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger();
    private final DedicatedServer server;
    private final long maxTickTime;

    public ServerWatchdog(DedicatedServer dedicatedServer) {
        this.server = dedicatedServer;
        this.maxTickTime = dedicatedServer.getMaxTickLength();
    }

    @Override
    public void run() {
        while (this.server.isRunning()) {
            long l = this.server.getNextTickTime();
            long l2 = Util.getMillis();
            long l3 = l2 - l;
            if (l3 > this.maxTickTime) {
                LOGGER.fatal("A single server tick took {} seconds (should be max {})", (Object)String.format(Locale.ROOT, "%.2f", Float.valueOf((float)l3 / 1000.0f)), (Object)String.format(Locale.ROOT, "%.2f", Float.valueOf(0.05f)));
                LOGGER.fatal("Considering it to be crashed, server will forcibly shutdown.");
                ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
                ThreadInfo[] arrthreadInfo = threadMXBean.dumpAllThreads(true, true);
                StringBuilder stringBuilder = new StringBuilder();
                Error error = new Error();
                for (ThreadInfo threadInfo : arrthreadInfo) {
                    if (threadInfo.getThreadId() == this.server.getRunningThread().getId()) {
                        error.setStackTrace(threadInfo.getStackTrace());
                    }
                    stringBuilder.append(threadInfo);
                    stringBuilder.append("\n");
                }
                CrashReport crashReport = new CrashReport("Watching Server", error);
                this.server.fillReport(crashReport);
                CrashReportCategory crashReportCategory = crashReport.addCategory("Thread Dump");
                crashReportCategory.setDetail("Threads", stringBuilder);
                File file = new File(new File(this.server.getServerDirectory(), "crash-reports"), "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-server.txt");
                if (crashReport.saveToFile(file)) {
                    LOGGER.error("This crash report has been saved to: {}", (Object)file.getAbsolutePath());
                } else {
                    LOGGER.error("We were unable to save this crash report to disk.");
                }
                this.exit();
            }
            try {
                Thread.sleep(l + this.maxTickTime - l2);
            }
            catch (InterruptedException interruptedException) {}
        }
    }

    private void exit() {
        try {
            Timer timer = new Timer();
            timer.schedule(new TimerTask(){

                @Override
                public void run() {
                    Runtime.getRuntime().halt(1);
                }
            }, 10000L);
            System.exit(1);
        }
        catch (Throwable throwable) {
            Runtime.getRuntime().halt(1);
        }
    }
}

