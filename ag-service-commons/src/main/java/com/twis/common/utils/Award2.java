package com.twis.common.utils;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
	
public class Award2 {

	public static void main(String[] args) throws Exception {
		Map<String, Integer> awardStockMap = new ConcurrentHashMap<>(); // 奖品
																		// <-->
																		// 奖品库存
		awardStockMap.put("1", 5000);
		awardStockMap.put("2", 1000);
		awardStockMap.put("3", 500);
		awardStockMap.put("5", 100);
		awardStockMap.put("iphone", 1);
		awardStockMap.put("未中奖", 59409); // 6601*10 -6601
		// 权重默认等于库存
		Map<String, Integer> awardWeightMap = new ConcurrentHashMap<>(); // 奖品
																							// <-->
																							// 奖品权重
		
		awardWeightMap.put("1", 3000);
		awardWeightMap.put("2", 2000);
		awardWeightMap.put("3", 100);
		awardWeightMap.put("5", 10);
		awardWeightMap.put("iphone", 0);
		awardWeightMap.put("未中奖", 59409); // 6601*10 -6601

		int userNum = 30000; // 日活用户数
		int drawNum = userNum * 3; // 每天抽奖次数 = 日活数*抽奖次数
		Map<String, Integer> dailyWinCountMap = new ConcurrentHashMap<>(); // 每天实际中奖计数
		for (int j = 0; j < 1; j++) { // 模拟每次抽奖
			// 排除掉库存为0的奖品
			Map<String, Integer> awardWeightHaveStockMap = awardWeightMap.entrySet().stream()
					.filter(e -> awardStockMap.get(e.getKey()) > 0)
					.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
			int totalWeight = (int) awardWeightHaveStockMap.values().stream().collect(Collectors.summarizingInt(i -> i))
					.getSum();
			int randNum = new Random().nextInt(totalWeight); // 生成一个随机数
			int prev = 0;
			String choosedAward = null;
			// 按照权重计算中奖区间
			for (Entry<String, Integer> e : awardWeightHaveStockMap.entrySet()) {
				if (randNum >= prev && randNum < prev + e.getValue()) {
					choosedAward = e.getKey(); // 落入该奖品区间
					break;
				}
				prev = prev + e.getValue();
			}
			dailyWinCountMap.compute(choosedAward, (k, v) -> v == null ? 1 : v + 1); // 中奖计数
			if (!"未中奖".equals(choosedAward)) { // 未中奖不用减库存
				awardStockMap.compute(choosedAward, (k, v) -> v - 1); // 奖品库存一
				if (awardStockMap.get(choosedAward) == 0) {
					System.out.printf("奖品：%s 库存为空%n", choosedAward); // 记录库存为空的顺序
				}
			}

		}
		System.out.println("各奖品中奖计数: " + dailyWinCountMap); // 每日各奖品中奖计数
		
		/*奖品：5 库存为空
		奖品：3 库存为空
		奖品：2 库存为空
		奖品：1 库存为空
		奖品：iphone 库存为空
		各奖品中奖计数: {1=5000, 2=1000, 3=500, 5=100, iphone=1, 未中奖=293399}

        ---> 调整权重后
		奖品：2 库存为空
		奖品：1 库存为空
		各奖品中奖计数: {1=5000, 2=1000, 3=140, 5=13, 未中奖=83847}*/
	}
}
