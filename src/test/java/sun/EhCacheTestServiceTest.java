package sun;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import sun.basetest.SpringTestCase;
import sun.service.EhCacheTestService;
import util.EhCacheUtil;

public class EhCacheTestServiceTest extends SpringTestCase{
	@Autowired  
    private EhCacheTestService ehCacheTestService;

//    @Test
    public void getTimestampTest() throws InterruptedException{  
        System.out.println("第一次调用：" + ehCacheTestService.getTimestamp("param"));
        Thread.sleep(2000);
        System.out.println("2秒之后调用：" + ehCacheTestService.getTimestamp("param"));
        Thread.sleep(11000);
        System.out.println("再过11秒之后调用：" + ehCacheTestService.getTimestamp("param"));
    }
    @Test
    public void EhCacheUtilTest(){  
    	System.out.println("+++++++++++++");
    	Map<String, Object>map=new HashMap<String,Object>();
		map.put("1", "1");
		map.put("2", "2");
		map.put("3", "3");
		map.put("4", "4");
    	EhCacheUtil.getInstance().putCache("key3", map);
    	System.out.println(EhCacheUtil.getInstance().getCache("key3"));
    	System.out.println(EhCacheUtil.getInstance().getCache("key2"));
    	EhCacheUtil.getInstance().testcache();
    }
    
    
}
