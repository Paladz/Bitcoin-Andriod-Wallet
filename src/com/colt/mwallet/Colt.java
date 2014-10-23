package com.colt.mwallet;

import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.InsufficientMoneyException;
import com.google.bitcoin.core.Wallet;
import com.google.zxing.WriterException;

import android.graphics.Bitmap;

public abstract class Colt {
	public static String type = "Bitcoin";
	public static boolean QR = true;
	abstract String[] balance();
	abstract Bitmap getQR(String input) throws Exception;
	abstract String[] address();
	abstract String estibalance();
	abstract String avibalance();
	abstract void DownloadChain();
	abstract void AddKey();
	abstract String PrintWallet();
	abstract void SentCoin(String adress, long amount) throws Exception;
	

}
