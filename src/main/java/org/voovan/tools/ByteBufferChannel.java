package org.voovan.tools;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

/**
 * ByteBuffer双向通道
 * 
 * @author helyho
 *
 * Voovan Framework.
 * WebSite: https://github.com/helyho/Voovan
 * Licence: Apache v2 License
 */
public class ByteBufferChannel implements ByteChannel {

	private ByteBuffer buffer;
	
	public ByteBufferChannel() {
		buffer = ByteBuffer.allocateDirect(0);
	}
	
	/**
	 * 重置
	 */
	public synchronized void reset() {
		buffer = ByteBuffer.allocateDirect(0);
	}
	
	/**
	 * 当前数据大小
	 * @return 数据大小
	 */
	public int size(){
		return buffer.limit();
	}

	/**
	 * 当前数据缓冲区
	 * @return 数据缓冲区
     */
	public synchronized ByteBuffer getBuffer(){
		return buffer;
	}
	
	@Override
	public synchronized int read(ByteBuffer dst) throws IOException {
			int readSize = 0;
			
			//确定读取大小
			if( dst.limit() > buffer.limit() ){
				readSize = buffer.limit();
			}else{
				readSize = dst.limit();
			}
			
			if(readSize>0)
			{
				dst.put(TByteBuffer.toArray(buffer),0,readSize);
				buffer.position(readSize);
				byte[] tempBytes = new byte[buffer.remaining()];
				buffer.get(tempBytes,0,buffer.remaining());
				buffer = ByteBuffer.wrap(tempBytes);
			}
			dst.flip();
			return readSize;
	}

	/**
	 * 没有作用永远返回 true
	 */
	@Override
	public boolean isOpen() {
		return true;
	}

	/**
	 * 没有作用
	 */
	@Override
	public void close() throws IOException {
		
	}

	@Override
	public synchronized int write(ByteBuffer src) throws IOException {
		if(src.limit()!=0){
			int newSize = buffer.limit()+src.limit();
			ByteBuffer tempBuffer = ByteBuffer.allocateDirect(newSize);
			tempBuffer.put(buffer);
			tempBuffer.put(src);
			buffer = tempBuffer;
			buffer.flip();
		}
		return src.limit();
	}
	
	@Override
	public String toString(){
		return buffer.toString();
	}
}
