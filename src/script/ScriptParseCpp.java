package script;

public class ScriptParseCpp{
	byte[] m_buf;
	int m_loc = 0;
	
	public ScriptParseCpp(byte[] buf) {
		this.m_buf = buf;
	}
	
	public ScriptParseCpp() {
		m_buf = new byte[1024];
	}
	
	public byte readByte() {
		byte b = m_buf[m_loc ++];
		
		return b;
	}
	
	public short readShort() {
		byte[] s = new byte[2];
		s[0] = m_buf[m_loc ++];
		s[1] = m_buf[m_loc ++];
		
		return (short) (((s[1] & 0xff) << 8) 
					| (s[0] & 0xff));
	}
	
	public int readInt() {
		byte[] s = new byte[4];
		s[0] = m_buf[m_loc ++];
		s[1] = m_buf[m_loc ++];
		s[2] = m_buf[m_loc ++];
		s[3] = m_buf[m_loc ++];
		
		return (int) (((s[3] & 0xff) << 24) 
					| ((s[2] & 0xff) << 16) 
					| ((s[1] & 0xff) << 8) 
					| (s[0] & 0xff));
	}
	
	public String readUtf(int strlen) {
		char[] cArr = new char[strlen];
		for (int i = 0; i < strlen; i ++) {
			cArr[i] = readChar();
		}
		
		String str = new String(cArr);
		
		return str;
	}
	
	private char readChar() {
		byte[] s = new byte[3];

		s[2] = m_buf[m_loc ++];
		s[1] = m_buf[m_loc ++];
		s[0] = m_buf[m_loc ++];
		
		char ch = (char) (((s[0] & 0x0F) << 12) |
					((s[1] & 0x3F) << 6)  |
					((s[2] & 0x3F) << 0));
		return ch;
		
//		return (char) (((s[1] & 0xff) << 8) 
//					| (s[0] & 0xff));
	}
}
