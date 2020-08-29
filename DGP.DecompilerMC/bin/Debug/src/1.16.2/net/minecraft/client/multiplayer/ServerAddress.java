/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Pair
 */
package net.minecraft.client.multiplayer;

import com.mojang.datafixers.util.Pair;
import java.net.IDN;
import java.util.Hashtable;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;

public class ServerAddress {
    private final String host;
    private final int port;

    private ServerAddress(String string, int n) {
        this.host = string;
        this.port = n;
    }

    public String getHost() {
        try {
            return IDN.toASCII(this.host);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            return "";
        }
    }

    public int getPort() {
        return this.port;
    }

    public static ServerAddress parseString(String string) {
        int n;
        Object object;
        int n2;
        if (string == null) {
            return null;
        }
        String[] arrstring = string.split(":");
        if (string.startsWith("[") && (n2 = string.indexOf("]")) > 0) {
            String string2 = string.substring(1, n2);
            object = string.substring(n2 + 1).trim();
            if (object.startsWith(":") && !object.isEmpty()) {
                object = object.substring(1);
                arrstring = new String[]{string2, object};
            } else {
                arrstring = new String[]{string2};
            }
        }
        if (arrstring.length > 2) {
            arrstring = new String[]{string};
        }
        String string3 = arrstring[0];
        int n3 = n = arrstring.length > 1 ? ServerAddress.parseInt(arrstring[1], 25565) : 25565;
        if (n == 25565) {
            object = ServerAddress.lookupSrv(string3);
            string3 = (String)object.getFirst();
            n = (Integer)object.getSecond();
        }
        return new ServerAddress(string3, n);
    }

    private static Pair<String, Integer> lookupSrv(String string) {
        try {
            String string2 = "com.sun.jndi.dns.DnsContextFactory";
            Class.forName("com.sun.jndi.dns.DnsContextFactory");
            Hashtable<String, String> hashtable = new Hashtable<String, String>();
            hashtable.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            hashtable.put("java.naming.provider.url", "dns:");
            hashtable.put("com.sun.jndi.dns.timeout.retries", "1");
            InitialDirContext initialDirContext = new InitialDirContext(hashtable);
            Attributes attributes = initialDirContext.getAttributes("_minecraft._tcp." + string, new String[]{"SRV"});
            Attribute attribute = attributes.get("srv");
            if (attribute != null) {
                String[] arrstring = attribute.get().toString().split(" ", 4);
                return Pair.of((Object)arrstring[3], (Object)ServerAddress.parseInt(arrstring[2], 25565));
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return Pair.of((Object)string, (Object)25565);
    }

    private static int parseInt(String string, int n) {
        try {
            return Integer.parseInt(string.trim());
        }
        catch (Exception exception) {
            return n;
        }
    }
}

