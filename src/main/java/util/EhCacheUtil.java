package util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import sun.SpringKit;
/**
 * EhCache - 缓存
 * 
 */
public class EhCacheUtil {

	private Cache cachesDemo = (Cache)SpringKit.getBean("demoCache");

	private static EhCacheUtil ehCacheUtil;
	
	private EhCacheUtil(){
		
	}
	
	public void initCache(){
		Map<String, Object>map=new HashMap<String,Object>();
		map.put("1", "1");
		map.put("2", "2");
		map.put("3", "3");
		Element element = cachesDemo.get("key1");
		if(element != null){
			System.out.println("+element != null++"+element.getObjectValue());
		}
		element = new Element("key1", map);
		cachesDemo.put(element);
	}
	/**
	 * 刷新缓存
	 */
	public void fush(){
		cachesDemo.removeAll();
		ehCacheUtil.initCache();
	}
	
	public static EhCacheUtil getInstance(){
		if(ehCacheUtil == null ){
			ehCacheUtil = new EhCacheUtil();
			ehCacheUtil.initCache();
		}
		return ehCacheUtil;
	}
	@SuppressWarnings("unchecked")
	public void putCache(String key,Map<String, Object> map){
		List<String> list =cachesDemo.getKeys();
		System.out.println("+keys++"+list);
		if(list.contains(key)) {
			System.out.println("+存在++"+key+"+进行更新+");
			cachesDemo.remove(key);
			Element element = new Element(key, map);
			cachesDemo.put(element);
			cachesDemo.flush();
		}else {
			Element element = new Element(key, map);
			cachesDemo.put(element);
			cachesDemo.flush();
			System.out.println("+不存在++"+key+"+进行插入+");
		}
	}
	public Element getCache(String key){
		Element element=cachesDemo.get(key);
		return element;
	}
	/**
	 * 测试缓存demoCache2
	 */
	public void testcache() {
		//demoCache2是chcahe.xml中cache的name
		Cache testCache=CacheManager.getInstance().getCache("demoCache2");
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
//        Cache cache = singletonManager.getCache("testCache");
        Element element1_ = testCache.get("key1");
        List<String>list1_=(List<String>) element1_.getObjectValue();
        System.out.println("+list1++"+list1_);
        
        Element element2_ = testCache.get("key2");
        List<String>list2_=(List<String>) element2_.getObjectValue();
        System.out.println("+list2++"+list2_);
	}
}
