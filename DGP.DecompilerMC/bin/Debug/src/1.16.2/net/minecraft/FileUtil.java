/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.SharedConstants;

public class FileUtil {
    private static final Pattern COPY_COUNTER_PATTERN = Pattern.compile("(<name>.*) \\((<count>\\d*)\\)", 66);
    private static final Pattern RESERVED_WINDOWS_FILENAMES = Pattern.compile(".*\\.|(?:COM|CLOCK\\$|CON|PRN|AUX|NUL|COM[1-9]|LPT[1-9])(?:\\..*)?", 2);

    public static String findAvailableName(Path path, String string, String string2) throws IOException {
        for (char c : SharedConstants.ILLEGAL_FILE_CHARACTERS) {
            string = string.replace(c, '_');
        }
        if (RESERVED_WINDOWS_FILENAMES.matcher(string = string.replaceAll("[./\"]", "_")).matches()) {
            string = "_" + string + "_";
        }
        Object object = COPY_COUNTER_PATTERN.matcher(string);
        int n = 0;
        if (((Matcher)object).matches()) {
            string = ((Matcher)object).group("name");
            n = Integer.parseInt(((Matcher)object).group("count"));
        }
        if (string.length() > 255 - string2.length()) {
            string = string.substring(0, 255 - string2.length());
        }
        while (true) {
            Object object2;
            String string3 = string;
            if (n != 0) {
                object2 = " (" + n + ")";
                int n2 = 255 - ((String)object2).length();
                if (string3.length() > n2) {
                    string3 = string3.substring(0, n2);
                }
                string3 = string3 + (String)object2;
            }
            string3 = string3 + string2;
            object2 = path.resolve(string3);
            try {
                Path path2 = Files.createDirectory((Path)object2, new FileAttribute[0]);
                Files.deleteIfExists(path2);
                return path.relativize(path2).toString();
            }
            catch (FileAlreadyExistsException fileAlreadyExistsException) {
                ++n;
                continue;
            }
            break;
        }
    }

    public static boolean isPathNormalized(Path path) {
        Path path2 = path.normalize();
        return path2.equals(path);
    }

    public static boolean isPathPortable(Path path) {
        for (Path path2 : path) {
            if (!RESERVED_WINDOWS_FILENAMES.matcher(path2.toString()).matches()) continue;
            return false;
        }
        return true;
    }

    public static Path createPathToResource(Path path, String string, String string2) {
        String string3 = string + string2;
        Path path2 = Paths.get(string3, new String[0]);
        if (path2.endsWith(string2)) {
            throw new InvalidPathException(string3, "empty resource name");
        }
        return path.resolve(path2);
    }
}

