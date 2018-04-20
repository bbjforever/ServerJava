package threadctrl;

public class ThreadTest {
	static ThreadTest tt = new ThreadTest();
	public static void main(String[] args) {
		thread1 t1 = new thread1();
		thread1 t2 = new thread1();
		
		while (true) {
			t1.start();
			t2.start();
			
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println(t1.getName() + " is avlied? -- " + t1.isAlive());
			
//			t2.interrupt();
//			t1.interrupt();
//			
//			while (!t1.isInterrupted()) {
//				System.out.println(t1.getName() + " isn't interrupt!");
//			}
//			while (!t2.isInterrupted()) {
//				System.out.println(t2.getName() + " isn't interrupt!");
//			}
		}
	}
	
	synchronized public void print(boolean isWait) throws Exception {
		System.out.println(Thread.currentThread().getName() + " is running!");
	}
	
	static class thread1 extends Thread {
		ThreadTest test = tt;
		
		@Override
		public void run() {
			try {
				test.print(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	static class thread2 extends Thread {
		ThreadTest test = tt;
		
		@Override
		public void run() {
			try {
				test.print(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
