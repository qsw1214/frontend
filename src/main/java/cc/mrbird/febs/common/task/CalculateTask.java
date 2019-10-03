package cc.mrbird.febs.common.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.resource.service.IResourceService;
import cc.mrbird.febs.search.entity.KeywordCount;
import cc.mrbird.febs.search.service.IEsResourceService;
import cc.mrbird.febs.search.service.IKeywordCountService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CalculateTask {
	
	@Autowired
	private IKeywordCountService keywordCountService;
	@Autowired
    private IEsResourceService esResourceService;
	@Autowired
    private RedisService redisService;
	@Autowired
    private IResourceService resourceService;

	@Scheduled(cron = "0 0 0 * * ?") // 每天00:00:00统计
//	@Scheduled(fixedRate = 10000)
	public void run() {
		try {
			log.info("reset api visit num");
			String key = FebsConstant.RES_DETAIL_API_VISIT;
			for(int i=0; i<24; i++){
				if(i<10)
					redisService.hset(key, "0"+i, "0");
				else
					redisService.hset(key, ""+i, "0");
			}
			log.info("calculate keyword num start");
			long startTime = System.currentTimeMillis();
			// 获取前一天日期
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");			
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1);
			Date date = (Date) calendar.getTime();
			String pre_one_day = df.format(date);
			File file = new File("log/search/search."+pre_one_day+".log");
			if (!file.exists()){
				log.info("{}.log is not exist", pre_one_day);
				return;
			}
			FileReader fileReader = new FileReader(file);
			// 使用流的方式读取内容
			BufferedReader bufreader = new BufferedReader(fileReader);
			Map<String, Integer> map = new HashMap<String, Integer>();
			String readLine = null;
			while ((readLine = bufreader.readLine()) != null) {
				// 将字母排序为小写
				readLine = readLine.toLowerCase();
				String[] str = readLine.split("\\|");
				if(str.length > 2 && str[1].equals("keyword")){
					// 分词
					String[] words = esResourceService.getAnalyzes("rms", str[2]);
					for (int i = 0; i < words.length; i++) {
						if (map.containsKey(words[i])) {
							map.put(words[i], map.get(words[i]) + 1);
						} else {
							map.put(words[i], 1);
						}
					}
				}
			}
			
			// 最小堆取 top 11
			int maxSize = 50;
			PriorityQueue<String> minHeap = new PriorityQueue<String>(maxSize,new Comparator<String>(){
			    @Override
			    public int compare(String s1,String s2){
			        return map.get(s1) - map.get(s2);
			    }
			});
			Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
			// 使用迭代器取值
			while (it.hasNext()) {
				Map.Entry<String, Integer> entry = it.next();
				if(minHeap.size()<maxSize){
					minHeap.add(entry.getKey());
				}else{
		            String word = minHeap.peek();
		            if (entry.getValue() > map.get(word)) {
		            	minHeap.poll();
		            	minHeap.add(entry.getKey());
		            }
		        }
			}
			List<String> list = new ArrayList<>(minHeap);
			List<KeywordCount> records = new ArrayList<>();
			for(String word: list){
				KeywordCount kc = new KeywordCount();
				kc.setKeyword(word);
				kc.setCount(map.get(word));
				kc.setSearchDate(date);
				records.add(kc);
			}
			List<Map<String,Object>> old = keywordCountService.findKeywordsByDate(1, date);
			if(old.isEmpty()){
				log.info("save result");
				keywordCountService.saveBatch(records);
			}
	        long endTime = System.currentTimeMillis();
	        log.info("calculate {}.log finish {} ms: {}", pre_one_day, endTime-startTime, list);
		} catch (Exception ignore) {
			log.error("calculate keyword num error", ignore);
		}
	}
	
	@Scheduled(cron = "0 0 * * * ?") // 每小时同步一次
	public void updateReadCount(){
		log.info("updateReadCount task start");
		long startTime = System.currentTimeMillis();
		String key = FebsConstant.RES_VISIT;
		Set<String> set = redisService.hkeys(key);
		if(set != null){
			for(String resourceId: set){
				String num = redisService.hget(key, resourceId);
				redisService.hdel(key, resourceId);
				if(num!=null && Integer.valueOf(num)>0)
					resourceService.increaseReadCount(Long.valueOf(resourceId), Integer.valueOf(num));
			}
		}
		long endTime = System.currentTimeMillis();
		log.info("updateReadCount task finish, cost {} ms", (endTime - startTime));
	}
	
	public static void main(String[] args) throws ParseException {
//		new CalculateTask().run();
//		new CalculateTask().updateReadCount();
	}

}
