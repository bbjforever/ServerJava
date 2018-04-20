package redis;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import redis.clients.jedis.Jedis;

public class RedisTest {
	Jedis jedis = null;
	PrintRedis pr = new PrintRedis();
	BlockingQueue<String> queue = new LinkedBlockingQueue<String> ();
	volatile boolean running = false;
	
	public RedisTest(Jedis j, String password) {
		this.jedis = j;
		
		if (password != null) {
			this.jedis.auth("bbjtest");
		}
	}
	
	class PrintRedis implements Runnable {
		@Override
		public void run() {
			running = true;
			
			System.out.println("dumpkey1: " + jedis.get("dumpkey1"));
			System.out.println("---------- setKeys ------------");
			for (;;) {
				String val = "";
				try {
//					String key = queue.take();
					String key = queue.poll(10000, TimeUnit.MILLISECONDS);
					System.out.println("key = " + key);
					if (key != null) {
						val = jedis.hget("myRole", key);
						System.out.println("value = " + val);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
				if (val.equals("exit")) {
					running = false;
				}
				
				if (!running) {
					return;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		RedisTest rt = new RedisTest(new Jedis("127.0.0.1"), "bbjtest");
		
		Jedis jd = rt.jedis;
		if (!jd.ping().equals("PONG")) {
			System.err.println("Jedis·þÎñÆ÷pingÊ§°Ü£¡");
			return;
		}
		
		Thread t = new Thread(rt.pr);
		t.start();
		
		try {
			Thread.sleep(200);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<String, String> mapContent = new HashMap<String, String> ();
		mapContent.put("name1", "bbj");
		mapContent.put("name2", "lanaya");
		mapContent.put("name3", "ak-47");
		mapContent.put("name4", "white");
		mapContent.put("name5", "godlike");
		jd.hmset("myRole", mapContent);
		
		Collection<String> repeatSet = new HashSet<String> ();
		while (rt.running) {
			Set<String> setKeys = jd.hkeys("myRole");
			if (!setKeys.isEmpty()) {
				Iterator iter = setKeys.iterator();
				while (iter.hasNext()) {
					String key = (String) iter.next();
					
					if (!repeatSet.contains(key)) {
						repeatSet.add(key);
						rt.queue.offer(key);
					}
				}
			}
			else {
				rt.running = false;
				System.out.println("Î´ÕÒµ½myRole");
				break;
			}
			
			try {
				Thread.sleep(2000);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
//		System.out.println("save result: " + jd.save());
	}
}
