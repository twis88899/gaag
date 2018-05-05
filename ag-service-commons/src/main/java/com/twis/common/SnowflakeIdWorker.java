package com.twis.common;

import java.util.Date;

/**
 * Twitter_Snowflake<br>
 * SnowFlake的结构如下(每部分用-分开):<br>
 * 0-000000000000-0000000000-000000000000000000000000000000-00000000000<br>
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0<br>
 * 30位时间截(秒级)，注意，30位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。
 * 30位的时间截，可以使用34年，年T = (1L << 30) / (60 * 60 * 24 * 365) = 34<br>
 * 22位的数据机器位，可以部署很4_194_304节点，包括10位datacenterId和12位workerId<br>
 * 11位序列，秒内的计数，11位的计数顺序号支持每个节点每秒(同一机器，同一时间截)产生2048个ID序号<br>
 * 加起来刚好64位，为一个Long型。<br>
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，
 * 并且效率较高，经测试，SnowFlake每秒能够产生2048左右。
 * 地区编码12+机器位22+时间戳30+自增序列11     =63
 */
public class SnowflakeIdWorker implements IDFactory {

    // ==============================Fields===========================================
//    /** 开始时间截 (2017-01-01 00:00:00 的秒值) */
    private final long epoch = 1485878400L;

	/** 机器id所占的位数 */
    private final long workerIdBits = 12L;

    /** 数据标识id所占的位数 */
    private final long datacenterIdBits = 10L;
    
    /**时间戳所占的位数*/
    private final long tsBits = 30L;

    /** 支持的最大机器id，结果是4095 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /** 支持的最大数据标识id，结果是1023 */
    private final long maxDataCenterId = -1L ^ (-1L << datacenterIdBits);

    /** 序列在id中占的位数 */
    private final long sequenceBits = 11L;

    /** 时间戳向左移12位 */
    private final long tsShift = sequenceBits;

    /**  数据标识id向左移42位(12+30) */
    private final long datacenterIdShift = sequenceBits + tsBits;

    /** 机器id向左移52位(12+30+10) */
    private final long workIdLeftShift = sequenceBits + tsBits + datacenterIdBits;

    /** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /** 机器ID(0~4095) */
    private long workerId;

    /** 数据中心ID(0~1023) */
    private long datacenterId;

    /** 秒内序列(0~4095) */
    private long sequence = 0L;

    /** 上次生成ID的时间截 */
    private long lastTimestamp = -1L;

    //==============================Constructors=====================================
    /**
     * 构造函数
     * @param workerId 工作ID (0~4095)
     * @param datacenterId 数据中心ID (0~1023)
     */
    public SnowflakeIdWorker(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDataCenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDataCenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    // ==============================Methods==========================================
    /**
     * 获得下一个ID (该方法是线程安全的)
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return workerId << workIdLeftShift
        		| (datacenterId << datacenterIdShift)
        		| ( (timestamp - epoch) << tsShift )
        		| sequence;
        
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以秒为单位的当前时间
     * 到 Tue Jan 19 11:14:07 CST 2038 位置都是31bit的值
     * @return 当前时间(秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis()/1000;
    }

    //==============================Test=============================================
    /** 测试 */
	public static void main(String[] args) {
		//IDFactory worker = new SnowflakeIdWorker(2095,999);
//    	System.out.println(new Date(0x7FFFFFFFL*1000));
//        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
//        for (int i = 0; i < 1000; i++) {
//            long id = idWorker.nextId();
//            System.out.println(Long.toBinaryString(id));
//            System.out.println(id);
//        }
		testNum();
//		showTime();
    }
    	
    static void testNum() {
		long num = 2283270069268875264L;
		long a = num & 0xFFF;
		num >>= 11;
		long b = (num & 0x3FFFFFFFL) + 1485878400L;
		num >>= 30;
		long c = num & 0x3FF;//10bit 学校 
		num >>= 10;  //区县
		System.out.println(a);
		System.out.println(new Date(b*1000));
		System.out.println(c);
		System.out.println(num);
    }
    
    
    @SuppressWarnings("deprecation")
	static void showTime(){
    	System.out.println(new Date(117,1,1,0,0,0));
    	System.out.println(new Date(117,1,1,0,0,0).getTime());
    	long b = 1485878400L;//2017-01-01 00:00:00
    	
    	long topNum = 0x3FFFFFFFL;//30bit
    	System.out.println(new Date((topNum+b)*1000));
    	//得以2017-01-01 00:00:00秒值为偏移量，30bit最大表示时间 Fri Feb 10 13:37:03 CST 2051
    }
    
    
    
    
    	
    	
}
