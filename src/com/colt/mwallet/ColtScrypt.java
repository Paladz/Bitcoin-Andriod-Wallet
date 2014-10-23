package com.colt.mwallet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Color;


import com.google.dogecoin.core.Address;
import com.google.dogecoin.core.AddressFormatException;
import com.google.dogecoin.core.BlockChain;
import com.google.dogecoin.core.DownloadListener;
import com.google.dogecoin.core.ECKey;
import com.google.dogecoin.core.InsufficientMoneyException;
import com.google.dogecoin.core.NetworkParameters;
import com.google.dogecoin.core.PeerGroup;
import com.google.dogecoin.core.Transaction;
import com.google.dogecoin.core.Wallet;
import com.google.dogecoin.net.discovery.DnsDiscovery;
import com.google.dogecoin.params.MainNetParams;
import com.google.dogecoin.store.BlockStore;
import com.google.dogecoin.store.BlockStoreException;
import com.google.dogecoin.store.SPVBlockStore;
import com.google.dogecoin.store.UnreadableWalletException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.ByteMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
 
public class ColtScrypt extends Colt
{
	private Wallet wallet;
	private BlockStore blockStore;
	private BlockChain chain;
	private PeerGroup peerGroup;
	private NetworkParameters parameter;
	//dude! i'm here!
	public static ColtScrypt theOne=null;
	
	public ColtScrypt(File walletFile, File blockFile) throws IOException, UnreadableWalletException, BlockStoreException
	{
		parameter = MainNetParams.get();
		//initial the wallet
		
		if(!walletFile.exists())
		{
			 Wallet temp = new Wallet(parameter);
			 BigInteger privKey = new BigInteger("45325672819920108");
			 temp.addKey(new ECKey(privKey));
   	         temp.saveToFile(walletFile);
		}
		wallet = Wallet.loadFromFile(walletFile);
		
		//blockStore = new MemoryBlockStore(parameter);
		blockStore = new SPVBlockStore(parameter, blockFile);
		chain = new BlockChain(parameter,wallet, blockStore);
		peerGroup = new PeerGroup(parameter, chain);
		peerGroup.addWallet(wallet);
		peerGroup.addPeerDiscovery(new DnsDiscovery(parameter));
		peerGroup.setMaxConnections(20);
		peerGroup.setFastCatchupTimeSecs(290440);
		peerGroup.start();
		this.DownloadChain();
		
		theOne = this;
	}
	
	public void SentCoin(String adress, long amount) throws AddressFormatException, InsufficientMoneyException
	{	
		BigInteger value = BigInteger.valueOf(amount);
		Address address = new Address(parameter, adress);
		Wallet.SendRequest req = Wallet.SendRequest.to(address, value);
		Wallet.SendResult result = wallet.sendCoins(peerGroup, req);
	}
	
	public String PrintWallet()
	{
		return wallet.toString();
	}
	
	public static ColtScrypt GettheOne()
	{
		return theOne;
	}
	
	public void AddKey()
	{
			wallet.addKey(new ECKey());
	}
	
	public void DownloadChain()
	{
		DownloadListener listener = new DownloadListener();
		peerGroup.startBlockChainDownload(listener);
	}
	
	public String avibalance()
	{
		return String.valueOf(((double) wallet.getBalance(Wallet.BalanceType.AVAILABLE).intValue())/100000000);
	}
	
	public String estibalance()
	{
		return String.valueOf(((double) wallet.getBalance(Wallet.BalanceType.ESTIMATED).intValue())/100000000);
	}
	
	public String[] address()
	{
		List<ECKey> keys= wallet.getKeys();
		String[] result = new String[wallet.getKeychainSize()];
		int count =0;
		for (ECKey key : keys) {
			result[count++] = key.toAddress(parameter).toString();	
        }		
		return result;
	}
	
	public Bitmap getQR(String input) throws WriterException
    {
    	QRCodeWriter creater = new QRCodeWriter();
        ByteMatrix matrix = creater.encode(input, BarcodeFormat.QR_CODE, 250, 250);
        Bitmap result= Bitmap.createBitmap(250, 250, Bitmap.Config.ARGB_8888);
        
        for (int x = 0; x < 250; x++) 
        {
            for (int y = 0; y < 250; y++) 
            {
            	if(matrix.get(x, y) == (byte)0x00)
            	   result.setPixel(x, y, Color.BLACK);  
            	else
            	   result.setPixel(x, y, Color.WHITE);
            }
        }
		return result;
    }
	
	public String[] balance()
	{
		List<Transaction> trans= wallet.getTransactionsByTime();
		String[] result = new String[trans.size()];
		int count =0;
		String Append = "";
		
		for (Transaction tran : trans) {
			Date time = tran.getUpdateTime();
			BigInteger value = tran.getValue(wallet);
			Address addr;
			if(value.signum() < 0)
			{
				addr = tran.getOutput(0).getScriptPubKey().getToAddress(parameter);
				Append = " -";
			}
			else
			{
				addr = tran.getInput(0).getFromAddress();
				Append = " +";
			}
			result[count++] = "Time: " + time.toGMTString() + "\nAddress: " + addr.toString() + "\nBalance:" +Append + ((double) value.intValue())/100000000;
        }		
		return result;
	}
	
	public Wallet getWallet()
	{
		return wallet;
	}
	
	
}
