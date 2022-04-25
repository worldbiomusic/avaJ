package com.avaj.utils;

public class Settings {
	public static final String CRYPTO_ALGORITHM = "RSA";
	
	public static final int GENESIS_DIFFICULTY = 3;
	public static final long GENESIS_REWARD = 1_000;
	public static final long TOTAL_SUPPLY = 5_143_521_600L; // 50 years
//	public static final 
	public static final double REWARD_HALVING_RATE = 1/10; 
	public static final int REWARD_HALVING_INTERVAL = 525_600; 
	public static final int DIFFICULTY_RETARGETING_INTERVAL = 1_440;
	
	// min: 1, hour: 60, day: 1,440, week: 10,080, month: about 43,200, year: 525,600
	public static final int MINING_AVG_SEC = 60;
	
	
	
}
