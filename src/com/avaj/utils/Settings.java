package com.avaj.utils;

import java.io.File;

public class Settings {
	public static final String CRYPTO_ALGORITHM = "RSA";

	public static final int GENESIS_DIFFICULTY = 5;
	public static final long GENESIS_REWARD = 1_000;
	public static final long TOTAL_SUPPLY = 5_143_521_600L; // 50 years
//	public static final 
	public static final double REWARD_HALVING_RATE = 1 / 10;
	public static final int REWARD_HALVING_INTERVAL = 525_600;
	public static final int DIFFICULTY_RETARGETING_INTERVAL = 1_440;

	// min: 1, hour: 60, day: 1,440, week: 10,080, month: about 43,200, year:
	// 525,600
	public static final int MINING_AVG_SEC = 60;

	public static final String avaJ_PUBLIC_KEY = "30819f300d06092a864886f70d010101050003818d00308189028181008fc6716531a1a32afaf714fdba91152d423a2b14041abc8487036b7fb7adfb13b9123a386372eda5e047c019d4aa5b24fee5859e6afd442e6aae2d884d085864a79d8338bb3cc5da15acafe594c4deeb779decac4ff97a96ee94c835b1a6179baf907aeb7939f3ba07984e76d1b8beeed54f8f8783d2d901b248e19d71972ad10203010001";
	public static final String avaJ_PRIVATE_KEY = "30820274020100300d06092a864886f70d01010105000482025e3082025a020100028181008fc6716531a1a32afaf714fdba91152d423a2b14041abc8487036b7fb7adfb13b9123a386372eda5e047c019d4aa5b24fee5859e6afd442e6aae2d884d085864a79d8338bb3cc5da15acafe594c4deeb779decac4ff97a96ee94c835b1a6179baf907aeb7939f3ba07984e76d1b8beeed54f8f8783d2d901b248e19d71972ad10203010001027f2b567b2d3048b3666bea63e96cd34eb98067b2e9d5a8398063dcceb530d36b5b5bedf7f4075b729c62d3e893e0b1791d2db8570e94316e313a4c466d4f75b4842db317dd57e1a836ca09565a7fbd874002ec4a30765efc3a6d7f795b0683708f765523738b8e3c5302e4414ac3c3f603a98430eaf618e7fd6cae9e9c81587b024100df0dcb67f0b4713ce15f6a3e69ddfe2861c8674f189ae0ffeedddb7fecfbded3172fb47fe52e93a9374018e03c67e15362f832b7ec36c4197e5873424e7e5d87024100a502edc9d059e26589b9c6d8eb8edfb027819594396189c76425d56bade085f263a736d37860254d5e47d816cb4f90606dd0f01ad609ff9b5b2da88b612a8ae7024072b7bb8787aaa61ab3a6c913b48b31ee3eec3d05d717c0e4a04b865ab2dd8f6a6da0616aedca18b38cf0f05a5376f1d25b325f936c6f7647ce4d5b6a1cc8acfd0241008efaf9b63c7e16092b8cb93b6ee90ef461570c98321c4d396392d0da0c0117af8f1fee06d130664222e697dc307111c62c81b5fb5ae9b0fd19c5775cc6660993024001670e20dbf5e3bd5ae2a5686fdb166f0d1d857bc8ba0a4fe51a59d3b1ecda1e2f1d327ee156c35f6cb19fa0161931d163f54489a9c5589a8b46ecae2d37c76c";

	public static final File DATA_DIR = new File(System.getProperty("user.home"), "avaJ");
	public static final File ACCOUNTS_DIR = new File(DATA_DIR, "accounts");
}
