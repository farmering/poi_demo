package sun.service.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import sun.service.EhCacheTestService;
@Service
public class EhCacheTestServiceImpl implements EhCacheTestService {
	//value=”cacheTest”与ehcache-setting.xml中的cache名称属性值一致。
    @Cacheable(value="cacheTest",key="#param")
    public String getTimestamp(String param) {
        Long timestamp = System.currentTimeMillis();
        return timestamp.toString();
    }
}