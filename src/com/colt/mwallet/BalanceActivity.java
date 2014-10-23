package com.colt.mwallet;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executor;

import com.google.bitcoin.core.AbstractWalletEventListener;
import com.google.bitcoin.core.Wallet;
import com.google.zxing.WriterException;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class BalanceActivity extends Activity {

	Colt CoinSystem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_balance);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		
		setCoinSystem();
		setList();
		
		Thread t = new Thread() {

			  @Override
			  public void run() {
			    try {
			      while (!isInterrupted()) {
			        Thread.sleep(3000);
			        runOnUiThread(new Runnable() {
			          public void run() {
			        	  setList();
			          }
			        });
			      }
			    } catch (InterruptedException e) {
			    }
			  }
			};
			t.start();

		
	}
	
	public void setCoinSystem()
	{
		
		if(Colt.type.equals("Bitcoin"))
    	{
			if(ColtSHA256.GettheOne() == null)
			{
				File blockFile = new File(this.getFilesDir(), "BlockStore");
	        	File walletFile = new File(this.getFilesDir(), "BitcoinWallet");
	        	try {
					ColtSHA256 CoinSystem = new ColtSHA256(walletFile, blockFile);
				} catch (Exception e) {}	
			}
			CoinSystem = ColtSHA256.GettheOne();
    	}
		
		if(Colt.type.equals("Litecoin"))
    	{
			if(ColtScrypt.GettheOne() == null)
			{
				File blockFile = new File(this.getFilesDir(), "BlockStore");
	        	File walletFile = new File(this.getFilesDir(), "BitcoinWallet");
	        	try {
					ColtScrypt CoinSystem = new ColtScrypt(walletFile, blockFile);
				} catch (Exception e) {}	
			}
			CoinSystem = ColtScrypt.GettheOne();
    	}
		
	}
	
	
	public void setList()
	{
		final ListView addlist = (ListView) findViewById(R.id.listView); 
		ArrayList<String> transaction = new ArrayList<String>();  
		transaction.addAll( Arrays.asList(CoinSystem.balance()) );
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.listlayout, transaction);
		addlist.setAdapter( listAdapter );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.balance, menu);
		return true;
	}
	
	public void ActivitySelected(View view) {
    }
	
	public void TransactionSelected(View view) {
        Intent intent = new Intent(this, SentActivity.class);
        startActivity(intent);
    }
	
	public void WalletSelected(View view) {
        Intent intent = new Intent(this, CoinActivity.class);
        startActivity(intent);
    }
	
	public void SetSelected(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
	
	


}
