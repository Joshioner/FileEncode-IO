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
	       //ȡ�ø�Ŀ¼·��  
	       String rootPath=getClass().getResource("/").getFile().toString();  
	       //��ǰĿ¼·��  
	       String currentPath1=getClass().getResource(".").getFile().toString();  
	       String currentPath2=getClass().getResource("").getFile().toString();  
	       //��ǰĿ¼���ϼ�Ŀ¼·��  
	       String parentPath=getClass().getResource("../").getFile().toString();  
	         
	       return rootPath;         
	  
	   }  
	public static Charset getFileEncode(String path) throws IOException {
		File file = new File(path);
		InputStream in = new java.io.FileInputStream(file);
		byte[] b = new byte[3];
		try {
			if (b[0] == -17 && b[1] == -69 && b[2] == -65) {
				System.out.println(file.getName() + "������ΪUTF-8");
				return Charset.forName("UTF-8");
			} else {
				System.out.println(file.getName() + "��������GBK��Ҳ��������������");
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
	 * ��ʱ�����Ϊ�հף�һ���������ļ��ı����ʽ������Ӧ���µ�
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
			buffer.put("���������� good luck".getBytes(charsetName));
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
