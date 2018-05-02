package com.ahajri.heaven.calendar.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class Mp3Downloader {

	public static void main(String[] args) throws Exception {
		URL conn = new URL("https://server10.mp3quran.net/basit_warsh/035.mp3");
		InputStream myUrlStream = conn.openStream();
		ReadableByteChannel myUrlChannel = Channels.newChannel(myUrlStream);

		FileChannel destinationChannel=new FileOutputStream(new File("/mp3/fatir.mp3")).getChannel();
		destinationChannel.transferFrom(myUrlChannel, 0, 4096);

		

	}

}
