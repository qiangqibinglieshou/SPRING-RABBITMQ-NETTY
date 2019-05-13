package com.nsk.cms.common.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

public class FileUtil {
	
	public static void write(Object o, String pathStr, String fileName) throws IOException {
		Path path = Paths.get(pathStr);
		if (!Files.exists(path)) {
		    Files.createDirectories(path);
		}
		path = Paths.get(pathStr + fileName);
		Files.write(path, o.toString().getBytes());
	}
}
