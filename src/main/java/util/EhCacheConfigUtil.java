package util;

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;


/**
 * EhCache - 缓存
 * 
 */

public class EhCacheConfigUtil {
	
	public static void main(String[] args) {
		//创建一个缓存管理器
        CacheManager singletonManager = CacheManager.create();
        //建立一个缓存实例
        Cache memoryOnlyCache = new Cache("testCache", 5000, false, false, 5, 2);
        //在内存管理器中添加缓存实例
        singletonManager.addCache(memoryOnlyCache);
//		Cache testCache=CacheManager.getInstance().getCache("testCache");
        //在缓存管理器中获取一个缓存实例
		Cache testCache=singletonManager.getCache("testCache");
		List<String>list1=new ArrayList<String>();
		list1.add("1");
		list1.add("2");
		list1.add("3");
		Element element1 =new Element("key1", list1);
		testCache.put(element1);
		List<String>list2=new ArrayList<String>();
		list2.add("1");
		list2.add("2");
		list2.add("3");
		list2.add("4");
		Element element2 =new Element("key2", list2);
		testCache.put(element2);
		//获取缓存个数
		int elementsInMemory = testCache.getSize();
        System.out.println("缓存个数++++"+elementsInMemory);
        //获取缓存实例
        Cache cache = singletonManager.getCache("testCache");
        Element element1_ = cache.get("key1");
        List<String>list1_=(List<String>) element1_.getObjectValue();
        System.out.println("+list1++"+list1_);
        
        Element element2_ = cache.get("key2");
        List<String>list2_=(List<String>) element2_.getObjectValue();
        System.out.println("+list2++"+list2_);
	}
}