package script;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Script {
	private ByteArrayInputStream m_bis;
	private DataInputStream m_dis;
	
	private ByteArrayOutputStream m_bos;
	private DataOutputStream m_dos;
	
	private int m_ioMark = 0;
	
	public Script(byte[] buf) {
        m_bis = new ByteArrayInputStream(buf);
        m_dis = new DataInputStream(m_bis);
	}
	
	public Script() {
		m_bos = new ByteArrayOutputStream(1024);
		m_dos = new DataOutputStream(m_bos);
	}
	
	public String getString() {
		try {
			if (m_dis.readByte() == DataDefine.TYPE_UTF) {
				return m_dis.readUTF();
			}
			throw new RuntimeException("当前对象不是字符串");
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public int getInteger() {
		try {
			if (m_dis.readByte() == DataDefine.TYPE_INT) {
				return m_dis.readInt();
			}
			throw new RuntimeException("当前对象不是Int型");
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean getBoolean() {
		try {
			if (m_dis.readByte() == DataDefine.TYPE_BOOL) {
				return m_dis.readBoolean();
			}
			throw new RuntimeException("当前对象不是Bool型");
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void putString(String str) {
		try {
			m_dos.writeByte(DataDefine.TYPE_UTF);
			m_dos.writeUTF(str);
		}
		catch (Exception e) {
			
		}
	}
	
	public void putInt(int val) {
		try {
			m_dos.writeByte(DataDefine.TYPE_INT);
			m_dos.writeInt(val);
		}
		catch (Exception e) {
			
		}
	}
	
	public void putBool(boolean b) {
		try {
			m_dos.writeByte(DataDefine.TYPE_BOOL);
			m_dos.writeBoolean(b);
		}
		catch (Exception e) {
			
		}
	}
	
	public byte[] toByteArray() {
		return m_bos.toByteArray();
	}
}