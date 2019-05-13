package com.nsk.cms.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.swing.JPopupMenu.Separator;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

//@Component
@Slf4j
public class PreventduplicatedUtil {

	private final static String LOCK_PATH = "locks" + File.pathSeparator;
	
	private final static String LOCK_FILE = "CMS.lock";
	
	private final static String PERMISSIONS = "rw-------";
	
	private static boolean isExcuting  = true;
	
	@PostConstruct
	public static void lock() throws IOException {
		// TODO Auto-generated method stub
		Runtime.getRuntime().addShutdownHook(new Thread(()->{
			if(isExcuting = true) {
				Path path = getLockFile();
				log.info("The program is done");
				path.toFile().delete();
			}else {
				log.info("The program is done");
			}
		}));
		checkRuning();
	}

	private static void checkRuning() throws IOException {
		Path path = getLockFile();
		if(path.toFile().exists()) {
			isExcuting = false;
			throw new RuntimeException("The programming is areadly running");
		}
		Set<PosixFilePermission> perms = PosixFilePermissions.fromString(PERMISSIONS);
		Files.createFile(path, PosixFilePermissions.asFileAttribute(perms));
	}

	private static Path getLockFile() {
		// TODO Auto-generated method stub
		return Paths.get(LOCK_PATH, LOCK_FILE);
	}
}
