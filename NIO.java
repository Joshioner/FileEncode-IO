package app.iters.android.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.commons.io.FileUtils;

public class NIO {
	public static void main(String[] args) throws IOException {
		
		String path = "D:\\workspace\\pmp_v1\\src\\app\\iters\\android\\io\\nio1.txt";
		String fileEncode=EncodingDetect.getJavaEncode(path);
		String fileContent=FileUtils.readFileToString(new File(path),fileEncode);  
		System.out.println(fileEncode);
		System.out.println(fileContent);
		
		Charset fileEncode1 = NIO.getFileEncode(path);
		String displayName = fileEncode1.displayName();
		System.out.println(displayName);
		NIO.file2Buffer(fileEncode1, path);
//		NIO.buffer2File(displayName, path);
	}
	public String getCurrentPath(){  
	       //取得根目录路径  
	       String rootPath=getClass().getResource("/").getFile().toString();  
	       //当前目录路径  
	       String currentPath1=getClass().getResource(".").getFile().toString();  
	       String currentPath2=getClass().getResource("").getFile().toString();  
	       //当前目录的上级目录路径  
	       String parentPath=getClass().getResource("../").getFile().toString();  
	         
	       return rootPath;         
	  
	   }  
	public static Charset getFileEncode(String path) throws IOException {
		File file = new File(path);
		InputStream in = new java.io.FileInputStream(file);
		byte[] b = new byte[3];
		try {
			if (b[0] == -17 && b[1] == -69 && b[2] == -65) {
				System.out.println(file.getName() + "：编码为UTF-8");
				return Charset.forName("UTF-8");
			} else {
				System.out.println(file.getName() + "：可能是GBK，也可能是其他编码");
				return Charset.forName("GBK");
			}
		} finally {
			in.read(b);
			in.close();
		}
	}

	public static void file2Buffer(Charset charset, String fileName) {
		file2Buffer(null, charset, fileName);
	}

	public static void file2Buffer(String charsetName, String fileName) {
		file2Buffer(charsetName, null, fileName);
	}

	/**
	 * 有时候输出为空白，一般是由于文件的编码格式，不对应导致的
	 * 
	 * @param charsetName
	 * @param fileName
	 */
	public static void file2Buffer(String charsetName, Charset charset, String fileName) {
		try {
			RandomAccessFile file = new RandomAccessFile(fileName, "rw");
			FileChannel fileChannel = file.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(1024);

			int count = fileChannel.read(buffer);
			while (count != -1) {
				buffer.flip();
				CharBuffer charBuffer = CharBuffer.allocate(1024);
				if (charset == null) {
					Charset charset2 = Charset.forName(charsetName);
					CharsetDecoder decoder = charset2.newDecoder();
					decoder.decode(buffer, charBuffer, true);
				} else if (charsetName == null) {
					CharsetDecoder decoder = charset.newDecoder();
					decoder.decode(buffer, charBuffer, true);
				}
				charBuffer.flip();
				while (charBuffer.hasRemaining()) {
					System.out.print(charBuffer.get());
				}
				buffer.clear();
				count = fileChannel.read(buffer);
			}
			fileChannel.close();
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void buffer2File(String charsetName, String fileName) {
		try {
			RandomAccessFile file = new RandomAccessFile(fileName, "rw");
			FileChannel fileChannel = file.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			buffer.put("世界如此美妙！ good luck".getBytes(charsetName));
			buffer.flip();
			fileChannel.write(buffer);
			fileChannel.close();
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
