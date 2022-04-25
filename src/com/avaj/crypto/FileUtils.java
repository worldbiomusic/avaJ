package com.avaj.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
	public static void save(File file, byte[] data) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(data);
		fos.flush();
		fos.close();
	}

	public static byte[] load(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[(int) file.length()];
		fis.read(data);
		fis.close();
		return data;
	}
}
