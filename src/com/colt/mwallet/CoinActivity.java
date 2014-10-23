package com.colt.mwallet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.bitcoin.core.AbstractWalletEventListener;
import com.google.bitcoin.core.BlockChain;
import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.PeerGroup;
import com.google.bitcoin.core.Wallet;
import com.google.bitcoin.net.discovery.DnsDiscovery;
import com.google.bitcoin.params.MainNetParams;
import com.google.bitcoin.store.BlockStore;
import com.google.bitcoin.store.BlockStoreException;
import com.google.bitcoin.store.SPVBlockStore;
import com.google.bitcoin.store.UnreadableWalletException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.ByteMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class CoinActivity extends Activity {
	
	Colt CoinSystem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coin);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		
		setCoinSystem();
		setBalance();             
		setList();
		setQrhint();	
		
		Thread t = new Thread() {

			  @Override
			  public void run() {
			    try {
			      while (!isInterrupted()) {
			        Thread.sleep(3000);
			        runOnUiThread(new Runnable() {
			          public void run() {
			        	  setBalance(); 
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
		ArrayList<String> address = new ArrayList<String>();  
		address.addAll( Arrays.asList(CoinSystem.address()) );
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.listlayout, address);
		addlist.setAdapter( listAdapter );
		addlist.setClickable(true);	
		
		addlist.setOnItemClickListener(new OnItemClickListener() 
		{
		      public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) 
		      {
		    	if(CoinSystem.QR)
		    	{
		        String selectedadd =(String) (addlist.getItemAtPosition(myItemInt));
		        Dialog qrwindow = new Dialog(CoinActivity.this);
		        qrwindow.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		        qrwindow.setContentView(getLayoutInflater().inflate(R.layout.qr_windows, null));
		        ImageView qrimg = (ImageView) qrwindow.findViewById(R.id.img);
		        try {
					qrimg.setImageBitmap(CoinSystem.getQR(selectedadd));
				} catch (Exception e) {
					e.printStackTrace();
				}
		        qrwindow.show();}
		      }

		});    
	}
	
	public void setBalance()
	{
		TextView walletbalance = (TextView)findViewById(R.id.walletbalance);
		TextView pendingbalance = (TextView)findViewById(R.id.pendingbalance);
		walletbalance.setText(CoinSystem.avibalance() + " BTC");
		pendingbalance.setText(CoinSystem.estibalance() + " BTC");
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.coin, menu);
		return true;
	}
	
	public void ActivitySelected(View view) {
        Intent intent = new Intent(this, BalanceActivity.class);
        startActivity(intent);
    }
	
	public void TransactionSelected(View view) {
        Intent intent = new Intent(this, SentActivity.class);
        startActivity(intent);
    }
	
	public void AddSelected(View view) {
		CoinSystem.AddKey();
		setList();
    }
	
	public void WalletSelected(View view) {

    }
    
    public void SetSelected(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    /*
    public void addressqrsel(View view) throws WriterException {
    	ListView addlist = (ListView) findViewById(R.id.listView); 
    	String x = addlist.getSelectedItem().toString();
    	
    	Dialog qrwindow = new Dialog(this);
        qrwindow.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        qrwindow.setContentView(getLayoutInflater().inflate(R.layout.qr_windows, null));
        ImageView qrimg = (ImageView) qrwindow.findViewById(R.id.img);
        qrimg.setImageBitmap(CoinSystem.getQR(x));
        qrwindow.show();
    }*/
    
    public void QRSelected(View view) throws WriterException {
        CoinSystem.QR = !CoinSystem.QR;
        setQrhint();
    }
    
    public void setQrhint()
	{
		ImageView img= (ImageView) findViewById(R.id.qr);
        if(CoinSystem.QR)
        {
        	img.setImageResource(R.drawable.ic_qron);
        }
        else
        {
        	img.setImageResource(R.drawable.ic_qroff);
        }	
	}
	

}
